package com.jsrdxzw.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Items {

    private String itemId;

    private String itemName;

    private String imgUrl;

    private Integer price;

    private Integer sellCounts;

}
