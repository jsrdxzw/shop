package com.jsrdxzw.service;

import com.jsrdxzw.pojo.Items;
import com.jsrdxzw.pojo.ItemsImg;
import com.jsrdxzw.pojo.ItemsParam;
import com.jsrdxzw.pojo.ItemsSpec;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description: 商品
 */
public interface ItemService {

    /**
     * 根据商品id查找详情
     *
     * @param itemId 分库分表，全局唯一id，需要String类型
     * @return 商品
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品id查询图片列表
     *
     * @param itemId 商品id
     * @return 图片列表
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId 商品id
     * @return 规格
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId 商品id
     * @return 参数
     */
    ItemsParam queryItemParam(String itemId);
}
