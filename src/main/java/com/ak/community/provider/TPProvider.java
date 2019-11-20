package com.ak.community.provider;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

@Component
public class TPProvider {
    @Value("${TP.secretId}")
    String secretId;
    @Value("${TP.secretKey}")
    String secretKey;

    public String upLoad(String fileName,ObjectMetadata objectMetadata,InputStream inputStream) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        String newFileName;
        COSClient cosClient = new COSClient(cred, clientConfig);
        try {
            String bucketName = "coummunity-1300724762";
            newFileName=fileName+UUID.randomUUID();
            cosClient.putObject(bucketName,newFileName,inputStream,objectMetadata);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
            return null;
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
            return null;
        }
        return newFileName;
    }
}
