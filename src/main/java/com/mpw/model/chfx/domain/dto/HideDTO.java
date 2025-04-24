package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 隐蔽判断请求对象
 */
@ApiModel("隐蔽判断请求对象")
public class HideDTO implements Serializable {

    /**
     * 装备类型
     */
    @ApiModelProperty("装备类型")
    private Integer weaponType;

    /**
     * 装备净高
     */
    @ApiModelProperty("装备净高")
    private Integer weaponPureHeight;

    /**
     * 装备部署范围
     */
    @ApiModelProperty("装备部署范围")
    private String deployRange;

    /**
     * 敌人经度
     */
    @ApiModelProperty("敌人经度")
    private Double baseLng;

    /**
     * 敌人纬度
     */
    @ApiModelProperty("敌人纬度")
    private Double baseLat;

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

    public HideDTO() {
    }

    public HideDTO(Integer weaponType, Integer weaponPureHeight, String deployRange, Double baseLng, Double baseLat, Integer green, Integer building) {
        this.weaponType = weaponType;
        this.weaponPureHeight = weaponPureHeight;
        this.deployRange = deployRange;
        this.baseLng = baseLng;
        this.baseLat = baseLat;
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

    public String getDeployRange() {
        return deployRange;
    }

    public void setDeployRange(String deployRange) {
        this.deployRange = deployRange;
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
