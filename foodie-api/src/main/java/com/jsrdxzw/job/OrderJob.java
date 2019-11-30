package com.jsrdxzw.job;

import com.jsrdxzw.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/27
 * @Description:
 */
@Component
public class OrderJob {

    private final OrderService orderService;

    public OrderJob(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 自动关闭长时间未支付的订单
     * 弊端：
     * 1 会有时间差，程序不严谨
     * 2 不支持集群，只能使用一台计算机统一执行定时任务
     * 3 会对数据库全表搜索,低效且影响数据库性能
     * 只适合小型轻量项目
     * 推荐使用延时队列来处理
     */
    @Scheduled(cron = "0 0 0/6 * * ?")
    public void autoCloseOrder() {
        orderService.closeOrder();
    }
}
