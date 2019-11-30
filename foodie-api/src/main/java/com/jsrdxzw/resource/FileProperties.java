package com.jsrdxzw.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/30
 * @Description:
 */
@EnableConfigurationProperties
@Component
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    private String imageUserFaceLocation;
    private String imageServerUrl;
    private String[] imageFormats;

    public String getImageUserFaceLocation() {
        return imageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        this.imageUserFaceLocation = imageUserFaceLocation;
    }

    public String[] getImageFormats() {
        return imageFormats;
    }

    public void setImageFormats(String[] imageFormats) {
        this.imageFormats = imageFormats;
    }

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }
}
