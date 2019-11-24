package com.ak.community.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.image.Kernel;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Component
public class TPProvider {
    @Value("${TP.secretId}")
    String secretId;
    @Value("${TP.secretKey}")
    String secretKey;

    TransferManager transferManager;

    public URL upLoad(String fileName,ObjectMetadata objectMetadata,InputStream inputStream) {
        URL url = null;
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        try {
            String bucketName = "coummunity-1300724762";
            String newFileName=fileName+UUID.randomUUID()+".jpg";
            //上传文件
            PutObjectResult putObjectResult = cosClient.putObject(bucketName, newFileName, inputStream, objectMetadata);
            //获取文件下载地址
            if (putObjectResult!=null){
                String key="/"+newFileName;
                url = cosClient.generatePresignedUrl(bucketName, key, new Date(new Date().getTime() + 5 * 60 * 10000 * 300));
            }
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
            return null;
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
            return null;
        }
        return url;
    }
}
