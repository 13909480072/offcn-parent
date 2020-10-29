package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.common.enume.ProjectStatusEnume;
import com.offcn.project.contants.ProjectConstant;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements com.offcn.project.service.ProjectCreateService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private TProjectMapper projectMapper;
    @Resource
    private TProjectImagesMapper imagesMapper;
    @Resource
    private TReturnMapper returnMapper;
    @Resource
    private TProjectTagMapper tagMapper;
    @Resource
    private TProjectTypeMapper tTypeMapper;

    @Override
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStorageVo storageVo) {
        TProject tProject = new TProject();
        BeanUtils.copyProperties(storageVo,tProject);
        tProject.setStatus(status.getCode()+"");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*tProject.setDeploydate(format.format(new Date()));*/
        tProject.setCreatedate(format.format(new Date()));
        //1.插入项目数据到数据库
        projectMapper.insert(tProject);
        //获取在project表中插入的id(在xml文件中写入了获取插入后的ID)
        Integer projectId = tProject.getId();
        //2.头图片的插入
        String headerImage = storageVo.getHeaderImage();
        TProjectImages headImag = new TProjectImages(null, projectId, headerImage, ProjectImageTypeEnume.HEADER.getCode());
        imagesMapper.insert(headImag);
        //2.2详情图插入
        List<String> detailsImage = storageVo.getDetailsImage();
        if (detailsImage != null && detailsImage.size()>0){
            for (String detail : detailsImage) {
                TProjectImages detalsImg = new TProjectImages(null,projectId,detail,ProjectImageTypeEnume.DETAILS.getCode());
                imagesMapper.insert(detalsImg);
            }
        }

        //3.标签的插入 tag
        List<Integer> tagids = storageVo.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tProjectTag = new TProjectTag(null,projectId,tagid);
            tagMapper.insert(tProjectTag);
        }
        //4.type的插入
        List<Integer> typeids = storageVo.getTypeids();
        for (Integer typeid : typeids) {
            TProjectType tProjectType = new TProjectType(null,projectId,typeid);
            tTypeMapper.insert(tProjectType);
        }
        //5.回报的插入
        List<TReturn> projectReturns = storageVo.getProjectReturns();
        for (TReturn projectReturn : projectReturns) {
            projectReturn.setProjectid(projectId);
            returnMapper.insert(projectReturn);
        }
        //6.删除redis的数据
//        stringRedisTemplate.delete(storageVo.getProjectToken())


    }

    @Override
    public String initCreateProject(Integer memberId) {
        //项目的临时令牌
        String token =ProjectConstant.PTOKEN + UUID.randomUUID().toString().replace("-", "");
        //项目的临时对象
        ProjectRedisStorageVo vo = new ProjectRedisStorageVo();
        vo.setProjectToken(token);
        vo.setMemberid(memberId);
        //vo转为json字符串
        String str = JSON.toJSONString(vo);
        //将项目的令牌存入redis中
        stringRedisTemplate.opsForValue().set(token,str);
        return token;

    }
}
