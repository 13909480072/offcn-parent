package com.offcn.user.component;

import com.offcn.common.util.HttpUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsTemplate {
    @Value("${sms.host}")
   private String host;
    @Value("${sms.path}")
   private String path;
    @Value("${sms.method:POST}")
   private String method;
    @Value("${sms.appcode}")
   private String appcode;

    public String sendCode(Map<String,String> querys){
        HttpResponse response = null;

        Map<String,String>  headers = new HashMap<String,String>();
        headers.put("Authorization","APPCODE "+appcode);

        Map<String, String> bodys = new HashMap<>();
        try {
            if (method.equalsIgnoreCase("get")){
                response = HttpUtils.doGet(host,path,method,headers,querys);
            }else{
                response = HttpUtils.doPost(host,path,method,headers,querys,bodys);
            }
            String string = EntityUtils.toString(response.getEntity());
            System.out.println(string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
