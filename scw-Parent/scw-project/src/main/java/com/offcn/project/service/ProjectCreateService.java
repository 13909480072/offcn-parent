package com.offcn.project.service;

import com.offcn.common.enume.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    /**
     * 初始化项目
     */
    public String initCreateProject(Integer memberId);

    //保存项目数据
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStorageVo storageVo);
}
