package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/15
 * Time: 18:33
 * Description:
 */
public interface DishService {
    /**
     * 新增菜品
     */
    void saveDishWithFlavors(DishDTO dishDTO);

    /**
     * 菜品分页查询
     */
    PageResult searchPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     * @param ids
     */
    void deleteByIds(List<Long> ids);
    /**
     * 根据id查询菜品
     */
    DishVO getById(Long id);
    /**
     * 修改菜品
     */
    void updateDish(DishDTO dishDTO);
    /**
     * 菜品停售启售
     *
     * @param id 菜品id
     */
    void updateStatus(Integer status, Long id);
    /**
     * 根据分类id查询菜品
     */
    List<Dish> searchDishesByCategoryId(Long categoryId, String name);

}
