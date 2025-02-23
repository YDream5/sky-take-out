package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author wyj
 * @version 1.0
 */
public interface ShoppingCartService {
    /**
     * 添加到购物车
     * @param shoppingCartDTO
     */
    void save(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> list();

    void subCart(ShoppingCartDTO shoppingCartDTO);

    void clearCart();
}
