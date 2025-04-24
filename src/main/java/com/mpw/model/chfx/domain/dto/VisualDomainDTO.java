package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 可视域分析判断请求对象
 */
@ApiModel("可视域分析判断请求对象")
public class VisualDomainDTO implements Serializable {

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
     * 装备净高（观察视线净高）
     */
    @ApiModelProperty("装备净高（观察视线净高）")
    private Integer weaponPureHeight;

    /**
     * 任务区域
     */
    @ApiModelProperty("任务区域")
    private String appointmentArea;

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

    public String getAppointmentArea() {
        return appointmentArea;
    }

    public void setAppointmentArea(String appointmentArea) {
        this.appointmentArea = appointmentArea;
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
