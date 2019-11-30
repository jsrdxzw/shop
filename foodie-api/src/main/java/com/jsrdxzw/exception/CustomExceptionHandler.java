package com.jsrdxzw.exception;

import com.jsrdxzw.utils.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/30
 * @Description: 自定义异常处理
 */

@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 上传文件捕获异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return JSONResult.errorMsg("文件上传大小不能超过500KB");
    }
}
