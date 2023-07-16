package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

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
}
