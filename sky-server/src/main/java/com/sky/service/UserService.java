package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author: fosss
 * Date: 2023/7/18
 * Time: 20:18
 * Description:
 */
public interface UserService {
    /**
     * 微信登录
     */
    User login(UserLoginDTO userLoginDTO);
}
