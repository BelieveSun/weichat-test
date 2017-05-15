package com.believe.sun;

import net.dongliu.requests.BaseRequestBuilder;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

import java.util.Map;

/**
 * Created by sungj on 17-5-15.
 */
public class Util {

    public static Response<String> get(String url, Map<String,Object> params){
        BaseRequestBuilder baseRequestBuilder = Requests.get(url);
        if(params != null)
            baseRequestBuilder.params(params);
        return baseRequestBuilder.verify(false).text();
    }
}
