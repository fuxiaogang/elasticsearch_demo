package com.wlw.es.wlsearch.dto;

import java.io.Serializable;

/**
 * @author fuxg
 * @create 2017-01-23 15:28
 */
public class SearchParam {

    protected String content; // 查询内容
    protected Integer cat; // 1-全部，2-店铺，3-产品，4-视频，5-新闻，6-问问，7-相册，8-游戏，9-人
    protected Long provid;
    protected Long cityid;
    protected Long areaid;
    protected Double latitude; // 当前维度
    protected Double longitude; // 当前经度
    protected Double radius; // 半径（km）

    protected Long locationProvid; //当前定位的省id
    protected Long locationCityid;
    protected Long locationAreaid;

    protected Long userid; //登陆用户id

    protected Integer pageSize;
    protected Integer page;

    protected String ver; //版本号
    protected String dvt; //设备 ios dvt = 1; android dvt = 2

    protected Integer source = 0; //app 0; pc 1;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCat() {
        return cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public Long getProvid() {
        return provid;
    }

    public void setProvid(Long provid) {
        this.provid = provid;
    }

    public Long getCityid() {
        return cityid;
    }

    public void setCityid(Long cityid) {
        this.cityid = cityid;
    }

    public Long getAreaid() {
        return areaid;
    }

    public void setAreaid(Long areaid) {
        this.areaid = areaid;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Long getLocationProvid() {
        return locationProvid;
    }

    public void setLocationProvid(Long locationProvid) {
        this.locationProvid = locationProvid;
    }

    public Long getLocationCityid() {
        return locationCityid;
    }

    public void setLocationCityid(Long locationCityid) {
        this.locationCityid = locationCityid;
    }

    public Long getLocationAreaid() {
        return locationAreaid;
    }

    public void setLocationAreaid(Long locationAreaid) {
        this.locationAreaid = locationAreaid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 0) pageSize = 8;
        if (pageSize > 50) pageSize = 50;
        return pageSize;
    }


    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        if (page == null || page <= 0) page = 1;
        if (page > 100) page = 100;
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getDvt() {
        return dvt;
    }

    public void setDvt(String dvt) {
        this.dvt = dvt;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "SearchParam{" +
                "content='" + content + '\'' +
                ", cat=" + cat +
                ", provid=" + provid +
                ", cityid=" + cityid +
                ", areaid=" + areaid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", locationProvid=" + locationProvid +
                ", locationCityid=" + locationCityid +
                ", locationAreaid=" + locationAreaid +
                ", userid=" + userid +
                ", pageSize=" + pageSize +
                ", page=" + page +
                ", ver='" + ver + '\'' +
                ", dvt='" + dvt + '\'' +
                '}';
    }
}
