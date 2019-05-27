package top.timebook.xdonlineeducationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.timebook.xdonlineeducationproject.config.WeChatConfig;
import top.timebook.xdonlineeducationproject.domain.User;
import top.timebook.xdonlineeducationproject.mapper.UserMapper;
import top.timebook.xdonlineeducationproject.service.UserService;
import top.timebook.xdonlineeducationproject.utils.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description 用户业务类
 * @Author xiongzl
 * @Date 2019/5/7 16:18
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    private final WeChatConfig weChatConfig;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(WeChatConfig weChatConfig, UserMapper userMapper) {
        this.weChatConfig = weChatConfig;
        this.userMapper = userMapper;
    }


    @Override
    public User saveWechatUser(String code) {
        // 获取Access_Token
        Map<String, Object> baseMap = getAccessToken(code);
        if (null == baseMap) {
            return null;
        }
        String accessToken = (String) baseMap.get("access_token");
        String openid = (String) baseMap.get("openid");

        // 判断用户时候注册
        User user = userMapper.findByOpendid(openid);
        if (null != user) {
            return user;
        }

        // 获取用户基本信息
        Map<String, Object> userMap = getUserInfo(accessToken, openid);
        if (null == userMap) {
            return null;
        }
        // 封装用户信息
        user = wrapUser(userMap, openid);
        userMapper.save(user);
        return user;
    }

    /**
     * @Author xiongzl
     * @Description 包装用户对象
     * @Date 2019/5/15 20:24 
     * @Param [userMap, openid]
     * @Return top.timebook.xdonlineeducationproject.domain.User
     **/
    private User wrapUser(Map<String, Object> userMap, String openid) {
        Double sexTemp = (Double) userMap.get("sex");
        int sex = sexTemp.intValue();
        String province = (String) userMap.get("province");
        String city = (String) userMap.get("city");
        String country = (String) userMap.get("country");
        String headimgurl = (String) userMap.get("headimgurl");
        String nickname = (String) userMap.get("nickname");
        String addr = country + "-" + province + "-" + city;
        try {
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            addr = new String(addr.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new User(openid, nickname, headimgurl, null, null, sex, addr, new Date());
    }

    /**
     * @Author xiongzl
     * @Description 获取token
     * @Date 2019/5/15 20:24
     * @Param [code]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private Map<String, Object> getAccessToken(String code) {
        String accessTokenUrl = String.format(WeChatConfig.OPEN_ACCESS_TOKEN_URL, weChatConfig.getOpenAppId(), weChatConfig.getOpenAppsecret(), code);
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);
        if (null == baseMap || baseMap.isEmpty()) {
            return null;
        }
        return baseMap;
    }

    /**
     * @Author xiongzl
     * @Description 获取用户信息
     * @Date 2019/5/15 20:24
     * @Param [accessToken, openid]
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private Map<String, Object> getUserInfo(String accessToken, String openid) {
        String userInfoUrl = String.format(WeChatConfig.OPEN_USER_INFO_URL, accessToken, openid);
        Map<String, Object> userMap = HttpUtils.doGet(userInfoUrl);
        if (null == userMap || userMap.isEmpty()) {
            return null;
        }
        return userMap;
    }


}
