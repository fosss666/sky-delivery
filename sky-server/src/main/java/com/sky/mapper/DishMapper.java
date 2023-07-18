package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from sky_take_out.dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insert(@Param("dish") Dish dish);

    /**
     * 条件查询
     *
     * @return
     */
    Page<DishVO> select(@Param("categoryId") Integer categoryId, @Param("name") String name, @Param("status") Integer status);

    /**
     * 删除菜品表中的菜品
     *
     * @param ids
     */
    void deleteBatch(@Param("ids") List<Long> ids);

    @Select("select  * from sky_take_out.dish where id=#{id}")
    Dish getById(Long id);

    /**
     * 修改菜品
     *
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    void update(@Param("dish") Dish dish);

    /**
     * 菜品停售启售
     *
     * @param id 菜品id
     */
    @Update("update sky_take_out.dish set status=#{status} where id=#{id}")
    void updateStatus(@Param("status") Integer status, @Param("id") Long id);

    /**
     * 根据分类id查询菜品
     */
    List<Dish> list(@Param("categoryId") Long categoryId, @Param("name") String name);

}
