package com.wlw.es.wlsearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author fuxg
 * @create 2017-04-11 10:58
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WLSearchResult {
    private Integer id;
    private String ico;
    private String title;
    private String logo;
    private String address;
    private String content;
    private String operatime;
    private Float price;
    private String meter;
    private String url;
    private String cat;
//    @JsonIgnore
    private Integer storeid;
    @JsonIgnore
    private Integer agent_type;
//    @JsonIgnore
    private Integer goods_type;
//    @JsonIgnore
    private float score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperatime() {
        return operatime;
    }

    public void setOperatime(String operatime) {
        this.operatime = operatime;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Integer getStoreid() {
        return storeid;
    }

    public void setStoreid(Integer storeid) {
        this.storeid = storeid;
    }

    public Integer getAgent_type() {
        return agent_type;
    }

    public void setAgent_type(Integer agent_type) {
        this.agent_type = agent_type;
    }

    public Integer getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(Integer goods_type) {
        this.goods_type = goods_type;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

}
