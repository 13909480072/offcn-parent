package com.offcn.order.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "订单模块")
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    @ApiOperation("保存订单")
    @PostMapping("/createOrder")
    public AppResponse<TOrder> createOrder(@RequestBody OrderInfoSubmitVo vo){
        //检查令牌
        String memberId = redisTemplate.opsForValue().get(vo.getAccessToken());
        if (memberId == null){
            AppResponse<TOrder> fail = AppResponse.fail(null);
            fail.setMsg("用户没有登录");
            return fail;
        }
        //登录成功
        TOrder tOrder = orderService.saveOrder(vo);
        AppResponse<TOrder> ok = AppResponse.ok(tOrder);
        return ok;
    }
}
