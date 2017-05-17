package com.believe.sun.service.impl;

import com.believe.sun.enity.WeiUserInfo;
import com.believe.sun.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by sungj on 17-5-17.
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public WeiUserInfo save(WeiUserInfo weiUserInfo) {
        //TODO:save to database
        return weiUserInfo;
    }
}
