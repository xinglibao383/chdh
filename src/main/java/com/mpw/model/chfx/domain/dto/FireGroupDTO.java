package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 敌方火力点组信息
 */
public class FireGroupDTO {


    /**
     * 敌人火力点经度
     */
    @ApiModelProperty("敌人火力点经度")
    private Double baseLng;

    /**
     * 敌人火力点经度
     */
    @ApiModelProperty("敌人火力点经度")
    private Double baseLat;

    /**
     * 火力点高度
     */
    @ApiModelProperty("火力点高度")
    private Integer weaponPureHeight;

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

    public Integer getWeaponPureHeight() {
        return weaponPureHeight;
    }

    public void setWeaponPureHeight(Integer weaponPureHeight) {
        this.weaponPureHeight = weaponPureHeight;
    }
}
