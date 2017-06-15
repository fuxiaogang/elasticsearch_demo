package com.wlw.es.wlsearch.dto;

import java.util.*;

import static javafx.scene.input.KeyCode.H;

/**
 * @author fuxg
 * @create 2017-01-24 15:45
 */
public class SearchResult {

    private Long count = 0l;
    private Long took; //耗时，ms
    private List<Map<String, Object>> datas = new ArrayList<>();

    public static SearchResult EMPTY = new SearchResult(Collections.emptyList(), 0l);

    public SearchResult(List<Map<String, Object>> datas, Long count) {
        this.count = count;
        this.datas = datas;
    }

    public SearchResult(List<Map<String, Object>> datas, Long count, Long took) {
        this.count = count;
        this.datas = datas;
        this.took = took;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public void setData(List<Map<String, Object>> datas) {
        this.datas = datas;
    }

    public Long getTook() {
        return took;
    }

    public void setTook(Long took) {
        this.took = took;
    }
}
