package com.wlw.es.wlsearch.service.impl;

import com.wlw.es.common.SearchConfig;
import com.wlw.es.wlsearch.config.BoostScoreEnum;
import com.wlw.es.wlsearch.config.CatEnum;
import com.wlw.es.wlsearch.dto.SearchParam;
import com.wlw.es.wlsearch.dto.WLSearchResult;
import com.wlw.es.wlsearch.service.WLSearchService;
import com.wlw.util.ScriptReader;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fuxg
 * @create 2017-04-10 18:00
 */
@Service("wl_all_search")
public class AllSearchServiceImpl implements WLSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Page<WLSearchResult> search(SearchParam searchParam, Class<? extends WLSearchResult> resultClass) {
        WLSearchBuilders wlSearchBuilders = new WLSearchBuilders(elasticsearchTemplate, searchParam);
        buildQuery(wlSearchBuilders, searchParam);
        buildSort(wlSearchBuilders, searchParam);
        Page<WLSearchResult> page = wlSearchBuilders.withPageable(new PageRequest(searchParam.getPage() - 1, searchParam.getPageSize())).execute(resultClass);
        return page;
    }

    protected void buildQuery(WLSearchBuilders wlSearchBuilders, SearchParam searchParam) {
        String searchText = searchParam.getContent();

        BoolQueryBuilder queryBuilder = wlSearchBuilders.getQueryBuilder();
        BoolQueryBuilder filterBuilder = wlSearchBuilders.getFilterBuilder();

        BoolQueryBuilder textQuery = QueryBuilders.boolQuery();
        if (!"%%".equals(searchText)) { // %% 搜索全部
            textQuery.should(QueryBuilders.matchPhraseQuery("title", searchText).slop(0).boost(BoostScoreEnum.HIGHT.getBoost()));
            textQuery.should(QueryBuilders.matchPhraseQuery("shorttitle", searchText).slop(0).boost(BoostScoreEnum.MIDDLE.getBoost()));
            textQuery.should(QueryBuilders.wildcardQuery("title.not_analyzed", "*" + searchText + "*"));  //性能大幅下降
        }

        BoolQueryBuilder goodsFilterBuilder = QueryBuilders.boolQuery();
        goodsFilterBuilder.must(QueryBuilders.termQuery("cat", CatEnum.GOODS.getName()))
                .must(QueryBuilders.termQuery("isshow", 1))
                .must(QueryBuilders.termQuery("isdelete", 0));

        BoolQueryBuilder storeFilterBuilder = QueryBuilders.boolQuery();
        storeFilterBuilder.must(QueryBuilders.termQuery("cat", CatEnum.STORE.getName()))
                .must(QueryBuilders.termQuery("isshow", 1))
                .must(QueryBuilders.termQuery("isdelete", 1))
                .must(QueryBuilders.termQuery("businessIf", 2));

        BoolQueryBuilder otherQuery = new BoolQueryBuilder();
        otherQuery.should(goodsFilterBuilder);
        otherQuery.should(storeFilterBuilder);
        otherQuery.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery("cat", CatEnum.GOODS.getName(), CatEnum.STORE.getName())));

        queryBuilder.must(textQuery);
        filterBuilder.must(otherQuery);
    }

    protected void buildSort(WLSearchBuilders wlSearchBuilders, SearchParam searchParam) {
        Long areaid = searchParam.getAreaid(); //选择的区
        Long cityid = searchParam.getCityid(); //选择的市
        Long provid = searchParam.getProvid(); //选择的省
        Long locationCityid = searchParam.getLocationCityid(); //手机当前定位的城市
        Double lat = searchParam.getLatitude();
        Double lon = searchParam.getLongitude();
        Map<String, Object> params = new HashMap<>();
        params.put("p_lat", lat);
        params.put("p_lon", lon);
        params.put("p_distance", SearchConfig.ORDER_BY_MATCH_DISTANCE);

        /**
         * 如果有选择区域，则优先显示选择区域内的数据；如果选择全国，则优先显示手机定位城市的数据
         */
        if (areaid != null && areaid > 0) {
            params.put("p_location_id", areaid);
            params.put("p_location_name", "areaid");
        } else if (cityid != null && cityid > 0) {
            params.put("p_location_id", cityid);
            params.put("p_location_name", "cityid");
        } else if (provid != null && provid > 0) {
            params.put("p_location_id", provid);
            params.put("p_location_name", "provid");
        } else {
            params.put("p_location_id", locationCityid);
            params.put("p_location_name", "cityid");
        }
        String scoreScript = ScriptReader.getSortAllScript();
        if (scoreScript != null) {
            wlSearchBuilders.withScriptSort(scoreScript, params);
        }
        wlSearchBuilders.withScoreSort();
        wlSearchBuilders.withDistanceSortBuilder();
    }

}
