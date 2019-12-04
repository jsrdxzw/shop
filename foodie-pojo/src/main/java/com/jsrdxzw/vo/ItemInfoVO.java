package com.jsrdxzw.vo;

import com.jsrdxzw.pojo.Items;
import com.jsrdxzw.pojo.ItemsImg;
import com.jsrdxzw.pojo.ItemsParam;
import com.jsrdxzw.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/10
 * @Description: 返回前端的item数据
 * 属性名称和前端要对应
 */
@Data
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
