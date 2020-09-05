package com.maodou.touchtransferpc.controller;

import com.maodou.touchtransferpc.dto.RestResult;
import com.maodou.touchtransferpc.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description TODO
 * @Author Wang Yucui
 * @Date 9/4/2020 2:51 PM
 */
@RestController
@Slf4j
@RequestMapping(value = "/image")
public class ImageController {
    @Autowired
    ImageService imageService;

    @ResponseBody
    @PostMapping(value = "/upload")
    public RestResult<Object> uploadImg(@RequestParam("files") MultipartFile[] files, String ifOpen) {
        log.info("Controller层,method[uploadImg]调用开始！");
        RestResult<Object> info = imageService.uploadImg(files, ifOpen);
        log.info("Controller层,method[uploadImg]调用结束！");
        return info;
    }

}
