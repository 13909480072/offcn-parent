package com.offcn.common.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.ToString;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@ToString
@Data
public class OSSTemplate {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
       private String endpoint;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
       private String accessKeyId;
       private String accessKeySecret;
       private String bucketName;

        //文件上传  String --》文件的路径url
    public String upload(InputStream inputStream,String fileName)  {
        //文件的路径根据时间日期  进行区分
        //一天一个目录
        //格式：pic/1027/fileName
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String foderName = format.format(new Date());
        //将fileName进行非重复操作
        fileName = UUID.randomUUID().toString().replace("-","")+"_"+fileName;
        //实现上传
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        ossClient.putObject(bucketName, "pic/"+foderName+"/"+fileName, inputStream);

        // 关闭OSSClient。
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();

        //获取访问资源的路径
        // https://fingmen.oss-cn-beijing.aliyuncs.com/pic/20201027/e8b827e200674ec0902912457975fdad_gu4.jpg
        return "http://"+bucketName+"."+endpoint+"/pic/"+foderName+"/"+fileName;

    }


}
