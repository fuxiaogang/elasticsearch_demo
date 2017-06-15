package com.wlw.es.wlsearch.controller;

import com.wlw.es.common.SearchConfig;
import com.wlw.es.wlsearch.dto.SearchParam;
import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuxg
 * @create 2017-04-21 11:20
 */
@Controller
@RequestMapping("/suggest")
public class WLSuggestionController {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping("/wl")
    @ResponseBody
    public List<String> suggestion(SearchParam searchParam) {
        String index = SearchConfig.INDEX_NAME_ALIAS;
        String searchText = searchParam.getContent();
        String field = "title.suggest";
        String sugname = "sug";
        List<String> types = new ArrayList<>();
        types.add("store");

        List<String> sugTitles = new ArrayList<>();
        if (!types.isEmpty()) {
            CompletionSuggestionBuilder suggestionBuilder = new CompletionSuggestionBuilder(sugname);
            suggestionBuilder.field(field).text(searchText).size(5).addContextField("type", types);
            SuggestResponse response = elasticsearchTemplate.suggest(suggestionBuilder, index);
            List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> results = response.getSuggest().getSuggestion(sugname).getEntries();
            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> op : results) {
                List<? extends Suggest.Suggestion.Entry.Option> options = op.getOptions();
                for (Suggest.Suggestion.Entry.Option pp : options) {
                    sugTitles.add(pp.getText().string());
                }
            }
        }
        return sugTitles;
    }
}
