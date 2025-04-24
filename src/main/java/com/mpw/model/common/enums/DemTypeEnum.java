package com.mpw.model.common.enums;

public enum DemTypeEnum {

    SCARIFY_SOIL("松土", 1),//松土
    ARTIFICIAL_STRUCTURE("人工建筑", 2),//人工建筑信息
    FOREST("丛林", 3),//丛林
    PAN_SOIL("硬土", 4),//硬土
    SANDY_SOIL("沙土", 5),//沙土
    IS_WATER("涉水", 6);//涉水

    public String getName() {
        return name;
    }

    public Integer getNum() {
        return num;
    }


    DemTypeEnum(String name, Integer num) {
        this.name = name;
        this.num = num;
    }

    private String name;
    private Integer num;

    public static DemTypeEnum getByName(String name){
        for (DemTypeEnum demTypeEnum : DemTypeEnum.values()) {
            if (demTypeEnum.getName().equals(name)) {
                return demTypeEnum;
            }
        }
        //默认硬土
        return PAN_SOIL;
    }
}
