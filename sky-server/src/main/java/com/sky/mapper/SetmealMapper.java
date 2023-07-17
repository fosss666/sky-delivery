package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 添加套餐
     */
    @AutoFill(OperationType.INSERT)
    void insert(@Param("setmeal") Setmeal setmeal);

    /**
     * 条件查询
     *
     * @return
     */
    Page<SetmealVO> selectList(@Param("categoryId") Integer categoryId, @Param("name") String name, @Param("status") Integer status);

    /**
     * 根据套餐id查询所有套餐
     */
    List<Setmeal> selectListByIds(List<Long> ids);

    /**
     * 批量删除
     */
    void deleteBatch(List<Long> ids);

    @Select("select * from sky_take_out.setmeal where id = #{id};")
    Setmeal getById(Long id);

    /**
     * 修改套餐表
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
