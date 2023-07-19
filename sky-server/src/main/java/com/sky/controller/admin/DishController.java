package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author: fosss
 * Date: 2023/7/15
 * Time: 18:31
 * Description:菜品接口
 */
@Api(tags = "菜品相关接口")
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 清除缓存
     */
    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 新增菜品
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result saveDishWithFlavors(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveDishWithFlavors(dishDTO);
        //清除该分类的缓存
        String key = RedisConstant.USER_DISH_PREFIX + dishDTO.getCategoryId();
        clearCache(key);

        return Result.success();
    }

    /**
     * 菜品分页查询
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> searchPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜单分页查询：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.searchPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result deleteByIds(@RequestParam("ids") List<Long> ids) {
        log.info("删除菜品：{}", ids);
        dishService.deleteByIds(ids);

        //清除所有缓存
        String key = RedisConstant.USER_DISH_PREFIX + "*";
        clearCache(key);

        return Result.success();
    }

    /**
     * 根据id查询菜品
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable("id") Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品：{}", dishDTO);
        dishService.updateDish(dishDTO);

        //清除所有缓存
        String key = RedisConstant.USER_DISH_PREFIX + "*";
        clearCache(key);

        return Result.success();
    }

    /**
     * 菜品停售启售
     *
     * @param id 菜品id
     */
    @ApiOperation("菜品停售启售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info("菜品停售启售：{} {}", status, id);
        dishService.updateStatus(status, id);

        //清除所有缓存
        String key = RedisConstant.USER_DISH_PREFIX + "*";
        clearCache(key);

        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> searchDishesByCategoryId(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                       @RequestParam(value = "name", required = false) String name) {
        log.info("根据分类id查询菜品:{} {}", categoryId, name);
        List<Dish> list = dishService.searchDishesByCategoryId(categoryId, name);
        return Result.success(list);
    }


}




















