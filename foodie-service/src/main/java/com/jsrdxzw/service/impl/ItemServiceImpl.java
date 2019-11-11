package com.jsrdxzw.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jsrdxzw.enums.CommentLevel;
import com.jsrdxzw.mapper.*;
import com.jsrdxzw.pojo.*;
import com.jsrdxzw.service.ItemService;
import com.jsrdxzw.utils.DesensitizationUtil;
import com.jsrdxzw.utils.PagedGridResult;
import com.jsrdxzw.vo.CommentLevelCountsVO;
import com.jsrdxzw.vo.ItemCommentVO;
import com.jsrdxzw.vo.searchItemsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description:
 */
@Service
public class ItemServiceImpl implements ItemService {

    private final ItemsMapper itemsMapper;
    private final ItemsImgMapper itemsImgMapper;
    private final ItemsSpecMapper itemsSpecMapper;
    private final ItemsParamMapper itemsParamMapper;
    private final ItemsCommentsMapper itemsCommentsMapper;
    private final ItemsMapperCustom itemsMapperCustom;

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


    private Integer getCommentCounts(String itemId, Integer level) {
        Example example = new Example(ItemsComments.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("itemId", itemId);
        if (level != null) {
            criteria.andEqualTo("commentLevel", level);
        }
        return itemsCommentsMapper.selectCountByExample(example);
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
