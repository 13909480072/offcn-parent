package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.common.enume.ProjectStatusEnume;
import com.offcn.common.response.AppResponse;
import com.offcn.common.vo.BaseVo;
import com.offcn.project.pojo.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "项目基本功能模块（创建、保存、项目信息获取、文件上传等）")
@RequestMapping("/project")
public class ProjectCreateController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ProjectCreateService projectCreateService;

    @ApiOperation("项目发起第一步--创建ProjectToken")
    @PostMapping("/init")//BaseVo 用户的临时ID
    public AppResponse<String> init(BaseVo vo){
        String assessToken = vo.getAccessToken();
        //通过登录令牌获取用户ID
        String memberId = stringRedisTemplate.opsForValue().get(assessToken);
        if(StringUtils.isEmpty(memberId)){

            return AppResponse.fail("无此用户，请先登录");
        }
        int id = Integer.parseInt(memberId);
        //保存临时项目信息到redis
        String projectToken = projectCreateService.initCreateProject(id);
        return AppResponse.ok(projectToken);
    }

    @ApiOperation("初始化第二步，收集页面数据")
    @PostMapping("/saveBaseInfo")
    public AppResponse<String> saveBaseInfo(ProjectBaseInfoVo vo){
        //将用户提交的数据 添加到projectRedisStorageVo的对象中
        //1.获取redis中的projectRedisStorageVo的对象
        String redisProjectStr = stringRedisTemplate.opsForValue().get(vo.getProjectToken());
        ProjectRedisStorageVo storageVo = JSON.parseObject(redisProjectStr, ProjectRedisStorageVo.class);
        //2.将用户填写的数据添加到storageVO对象中
        BeanUtils.copyProperties(vo,storageVo);
        //3.将加好数据的storageVO存入到redis中
        redisProjectStr = JSON.toJSONString(storageVo);
        stringRedisTemplate.opsForValue().set(vo.getProjectToken(),redisProjectStr);
        return AppResponse.ok("基本信息添加成功");

    }

    @ApiOperation("初始化第三步，提交回报增量保存")
    @PostMapping("/saveReturn")
    public AppResponse<Object> saveReturnInfo(@RequestBody List<ProjectReturnVo> pro){
        //目的：从redis中获取数据，加入属性，存入到redis中
        if (pro != null && pro.size()>0){
            ProjectReturnVo projectReturnVo = pro.get(0);
            String projectToken = projectReturnVo.getProjectToken();
            //1.取得redis中之前存储JSON结构的项目信息
            String storageStr = stringRedisTemplate.opsForValue().get(projectToken);
            ProjectRedisStorageVo storageVo = JSON.parseObject(storageStr, ProjectRedisStorageVo.class);
            //2.添加数据
            List<TReturn> list = new ArrayList<>();
            for (ProjectReturnVo vo : pro) {
                TReturn tReturn = new TReturn();
                BeanUtils.copyProperties(vo,tReturn);
                list.add(tReturn);
            }
            storageVo.setProjectReturns(list);
            String str = JSON.toJSONString(storageVo);
            //3.存入redis中
            stringRedisTemplate.opsForValue().set(projectToken,str);
            return AppResponse.ok("回报添加成功");
        }else {
            return AppResponse.fail("数据为空");
        }
    }

    @ApiOperation("初始化第四步，保存完成")
    @PostMapping("/saveProject")//ops判断用户操作
    public AppResponse<Object> submit(String accessToken,String projectToken,String ops){
        //根据用户信息accessToken获取用户信息
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)){
            return AppResponse.fail("用户登录过期，请重新登录");
        }
        //获取项目信息
        String storageStr = stringRedisTemplate.opsForValue().get(projectToken);
        ProjectRedisStorageVo storageVo = JSON.parseObject(storageStr, ProjectRedisStorageVo.class);
        //判断用户操作类型是否为空
        if (!StringUtils.isEmpty(ops)){
            if (ops.equalsIgnoreCase("1")) {
                projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH,storageVo);
            }else if ("0".equals(ops)){
                projectCreateService.saveProjectInfo(ProjectStatusEnume.DRAFT,storageVo);
            }
            return AppResponse.ok("数据导入成功");
        }
        return AppResponse.fail("数据导入失败");
    }
}
