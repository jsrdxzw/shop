package com.jsrdxzw.rabbitmq.producer;

/**
 * @author xuzhiwei
 * @date 2020/04/05
 */
public interface SendCallback {
    /**
     * 成功的回调函数
     */
    void onSuccess();

    /**
     * 失败的回调函数
     */
    void onFailure();
}
