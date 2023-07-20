package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/19
 * Time: 20:37
 * Description:
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     */
    @Transactional
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //先判断购物车是否存在，如果存在则修改数量，否则添加购物车
        ShoppingCart sc = shoppingCartMapper.queryByConditions(shoppingCart);
        if (sc != null) {
            //存在该购物车，则在数量上加1
            shoppingCartMapper.increNumber(sc.getId());
        } else {
            //判断要添加的是套餐还是菜品，添加购物车
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //要添加的是菜品，构造对象
                //查询菜品
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());

            } else {
                //要添加的是套餐，构造套餐对象
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            //添加购物车
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     */
    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.list();
    }

    /**
     * 清空购物车
     */
    @Override
    public void clear() {
        shoppingCartMapper.clear();
    }

    /**
     * 删除购物车中的一个商品
     */
    @Override
    public void removeOne(ShoppingCartDTO shoppingCartDTO) {
        //查询该商品，如果数量>1，则数量-1，否则直接删除
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart sc = shoppingCartMapper.queryByConditions(shoppingCart);

        if (sc != null && sc.getNumber() > 1) {
            shoppingCartMapper.decNumber(sc.getId());
        } else if (sc != null) {
            //直接删除
            shoppingCartMapper.deleteOne(sc.getId());
        }

    }
}


















