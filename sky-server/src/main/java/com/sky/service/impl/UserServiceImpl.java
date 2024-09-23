package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.common.utils.HttpUtil;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.vo.UserLoginVO;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wyj
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    private String getOpenId(String code){
        Map<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");

        String json = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", map);

        JSONObject jsonObject = JSON.parseObject(json);

        String openid = jsonObject.getString("openid");
        return openid;
    }

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //1 调用微信官方接口获取当前用户的openId
        String openid=getOpenId(userLoginDTO.getCode());


        //2 id为空登陆失败
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        }

        //3判断是否为新用户，若是就自动注册
        User user=userMapper.getByOpenId(openid);
        if(user==null){
            //创建user
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //4返回用户对象
        return user;

    }
}
