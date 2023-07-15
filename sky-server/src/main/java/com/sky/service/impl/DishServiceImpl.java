package com.sky.service.impl;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/15
 * Time: 18:34
 * Description:
 */
@Service
public class DishServiceImpl implements DishService {
    @Resource
    private DishMapper dishMapper;

    @Resource
    private DishFlavorsMapper dishFlavorsMapper;

    /**
     * 新增菜品
     */
    @Transactional
    @Override
    public void saveDishWithFlavors(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);


        //菜单表
        dishMapper.insert(dish);

        Long dishId = dish.getId();
        //口味表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            //设置dishId
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            //插入口味
            dishFlavorsMapper.insertBatch(flavors);
        }

    }
}



















