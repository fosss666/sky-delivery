package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: fosss
 * Date: 2023/7/24
 * Time: 13:35
 * Description: 自定义定时任务类
 */
@Slf4j
@Component
public class MyTask {

    @Scheduled(cron = "0/5 * * * * ? ")
    public void test() {
        log.info("定时任务开始：{}", LocalDateTime.now());
    }
}



















