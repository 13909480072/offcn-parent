package com.offcn.project.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.common.util.OSSTemplate;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(tags = "众筹项目模块")
public class ProjectInfoController {

    @Autowired
    private OSSTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("file") MultipartFile[] files) throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null ){
                String url = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                list.add(url);
            }
        }
        map.put("urls",list);
        return  AppResponse.ok(map);
    }

    @ApiOperation("获取项目回收报表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnList(@PathVariable("projectId") Integer projectId){
        List<TReturn> returns = projectInfoService.getReturns(projectId);
        return AppResponse.ok(returns);
    }

    @ApiOperation("获取系统所有的项目")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProject(){
        //1.创建集合存储全部项目的VO
        List<ProjectVo> prosVo = new ArrayList<>();
        //2.查询全部项目
        List<TProject> allProject = projectInfoService.findAllProject();
        //3.遍历项目集合
        for (TProject tProject : allProject) {
            //获取项目编号
            Integer id = tProject.getId();
            //根据项目编号获取项目配图
            List<TProjectImages> projectImages = projectInfoService.getProjectImages(id);
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(tProject,projectVo);
            //遍历项目配图集合
            for (TProjectImages tProjectImages : projectImages) {
                //如果图片类型是头部图片，则设置头部图片路径到项目VO
                if (tProjectImages.getImgtype()==0){
                    projectVo.setHeaderImage(tProjectImages.getImgurl());
                }
            }
            //吧项目VO添加到项目VO集合
            prosVo.add(projectVo);
        }
        return AppResponse.ok(prosVo);
    }

    @ApiOperation("获取项目信息详情")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId") Integer projectId){
        //数据库查询出来的结果
        TProject projectInfo = projectInfoService.findProjectInfo(projectId);
        //返回的结果是
        ProjectDetailVo projectVo = new ProjectDetailVo();
        //1.查出项目的所有图片
        List<TProjectImages> projectImages = projectInfoService.getProjectImages(projectInfo.getId());
        List<String> detailsImage = projectVo.getDetailsImage();
        if (detailsImage==null){
            detailsImage=new ArrayList<>();
        }
        for (TProjectImages tProjectImages : projectImages) {
            if (tProjectImages.getImgtype()==0){//头图
                projectVo.setHeaderImage(tProjectImages.getImgurl());
            }else{//详情图片
                detailsImage.add(tProjectImages.getImgurl());
            }
        }
        projectVo.setDetailsImage(detailsImage);

        //2.项目的所有支付hi回报
        List<TReturn> returns = projectInfoService.getReturns(projectInfo.getId());
        projectVo.setProjectReturns(returns);
        BeanUtils.copyProperties(projectInfo,projectVo);
        return AppResponse.ok(projectVo);
    }

    @ApiOperation("获取所有的标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag(){
        List<TTag> allTag = projectInfoService.findAllTag();
        return AppResponse.ok(allTag);
    }

    @ApiOperation("获取所有的分类")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType(){
        List<TType> allType = projectInfoService.findAllType();
        return AppResponse.ok(allType);
    }
    
    @ApiOperation("根据回报的主键查询回报")
    @GetMapping("/findReturnByRid/{returnId}")
    public AppResponse<TReturn> findReturnByRid(@PathVariable("returnId") Integer returnId){
        TReturn returnByRid = projectInfoService.findReturnByRid(returnId);
        return AppResponse.ok(returnByRid);
    }

}
