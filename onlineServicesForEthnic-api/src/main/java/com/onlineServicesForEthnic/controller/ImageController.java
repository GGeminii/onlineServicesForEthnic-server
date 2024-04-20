package com.onlineServicesForEthnic.controller;

import com.onlineServicesForEthnic.constant.MessageConstant;
import com.onlineServicesForEthnic.constant.ResultData;
import com.onlineServicesForEthnic.exception.ServiceException;
import com.onlineServicesForEthnic.utils.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
/*
  <p>图片接口</p>
  通常做一些图片的通用操作
 */
public class ImageController {

    private final AliOSSUtil aliOSSUtil;

    /**
     * 上传图片
     *
     * @param file form-data表单的上传文件
     * @return 图片的URL地址
     */
    @PostMapping("/upload")
    public ResultData<String> uploadImage(MultipartFile file) {
        if (file == null) {
            throw new ServiceException("请选择提交的文件");
        }
        try {
            return ResultData.ok(MessageConstant.SUCCESS, aliOSSUtil.upload(file));
        } catch (IOException e) {
            throw new ServiceException("图片上传服务器失败");
        }
    }
}
