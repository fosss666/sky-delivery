package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/16
 * Time: 11:24
 * Description:套餐菜品关联表
 */
@Mapper
public interface SetmealDishMapper {

    List<SetmealDish> getSetmealsByDishIds(@Param("ids") List<Long> ids);

    /**
     * 添加套餐菜品关系
     */
    void insertBatch(@Param("setmealDishes") List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id批量删除关系
     */
    void deleteBatchBySetmealIds(List<Long> ids);

    /**
     * 根据套餐id查询关联的菜品
     */
    @Select("select * from sky_take_out.setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getBySetmealId(Long id);
}
