package com.sky.constant;

/**
 * @author: fosss
 * Date: 2023/7/18
 * Time: 20:26
 * Description:微信登录常量
 */
public class WechatLoginConstant {
    public static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    public static final String LOGIN_PARAM_APPID = "appid";
    public static final String LOGIN_PARAM_SECRET = "secret";
    public static final String LOGIN_PARAM_JS_CODE = "js_code";
    public static final String LOGIN_PARAM_GRANT_TYPE = "grant_type";

    public static final String GRANT_TYPE_VALUE = "authorization_code";

    public static final String RESPONSE_OPENID = "openid";
}
