package com.ak.community.controller;

import ch.qos.logback.core.util.FileUtil;
import com.ak.community.dto.FileDTO;
import com.ak.community.provider.TPProvider;
import com.qcloud.cos.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.bouncycastle.asn1.bc.ObjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by codedrinker on 2019/6/26.
 */
@Controller
public class FileController {
    @Autowired
    TPProvider tpProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multfile = multipartRequest.getFile("editormd-image-file");

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multfile.getSize());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma","no-che");
        objectMetadata.setContentType(multfile.getContentType());
        objectMetadata.setContentDisposition("inline;filename=" + multfile.getName());

//        String fileName = multfile.getOriginalFilename();
//        // 获取文件后缀
//        String prefix=fileName.substring(fileName.lastIndexOf("."));
//        // 用uuid作为文件名，防止生成的临时文件重复
//        final File excelFile = File.createTempFile(String.valueOf(UUID.randomUUID()), prefix);
//        // MultipartFile to File
//        multfile.transferTo(excelFile);
        String fileName = tpProvider.upLoad(multfile.getName(), objectMetadata, multfile.getInputStream());

        FileDTO fileDTO = new FileDTO();
        fileDTO.setMessage("success");
        fileDTO.setUrl(fileName);
        fileDTO.setSuccess(1);

        //删除临时文件
//        deleteFile(excelFile);
        return fileDTO;
    }
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
