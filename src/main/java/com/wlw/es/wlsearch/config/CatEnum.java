/**
 *
 */
package com.wlw.es.wlsearch.config;

/**
 * @author fuxg
 * @Description TODO
 * @date 2016年6月4日
 */
public enum CatEnum {

    ALL("所有", 1), STORE("店铺", 2), GOODS("商品", 3);
    private String name;
    private int id;

    private CatEnum(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static CatEnum getCatById(int id) {
        for (CatEnum catEnum : CatEnum.values()) {
            if (catEnum.getId() == id) {
                return catEnum;
            }
        }
        return CatEnum.ALL;
    }

    public static CatEnum getCatByName(String name) {
        for (CatEnum catEnum : CatEnum.values()) {
            if (catEnum.getName().equals(name)) {
                return catEnum;
            }
        }
        return CatEnum.ALL;
    }

}
