package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author wyj
 * @version 1.0
 */

@RestController("userShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/user/shop")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY="SHOP_STATUS";

    /**
     * 查询店铺状态
     * @return
     */
    @ApiOperation("查询店铺状态")
    @GetMapping("/status")
    public Result<Integer> checkStatus(){

        Integer status= (Integer) redisTemplate.opsForValue().get(KEY);

        log.info("店铺营业状态为{}",status==1?"营业中":"打样了");
        return Result.success(status);

    }
}
