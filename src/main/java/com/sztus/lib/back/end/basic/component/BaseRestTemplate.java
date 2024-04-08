package com.sztus.lib.back.end.basic.component;

import com.alibaba.fastjson.JSONObject;
import com.sztus.lib.back.end.basic.type.constant.CommonConst;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * @author MAX
 * @date 2022.09.09
 */
public abstract class BaseRestTemplate {

    /**
     * Http Get无参请求
     *
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T get(String url, Class<T> responseClass) {
        return restTemplate().getForObject(url, responseClass);
    }

    /**
     * Http Get无参请求 并且添加token
     *
     * @param url           url
     * @param accessToken   访问令牌
     * @param responseClass 响应类
     * @return {@link T}
     */
    public <T> T get(String url, String accessToken, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CommonConst.ACCESS_TOKEN, accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate().exchange(url, HttpMethod.GET, requestEntity, responseClass).getBody();

    }

    public <T> T getByParameter(String url, String accessToken, JSONObject paramJson, Class<T> responseClass) {
        URI uri = getUri(url, jsonObjectToMultiValueMap(paramJson));

        HttpHeaders headers = new HttpHeaders();
        headers.add(CommonConst.ACCESS_TOKEN, accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        return restTemplate().exchange(uri, HttpMethod.GET, requestEntity, responseClass).getBody();
    }


    /**
     * 直接映射泛型
     *
     * @param url
     * @param responseType
     * @return {@link ResponseEntity}<{@link T}>
     */
    public <T> T get(String url, MultiValueMap<String, String> paramMap, ParameterizedTypeReference<T> responseType) {
        URI uri = getUri(url, paramMap);
        return restTemplate().exchange(uri, HttpMethod.GET, null, responseType).getBody();
    }

    /**
     * 直接映射泛型
     *
     * @param url
     * @param responseType
     * @return {@link ResponseEntity}<{@link T}>
     */
    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType) {
        return restTemplate().exchange(url, HttpMethod.GET, null, responseType);
    }

    /**
     * Http Get有参请求
     *
     * @param url           请求地址，无需拼接参数
     * @param paramJson     JSON参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T getByQuery(String url, JSONObject paramJson, Class<T> responseClass) {
        MultiValueMap<String, String> paramMap = jsonObjectToMultiValueMap(paramJson);
        return getByQuery(url, paramMap, responseClass);
    }

    /**
     * Http Get有参请求
     *
     * @param url           请求地址，无需拼接参数
     * @param paramMap      Map参数，建议配合HttpParamVariable使用
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T getByQuery(String url, MultiValueMap<String, String> paramMap, Class<T> responseClass) {
        URI uri = getUri(url, paramMap);
        return restTemplate().getForObject(uri, responseClass);
    }

    /**
     * Http Post无参请求
     *
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T post(String url, Class<T> responseClass) {
        return exchange(HttpMethod.POST, url, responseClass);
    }


    /**
     * Http Post有参请求
     *
     * @param url           请求地址
     * @param paramJson     JSON参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T postByForm(String url, JSONObject paramJson, Class<T> responseClass) {
        MultiValueMap<String, String> paramMap = jsonObjectToMultiValueMap(paramJson);
        return exchangeByForm(HttpMethod.POST, url, paramMap, responseClass);
    }

    /**
     * Http Post有参请求
     *
     * @param url           请求地址
     * @param paramMap      Map参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T postByForm(String url, MultiValueMap<String, String> paramMap, Class<T> responseClass) {
        return exchangeByForm(HttpMethod.POST, url, paramMap, responseClass);
    }


    /**
     * Http Post有参请求 并映射泛型
     *
     * @param url          url
     * @param paramJson    param json
     * @param responseType 响应类型
     * @return {@link T}
     */
    public <T> T postByForm(String url, JSONObject paramJson, ParameterizedTypeReference<T> responseType) {
        MultiValueMap<String, String> paramMap = jsonObjectToMultiValueMap(paramJson);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> httpEntity = new HttpEntity<>(paramMap, headers);
        return restTemplate().exchange(url, HttpMethod.POST, httpEntity, responseType).getBody();
    }


    /**
     * Http Post有参请求
     *
     * @param url           请求地址
     * @param object        object参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T postByRequestBody(String url, Object object, Class<T> responseClass) {
        return exchangeByRequestBody(HttpMethod.POST, url, object, responseClass);
    }

    /**
     * Http Post有参请求 并添加token
     *
     * @param url           url
     * @param accessToken   访问令牌
     * @param object        对象
     * @param responseClass 响应类
     * @return {@link T}
     */
    public <T> T postByRequestBody(String url, String accessToken, Object object, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CommonConst.ACCESS_TOKEN, accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
        return restTemplate().exchange(url, HttpMethod.POST, requestEntity, responseClass).getBody();
    }

    /**
     * 直接映射泛型
     *
     * @param url
     * @param object
     * @param responseType
     * @return {@link ResponseEntity}<{@link T}>
     */
    public <T> T postByRequestBody(String url, Object object, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
        return restTemplate().exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
    }

    /**
     * 直接映射泛型并添加token
     *
     * @param url          url
     * @param accessToken  访问令牌
     * @param object       对象
     * @param responseType 响应类型
     * @return {@link ResponseEntity}<{@link T}>
     */
    public <T> T postByRequestBody(String url, String accessToken, Object object, ParameterizedTypeReference<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(CommonConst.ACCESS_TOKEN, accessToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(object, headers);
        return restTemplate().exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
    }


    /**
     * Http Put无参请求
     *
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T put(String url, Class<T> responseClass) {
        return exchange(HttpMethod.PUT, url, responseClass);
    }

    /**
     * Http Put有参请求
     *
     * @param url           请求地址
     * @param paramJson     JSON参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T putByForm(String url, JSONObject paramJson, Class<T> responseClass) {
        MultiValueMap<String, String> paramMap = jsonObjectToMultiValueMap(paramJson);
        return exchangeByForm(HttpMethod.PUT, url, paramMap, responseClass);
    }

    /**
     * Http Put有参请求
     *
     * @param url           请求地址
     * @param paramMap      Map参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T putByForm(String url, MultiValueMap<String, String> paramMap, Class<T> responseClass) {
        return exchangeByForm(HttpMethod.PUT, url, paramMap, responseClass);
    }

    /**
     * Http Put有参请求
     *
     * @param url           请求地址
     * @param object        object参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T putByRequestBody(String url, Object object, Class<T> responseClass) {
        return exchangeByRequestBody(HttpMethod.PUT, url, object, responseClass);
    }

    /**
     * Http Delete无参请求
     *
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T delete(String url, Class<T> responseClass) {
        return exchange(HttpMethod.DELETE, url, responseClass);
    }

    /**
     * Http Delete有参请求
     *
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T delete(String url, MultiValueMap<String, String> paramMap, Class<T> responseClass) {
        URI uri = getUri(url, paramMap);
        return exchange(HttpMethod.DELETE, uri.toString(), responseClass);
    }


    /**
     * Http Delete有参请求
     *
     * @param url           请求地址
     * @param paramJson     JSON参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T deleteByForm(String url, JSONObject paramJson, Class<T> responseClass) {
        MultiValueMap<String, String> paramMap = jsonObjectToMultiValueMap(paramJson);
        return exchangeByForm(HttpMethod.DELETE, url, paramMap, responseClass);
    }

    /**
     * Http Delete有参请求
     *
     * @param url           请求地址
     * @param paramMap      Map参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T deleteByForm(String url, MultiValueMap<String, String> paramMap, Class<T> responseClass) {
        return exchangeByForm(HttpMethod.DELETE, url, paramMap, responseClass);
    }

    /**
     * Http Delete有参请求
     *
     * @param url           请求地址
     * @param object        object参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T deleteByRequestBody(String url, Object object, Class<T> responseClass) {
        return exchangeByRequestBody(HttpMethod.DELETE, url, object, responseClass);
    }

    /**
     * Http Post有参请求
     *
     * @param url           请求地址
     * @param object        object参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T postByRequestBody(String url, Object object, MultiValueMap<String, String> headerMap, Class<T> responseClass) {
        return exchangeByRequestBody(HttpMethod.POST, url, object, headerMap, responseClass);
    }

    /**
     * Http Put有参请求
     *
     * @param url           请求地址
     * @param object        object参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T putByRequestBody(String url, Object object, MultiValueMap<String, String> headerMap, Class<T> responseClass) {
        return exchangeByRequestBody(HttpMethod.PUT, url, object, headerMap, responseClass);
    }

    /**
     * Http Get无参请求
     *
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T get(String url, MultiValueMap<String, String> headerMap, Class<T> responseClass) {
        return exchange(HttpMethod.GET, url, headerMap, responseClass);
    }

    /**
     * @param method        HttpMethod GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    public <T> T exchange(HttpMethod method, String url, MultiValueMap<String, String> headerMap, Class<T> responseClass) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headerMap);
        ResponseEntity<T> response = restTemplate().exchange(url, method, httpEntity, responseClass);
        return response.getBody();
    }

    /**
     * @param method        HttpMethod GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
     * @param url           请求地址
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    private <T> T exchange(HttpMethod method, String url, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<T> response = restTemplate().exchange(url, method, httpEntity, responseClass);
        return response.getBody();
    }

    /**
     * @param method        HttpMethod GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
     * @param url           请求地址
     * @param paramsMap     Map参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    private <T> T exchangeByForm(HttpMethod method, String url, MultiValueMap<String, String> paramsMap, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> httpEntity = new HttpEntity<>(paramsMap, headers);
        ResponseEntity<T> response = restTemplate().exchange(url, method, httpEntity, responseClass);
        return response.getBody();
    }

    /**
     * @param method        HttpMethod GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
     * @param url           请求地址
     * @param object        body参数
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    private <T> T exchangeByRequestBody(HttpMethod method, String url, Object object, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(object, headers);
        ResponseEntity<T> response = restTemplate().exchange(url, method, httpEntity, responseClass);
        return response.getBody();
    }

    /**
     * 将JSONObject转成MultiValueMap类型
     *
     * @param paramJson json参数
     * @return MultiValueMap
     */
    private MultiValueMap<String, String> jsonObjectToMultiValueMap(JSONObject paramJson) {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> loopEntry : paramJson.entrySet()) {
            paramsMap.add(loopEntry.getKey(), loopEntry.getValue().toString());
        }
        return paramsMap;
    }

    /**
     * 将参数拼接到URL上
     *
     * @param url      请求地址
     * @param paramMap Map参数
     * @return URI
     */
    private URI getUri(String url, MultiValueMap<String, String> paramMap) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        builder.queryParams(paramMap);
        return builder.build().encode().toUri();
    }

    /**
     * @param method        HttpMethod GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
     * @param url           请求地址
     * @param object        body参数
     * @param headerMap     请求头
     * @param responseClass 指定响应类型
     * @param <T>           泛型
     * @return 返回指定类型对象
     */
    private <T> T exchangeByRequestBody(HttpMethod method, String url, Object object, MultiValueMap<String, String> headerMap, Class<T> responseClass) {
        headerMap.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> httpEntity = new HttpEntity<>(object, headerMap);
        ResponseEntity<T> response = restTemplate().exchange(url, method, httpEntity, responseClass);
        return response.getBody();
    }

    protected abstract RestTemplate restTemplate();
}
