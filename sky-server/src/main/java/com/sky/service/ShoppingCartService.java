package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

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
}
