package com.wlw.es.wlsearch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wlw.es.common.SearchConfig;
import com.wlw.es.wlsearch.dto.SearchParam;
import com.wlw.es.wlsearch.dto.WLSearchResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.script.ScriptScoreFunctionBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author fuxg
 * @create 2017-04-10 15:15
 */
public class WLSearchBuilders {

    final Logger logger = LoggerFactory.getLogger(WLSearchBuilders.class);

    private ElasticsearchTemplate elasticsearchTemplate;
    private String indexName = SearchConfig.INDEX_NAME_ALIAS;
    private String[] types;
    private SearchParam searchParam;
    private BoolQueryBuilder filterBuilder;
    private BoolQueryBuilder queryBuilder;
    private NativeSearchQueryBuilder nativeSearchQueryBuilder;
    private List<SortBuilder> sortBuilders;
    private FunctionScoreQueryBuilder functionScoreQueryBuilder;
    private Pageable pageable;

    public WLSearchBuilders(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        init();
    }

    public WLSearchBuilders(ElasticsearchTemplate elasticsearchTemplate, SearchParam searchParam, String... types) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.types = types;
        this.searchParam = searchParam;
        init();
    }

    public WLSearchBuilders(ElasticsearchTemplate elasticsearchTemplate, SearchParam searchParam) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.searchParam = searchParam;
        init();
    }

    private void init() {
        nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        sortBuilders = new ArrayList<>();
        queryBuilder = QueryBuilders.boolQuery();
        filterBuilder = QueryBuilders.boolQuery();
    }

    public BoolQueryBuilder getFilterBuilder() {
        return filterBuilder;
    }

    public BoolQueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public WLSearchBuilders withQuery(QueryBuilder qb) {
        queryBuilder.must(qb);
        return this;
    }

    public WLSearchBuilders withFilter(QueryBuilder qb) {
        filterBuilder.must(qb);
        return this;
    }

    public WLSearchBuilders withDistanceSortBuilder() {
        Double lon = searchParam.getLongitude();
        Double lat = searchParam.getLatitude();
        if (lon != null && lat != null) {
            GeoDistanceSortBuilder sort = SortBuilders.geoDistanceSort("location");
            sort.unit(DistanceUnit.KILOMETERS);
            sort.order(SortOrder.ASC);
            sort.point(lat, lon);
            withSortBuilder(sort);
        }
        return this;
    }

    public WLSearchBuilders withScoreSort() {
        withSortBuilder(new FieldSortBuilder("_score").order(SortOrder.DESC));
        return this;
    }

    public WLSearchBuilders withSortBuilder(SortBuilder sortBuilder) {
        this.sortBuilders.add(sortBuilder);
        return this;
    }

    public WLSearchBuilders withScriptSort(String scriptStr, Map params) {
        Script script = new Script(scriptStr, Script.DEFAULT_TYPE, "groovy", params);
        ScriptScoreFunctionBuilder scriptScoreFunctionBuilder = new ScriptScoreFunctionBuilder(script);
        functionScoreQueryBuilder = new FunctionScoreQueryBuilder(queryBuilder);
        functionScoreQueryBuilder.add(scriptScoreFunctionBuilder).boostMode("replace");
        return this;
    }

    public WLSearchBuilders withPageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public Page execute(Class<? extends WLSearchResult> resultClass) {
        build();
        if (resultClass == WLSearchResult.class) {
            nativeSearchQueryBuilder.withSourceFilter(new SourceFilter() {
                @Override
                public String[] getIncludes() {
                    return FETCH_FIELDS;
                }

                @Override
                public String[] getExcludes() {
                    return new String[0];
                }
            });
        }
        Page page = elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), reponse -> {
            Long tookTime = reponse.getTookInMillis();
            if (tookTime < 200) {
                logger.info("take time:{}ms", reponse.getTookInMillis());
            } else {
                logger.warn("slow query!,take time:{}ms!,hit rows:{}", reponse.getTookInMillis(), reponse.getHits().totalHits());
            }
            return resultMapper(reponse, resultClass);
        });
        return page;
    }

    private void build() {
        nativeSearchQueryBuilder.withIndices(indexName);
        if (types != null) {
            nativeSearchQueryBuilder.withTypes(types);
        }
        if (functionScoreQueryBuilder != null) {
            nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
        } else {
            nativeSearchQueryBuilder.withQuery(queryBuilder);
        }
        nativeSearchQueryBuilder.withFilter(filterBuilder);
        sortBuilders.forEach(sort -> {
            nativeSearchQueryBuilder.withSort(sort);
        });
        if (pageable != null) {
            nativeSearchQueryBuilder.withPageable(pageable);
        }
    }

    private PageImpl resultMapper(SearchResponse response, Class<? extends WLSearchResult> resultCls) {
        List results = new ArrayList();
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            WLSearchResult result = JSONObject.parseObject(hit.getSourceAsString(), resultCls);
            String cat = result.getCat();
            //内容截取
            String content = result.getContent();
            if (content != null && content.length() > 120) {
                result.setContent(content.substring(0, 120));
            }
            //距离
            Object[] sort = hit.getSortValues();
            if (sort != null && sort.length == 2) {
                Float distance = new BigDecimal((Double) sort[1]).floatValue();
                if (distance < 100000) {
                    result.setMeter(new BigDecimal((Double) sort[1]).setScale(2, BigDecimal.ROUND_HALF_UP) + "km");
                } else {
                    result.setMeter("未知");
                }
            } else {
                result.setMeter("未知");
            }

            if (sort.length > 0) {
                result.setScore(Float.parseFloat(sort[0].toString()));
            } else {
                result.setScore(hit.getScore());
            }
            results.add(result);
        }
        return new PageImpl(results, pageable, response.getHits().getTotalHits());
    }

    static final String[] FETCH_FIELDS = {"id", "title", "address",
            "content", "operatime", "price", "meter", "cat", "storeid", "goods_type", "agent_type"};
}

