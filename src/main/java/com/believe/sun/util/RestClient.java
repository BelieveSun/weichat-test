package com.believe.sun.util;

import net.dongliu.requests.Response;

import java.util.List;
import java.util.Map;

/**
 * Created by tom on 2/23/16.
 */
public interface RestClient {
    Response<String> get(String url);
    Response<String> get(String url, Map<String, Object> params);
    Response<String> post(String url);
    Response<String> postJSON(String url, Map<String, Object> params);
    Response<String> postJSON(String url, List<Map<String, Object>> params);
    Response<String> postJSONArray(String url, List<Object> params);
    Response<String> postForm(String url, Map<String, Object> params);
    Response<String> postMultipart(String url, Map<String, Object> params, String fileAttribute, String fileName, byte[] file);
    Response<String> put(String url, Map<String, Object> params);
    Response<String> patch(String url, List<Map<String, Object>> params);
    Response<String> delete(String url);
}
