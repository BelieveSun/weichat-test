package com.believe.sun.service.impl;

import com.believe.sun.util.Util;
import com.believe.sun.enity.TokenMessage;
import com.believe.sun.enity.WeiUserInfo;
import com.believe.sun.service.WeiChatTestService;
import net.dongliu.requests.Response;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sungj on 17-5-16.
 */
@Service
public class WeiChatTestServiceImpl implements WeiChatTestService {

    Logger logger = Logger.getLogger(WeiChatTestService.class);
    @Value("${weichat_token}")
    private String token;
    @Value("${weichat_state}")
    private String state;
    @Value("${weichat_appid}")
    private String appid;
    @Value("${weichat_secret}")
    private String secret;
    @Value("${weichat_tokenUrl}")
    private String tokenUrl;
    @Value("${weichat_weiUserUrl}")
    private String weiUserUrl;
    @Value("${weichat_saveUrl}")
    private String saveUrl;
    @Value("${weichat_imgurl}")
    private String imgurl;
    @Value("${weichat_isFileName}")
    private boolean isFileName;

    //TODO:accessToken 是否要重复使用？
    //TODO:用户信息应该调用一次,以后应该从本地获取用户信息
    @Override
    public TokenMessage getAccessToken(String code) {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("secret",secret);
        params.put("code",code);
        params.put("grant_type","authorization_code");
        Response<String> response = Util.get(tokenUrl, params);
        if(response.getStatusCode() == 200){
            String body = response.getBody();
            try {
                JSONObject jsonObject = new JSONObject(body);
                if(jsonObject.has("errcode")){
                    logger.debug("can't get url :"+tokenUrl+" response:{ errcode :"+jsonObject.getString("errcode")
                            +" errmsg :"+jsonObject.getString("errmsg")+" }");
                    return null;
                }
                String accessToken = jsonObject.getString("access_token");
                Long expires_in = jsonObject.getLong("expires_in");
                String refresh_token = jsonObject.getString("refresh_token");
                String openid = jsonObject.getString("openid");
                String scope = jsonObject.getString("scope");
                return new TokenMessage(accessToken,expires_in,refresh_token,openid,scope);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        logger.debug("can't get url :"+tokenUrl+" response: { status :"+response.getStatusCode()
                + " body :"+response.getBody());
        return null;
    }

    @Override
    public WeiUserInfo getWeiUserInfo(String accessToken, String oppenId) {
        Map<String,Object> params = new HashMap<>();
        params.put("access_token",accessToken);
        params.put("openid",oppenId);
        params.put("lang","zh_CN");
        Response<String> response = Util.get(weiUserUrl, params);
        if(response.getStatusCode() == 200){
            String json = null;
            try {
                json = new String(response.getBody().getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (json == null) return null;
            try {
                JSONObject jsonObject = new JSONObject(json);
                if(jsonObject.has("errcode")){
                    String errcode = jsonObject.getString("errcode");
                    String errmsg = jsonObject.getString("errmsg");
                    logger.debug("can't get url :"+weiUserUrl+" response:{ errcode :"+errcode+" errmsg :"+errmsg+" }");
                    return null;
                }
                WeiUserInfo userInfo = new WeiUserInfo();
                userInfo.setOpenid(jsonObject.getString("openid"));
                userInfo.setNickname(jsonObject.getString("nickname"));
                userInfo.setSex((byte) jsonObject.getInt("sex"));
                userInfo.setProvince(jsonObject.getString("province"));
                userInfo.setCity(jsonObject.getString("city"));
                userInfo.setCountry(jsonObject.getString("country"));
                //download image and save to local　server
                String headimgurl = jsonObject.getString("headimgurl");
                String saveUrl = this.saveUrl+"/"+userInfo.getOpenid()+".ico";
                String imgurl = this.imgurl+"/"+userInfo.getOpenid()+".ico";
                Util.saveImage(headimgurl,saveUrl,isFileName);
                logger.debug("saveUrl : "+saveUrl+" , imgurl : "+imgurl);
                userInfo.setHeadimgurl(imgurl);
                userInfo.setPrivilege(jsonObject.getJSONArray("privilege").toString());
                if(jsonObject.has("unionid"))
                    userInfo.setUnionid(jsonObject.getString("unionid"));
                return userInfo;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug("can't get url :"+weiUserUrl+" response: { status :"+response.getStatusCode()
                + " body :"+response.getBody());
        return null;
    }

    @Override
    public String getSHA1(String timestamp, String nonce, String encrypt) {
        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt };
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public boolean checkState(String state) {
        if(this.state.equals(state)) return true;
        return false;
    }

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] arr = new String[] { token, timestamp, nonce };
        // 将 token、timestamp、nonce 三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行 sha1 加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        content = null;
        // 将 sha1 加密后的字符串可与 signature 对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }


}
