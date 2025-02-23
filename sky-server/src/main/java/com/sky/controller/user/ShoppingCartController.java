package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */

@Slf4j
@Api(tags = "购物车相关接口")
@RequestMapping("/user/shoppingCart")
@RestController
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加到购物车")
    public Result addToCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("接收到购物车信息{}",shoppingCartDTO);

        shoppingCartService.save(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        log.info("进入查看购物车");
       List<ShoppingCart> shoppingCarts= shoppingCartService.list();
       return Result.success(shoppingCarts);
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result subCart(@RequestBody ShoppingCartDTO shoppingCartDTO){

        log.info("接收到{}",shoppingCartDTO);
        shoppingCartService.subCart(shoppingCartDTO);
        return Result.success();

    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean(){
        log.info("进入清空购物车");
        shoppingCartService.clearCart();
        return Result.success();
    }
}
