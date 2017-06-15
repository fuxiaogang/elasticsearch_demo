package com.wlw.es.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author fuxg
 * @create 2017-04-18 16:09
 */
@Service
public class IndexInitService {

    @Autowired
    ElasticsearchService elasticsearchService;

    @Value("${index.name}")
    private String indexName;

    public void init() {
        String indexsetting = ReadESMappingFile.read("index.json");
        elasticsearchService.createIndex(indexName, indexsetting);
        elasticsearchService.createType(indexName, "goods", ReadESMappingFile.read("goods.json"));
        elasticsearchService.createType(indexName, "store", ReadESMappingFile.read("store.json"));
    }

}
