package com.jsrdxzw.controller.center;

import com.jsrdxzw.bo.center.OrderItemsCommentBO;
import com.jsrdxzw.controller.BaseController;
import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.pojo.OrderItems;
import com.jsrdxzw.pojo.Orders;
import com.jsrdxzw.service.center.MyCommentsService;
import com.jsrdxzw.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/12/03
 * @Description:
 */
@Api(value = "用户中心评价模块", tags = {"用户中心评价模块相关接口"})
@RestController
@RequestMapping("/mycomments")
public class MyCommentsController extends BaseController {
    private final MyCommentsService myCommentsService;

    public MyCommentsController(MyCommentsService myCommentsService) {
        this.myCommentsService = myCommentsService;
    }

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/pending")
    public JSONResult pending(@RequestParam String userId, @RequestParam String orderId) {
        // 判断用户和订单是否已经关联
        JSONResult jsonResult = checkUserOrder(userId, orderId);
        if (jsonResult.getStatus() != HttpStatus.OK.value()) {
            return jsonResult;
        }
        Orders myOrder = (Orders) jsonResult.getData();
        if (myOrder.getIsComment() == YesOrNo.YES.type) {
            return JSONResult.errorMsg("该笔订单已经评价");
        }
        List<OrderItems> orderItems = myCommentsService.queryPendingComments(orderId);
        return JSONResult.ok(orderItems);
    }

    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public JSONResult saveList(@RequestParam String userId, @RequestParam String orderId, @RequestBody List<OrderItemsCommentBO> commentList) {
        // 判断用户和订单是否已经关联
        JSONResult jsonResult = checkUserOrder(userId, orderId);
        if (jsonResult.getStatus() != HttpStatus.OK.value()) {
            return jsonResult;
        }

        if (commentList == null || commentList.isEmpty()) {
            return JSONResult.errorMsg("评论内容不能为空");
        }

        myCommentsService.saveComments(orderId, userId, commentList);
        return JSONResult.ok();
    }

    @ApiOperation(value = "查询我的评价列表", notes = "查询我的评价列表", httpMethod = "POST")
    @PostMapping("/query")
    public JSONResult query(@RequestParam String userId, @RequestParam Integer page, @RequestParam Integer pageSize) {
        // 判断用户和订单是否已经关联
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        return JSONResult.ok(myCommentsService.queryMyComments(userId, page, pageSize));
    }
}
