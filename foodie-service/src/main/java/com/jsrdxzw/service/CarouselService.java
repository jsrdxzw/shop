package com.jsrdxzw.service;

import com.jsrdxzw.pojo.Carousel;

import java.util.List;

/**
 * 轮播图
 *
 * @author 01027698
 */
public interface CarouselService {

    /**
     * 查询所有轮播图列表
     *
     * @param isShow 是否显示照片
     * @return 轮播图
     */
    List<Carousel> queryAll(int isShow);
}
