package com.jsrdxzw.vo;

/**
 * @Author: xuzhiwei
 * @Date: 2019/11/11
 * @Description: 用于展示商品搜索列表的VO
 */
public class searchItemsVO {
    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imgUrl;

    /**
     * 涉及金额最好以分为单位
     */
    private Integer price;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(Integer sellCounts) {
        this.sellCounts = sellCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
