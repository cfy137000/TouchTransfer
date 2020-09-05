package com.maodou.touchtransferpc.service.serviceImpl;

import cn.hutool.core.date.DateUtil;
import com.maodou.touchtransferpc.component.Config;
import com.maodou.touchtransferpc.constant.PathConstant;
import com.maodou.touchtransferpc.dto.RestResult;
import com.maodou.touchtransferpc.service.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @Description 图片接口实现类
 * @Author Wang Yucui
 * @Date 9/4/2020 2:54 PM
 */
@Service
public class ImageServiceImple implements ImageService {
    private String fileDirPath;
    private File filedir;
    private String subPath;

    /**
     * @Description: 上传图片
     * @param: MultipartRequest file
     * @return：RestResult<Object>
     */
    @Override
    public RestResult<Object> uploadImg(MultipartFile[] files, String ifOpen) {
        createDir();
        for (int i = 0; i < files.length; i++) {
            String originalFilename = files[i].getOriginalFilename();
            writeUploadFile(files[i], originalFilename);
        }
        if ("open".equals(ifOpen)) {
            try {
                filedir = new File(fileDirPath);
                Desktop.getDesktop().open(filedir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new RestResult<>(200, "success");
    }

    private void writeUploadFile(MultipartFile file, String fileName) {

        InputStream input = null;
        FileOutputStream fos = null;
        try {
            input = file.getInputStream();
            StringBuilder nameBuilder = new StringBuilder();
            String realName = nameBuilder
                    .append(Config.getINSTANCE().getStorePath())
                    .append("/")
                    .append(subPath)
                    .append("/")
                    .append(fileName)
                    .toString();
            fos = new FileOutputStream(realName);
            IOUtils.copy(input, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(fos);
        }
    }

    private void createDir() {
        subPath= DateUtil.today();
        fileDirPath = Config.getINSTANCE().getStorePath() + "/" + subPath;
        File imgaeFolderDir = new File(Config.getINSTANCE().getStorePath());
        if (!imgaeFolderDir.exists()) {
            imgaeFolderDir.mkdirs();
        }
        File fileDir = new File(fileDirPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }
}
