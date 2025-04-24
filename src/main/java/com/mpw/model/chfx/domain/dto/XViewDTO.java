package com.mpw.model.chfx.domain.dto;

import java.io.Serializable;

public class XViewDTO implements Serializable {
    private Integer weaponPureHeight;
    private double baseLng;
    private double baseLat;
    private String deployRange;
    private Integer green;
    private Integer building;

    public XViewDTO(Integer weaponPureHeight, double baseLng, double baseLat, String deployRange, Integer green, Integer building) {
        this.weaponPureHeight = weaponPureHeight;
        this.baseLng = baseLng;
        this.baseLat = baseLat;
        this.deployRange = deployRange;
        this.green = green;
        this.building = building;
    }

    public Integer getWeaponPureHeight() {
        return weaponPureHeight;
    }

    public void setWeaponPureHeight(Integer weaponPureHeight) {
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

    public Integer getGreen() {
        return green;
    }

    public void setGreen(Integer green) {
        this.green = green;
    }

    public Integer getBuilding() {
        return building;
    }

    public void setBuilding(Integer building) {
        this.building = building;
    }
}
