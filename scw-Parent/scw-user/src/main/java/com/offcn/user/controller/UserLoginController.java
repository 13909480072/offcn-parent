package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户登录/注册模块（包括忘记密码等）")
@RequestMapping("/user")
public class UserLoginController {
    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("获取注册的验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phoneNo",value = "手机号",required = true)
    })
    @PostMapping("/senfCode")
    public AppResponse<Object> sendCode(String phoneNo){
        //1.生成验证码保存到服务器中，准备用户提交上的进行对比
        String code = UUID.randomUUID().toString().replace("-","").substring(0, 4);
        //2.保存验证码和手机号的对应关系，设置验证码过期时间
        redisTemplate.opsForValue().set(phoneNo,code,60*5, TimeUnit.SECONDS);
        //3.短信发送构造参数
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile",phoneNo);
        querys.put("param","code:"+code);
        querys.put("tpl_id","TP1711063");//短信模板
        //4.发送短信
        String sendCode = smsTemplate.sendCode(querys);
        if (sendCode.equals("")||sendCode.equals("fail")){
            //短信失败
            return  AppResponse.fail("短信发送失败");
        }
        return AppResponse.ok(sendCode);
    }

    @ApiOperation("用户注册")
    @PostMapping("/regist")
    public AppResponse<Object> regist(UserRegistVo registVo){
        //1.校验验证码
        String code = redisTemplate.opsForValue().get(registVo.getLoginacct());
        if (!StringUtils.isEmpty(code)){
            //redis中有验证码
            boolean b = code.equalsIgnoreCase(registVo.getCode());
            if (b){
                //2.将vo转业务能用的数据对象
                TMember member = new TMember();
                BeanUtils.copyProperties(registVo,member);
                //3.将用户信息注册到信息库
                try {
                    userService.registerUser(member);
                    //4.注册成功后，删除验证码
                    redisTemplate.delete(registVo.getLoginacct());
                    return AppResponse.ok("注册成功");
                } catch (Exception e) {
                    System.out.println("用户注册失败"+member.getLoginacct());
                    return AppResponse.fail(e.getMessage());
                }
            }else{
                return AppResponse.fail("验证码错误");
            }
        }else{
            return AppResponse.fail("验证码过期，请重新获取");
        }
    }

    //用户登录
    @ApiOperation("用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="username", value = "用户名",required = true),
            @ApiImplicitParam(name="password",value = "密码",required = true)
    })
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username,String password){
        //1.尝试登录
        TMember member = userService.login(username, password);
        if (member == null){
            //登录失败
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户密码错误");
            return fail;
        }

        //2.登录成功，生成令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(member,vo);
        vo.setAccessToken(token);
        //3.经常根据令牌查询用户的id信息
        redisTemplate.opsForValue().set(token,member.getId()+"",2,TimeUnit.HOURS);
        return AppResponse.ok(vo);
    }


        //3.根据用户编号获取用户信息
        @ApiOperation("根据id查找用户")
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tmember = userService.findTmemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tmember,userRespVo);
        return AppResponse.ok(userRespVo);
    }

}
