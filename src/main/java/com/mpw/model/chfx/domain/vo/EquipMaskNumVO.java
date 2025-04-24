package com.mpw.model.chfx.domain.vo;

/**
 * 一个区域可以隐蔽的装备数量VO
 */
public class EquipMaskNumVO {
    /**
     * 装备名称
     */
    private String equipName;
    /**
     * 一个区域可以隐蔽的装备数量
     */
    private Integer maskNum;

    public EquipMaskNumVO() {
    }

    public EquipMaskNumVO(String equipName, Integer maskNum) {
        this.equipName = equipName;
        this.maskNum = maskNum;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public Integer getMaskNum() {
        return maskNum;
    }

    public void setMaskNum(Integer maskNum) {
        this.maskNum = maskNum;
    }
}
