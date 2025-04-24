package com.mpw.model.chfx.domain.dto;

public class staffDTO {


    /**
     * 范围半径
     */
    private String radiuStep;
    /**
     *  人员伤害信息
     */
    private String shLevel;

    /**
     * 人员伤害等级
     */
    private String cyLevel;

    /**
     * 人员伤害区域
     */
    private String wtk;


    public String getRadiuStep() {
        return radiuStep;
    }

    public void setRadiuStep(String radiuStep) {
        this.radiuStep = radiuStep;
    }

    public String getShLevel() {
        return shLevel;
    }

    public void setShLevel(String shLevel) {
        this.shLevel = shLevel;
    }

    public String getCyLevel() {
        return cyLevel;
    }

    public void setCyLevel(String cyLevel) {
        this.cyLevel = cyLevel;
    }

    public String getWtk() {
        return wtk;
    }

    public void setWtk(String wtk) {
        this.wtk = wtk;
    }
}
