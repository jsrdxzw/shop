package com.jsrdxzw.service.impl;

import com.jsrdxzw.pojo.Items;
import com.jsrdxzw.service.ItemService;
import com.jsrdxzw.utils.PagedGridResult;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * https://segmentfault.com/a/1190000020179390
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.2/java-rest-high-search.html#java-rest-high-search-request-optional
 * es7的使用方法
 */
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    public static final String FOODIE_ITEMS_IK = "foodie-items-ik";
    private final RestHighLevelClient restHighLevelClient;

    public ItemServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        final String itemNameField = "itemName";
        SearchRequest searchRequest = new SearchRequest(FOODIE_ITEMS_IK);
        SearchSourceBuilder builder = new SearchSourceBuilder();

        builder.query(QueryBuilders.matchQuery(itemNameField, keywords))
                .from(page * pageSize)
                .size(pageSize);

        if ("c".equals(sort)) {
            builder.sort(new FieldSortBuilder("sellCounts").order(SortOrder.DESC));
        } else if ("p".equals(sort)) {
            builder.sort(new FieldSortBuilder("price").order(SortOrder.ASC));
        } else {
            // 必须使用keyword才能对分词进行排序
            builder.sort(new FieldSortBuilder("itemName.keyword").order(SortOrder.ASC));
        }

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 默认的标签是em
        HighlightBuilder.Field highlightField = new HighlightBuilder.Field(itemNameField).highlighterType("unified");
        highlightBuilder.field(highlightField);
        builder.highlighter(highlightBuilder);

        SearchRequest source = searchRequest.source(builder);

        List<Items> items = new ArrayList<>();
        PagedGridResult pagedGridResult = new PagedGridResult();

        try {
            SearchResponse response = restHighLevelClient.search(source, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits.getHits()) {
                HighlightField field = hit.getHighlightFields().get(itemNameField);
                if (field != null && field.getFragments() != null) {
                    String itemName = field.getFragments()[0].toString();
                    Items item = Items.builder()
                            .itemName(itemName)
                            .imgUrl((String) hit.getSourceAsMap().get("imgUrl"))
                            .itemId((String) hit.getSourceAsMap().get("itemId"))
                            .price((Integer) hit.getSourceAsMap().get("price"))
                            .sellCounts((Integer) hit.getSourceAsMap().get("sellCounts"))
                            .build();
                    items.add(item);
                }
            }
            pagedGridResult.setRows(items);
            // page 默认从0开始，返回给客户端需要加1
            pagedGridResult.setPage(page + 1);
            pagedGridResult.setTotal(items.size() / pageSize);
            pagedGridResult.setRecords(hits.getTotalHits().value);
        } catch (IOException e) {
            log.error("es search error:{}", e);
        }
        return pagedGridResult;
    }
}
