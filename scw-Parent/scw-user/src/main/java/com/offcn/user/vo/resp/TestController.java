package com.offcn.user.vo.resp;

import org.springframework.web.bind.annotation.RequestMapping;

public class TestController {
    @RequestMapping("/myController")
    public String test(){
        return "myController";
    }
}
