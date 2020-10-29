package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

public interface ProjectInfoService {
    //获取项目回报列表
    public List<TReturn> getReturns(Integer projectId);
    //获取系统中的所有项目
    public List<TProject> findAllProject();
    //获取项目图片
    public List<TProjectImages> getProjectImages(Integer id);
    //获取项目信息
    public TProject findProjectInfo(Integer projectId);
    //获取所有的标签
    public List<TTag> findAllTag();
    //获取所有的分类
    public List<TType> findAllType();
    //根据回报ID获取回报信息
    public TReturn findReturnByRid(Integer returnId);
}
