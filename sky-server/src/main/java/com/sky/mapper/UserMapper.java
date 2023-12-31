package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author: fosss
 * Date: 2023/7/18
 * Time: 20:34
 * Description:
 */
@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     */
    @Select("select * from sky_take_out.user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 添加用户
     */
    void insert(User user);

    @Select("select * from sky_take_out.user where id=#{userId}")
    User getById(Long userId);

    /**
     * 统计某时间段内用户数量
     */
    Integer countUserByMap(Map<String, LocalDateTime> map);
}
