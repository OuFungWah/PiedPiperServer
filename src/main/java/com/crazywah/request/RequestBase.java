package com.crazywah.request;

import com.crazywah.common.NIMInfo;
import com.crazywah.tools.CheckSumBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Random;

public abstract class RequestBase {

    protected LinkedMultiValueMap<String,String> params = new LinkedMultiValueMap<>();

    /**
     * 返回请求目标Url
     * @return
     */
    abstract protected String getUrl();

    /**
     * NIM接口说明
     * 所有接口都只支持POST请求；
     * 所有接口请求Content-Type类型为：application/x-www-form-urlencoded;charset=utf-8；
     * 所有接口返回类型为JSON，同时进行UTF-8编码。
     * @return
     */
    protected String requestToNIM(){
        HttpEntity<LinkedMultiValueMap> requestEntity = new HttpEntity<>(params,buildNIMHeaders());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        ResponseEntity<String> responseEntity = restTemplate.exchange(getUrl(),HttpMethod.POST,requestEntity,String.class);
        return responseEntity.getBody();
    }

    /**
     *
     * @return 返回包装好的请求头
     */
    public static HttpHeaders buildNIMHeaders(){
        String appKey = NIMInfo.APP_KEY;
        long curTime = System.currentTimeMillis()/1000L;
        long random = new Random().nextLong();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("AppKey",appKey);
        httpHeaders.add("Nonce",random+"");
        httpHeaders.add("CurTime",curTime+"");
        httpHeaders.add("CheckSum",CheckSumBuilder.getCheckSum(NIMInfo.APP_SECRET,random+"",curTime+""));
        httpHeaders.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }

}
