package com.believe.sun.api;


import com.believe.sun.Util;
import com.believe.sun.enity.TokenMessage;
import com.believe.sun.enity.WeiUserInfo;
import net.dongliu.requests.Response;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by sungj on 17-5-12.
 */
@RestController
@RequestMapping("/weichat")
public class WeiChatTestAPI {

    private Logger logger = Logger.getLogger(WeiChatTestAPI.class);

    private String token = "weixinsun123456";

    private String state = "weichattest";

    private String appid = "";

    private String secret = "";

    private String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

    private String weiUserUrl = "https://api.weixin.qq.com/sns/userinfo";

    @RequestMapping("/test")
    public ResponseEntity<Map<String, Object>> test(){
        Map<String,Object> map = new HashMap<>();
        map.put("test","hello !");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping("/callback")
    public ResponseEntity<Map<String, Object>> callback(@RequestParam(value = "code",required = false) String code,
                                                        @RequestParam(value = "state",required = false) String state){
        logger.info("code : "+code+"  state : "+state);
        if(!this.state.equals(state)) return null;
        try {
            TokenMessage accessToken = getAccessToken(code);
            WeiUserInfo weiUserInfo = getWeiUserInfo(accessToken.getAccessToken(), accessToken.getOpenid());
            Map<String,Object> map = new HashMap<>();
            map.put("data",weiUserInfo);
            map.put("error",0);
            return new ResponseEntity<>(map,HttpStatus.OK);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WeiUserInfo getWeiUserInfo(String accessToken,String oppenId) throws JSONException {
        Map<String,Object> params = new HashMap<>();
        params.put("access_token",accessToken);
        params.put("oppenId",oppenId);
        params.put("lang","zh_CN");
        Response<String> response = Util.get(weiUserUrl, params);
        if(response.getStatusCode() == 200){

            JSONObject jsonObject = new JSONObject(response.getBody());
            WeiUserInfo userInfo = new WeiUserInfo();
            userInfo.setOpenid(jsonObject.getString("openId"));
            userInfo.setNickname(jsonObject.getString("nickname"));
            userInfo.setSex((byte) jsonObject.getInt("sex"));
            userInfo.setProvince(jsonObject.getString("province"));
            userInfo.setCity(jsonObject.getString("city"));
            userInfo.setCountry(jsonObject.getString("country"));
            //TODO:应该下载图片，保存到本地，引用本地url
            userInfo.setHeadimgurl(jsonObject.getString("headimagurl"));
            userInfo.setPrivilege(jsonObject.getJSONArray("privilege").toString());
            userInfo.setUnionid(jsonObject.getString("unionid"));
            return userInfo;
        }
        //TODO:
        return null;
    }


    private TokenMessage getAccessToken(String code) throws JSONException {
        Map<String,Object> params = new HashMap<>();
        params.put("appid",appid);
        params.put("secret",secret);
        params.put("code",code);
        params.put("grant_type","authorization_code");
        Response<String> response = Util.get(tokenUrl, params);
        if(response.getStatusCode() == 200){
            String body = response.getBody();
            JSONObject jsonObject = new JSONObject(body);

            String accessToken = jsonObject.getString("access_token");
            Long expires_in = jsonObject.getLong("expires_in");
            String refresh_token = jsonObject.getString("refresh_token");
            String openid = jsonObject.getString("openid");
            String scope = jsonObject.getString("scope");
            //TODO:accessToken 是否要重复使用？
            //TODO:用户信息应该调用一次,以后应该从本地获取用户信息
            TokenMessage tokenMessage = new TokenMessage(accessToken,expires_in,refresh_token,openid,scope);
            return tokenMessage;
        }
        //TODO:
        return null;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String weichat(HttpServletResponse response,
                                  @RequestParam(value = "signature",required = false) String signature,
                                  @RequestParam(value = "timestamp",required = false) String timestamp,
                                  @RequestParam(value = "nonce",required = false) String nonce,
                                  @RequestParam(value = "echostr",required = false) String echostr){
        boolean c = false;
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/xml; utf-8");
        try {
            String sha1 = getSHA1(token, timestamp, nonce, "");
            logger.debug("sha1 "+sha1);
            c = checkSignature(signature, timestamp, nonce);
        }catch (Exception e){
            return "esc";
        }

        if(c){
            logger.debug("success auth, return "+echostr );
            try {
                response.getWriter().write(echostr);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private   String getSHA1(String token, String timestamp, String nonce, String encrypt)
    {
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

    public  boolean checkSignature(String signature, String timestamp, String nonce) {
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
}
