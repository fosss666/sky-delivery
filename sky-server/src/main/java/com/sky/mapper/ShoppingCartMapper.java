package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/19
 * Time: 20:39
 * Description:
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * 动态sql条件查询
     *
     * @param shoppingCart
     * @return
     */
    ShoppingCart queryByConditions(ShoppingCart shoppingCart);

    /**
     * 修改购物车中的数量字段
     *
     * @param id
     */
    @Update("update sky_take_out.shopping_cart set number=number+1 where id=#{id}")
    void updateNumber(Long id);

    /**
     * 添加购物车
     *
     * @param shoppingCart
     */
    @Insert("insert into sky_take_out.shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, " +
            "create_time) VALUES (#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 查看购物车
     */
    @Select("select * from sky_take_out.shopping_cart")
    List<ShoppingCart> list();

    /**
     * 清空购物车
     */
    @Delete("delete from sky_take_out.shopping_cart")
    void clear();
}
