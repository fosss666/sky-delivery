package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/17
 * Time: 12:08
 * Description:套餐相关接口
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    /**
     * 新增套餐
     */
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    @ApiOperation("新增套餐")
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐分页查询
     */
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> searchPage(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询：{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.searchPage(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result deleteBatch(@RequestParam("ids") List<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     */
    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable("id") Long id) {
        log.info("根据id查询套餐：{}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("修改套餐")
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 套餐停售、启售
     */
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    @ApiOperation("套餐停售、启售")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable("status") Integer status, @RequestParam("id") Long id) {
        log.info("套餐停售、启售：{} {}", status == 1 ? "启售" : "停售", id);
        setmealService.updateStatus(status, id);
        return Result.success();
    }
}


















