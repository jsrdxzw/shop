package com.jsrdxzw.service.impl;

import com.github.pagehelper.PageHelper;
import com.jsrdxzw.enums.CommentLevel;
import com.jsrdxzw.enums.YesOrNo;
import com.jsrdxzw.mapper.*;
import com.jsrdxzw.pojo.*;
import com.jsrdxzw.service.ItemService;
import com.jsrdxzw.utils.DesensitizationUtil;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.CommentLevelCountsVO;
import com.jsrdxzw.vo.ItemCommentVO;
import com.jsrdxzw.vo.ShopCartVO;
import com.jsrdxzw.vo.searchItemsVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
@Slf4j
@Service
public class ItemServiceImpl extends BaseService implements ItemService {

    private final ItemsMapper itemsMapper;
    private final ItemsImgMapper itemsImgMapper;
    private final ItemsSpecMapper itemsSpecMapper;
    private final ItemsParamMapper itemsParamMapper;
    private final ItemsCommentsMapper itemsCommentsMapper;
    private final ItemsMapperCustom itemsMapperCustom;

    @Autowired
    private RedissonClient redissonClient;

    public ItemServiceImpl(ItemsMapper itemsMapper, ItemsImgMapper itemsImgMapper, ItemsSpecMapper itemsSpecMapper, ItemsParamMapper itemsParamMapper, ItemsCommentsMapper itemsCommentsMapper, ItemsMapperCustom itemsMapperCustom) {
        this.itemsMapper = itemsMapper;
        this.itemsImgMapper = itemsImgMapper;
        this.itemsSpecMapper = itemsSpecMapper;
        this.itemsParamMapper = itemsParamMapper;
        this.itemsCommentsMapper = itemsCommentsMapper;
        this.itemsMapperCustom = itemsMapperCustom;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setTotalCounts(totalCounts);
        commentLevelCountsVO.setGoodCounts(goodCounts);
        commentLevelCountsVO.setNormalCounts(normalCounts);
        commentLevelCountsVO.setBadCounts(badCounts);
        return commentLevelCountsVO;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("itemId", itemId);
        map.put("level", level);

        // mybatis-pagehelper

        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);
        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }

        return setterPagedGrid(list, page);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("keywords", keywords);
        map.put("sort", sort);
        PageHelper.startPage(page, pageSize);
        List<searchItemsVO> list = itemsMapperCustom.searchItems(map);

        return setterPagedGrid(list, page);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("catId", catId);
        map.put("sort", sort);
        PageHelper.startPage(page, pageSize);
        List<searchItemsVO> list = itemsMapperCustom.searchItemsByThirdCat(map);

        return setterPagedGrid(list, page);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> specIdsList = Arrays.asList(ids);
        return itemsMapperCustom.queryItemsBySpecIds(specIdsList);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemStock(String specId, int buyCounts) {
        // 防止库存会成为负数的问题
        // synchronized 不推荐使用，集群下无用，性能不好
        // 锁数据库，不推荐，性能地下
        // 分布式锁 OK zookeeper redis 数据库
        // SQL语句判断更新,适合并发小的情况
        RLock lock = redissonClient.getLock("item_lock" + ":" + specId);
        lock.lock(5, TimeUnit.SECONDS);
        try {
            int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
            if (result != 1) {
                // 由于在一个事务中，所以发生异常会回滚
                throw new RuntimeException("库存不足, 订单创建失败");
            }
        } catch (Exception e) {
            log.error("decreaseItemSpecStock error:{}", e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

    private Integer getCommentCounts(String itemId, Integer level) {
        Example example = new Example(ItemsComments.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("itemId", itemId);
        if (level != null) {
            criteria.andEqualTo("commentLevel", level);
        }
        return itemsCommentsMapper.selectCountByExample(example);
    }

}
