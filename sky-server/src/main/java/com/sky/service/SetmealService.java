package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

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

    /**
     * 根据id查询套餐
     */
    SetmealVO getById(Long id);

    /**
     * 修改套餐
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 套餐停售、启售
     */
    void updateStatus(Integer status, Long id);

    /**
     * 条件查询
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     *
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
