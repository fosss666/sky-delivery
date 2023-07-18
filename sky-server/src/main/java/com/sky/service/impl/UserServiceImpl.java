package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.WechatLoginConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: fosss
 * Date: 2023/7/18
 * Time: 20:24
 * Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private WeChatProperties weChatProperties;
    @Resource
    private UserMapper userMapper;

    /**
     * 微信登录
     */
    @Transactional
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //构造参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put(WechatLoginConstant.LOGIN_PARAM_APPID, weChatProperties.getAppid());
        paramMap.put(WechatLoginConstant.LOGIN_PARAM_SECRET, weChatProperties.getSecret());
        paramMap.put(WechatLoginConstant.LOGIN_PARAM_JS_CODE, userLoginDTO.getCode());
        paramMap.put(WechatLoginConstant.LOGIN_PARAM_GRANT_TYPE, WechatLoginConstant.GRANT_TYPE_VALUE);

        String json = HttpClientUtil.doGet(WechatLoginConstant.WECHAT_LOGIN_URL, paramMap);

        //获取openid
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString(WechatLoginConstant.RESPONSE_OPENID);
        if (openid == null || openid.equals("")) {
            throw new LoginFailedException("登录失败，请重试");
        }

        //根据openid查询用户
        User user = userMapper.getByOpenid(openid);

        //判断是否注册过
        if (user == null) {
            //没注册过，则进行注册
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            //添加,级的返回用户id
            userMapper.insert(user);
        }

        return user;
    }
}

















