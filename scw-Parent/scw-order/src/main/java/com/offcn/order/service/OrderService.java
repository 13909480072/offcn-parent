package com.offcn.order.service;

import com.offcn.order.pojo.TOrder;
import com.offcn.order.vo.req.OrderInfoSubmitVo;

public interface OrderService {
    //保存订单方法
    public TOrder saveOrder(OrderInfoSubmitVo vo);
}
