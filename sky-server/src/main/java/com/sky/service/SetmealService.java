package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/17
 * Time: 15:50
 * Description:
 */
public interface SetmealService {
    /**
     * 新增套餐
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     */
    PageResult searchPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     */
    void deleteBatch(List<Long> ids);
}
