package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Resource
    private TReturnMapper tReturnMapper;

    @Resource
    private TProjectMapper projectMapper;

    @Resource
    private TProjectImagesMapper projectImagesMapper;

    @Resource
    private TTagMapper tagMapper;

    @Resource
    private TTypeMapper typeMapper;

    @Override
    public TReturn findReturnByRid(Integer returnId) {

        return tReturnMapper.selectByPrimaryKey(returnId);
    }

    @Override
    public List<TProject> findAllProject() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TType> findAllType() {
        List<TType> tTypes = typeMapper.selectByExample(null);
        return tTypes;
    }

    @Override
    public List<TTag> findAllTag() {
        List<TTag> tTags = tagMapper.selectByExample(null);
        return tTags;
    }

    @Override
    public TProject findProjectInfo(Integer projectId) {
        TProject project = projectMapper.selectByPrimaryKey(projectId);
        return project;
    }

    @Override
    public List<TProjectImages> getProjectImages(Integer id) {
        TProjectImagesExample example = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(id);
        return projectImagesMapper.selectByExample(example);
    }

    @Override
    public List<TReturn> getReturns(Integer projectId) {
        TReturnExample example = new TReturnExample();
        TReturnExample.Criteria criteria = example.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return tReturnMapper.selectByExample(example);
    }
}
