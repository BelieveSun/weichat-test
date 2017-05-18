package com.believe.sun.service;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by sungj on 17-5-17.
 */
public interface UtilService {
    String saveImage(String urlString, String saveUrl,String fileName,boolean isfilename) throws IOException, JSONException;
}
