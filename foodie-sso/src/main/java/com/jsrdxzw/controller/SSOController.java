package com.jsrdxzw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SSOController {

    @GetMapping("/doLogin")
    public String hello(String returnUrl, Model model) {
        model.addAttribute("returnUrl", returnUrl);
        // 验证登录, 对应图中的7，8
        // redis存token（uuid+user的基本信息）
        // redisOperator.set(REDIS_USER_TOKEN+":"+usersVO.getId(), JsonUtils.objectToJson(usersVO));

        // 生成全局的ticket门票，代表用户在CAS端登录过
        // String userTicket = UUID.randomUUID().toString().trim();
        // redisOperator.set(REDIS_USER_TICKET+":"+usersVO.getId(), userTicket);
        // TODO 还有一些步骤待完成
        return "login";
    }

    @GetMapping("/music")
    public String music() {
        return "music";
    }

    @GetMapping("mtv")
    public String mtv() {
        return "mtv";
    }
}
