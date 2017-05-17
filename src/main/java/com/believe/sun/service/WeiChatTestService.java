package com.believe.sun.service;

import com.believe.sun.enity.TokenMessage;
import com.believe.sun.enity.WeiUserInfo;

/**
 * Created by sungj on 17-5-16.
 */
public interface WeiChatTestService {

    TokenMessage getAccessToken(String code);

    WeiUserInfo getWeiUserInfo(String accessToken, String oppenId);

    String getSHA1(String timestamp, String nonce, String encrypt);

    boolean checkState(String state);

    boolean checkSignature(String signature, String timestamp, String nonce);
}
