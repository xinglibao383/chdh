package com.mpw.model.common.enums;

public enum DEMType1Enum {

    ALLOTMENTS("菜地", 0, "allotments"),//松土
    CEMETERY("墓地", 1, "cemetery"),//人工建筑
    FARMLAND("农田", 2, "farmland"),//松土
    FOREST("森林", 3, "forest"),//丛林
    GRASS("草坪", 4, "grass"),//松土
    INDUSTRIAL("工厂", 5, "industrial"),//硬土
    MILITARY("军队", 6, " military"),//硬土
    OTHER("其他", 7, "other"),//沙土
    PARK("公园", 8, "park"),//人工建筑
    RESIDENTIAL("居民区", 9, "residential"),//人工建筑
    SCRUB("矮树", 10, "scrub"),//丛林
    UNCLASSIFIED("未分类", 11, "unclassified"),//未分类
    WATER("水", 12, "water");//水


    public String getName() {
        return name;
    }

    public Integer getNum() {
        return num;
    }

    public String getStr() {
        return str;
    }

    DEMType1Enum(String name, Integer num, String str) {
        this.name = name;
        this.num = num;
        this.str = str;
    }

    private String name;
    private Integer num;
    private String str;
}
