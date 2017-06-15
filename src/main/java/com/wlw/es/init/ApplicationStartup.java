package com.wlw.es.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author fuxg
 * @create 2017-06-15 17:10
 */

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    IndexInitService indexInitService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        indexInitService.init();
    }
}
