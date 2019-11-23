package com.jsrdxzw.service;

import com.jsrdxzw.pojo.Items;
import com.jsrdxzw.pojo.ItemsImg;
import com.jsrdxzw.pojo.ItemsParam;
import com.jsrdxzw.pojo.ItemsSpec;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.CommentLevelCountsVO;
import com.jsrdxzw.vo.ShopCartVO;

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

    /**
     * 商品评价统计
     *
     * @param itemId 商品id
     * @return 商品评价vo
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品id查询商品的评价（分页）
     *
     * @param itemId   itemId
     * @param level    level
     * @param page     当前页
     * @param pageSize 页面展示数量
     * @return 自定义封装分页
     */
    PagedGridResult queryPagedComments(String itemId, Integer level,
                                       Integer page, Integer pageSize);

    /**
     * 搜索商品
     *
     * @param keywords 关键词
     * @param sort     排序
     * @param page     当前页
     * @param pageSize 页面展示数量
     * @return 自定义封装分页
     */
    PagedGridResult searchItems(String keywords, String sort,
                                Integer page, Integer pageSize);

    /**
     * 根据分类id搜索商品
     *
     * @param catId    分类id
     * @param sort     排序
     * @param page     当前页
     * @param pageSize 页面展示数量
     * @return 自定义封装分页
     */
    PagedGridResult searchItems(Integer catId, String sort,
                                Integer page, Integer pageSize);

    /**
     * 根据规格查询购物车商品数据
     *
     * @param specIds 1001,1002
     * @return
     */
    List<ShopCartVO> queryItemsBySpecIds(String specIds);

    /**
     * 商品规格对象
     *
     * @param specId 商品规格id
     * @return
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品的if获取主图片url
     *
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);

    /**
     * 扣除库存
     *
     * @param specId
     * @param buyCounts
     */
    void decreaseItemStock(String specId, int buyCounts);
}
