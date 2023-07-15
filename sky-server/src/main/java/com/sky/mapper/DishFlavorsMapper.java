package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/7/15
 * Time: 18:38
 * Description:
 */
@Mapper
public interface DishFlavorsMapper {
    /**
     * 批量插入口味
     *
     * @param flavors
     */
    void insertBatch(@Param("flavors") List<DishFlavor> flavors);
}
