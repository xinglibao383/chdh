package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 通视判断请求对象
 */
@ApiModel("通视判断请求对象")
public class CanSeeDTO implements Serializable {

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
     * 作战范围半径
     */
    @ApiModelProperty("作战范围半径")
    private Double radius;

    /**
     * 作战范围
     */
    @ApiModelProperty("作战范围")
    private String fightRange;

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

    public CanSeeDTO() {
    }

    public CanSeeDTO(Integer weaponType, Integer weaponPureHeight, Double baseLng, Double baseLat, Double radius, String fightRange, Integer green, Integer building) {
        this.weaponType = weaponType;
        this.weaponPureHeight = weaponPureHeight;
        this.baseLng = baseLng;
        this.baseLat = baseLat;
        this.radius = radius;
        this.fightRange = fightRange;
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

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getFightRange() {
        return fightRange;
    }

    public void setFightRange(String fightRange) {
        this.fightRange = fightRange;
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
