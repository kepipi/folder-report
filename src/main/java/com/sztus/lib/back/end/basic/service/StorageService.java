package com.sztus.lib.back.end.basic.service;

import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sztus.lib.back.end.basic.dao.service.FileMappingService;
import com.sztus.lib.back.end.basic.dao.service.StorageProviderParameterService;
import com.sztus.lib.back.end.basic.dao.service.StorageProviderService;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.domain.FileMapping;
import com.sztus.lib.back.end.basic.object.domain.StorageProvider;
import com.sztus.lib.back.end.basic.object.domain.StorageProviderParameter;
import com.sztus.lib.back.end.basic.object.request.*;
import com.sztus.lib.back.end.basic.object.response.StorageFileUploadResponse;
import com.sztus.lib.back.end.basic.provider.S3ProviderByAws;
import com.sztus.lib.back.end.basic.type.constant.StatusConst;
import com.sztus.lib.back.end.basic.type.constant.StorageJsonKey;
import com.sztus.lib.back.end.basic.type.enumerate.*;
import com.sztus.lib.back.end.basic.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Max
 * @date 2023/05/10
 */
@Slf4j
@Service
public class StorageService {

    private final S3ProviderByAws s3ProviderByAws;
    private final FileMappingService fileMappingService;
    private final StorageProviderService storageProviderService;
    private final StorageProviderParameterService storageProviderParameterService;


    public StorageService(S3ProviderByAws s3ProviderByAws, FileMappingService fileMappingService, StorageProviderService storageProviderService, StorageProviderParameterService storageProviderParameterService) {
        this.s3ProviderByAws = s3ProviderByAws;
        this.fileMappingService = fileMappingService;
        this.storageProviderService = storageProviderService;
        this.storageProviderParameterService = storageProviderParameterService;
    }


    public StorageFileUploadResponse uploadFileToS3(StorageFileUploadRequest request) throws BusinessException{
        JSONObject connectInfo = getConnectInfoFromRedis(ConnectionTypeEnum.S3.getText());

        AmazonS3 s3Client = s3ProviderByAws.getS3ClientInstance(connectInfo);
        try {
            String bucketName = connectInfo.getString(StorageJsonKey.BUCKET);
            String relativePath = processObjectPath(bucketName, request.getPath());

            String fileName = request.getFileName();
            String fileBody = request.getFileBody();

            InputStream inputStream = ConvertUtil.stringToStream(fileBody);

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            if (StringUtils.isNotBlank(request.getContentType())) {
                objectMetadata.setContentType(request.getContentType());
            }

            s3Client.putObject(relativePath, fileName, inputStream, objectMetadata);

            String objectUrl = s3ProviderByAws.getObjectUrl(s3Client, relativePath, fileName);


            StorageFileUploadResponse storageFileUploadResponse = new StorageFileUploadResponse();

            String uuid = UUID.randomUUID().toString().replace("-", "");

            FileMapping fileMapping = new FileMapping();
            fileMapping.setFileCode(uuid);
            fileMapping.setFileName(fileName);
            fileMapping.setRelativePath(relativePath);
            fileMapping.setObjectUrl(objectUrl);
            fileMappingService.saveFileMapping(fileMapping);

            storageFileUploadResponse.setFileUrl(objectUrl);
            storageFileUploadResponse.setFileCode(uuid);
            return storageFileUploadResponse;
        } catch (Exception e) {
            log.error("StorageService uploadFile error", e);
            throw new BusinessException(StorageError.FILE_UPLOAD_FAILURE);
        } finally {
            s3Client.shutdown();
        }
    }


    private String processObjectPath(String bucketName, String filePath) throws BusinessException {
        String pattern = "[:*?\"<>|]";

        Matcher m = Pattern.compile(pattern).matcher(filePath);
        if (m.find()) {
            throw new BusinessException(StorageError.FILE_PATH_IS_WRONG);
        }
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.lastIndexOf("/"));
        }
        if (filePath.startsWith("/")) {
            return bucketName + filePath;
        }
        return bucketName + "/" + filePath;
    }

    private JSONObject getConnectInfoFromRedis(String providerName) throws BusinessException {

        JSONObject connectInfoJson = getConnectInfoByProviderName(providerName);
        return connectInfoJson;
    }

    private JSONObject getConnectInfoByProviderName(String providerName) throws BusinessException {
        StorageProvider storageProvider = storageProviderService.getProviderByName(providerName);
        if (Objects.nonNull(storageProvider) && Objects.nonNull(storageProvider.getStatus())) {
            if (storageProvider.getStatus() == StatusConst.DISABLED) {
                throw new BusinessException(StorageError.PROVIDER_IS_DISABLED);
            }
        } else {
            throw new BusinessException(StorageError.PROVIDER_IS_NOT_EXISTED);
        }

        List<StorageProviderParameter> parameterList = storageProviderParameterService.getProviderParameterList(Collections.singletonList(storageProvider.getId()));

        JSONObject connectInfoJson = new JSONObject();

        for (StorageProviderParameter loopParameter : parameterList) {
            connectInfoJson.put(loopParameter.getParameterKey(), loopParameter.getParameterValue());
        }

        connectInfoJson.put("endpoint", storageProvider.getEndpoint());

        if (connectInfoJson.isEmpty()) {
            throw new BusinessException(StorageError.CONNECTION_PARAMETER_IS_NOT_EXISTED);
        }
        return connectInfoJson;
    }
}
