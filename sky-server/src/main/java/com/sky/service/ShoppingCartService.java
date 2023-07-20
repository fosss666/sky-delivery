package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

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
    /**
     * 查看购物车
     */
    List<ShoppingCart> list();
    /**
     * 清空购物车
     */
    void clear();
    /**
     * 删除购物车中的一个商品
     */
    void removeOne(ShoppingCartDTO shoppingCartDTO);
}
