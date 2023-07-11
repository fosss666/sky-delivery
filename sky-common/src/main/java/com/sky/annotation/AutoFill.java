package com.sky.annotation;

import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: fosss
 * Date: 2023/7/11
 * Time: 21:13
 * Description:自动填充注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //设置自动填充类型
    OperationType vlaue();
}
