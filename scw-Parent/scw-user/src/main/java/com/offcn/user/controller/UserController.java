package com.offcn.user.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/*@Api(tags = "第一个Swagger测试")
@RestController*/
public class UserController {
    @ApiOperation("测试方法hello")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="name",value = "姓名",required = true)})
    @GetMapping("/hello")
    public String hello(String name){
        return "第一个hello的请求";
    }


   /* @ApiOperation("测试添加用户的操作")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="name",value = "姓名",required = true),
            @ApiImplicitParam(name="id",value = "识别码",required = true)})
*/
/*    @PostMapping("/user")
    public User createUser(String name,int id){
        return new User(id,name);
    }

    @ApiOperation("测试添加")
    @PostMapping("/user/show")
    public User createUser(User user){
        return user;
    }*/
}
