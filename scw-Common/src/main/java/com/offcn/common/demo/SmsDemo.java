package com.offcn.common.demo;

import com.offcn.common.util.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class SmsDemo {
    public static void main(String[] args){
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method="POST";
        String appcode = "2aa85a27075a47c1999cbe87cd2b0aac";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式（中间是英文空格）为Authorization：APPCODE ……
        headers.put("Authorization","APPCODE "+appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile","13909480072");
        querys.put("param","code:857857");
        querys.put("tpl_id","TP1711063");
        Map<String, String> bodys = new HashMap<>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, "");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
