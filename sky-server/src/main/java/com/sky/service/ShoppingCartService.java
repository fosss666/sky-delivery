package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @author: fosss
 * Date: 2023/7/19
 * Time: 20:36
 * Description:
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
