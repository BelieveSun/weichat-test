package com.believe.sun.util;

import net.dongliu.requests.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 2/23/16.
 */
@Component
public class RestClientImpl implements RestClient {
    private static final Logger logger = Logger.getLogger(RestClientImpl.class);

    private Map<String, Object> authHeaders;

    public RestClientImpl() {

    }

    public RestClientImpl(String clientId,String clientSecret) {
        authHeaders = new HashMap<>();
        authHeaders.put("X-client-id", clientId);
        authHeaders.put("X-client-secret", clientSecret);
        authHeaders.put("X-grant-type", "client_credentials");
    }

    @Override
    public Response<String> get(String url) {
        return get(url, null);
    }

    @Override
    public Response<String> get(String url, Map<String, Object> params) {
        BaseRequestBuilder requestBuilder = Requests.get(url);
        if (params != null) {
            requestBuilder = requestBuilder.params(params);
        }
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        return requestBuilder.verify(false).text();
    }

    @Override
    public Response<String> post(String url) {
        return postForm(url, null);
    }

    @Override
    public Response<String> postJSON(String url, Map<String, Object> params) {
        PostRequestBuilder requestBuilder = Requests.post(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        JSONObject postData = new JSONObject();
        for(Map.Entry<String, Object> param: params.entrySet()){
            try {
                postData.put(param.getKey(), param.getValue());
            } catch (JSONException e) {
                logger.error("Failed to build post data. Params: " + params.toString(), e);
            }
        }

        return requestBuilder.data(postData.toString()).verify(false).text();
    }

    @Override
    public Response<String> postJSON(String url, List<Map<String, Object>> params) {
        PostRequestBuilder requestBuilder = Requests.post(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        JSONArray postData = new JSONArray();
        for(Map<String, Object> object: params){
            try {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, Object> param : object.entrySet()) {
                    jsonObject.put(param.getKey(), param.getValue());
                }
                postData.put(jsonObject);
            } catch (JSONException e) {
                logger.error("Failed to build post data. Params: " + params.toString(), e);
            }
        }
        return requestBuilder.data(postData.toString()).verify(false).text();
    }

    @Override
    public Response<String> postJSONArray(String url, List<Object> params) {
        PostRequestBuilder requestBuilder = Requests.post(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        JSONArray postData = new JSONArray();
        for(Object object: params){
            postData.put(object);
        }
        return requestBuilder.data(postData.toString()).verify(false).text();
    }

    @Override
    public Response<String> postForm(String url, Map<String, Object> params) {
        PostRequestBuilder requestBuilder = Requests.post(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        if (params != null) {
            requestBuilder = requestBuilder.params(params);
        }

        return requestBuilder.verify(false).text();
    }

    @Override
    public Response<String> postMultipart(String url, Map<String, Object> params, String fileAttribute, String fileName, byte[] file) {
        PostRequestBuilder requestBuilder = Requests.post(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        if (params != null) {
            requestBuilder = requestBuilder.params(params);
        }

        return requestBuilder.addMultiPart(fileAttribute, "multipart/form-data", fileName, file).text();
    }

    @Override
    public Response<String> put(String url, Map<String, Object> params) {
        BodyRequestBuilder requestBuilder = Requests.put(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        JSONObject postData = new JSONObject();
        for(Map.Entry<String, Object> param: params.entrySet()){
            try {
                postData.put(param.getKey(), param.getValue());
            } catch (JSONException e) {
                logger.error("Failed to build post data. Params: " + params.toString(), e);
            }
        }

        return requestBuilder.data(postData.toString()).verify(false).text();
    }

    @Override
    public Response<String> patch(String url, List<Map<String, Object>> params) {
        BodyRequestBuilder requestBuilder = Requests.patch(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        requestBuilder.addHeader("Content-Type", "application/json");
        JSONArray postData = new JSONArray();
        for(Map<String, Object> object: params){
            try {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, Object> param : object.entrySet()) {
                    jsonObject.put(param.getKey(), param.getValue());
                }
                postData.put(jsonObject);
            } catch (JSONException e) {
                logger.error("Failed to build post data. Params: " + params.toString(), e);
            }
        }

        return requestBuilder.data(postData.toString()).verify(false).text();
    }

    @Override
    public Response<String> delete(String url) {
        BaseRequestBuilder requestBuilder = Requests.delete(url);
        if (authHeaders != null) {
            requestBuilder = requestBuilder.headers(authHeaders);
        }
        return requestBuilder.verify(false).text();
    }
}
