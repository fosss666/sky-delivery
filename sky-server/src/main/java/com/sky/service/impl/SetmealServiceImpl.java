package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/17
 * Time: 15:50
 * Description:
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     */
    @Transactional
    @Override
    public void addSetmeal(SetmealDTO setmealDTO) {
        //套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);
        //套餐菜品关系表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐分页查询
     */
    @Override
    public PageResult searchPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.selectList(setmealPageQueryDTO.getCategoryId(), setmealPageQueryDTO.getName(), setmealPageQueryDTO.getStatus());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     */
    @Transactional
    @Override
    public void deleteBatch(List<Long> ids) {
        //查询所有套餐
        List<Setmeal> setmealList = setmealMapper.selectListByIds(ids);
        //判断是否有启售中的
        for (Setmeal setmeal : setmealList) {
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException("套餐启售，不可删除");
            }
        }

        //删除套餐
        setmealMapper.deleteBatch(ids);

        //删除套餐菜品关系
        setmealDishMapper.deleteBatchBySetmealIds(ids);
    }

    /**
     * 根据id查询套餐
     */
    @Transactional
    @Override
    public SetmealVO getById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        //查询套餐
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);
        //查询关联的菜品
        List<SetmealDish> setmealDishList = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishList);
        return setmealVO;
    }

    /**
     * 修改套餐
     */
    @Transactional
    @Override
    public void update(SetmealDTO setmealDTO) {
        //修改套餐表
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        //修改关联表  先删后添加
        List<Long> list = new ArrayList<>();
        list.add(setmeal.getId());
        setmealDishMapper.deleteBatchBySetmealIds(list);
        setmealDishMapper.insertBatch(setmealDTO.getSetmealDishes());

    }

    /**
     * 套餐停售、启售
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        setmealMapper.updateStatus(status, id);
    }
}


















