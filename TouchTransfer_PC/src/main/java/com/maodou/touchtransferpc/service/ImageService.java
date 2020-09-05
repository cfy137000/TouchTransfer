package com.maodou.touchtransferpc.service;

import com.maodou.touchtransferpc.dto.RestResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description 图片接口
 * @Author Wang Yucui
 * @Date 9/4/2020 2:53 PM
 */
public interface ImageService {
    /**
     * @Description: 上传图片
     * @param: MultipartRequest file
     * @return：RestResult<Object>
     */

    RestResult<Object> uploadImg( MultipartFile[] files, String id);

}
