package com.offcn.order.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.order.vo.req.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignException implements com.offcn.order.service.ProjectServiceFeign {
    @Override
    public AppResponse<List<TReturn>> getReturnList(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务失败【订单】");
        return fail;
    }
}
