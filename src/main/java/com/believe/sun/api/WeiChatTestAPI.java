package com.believe.sun.api;


import com.believe.sun.enity.TokenMessage;
import com.believe.sun.enity.WeiUserInfo;
import com.believe.sun.result.BaseResult;
import com.believe.sun.result.DataResult;
import com.believe.sun.result.ErrorCode;
import com.believe.sun.service.UserService;
import com.believe.sun.service.WeiChatTestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by sungj on 17-5-12.
 */
@RestController
@RequestMapping("/weichat")
public class WeiChatTestAPI {

    private Logger logger = Logger.getLogger(WeiChatTestAPI.class);

    private final WeiChatTestService weiChatTestService;

    private final UserService userService;

    @Autowired
    public WeiChatTestAPI(WeiChatTestService weiChatTestService, UserService userService) {
        this.weiChatTestService = weiChatTestService;
        this.userService = userService;
    }

    @RequestMapping("/test")
    public ResponseEntity<Map<String, Object>> test(){
        Map<String,Object> map = new HashMap<>();
        map.put("test","hello !");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping("/callback")
    public ResponseEntity<BaseResult> callback(@RequestParam(value = "code",required = false) String code,
                                               @RequestParam(value = "state",required = false) String state) {
        logger.info("code : " + code + "  state : " + state);
        if (!weiChatTestService.checkState(state)){
            logger.debug("state not match");
            BaseResult result = new BaseResult();
            result.setError(ErrorCode.STATE_NOT_MATCH.getCode());
            result.setMessage(ErrorCode.STATE_NOT_MATCH.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        TokenMessage tokenMessage = weiChatTestService.getAccessToken(code);
        if(tokenMessage == null){
            logger.debug("can't get token");
            BaseResult result = new BaseResult();
            result.setError(ErrorCode.SERVER_INNER_ERROR.getCode());
            result.setMessage(ErrorCode.SERVER_INNER_ERROR.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        WeiUserInfo weiUserInfo = weiChatTestService.getWeiUserInfo(tokenMessage.getAccessToken(), tokenMessage.getOpenid());
        weiUserInfo = userService.save(weiUserInfo);
        if(weiUserInfo == null){
            logger.debug("can't get user info");
            BaseResult result = new BaseResult();
            result.setError(ErrorCode.SERVER_INNER_ERROR.getCode());
            result.setMessage(ErrorCode.SERVER_INNER_ERROR.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        DataResult<WeiUserInfo> dataResult = new DataResult<>();
        dataResult.setError(ErrorCode.SUCCESS.getCode());
        dataResult.setMessage(ErrorCode.SUCCESS.getMessage());
        dataResult.setData(weiUserInfo);
        return new ResponseEntity<>(dataResult,HttpStatus.OK);
    }



    @RequestMapping(value = "",method = RequestMethod.GET)
    public String weichat(HttpServletResponse response,
                                  @RequestParam(value = "signature",required = false) String signature,
                                  @RequestParam(value = "timestamp",required = false) String timestamp,
                                  @RequestParam(value = "nonce",required = false) String nonce,
                                  @RequestParam(value = "echostr",required = false) String echostr){
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "text/xml; utf-8");
        try {
            String sha1 = weiChatTestService.getSHA1(timestamp, nonce, "");
            logger.debug("sha1 "+sha1);
        }catch (Exception e){
            return "esc";
        }

        if(weiChatTestService.checkSignature(signature, timestamp, nonce)){
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

}
