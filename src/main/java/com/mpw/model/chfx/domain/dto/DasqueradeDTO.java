package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 伪装程度分析判断请求对象
 */
@ApiModel("伪装程度分析判断请求对象")
public class DasqueradeDTO implements Serializable {

    /**
     * 我方任务区域
     */
    @ApiModelProperty("我方任务区域")
    private String appointmentArea;

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

    /**
     * 敌方火力点组
     */
    @ApiModelProperty("敌方火力点组信息")
    private List<FireGroupDTO> fireGroupList;

    public String getAppointmentArea() {
        return appointmentArea;
    }

    public void setAppointmentArea(String appointmentArea) {
        this.appointmentArea = appointmentArea;
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

    public Integer getWeaponPureHeight() {
        return weaponPureHeight;
    }

    public void setWeaponPureHeight(Integer weaponPureHeight) {
        this.weaponPureHeight = weaponPureHeight;
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

    public List<FireGroupDTO> getFireGroupList() {
        return fireGroupList;
    }

    public void setFireGroupList(List<FireGroupDTO> fireGroupList) {
        this.fireGroupList = fireGroupList;
    }
}
