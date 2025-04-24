package com.mpw.model.chfx.domain.dto;

public class Dem2DTO {
    //射击位置
    //高度
    private double weaponPureHeight;
    //经度
    private double baseLng;
    //纬度
    private double baseLat;

    private String deployRange;
    private String deployRangeTwo;




    public double getWeaponPureHeight() {
        return weaponPureHeight;
    }

    public void setWeaponPureHeight(double weaponPureHeight) {
        this.weaponPureHeight = weaponPureHeight;
    }

    public double getBaseLng() {
        return baseLng;
    }

    public void setBaseLng(double baseLng) {
        this.baseLng = baseLng;
    }

    public double getBaseLat() {
        return baseLat;
    }

    public void setBaseLat(double baseLat) {
        this.baseLat = baseLat;
    }

    public String getDeployRange() {
        return deployRange;
    }

    public void setDeployRange(String deployRange) {
        this.deployRange = deployRange;
    }

    public String getDeployRangeTwo() {
        return deployRangeTwo;
    }

    public void setDeployRangeTwo(String deployRangeTwo) {
        this.deployRangeTwo = deployRangeTwo;
    }
}
