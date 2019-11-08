package com.jsrdxzw.bo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/04
 * @Description: 前端传来的数据包
 */
@ApiModel(value = "用户对象BO", description = "从客户端传来的信息封装")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "jsrdxzw", required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123456", required = true)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123456")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
