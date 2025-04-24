package com.mpw.model.chfx.domain.dto;

public class PersonnelDamageDTO {


    /**
     * 毁伤半径
     */
    private String radiuStep;
    /**
     * 时间ms
     */
    private double time;
    /**
     * 动压（单位：kPa）
     */
    private String overpressure;
    /**
     * 冲击波超压（单位：kPa）
     */
    private String dynamicPressurs;
    /**
     * 比冲量（Pa·s）
     */
    private String impulse;
    /**
     * 峰值压力
     */
    private String peakPressire;
    /**
     * 破坏岩石wkt
     */
    private String groundDamageWkt;
    /**
     * 破坏建筑wkt
     */
    private String buildingDamageWkt;

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


    public String getOverpressure() {
        return overpressure;
    }

    public void setOverpressure(String overpressure) {
        this.overpressure = overpressure;
    }

    public String getDynamicPressurs() {
        return dynamicPressurs;
    }

    public void setDynamicPressurs(String dynamicPressurs) {
        this.dynamicPressurs = dynamicPressurs;
    }

    public String getImpulse() {
        return impulse;
    }

    public void setImpulse(String impulse) {
        this.impulse = impulse;
    }

    public String getPeakPressire() {
        return peakPressire;
    }

    public void setPeakPressire(String peakPressire) {
        this.peakPressire = peakPressire;
    }

    public String getGroundDamageWkt() {
        return groundDamageWkt;
    }

    public void setGroundDamageWkt(String groundDamageWkt) {
        this.groundDamageWkt = groundDamageWkt;
    }

    public String getBuildingDamageWkt() {
        return buildingDamageWkt;
    }

    public void setBuildingDamageWkt(String buildingDamageWkt) {
        this.buildingDamageWkt = buildingDamageWkt;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
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
