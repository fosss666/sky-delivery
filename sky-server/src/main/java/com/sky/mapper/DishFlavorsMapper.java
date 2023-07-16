package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/15
 * Time: 18:38
 * Description:
 */
@Mapper
public interface DishFlavorsMapper {
    /**
     * 批量插入口味
     *
     * @param flavors
     */
    void insertBatch(@Param("flavors") List<DishFlavor> flavors);

    /**
     * 根据菜品id删除口味表中的口味
     *
     * @param ids
     */
    void deleteBatch(@Param("ids") List<Long> ids);

    /**
     * 根据菜品id查询口味
     *
     * @param id 菜品id
     * @return 所有口味
     */
    @Select("select * from sky_take_out.dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getByDishId(@Param("dishId") Long id);
}
