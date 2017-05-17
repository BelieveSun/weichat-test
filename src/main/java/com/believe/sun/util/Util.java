package com.believe.sun.util;

import net.dongliu.requests.BaseRequestBuilder;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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

    public static void saveImage(String urlString, String saveUrl,boolean isfilename) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        if(isfilename){
            // 输出的文件流
            OutputStream os = new FileOutputStream(saveUrl);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }else{

        }


    }
}
