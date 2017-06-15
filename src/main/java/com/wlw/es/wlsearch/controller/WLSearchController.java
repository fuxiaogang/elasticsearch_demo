package com.wlw.es.wlsearch.controller;

import com.wlw.es.wlsearch.config.CatEnum;
import com.wlw.es.wlsearch.dto.SearchParam;
import com.wlw.es.wlsearch.dto.WLSearchResult;
import com.wlw.es.wlsearch.service.impl.AllSearchServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author fuxg
 * @create 2017-02-04 14:15
 */
@RestController
public class WLSearchController {

    private final Logger logger = LoggerFactory.getLogger(WLSearchController.class);

    @Autowired
    AllSearchServiceImpl allSearchService;

    @RequestMapping("/")
    public Map<String, Object> index(final SearchParam searchParam) {
        return search(searchParam);
    }

    @RequestMapping(value = "search", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> search(final SearchParam searchParam) {
        logger.info("search request: " + searchParam.toString());

        searchParam.setCat(CatEnum.ALL.getId());

        final Map<String, Object> result = new HashMap<>();
        String searchText = searchParam.getContent();
        if (StringUtils.isBlank(searchText)) {
            result.put("status", "FAILD");
            result.put("code", "1");
            result.put("message", "搜索内容为空");
        } else {
            Page<WLSearchResult> page = allSearchService.search(searchParam, WLSearchResult.class);
            List<WLSearchResult> datas = page.getContent();
            if (!datas.isEmpty()) {
                result.put("list", datas);
            } else {
                result.put("code", "99");
                result.put("message", "查无数据");
            }
            logger.info("search response: hit rows={}", page.getTotalElements());
        }
        return result;
    }
}

