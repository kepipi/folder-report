package com.sztus.lib.back.end.basic.service;

import com.sztus.lib.back.end.basic.component.BaseRestTemplate;
import com.sztus.lib.back.end.basic.component.SimpleRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author QYP
 * @date 2024/4/7 18:26
 */

@Service
public class ResetTemplateService {

    @Autowired
    private SimpleRestTemplate simpleRestTemplate;

    public String doPostByRequestBody(String callUrl, String callData) {
        String result = simpleRestTemplate.postByRequestBody(callUrl, callData, String.class);
        return result;
    }
}
