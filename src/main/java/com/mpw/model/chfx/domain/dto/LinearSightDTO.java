package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 通视性分析判断请求对象
 */
@ApiModel("通视性分析判断请求对象")
public class LinearSightDTO implements Serializable {

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
     * 观测高度
     */
    @ApiModelProperty("观测高度")
    private Integer weaponPureHeight;

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
     * 目标高度
     */
    @ApiModelProperty("目标高度")
    private Integer targetHeight;


    /**
     * 环境影响
     */
    @ApiModelProperty("环境影响")
    private List<String> environmentalValue;

    /**
     * 季节影响
     */
    @ApiModelProperty("季节影响")
    private  String season;

    /**
     * 植被区域信息
     */
    @ApiModelProperty("植被区域信息")
    private List<VegetationDTO> vegetationDTOList;

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

    public Integer getTargetHeight() {
        return targetHeight;
    }

    public void setTargetHeight(Integer targetHeight) {
        this.targetHeight = targetHeight;
    }

    public List<String> getEnvironmentalValue() {
        return environmentalValue;
    }

    public void setEnvironmentalValue(List<String> environmentalValue) {
        this.environmentalValue = environmentalValue;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public List<VegetationDTO> getVegetationDTOList() {
        return vegetationDTOList;
    }

    public void setVegetationDTOList(List<VegetationDTO> vegetationDTOList) {
        this.vegetationDTOList = vegetationDTOList;
    }
}
