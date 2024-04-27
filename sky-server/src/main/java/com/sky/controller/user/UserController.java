package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wyj
 * @version 1.0
 */
@RestController
@Api(tags="C端用户登录相关接口")
@Slf4j
@RequestMapping("/user/user")
public class UserController {
//写了个todo
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;
    /**
     * 微信登陆
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登陆")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){

        log.info("接受的微信登陆参数{}",userLoginDTO);
        //微信登陆
        User user=userService.wxLogin(userLoginDTO);

        Map<String, Object>claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());

        //生成jwt令牌
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(),

                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder().id(user.getId())
                .openid(user.getOpenid())
                .token(token).build();
        return Result.success(userLoginVO);
    }
}
