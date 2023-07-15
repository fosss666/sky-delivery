package com.sky.service;

import com.sky.dto.DishDTO;

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
}
