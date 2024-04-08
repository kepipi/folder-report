package com.sztus.lib.back.end.basic.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sztus.lib.back.end.basic.dao.service.FileService;
import com.sztus.lib.back.end.basic.object.domain.File;
import com.sztus.lib.back.end.basic.type.constant.JsonKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author QYP
 * @date 2024/4/7 14:39
 */
@Service
public class FileBusinessService {


    @Autowired
    private FileService fileService;

    @Autowired
    private ResetTemplateService resetTemplateService;


    private static final String AI_URL = "http://ec2-13-42-55-109.eu-west-2.compute.amazonaws.com:8080/imageDescription";

    public List<File> listFile(Long locationId) {
        return fileService.list(new LambdaQueryWrapper<File>().eq(File::getLocationId, locationId));
    }

    public void deleteFile(Long fileId) {
        fileService.removeById(fileId);
    }

    public void aiAnalyse(List<String> fileUrlList) throws IOException {
        for (String url : fileUrlList) {
            JSONObject data = new JSONObject();
            data.put(JsonKey.FILE_URL, url);
            String requestBody = resetTemplateService.doPostByRequestBody(AI_URL, data.toJSONString());
            System.out.println(requestBody);
        }
    }
}
