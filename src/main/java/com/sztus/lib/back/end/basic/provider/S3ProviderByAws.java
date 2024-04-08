package com.sztus.lib.back.end.basic.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.object.request.UploadFileRequest;
import com.sztus.lib.back.end.basic.type.constant.StorageJsonKey;
import com.sztus.lib.back.end.basic.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author daniel
 * @date 2020/11/28
 */

@Service
@Slf4j
public class S3ProviderByAws {

    Regions regions = Regions.US_WEST_1;

    public AmazonS3 getS3ClientInstance(JSONObject connectInfo) {
        String accessKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_ID);
        String secretKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_SECRET);
        String region = connectInfo.getString(StorageJsonKey.REGION);
        return createS3ClientInstance(accessKey, secretKey, getRegions(region), 100000);
    }

    /**
     * gets default s3Client
     * regions is Singapore
     */
    public AmazonS3 createS3ClientInstance(String accessKey, String secretKey, Regions region, Integer connectionTimeout) {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withClientConfiguration(new ClientConfiguration()
                        .withConnectionTimeout(connectionTimeout))
                .build();
    }


    public String getObjectUrl(AmazonS3 s3Client, String bucketName, String keyName) {
        S3Object object = s3Client.getObject(bucketName, keyName);
        if (object == null) {
            return null;
        }
        return object.getObjectContent().getHttpRequest().getURI().toString();
    }

    /**
     * 创建一个分段上传的对象
     *
     * @param connectInfo aws S3 的链接参数
     * @param request     上传文件对象
     * @return 分段上传表示 ID
     */
    public String creatMultipartUpload(JSONObject connectInfo, UploadFileRequest request) {

        String accessKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_ID);
        String secretKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_SECRET);
        String bucketName = connectInfo.getString(StorageJsonKey.BUCKET);
        String region = connectInfo.getString(StorageJsonKey.REGION);
        AmazonS3 s3Client = createS3ClientInstance(accessKey, secretKey, getRegions(region), -1);
        try {
            InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, request.getFileName());
            InitiateMultipartUploadResult result = s3Client.initiateMultipartUpload(initRequest);
            return result.getUploadId();
        } finally {
            s3Client.shutdown();
        }
    }

    /**
     * 执行单次的分段上传任务并返回当前的偏移量
     *
     * @param connectInfo S3 连接参数
     * @param request     分段上传参数
     * @return partETag 偏移量对象
     * @throws BusinessException 自定义异常
     */
    public String multipartUploadFile(JSONObject connectInfo, UploadFileRequest request) throws BusinessException {

        String accessKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_ID);
        String secretKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_SECRET);
        String bucketName = connectInfo.getString(StorageJsonKey.BUCKET);
        String region = connectInfo.getString(StorageJsonKey.REGION);
        AmazonS3 s3Client = createS3ClientInstance(accessKey, secretKey, getRegions(region), -1);

        String fileBody = request.getFileBody();
        Integer current = request.getCurrent();

        try {
            InputStream inputStream = ConvertUtil.stringToStream(fileBody);

            UploadPartRequest uploadRequest = new UploadPartRequest().withBucketName(bucketName).withKey(request.getFileName()).withUploadId(request.getUploadId()).withPartNumber(current).withInputStream(inputStream).withPartSize(inputStream.available());

            PartETag partEtag = s3Client.uploadPart(uploadRequest).getPartETag();
            return JSON.toJSONString(partEtag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(" Multipart upload server exception.");
        } finally {
            s3Client.shutdown();
        }
    }

    /**
     * 合并之前的分段上传内容并返回可下载的链接
     *
     * @param connectInfo 链接信息
     * @param request     分段上传参数
     * @return 下载链接
     * @throws BusinessException 自定义异常
     */
    public String completeMultipartUploadFile(JSONObject connectInfo, UploadFileRequest request) throws BusinessException {

        String accessKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_ID);
        String secretKey = connectInfo.getString(StorageJsonKey.ACCESS_KEY_SECRET);
        String bucketName = connectInfo.getString(StorageJsonKey.BUCKET);
        String region = connectInfo.getString(StorageJsonKey.REGION);
        AmazonS3 s3Client = createS3ClientInstance(accessKey, secretKey, getRegions(region), -1);

        String fileName = request.getFileName();
        String uploadId = request.getUploadId();
        JSONArray partEtagArray = request.getPartETagArray();
        List<PartETag> partEtagList = JSON.parseArray(JSON.toJSONString(partEtagArray), PartETag.class);

        try {
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, fileName, uploadId, partEtagList);
            s3Client.completeMultipartUpload(compRequest);

            String fileUrl = s3Client.getObject(bucketName, fileName).getObjectContent().getHttpRequest().getURI().toString();
            log.error("[ completeMultipartUploadFile ] file name is :{} , url is :{}", fileName, fileUrl);
            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Complete multipart uploadFile exception");
        } finally {
            s3Client.shutdown();
        }
    }


    private static final Map<String, Regions> REGION_MAP = new HashMap<>();

    static {
        REGION_MAP.put("GovCloud", Regions.GovCloud);
        REGION_MAP.put("US_GOV_EAST_1", Regions.US_GOV_EAST_1);
        REGION_MAP.put("US_EAST_2", Regions.US_EAST_2);
        REGION_MAP.put("US_EAST_1", Regions.US_EAST_1);
        REGION_MAP.put("US_WEST_1", Regions.US_WEST_1);
        REGION_MAP.put("US_WEST_2", Regions.US_WEST_2);
        REGION_MAP.put("EU_WEST_1", Regions.EU_WEST_1);
        REGION_MAP.put("EU_WEST_2", Regions.EU_WEST_2);
        REGION_MAP.put("EU_WEST_3", Regions.EU_WEST_3);
        REGION_MAP.put("EU_CENTRAL_1", Regions.EU_CENTRAL_1);
        REGION_MAP.put("EU_NORTH_1", Regions.EU_NORTH_1);
        REGION_MAP.put("AP_EAST_1", Regions.AP_EAST_1);
        REGION_MAP.put("AP_SOUTH_1", Regions.AP_SOUTH_1);
        REGION_MAP.put("AP_SOUTHEAST_1", Regions.AP_SOUTHEAST_1);
        REGION_MAP.put("AP_SOUTHEAST_2", Regions.AP_SOUTHEAST_2);
        REGION_MAP.put("AP_NORTHEAST_1", Regions.AP_NORTHEAST_1);
        REGION_MAP.put("SA_EAST_1", Regions.SA_EAST_1);
        REGION_MAP.put("CN_NORTH_1", Regions.CN_NORTH_1);
        REGION_MAP.put("CA_CENTRAL_1", Regions.CA_CENTRAL_1);
        REGION_MAP.put("ME_SOUTH_1", Regions.ME_SOUTH_1);
        REGION_MAP.put("US_ISO_EAST_1", Regions.US_ISO_EAST_1);
        REGION_MAP.put("US_ISOB_EAST_1", Regions.US_ISOB_EAST_1);
        REGION_MAP.put("US_ISO_WEST_1", Regions.US_ISO_WEST_1);
    }

    public Regions getRegions(String region) {
        return StringUtils.isEmpty(region) ? regions : REGION_MAP.get(region.toUpperCase());
    }

    public String convertDomainName(String objectUrl, String domainName) {
        Set<String> s3DomainName = DOMAIN_NAME_MAP.keySet();
        for (String item : s3DomainName) {
            if (objectUrl.contains(item)) {
                if (StringUtils.isNotBlank(domainName)) {
                    return objectUrl.replace(item, domainName);
                }
                return objectUrl.replace(item, DOMAIN_NAME_MAP.get(item));
            }
        }
        return objectUrl;
    }

    private static final Map<String, String> DOMAIN_NAME_MAP = new HashMap<>();

    static {
        DOMAIN_NAME_MAP.put("sztus-storage.s3.us-west-1.amazonaws.com", "storage.sztus.com");
        DOMAIN_NAME_MAP.put("s3.us-west-1.amazonaws.com/sztus-storage", "storage.sztus.com");

        DOMAIN_NAME_MAP.put("sztus-assets.s3.us-west-1.amazonaws.com", "assets.sztus.com");
        DOMAIN_NAME_MAP.put("s3.us-west-1.amazonaws.com/sztus-assets", "assets.sztus.com");

        DOMAIN_NAME_MAP.put("sztnb-storage.s3.us-west-2.amazonaws.com", "storage.sztnb.com");
        DOMAIN_NAME_MAP.put("s3.us-west-2.amazonaws.com/sztnb-storage", "storage.sztnb.com");

        DOMAIN_NAME_MAP.put("sztnb-assets.s3.ap-east-1.amazonaws.com", "assets.sztnb.com");
        DOMAIN_NAME_MAP.put("s3.ap-east-1.amazonaws.com/sztnb-assets", "assets.sztnb.com");

        DOMAIN_NAME_MAP.put("blankink-2.s3.us-west-2.amazonaws.com", "s4.esignwiz.com");
        DOMAIN_NAME_MAP.put("s3.us-west-2.amazonaws.com/blankink-2", "s4.esignwiz.com");
    }
}
