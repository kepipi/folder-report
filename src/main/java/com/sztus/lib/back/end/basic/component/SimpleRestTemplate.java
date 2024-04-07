package com.sztus.lib.back.end.basic.component;


import com.sztus.lib.back.end.basic.config.RestTemplateConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author MAX
 * @date 2022.09.09
 */
@Component
public class SimpleRestTemplate extends BaseRestTemplate {

    private final RestTemplateConfiguration restTemplateConfiguration;

    public SimpleRestTemplate(RestTemplateConfiguration restTemplateConfiguration) {
        this.restTemplateConfiguration = restTemplateConfiguration;
    }

    @Override
    protected RestTemplate restTemplate() {
        return restTemplateConfiguration.getRestTemplate();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate();
    }

}
