package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 直线通视判断请求对象
 */
@ApiModel("直线通视判断请求对象")
public class CanSeeLineDTO implements Serializable {

    /**
     * 装备类型
     */
    @ApiModelProperty("装备类型")
    private Integer weaponType;

    /**
     * 装备净高（观察视线净高）
     */
    @ApiModelProperty("装备净高（观察视线净高）")
    private Integer weaponPureHeight;

    /**
     * 观察点经度
     */
    @ApiModelProperty("观察点经度")
    private Double baseLng;

    /**
     * 观察点纬度
     */
    @ApiModelProperty("观察点纬度")
    private Double baseLat;

    /**
     * 目标点经度
     */
    @ApiModelProperty("目标点经度")
    private Double targetLng;

    /**
     * 目标点纬度
     */
    @ApiModelProperty("目标点纬度")
    private Double targetLat;

    /**
     * 考虑植被
     */
    @ApiModelProperty("考虑植被")
    private Integer green;

    /**
     * 考虑建筑
     */
    @ApiModelProperty("考虑建筑")
    private Integer building;

    public CanSeeLineDTO() {
    }

    public CanSeeLineDTO(Integer weaponType, Integer weaponPureHeight, Double baseLng, Double baseLat, Double targetLng, Double targetLat, Integer green, Integer building) {
        this.weaponType = weaponType;
        this.weaponPureHeight = weaponPureHeight;
        this.baseLng = baseLng;
        this.baseLat = baseLat;
        this.targetLng = targetLng;
        this.targetLat = targetLat;
        this.green = green;
        this.building = building;
    }

    public Integer getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Integer weaponType) {
        this.weaponType = weaponType;
    }

    public Integer getWeaponPureHeight() {
        return weaponPureHeight;
    }

    public void setWeaponPureHeight(Integer weaponPureHeight) {
        this.weaponPureHeight = weaponPureHeight;
    }

    public Double getBaseLng() {
        return baseLng;
    }

    public void setBaseLng(Double baseLng) {
        this.baseLng = baseLng;
    }

    public Double getBaseLat() {
        return baseLat;
    }

    public void setBaseLat(Double baseLat) {
        this.baseLat = baseLat;
    }

    public Double getTargetLng() {
        return targetLng;
    }

    public void setTargetLng(Double targetLng) {
        this.targetLng = targetLng;
    }

    public Double getTargetLat() {
        return targetLat;
    }

    public void setTargetLat(Double targetLat) {
        this.targetLat = targetLat;
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
