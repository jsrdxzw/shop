package com.jsrdxzw.controller.center;

import com.jsrdxzw.bo.center.CenterUserBO;
import com.jsrdxzw.controller.BaseController;
import com.jsrdxzw.pojo.ShopUser;
import com.jsrdxzw.resource.FileProperties;
import com.jsrdxzw.service.center.CenterUserService;
import com.jsrdxzw.utils.CookieUtils;
import com.jsrdxzw.utils.DateUtil;
import com.jsrdxzw.utils.JSONResult;
import com.jsrdxzw.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/28
 * @Description:
 */
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("/userInfo")
public class CenterUserController extends BaseController {

    private final CenterUserService centerUserService;

    private final FileProperties fileProperties;

    public CenterUserController(CenterUserService centerUserService, FileProperties fileProperties) {
        this.centerUserService = centerUserService;
        this.fileProperties = fileProperties;
    }

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true) MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 定义头像的保存地址
        if (file.isEmpty()) {
            return JSONResult.errorMsg("文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (StringUtils.isNotBlank(fileName)) {
            String[] split = fileName.split("\\.");
            String suffix = split[split.length - 1];
            if (validateImageFormat(suffix)) {
                return JSONResult.errorMsg("图片格式不正确！");
            }
            // 覆盖式上传，增量式可以再拼接时间
            String newFileName = "face-" + userId + "." + suffix;
            File finalFilePath = new File(fileProperties.getImageUserFaceLocation() + File.separator + userId + File.separator + newFileName);
            if (finalFilePath.getParentFile() != null) {
                finalFilePath.getParentFile().mkdirs();
            }
            // 拷贝文件
            try (
                    InputStream inputStream = file.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(finalFilePath)
            ) {
                IOUtils.copy(inputStream, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String faceUrl = fileProperties.getImageServerUrl() + userId + "/" + newFileName + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
            ShopUser user = centerUserService.updateUserFace(userId, faceUrl);
            userDataMasking(user);
            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
        } else {
            return JSONResult.errorMsg("文件不能为空！");
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(@RequestParam String userId, @RequestBody @Valid CenterUserBO centerUserBO,
                             BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }
        ShopUser user = centerUserService.updateUserInfo(userId, centerUserBO);
        ShopUser safeUser = userDataMasking(user);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(safeUser), true);
        // TODO 后续整合redis做分布式会话
        return JSONResult.ok();
    }

    private Map<String, String> getErrors(BindingResult result) {
        List<FieldError> errorList = result.getFieldErrors();
        Map<String, String> map = new HashMap<>(errorList.size());
        for (FieldError fieldError : errorList) {
            // 发生验证错误对应的属性
            String field = fieldError.getField();
            // 错误信息
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field, defaultMessage);
        }
        return map;
    }

    private boolean validateImageFormat(String suffix) {
        for (String imageFormat : fileProperties.getImageFormats()) {
            if (!imageFormat.equalsIgnoreCase(suffix)) {
                return false;
            }
        }
        return true;
    }
}
