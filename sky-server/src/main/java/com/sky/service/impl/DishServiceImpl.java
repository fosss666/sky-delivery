package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorsMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private SetmealDishMapper setmealDishMapper;

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

    /**
     * 菜品分页查询
     */
    @Override
    public PageResult searchPage(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.select(dishPageQueryDTO.getCategoryId(), dishPageQueryDTO.getName(), dishPageQueryDTO.getStatus());
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除菜品
     *
     * @param ids
     */
    @Transactional
    @Override
    public void deleteByIds(List<Long> ids) {
        //判断当前菜品是否存在起售中的
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish != null && dish.getStatus().equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException("菜品起售，不可删除");
            }
        }

        //判断当前菜品是否被套餐关联
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealsByDishIds(ids);
        if (setmealDishes != null && setmealDishes.size() > 0) {
            //有关联的
            throw new DeletionNotAllowedException("菜品与套餐关联，不能删除");
        }

        //根据菜品id删除口味表中的口味
        dishFlavorsMapper.deleteBatch(ids);

        //删除菜品表中的菜品
        dishMapper.deleteBatch(ids);

    }

    /**
     * 根据id查询菜品
     */
    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = new DishVO();
        //查询菜品信息
        Dish dish = dishMapper.getById(id);
        BeanUtils.copyProperties(dish, dishVO);
        //查询并设置口味信息
        List<DishFlavor> dishFlavors = dishFlavorsMapper.getByDishId(id);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /**
     * 修改菜品
     */
    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        Long dishId = dishDTO.getId();
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        ArrayList<Long> id = new ArrayList<>();
        id.add(dishId);

        //创建时间会丢失，不采用此方法
        ////先删除再增加
        //dishMapper.deleteBatch(id);
        //dishMapper.insert(dish);

        dishMapper.update(dish);

        //口味表没有创建时间，所以可以采用这个方法。先删除再增加口味
        if (dishDTO.getFlavors() != null && dishDTO.getFlavors().size() > 0) {
            dishFlavorsMapper.deleteBatch(id);

            //设置dishId！！！
            List<DishFlavor> dishFlavorList = dishDTO.getFlavors().stream().map(flavor -> {
                flavor.setDishId(dishId);
                return flavor;
            }).collect(Collectors.toList());
            //执行插入
            dishFlavorsMapper.insertBatch(dishFlavorList);
        }
    }

    /**
     * 菜品停售启售
     *
     * @param id 菜品id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        dishMapper.updateStatus(status, id);
    }

    /**
     * 根据分类id查询菜品
     */
    @Override
    public List<Dish> searchDishesByCategoryId(Long categoryId, String name) {
        return dishMapper.list(categoryId, name);

    }

    /**
     * 条件查询菜品和口味
     *
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish.getCategoryId(), dish.getName());

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorsMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}



















