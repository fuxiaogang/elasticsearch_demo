package com.wlw.es.wlsearch.config;

/**
 * @author fuxg
 * @create 2017-04-22 10:50
 */
public enum BoostScoreEnum {
    HIGHT(1.2f), MIDDLE(0.5f), LOW(0.3f);

    private Float boost;

    BoostScoreEnum(float boost) {
        this.boost = boost;
    }

    public float getBoost() {
        return boost;
    }
}
