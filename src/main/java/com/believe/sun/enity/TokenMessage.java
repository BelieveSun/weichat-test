package com.believe.sun.enity;

/**
 * Created by sungj on 17-5-15.
 */
public class TokenMessage {
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private String openid;
    private String scope;

    public TokenMessage() {
    }

    public TokenMessage(String accessToken, Long expiresIn, String refreshToken, String openid, String scope) {

        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.openid = openid;
        this.scope = scope;
    }

    public String getAccessToken() {

        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
