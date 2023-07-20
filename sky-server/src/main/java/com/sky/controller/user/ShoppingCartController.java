package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/19
 * Time: 20:33
 * Description:购物车相关接口
 */
@Api(tags = "购物车相关接口")
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     */
    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     */
    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("查看购物车");
        List<ShoppingCart> list = shoppingCartService.list();
        return Result.success(list);
    }

    /**
     * 清空购物车
     */
    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result clear() {
        log.info("清空购物车");
        shoppingCartService.clear();
        return Result.success();
    }

    /**
     * 删除购物车中的一个商品
     */
    @ApiOperation("删除购物车中的一个商品")
    @PostMapping("/sub")
    public Result removeOne(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中的一个商品:{}", shoppingCartDTO);
        shoppingCartService.removeOne(shoppingCartDTO);
        return Result.success();
    }
}




















