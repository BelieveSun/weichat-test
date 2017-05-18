package com.believe.sun.service.impl;

import com.believe.sun.service.UtilService;
import com.believe.sun.util.RestClient;
import net.dongliu.requests.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sungj on 17-5-15.
 */
@Service
public class UtilServiceImpl implements UtilService{

    Logger logger = Logger.getLogger(UtilServiceImpl.class);

    @Resource(name = "oauthClient")
    private RestClient restClient;



    public String saveImage(String urlString, String saveUrl,String filename,boolean isfilename) throws IOException, JSONException {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        if(isfilename){
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            saveUrl = saveUrl+File.separator+filename;
            OutputStream os = new FileOutputStream(saveUrl);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            return null;
        }
        byte[] bytes = IOUtils.toByteArray(is);
        Response<String> response = restClient.postMultipart(saveUrl, null, "uploadFile", filename, bytes);
        if(response.getStatusCode() == 200){
            JSONObject jsonObject = new JSONObject(response.getBody());
            int error = jsonObject.getInt("error");
            if(error != 0){
                logger.debug("error : "+error+" message : "+jsonObject.getString("message"));
                throw new RuntimeException("can't upload");
            }
            JSONObject data = jsonObject.getJSONObject("data");
            //关闭
            is.close();
            return data.getString("fileUrl");
        }else {
            logger.info("upload failed ,responseCode :"+response.getStatusCode());
            throw new RuntimeException();
        }
    }
}
