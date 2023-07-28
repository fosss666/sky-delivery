package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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

    /**
     * 套餐停售、启售
     */
    @Update("update sky_take_out.setmeal set status=#{status} where id=#{id}")
    void updateStatus(Integer status, Long id);

    /**
     * 动态条件查询套餐
     *
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     *
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from sky_take_out.setmeal_dish sd left join sky_take_out.dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据条件统计套餐数量
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
