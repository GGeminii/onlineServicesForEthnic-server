package com.onlineServicesForEthnic.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.onlineServicesForEthnic.properties.AliOSSProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类
 */
@Component
@RequiredArgsConstructor
public class AliOSSUtil {
    private final AliOSSProperties aliOSSProperties;

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        //用@ConfigurationProperties(prefix = " ")获取配置文件信息,获取阿里云OSS参数
        String accessKeyId = aliOSSProperties.getAccessKeyId();
        String bucketName = aliOSSProperties.getBucketName();
        String accessKeySecret = aliOSSProperties.getAccessKeySecret();
        String endpoint = aliOSSProperties.getEndpoint();

        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖
        String originalFilename = file.getOriginalFilename();
        String fileName = null;
        if (originalFilename != null) {
            fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
