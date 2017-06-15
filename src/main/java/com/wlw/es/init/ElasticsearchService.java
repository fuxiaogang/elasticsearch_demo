package com.wlw.es.init;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.index.engine.DocumentMissingException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author fuxg
 * @create 2017-04-18 15:09
 */

@Service
public class ElasticsearchService {

    Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    public SearchHit query(String indexName, String type, String field, String value) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withFilter(QueryBuilders.termQuery(field, value))
                .withIndices(indexName)
                .withTypes(type);
        return elasticsearchTemplate.query(searchQueryBuilder.build(), reponse -> {
            SearchHits hits = reponse.getHits();
            long total = hits.getTotalHits();
            if (total == 0) {
                return null;
            } else {
                return hits.getHits()[0];
            }
        });
    }

    public SearchHit queryByid(String indexName, String type, String id) {
        return query(indexName, type, "id", id);
    }


    public boolean insert(String indexName, String type, String id, Map<String, Object> data) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withIndexName(indexName)
                    .withType(type)
                    .withId(id)
                    .withSource(JSONObject.toJSONString(data))
                    .build();
            elasticsearchTemplate.index(indexQuery);
            logger.info("insert document index={},type={},id={} successful", indexName, type, id);
            return true;
        } catch (Exception e) {
            logger.error("insert document index={},type={},id={} fail", indexName, type, id, e);
            return false;
        }
    }

    public boolean updateDocument(String indexName, String type, String id, Map<String, Object> data) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(indexName, type, id);
            updateRequest.doc(data);
            elasticsearchTemplate.getClient().update(updateRequest).actionGet();
            logger.info("update document index={},type={},id={} successful", indexName, type, id);
            return true;
        } catch (DocumentMissingException e) {
            logger.error("update document index={},type={},id={} fail,miss document", indexName, type, id);
            return false;
        } catch (Exception e) {
            logger.error("update document index={},type={},id={} fail", indexName, type, id, e);
            return false;
        }
    }


    public boolean deleteDocument(String indexName, String type, String id) {
        try {
            elasticsearchTemplate.delete(indexName, type, id);
            logger.info("delete document index={},type={},id={} successful", indexName, type, id);
            return true;
        } catch (Exception e) {
            logger.error("delete document index={},type={},id={} fail", indexName, type, id, e);
            return false;
        }
    }

    public boolean createIndex(String indexName,Object setting) {
        if (!elasticsearchTemplate.indexExists(indexName)) {
            return elasticsearchTemplate.createIndex(indexName, setting);
        } else {
            return false;
        }
    }

    public boolean createIndex(String indexName) {
        if (!elasticsearchTemplate.indexExists(indexName)) {
            return elasticsearchTemplate.createIndex(indexName);
        } else {
            return false;
        }
    }

    public boolean createType(String indexName, String type, String mapping) {
        if (StringUtils.isNotBlank(indexName) && StringUtils.isNotBlank(type) && StringUtils.isNotBlank(mapping)) {
            return elasticsearchTemplate.putMapping(indexName, type, mapping);
        }
        return false;
    }


    /**
     * 创建索引的别名
     */
    public Boolean createAliase(String alias, String index) {
        AliasQuery aliasQuery = new AliasQuery();
        aliasQuery.setIndexName(index);
        aliasQuery.setAliasName(alias);
        return elasticsearchTemplate.addAlias(aliasQuery);
    }

    /**
     * 删除索引别名
     */
    public boolean deleteAliases(String index, String alias) {
        AliasQuery aliasQuery = new AliasQuery();
        aliasQuery.setIndexName(index);
        aliasQuery.setAliasName(alias);
        return elasticsearchTemplate.removeAlias(aliasQuery);
    }

}
