package com.wlw.es.wlsearch.service;

import com.wlw.es.wlsearch.dto.SearchParam;
import com.wlw.es.wlsearch.dto.SearchResult;
import com.wlw.es.wlsearch.dto.WLSearchResult;
import org.springframework.data.domain.Page;

/**
 * @author fuxg
 * @create 2017-01-23 15:22
 */
public interface WLSearchService {

    Page<WLSearchResult> search(SearchParam searchParam, Class<? extends WLSearchResult> resultClass);

}
