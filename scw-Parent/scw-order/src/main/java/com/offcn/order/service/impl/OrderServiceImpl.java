package com.offcn.order.service.impl;

import com.offcn.common.enume.OrderStatusEnume;
import com.offcn.common.response.AppResponse;
import com.offcn.common.util.AppDateUtils;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.req.resp.TReturn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private TOrderMapper orderMapper;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    /**
     * 保存订单的方法
     * @param vo
     * @return
     */
    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        //1.创建订单对象
        TOrder order = new TOrder();
        BeanUtils.copyProperties(vo, order);
        //vo没有的数据
        //获取用户ID，登录时在redis中存储，键是  令牌
        String accessToken = vo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        order.setMemberid(Integer.parseInt(memberId));
        //订单编号
        String ordernum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(ordernum);
        //状态
        order.setStatus(OrderStatusEnume.UNPAY.getCode() + "");
        //创建时间
        order.setCreatedate(AppDateUtils.getFormatTime());
        //money = rtncount * 支付金额 + 运费 ==》TReturn
        AppResponse<List<TReturn>> returnList = projectServiceFeign.getReturnList(vo.getProjectid());
        List<TReturn> tReturnList = returnList.getData();
        /* if (data != null) {*/
            for (TReturn tReturn : tReturnList) {
                if (tReturn.getId().equals(vo.getReturnid())) {
                    //支持金额tReturn.getSupportmoney();
                    //运费tReturn.getFreight();
                    Integer money = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
                    order.setMoney(money);
                }
            }
            orderMapper.insertSelective(order);
            return order;
        /*}
        return null;*/
    }

}
