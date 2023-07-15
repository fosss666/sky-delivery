package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author: fosss
 * Date: 2023/7/14
 * Time: 21:35
 * Description:公共接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     *
     * @return 图片路径
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        //拼接图片名
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = file.getOriginalFilename();

        //在文件名称中添加随机唯一的值，防止上传文件时由于文件名相同而导致文件覆盖
        //replace是替换掉uuid中的‘-’
        String uuid = UUID.randomUUID().toString().replace("-", "");
        objectName = uuid + objectName;

        //将文件根据时间进行分类
        String time = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        objectName = time + "/" + objectName;

        //上传
        try {
            String path = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(path);
        } catch (IOException e) {
            log.error("文件上传失败");
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }

    }
}





















