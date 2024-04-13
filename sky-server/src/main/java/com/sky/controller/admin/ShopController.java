package com.sky.controller.admin;

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

@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺相关接口")
@RequestMapping("/admin/shop")
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String KEY ="SHOP_STATUS";
    /**
     * 设置店铺营业状态
     *
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result setStatus(@PathVariable Integer status){

        log.info("店铺营业状态为{}",status==1?"营业中":"打样了");

        redisTemplate.opsForValue().set(KEY, status);

        return Result.success();

    }

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
