package com.offcn.common.demo;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OSSDemo {

    public static void main(String[] args) throws FileNotFoundException {
     // Endpoint以杭州为例，其它Region请按实际情况填写。
            String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
            String accessKeyId = "LTAI4G35pR5s6WzNiVG78PSe";
            String accessKeySecret = "Bhc26AERKyiePtIHfSQjP8K6enGgSt";

    // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

    // 上传文件流。
            InputStream inputStream = new FileInputStream("E:\\oldimage\\bizhi.jpg");
            ossClient.putObject("fingmen", "pic/myimg.jpg", inputStream);

    // 关闭OSSClient。
        ossClient.shutdown();



        System.out.println("测试完成");
    }
}
