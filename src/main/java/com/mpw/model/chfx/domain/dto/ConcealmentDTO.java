package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 隐蔽概率分析判断请求对象
 */
@ApiModel("隐蔽概率分析判断请求对象")
public class ConcealmentDTO implements Serializable {

    /**
     * 红方任务区域
     */
    @ApiModelProperty("红方任务区域")
    private String redDeployRange;

    /**
     * 蓝方任务区域
     */
    @ApiModelProperty("蓝方任务区域")
    private String blueDeployRange;

    /**
     * 红方火力点经度
     */
    @ApiModelProperty("红方火力点经度")
    private Double baseLat;

    /**
     * 红方火力点纬度
     */
    @ApiModelProperty("红方火力点纬度")
    private Double baseLng;

    /**
     * 蓝方火力点经度
     */
    @ApiModelProperty("蓝方火力点经度")
    private Double targetLat;

    /**
     * 蓝方火力点纬度
     */
    @ApiModelProperty("蓝方火力点纬度")
    private Double targetLng;

    /**
     * 红方火力点高度
     */
    @ApiModelProperty("红方火力点高度")
    private Integer previewHeightRed;

    /**
     * 蓝方火力点高度
     */
    @ApiModelProperty("蓝方火力点高度")
    private Integer previewHeightBlue;

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
     * 红方火力点组
     */
    @ApiModelProperty("红方火力点组信息")
    private List<FireGroupDTO> fireGroupListRed;

    /**
     * 蓝方火力点组
     */
    @ApiModelProperty("蓝方火力点组信息")
    private List<FireGroupDTO> fireGroupListBlue;

    public String getRedDeployRange() {
        return redDeployRange;
    }

    public void setRedDeployRange(String redDeployRange) {
        this.redDeployRange = redDeployRange;
    }

    public String getBlueDeployRange() {
        return blueDeployRange;
    }

    public void setBlueDeployRange(String blueDeployRange) {
        this.blueDeployRange = blueDeployRange;
    }

    public Double getBaseLat() {
        return baseLat;
    }

    public void setBaseLat(Double baseLat) {
        this.baseLat = baseLat;
    }

    public Double getBaseLng() {
        return baseLng;
    }

    public void setBaseLng(Double baseLng) {
        this.baseLng = baseLng;
    }

    public Double getTargetLat() {
        return targetLat;
    }

    public void setTargetLat(Double targetLat) {
        this.targetLat = targetLat;
    }

    public Double getTargetLng() {
        return targetLng;
    }

    public void setTargetLng(Double targetLng) {
        this.targetLng = targetLng;
    }

    public Integer getPreviewHeightRed() {
        return previewHeightRed;
    }

    public void setPreviewHeightRed(Integer previewHeightRed) {
        this.previewHeightRed = previewHeightRed;
    }

    public Integer getPreviewHeightBlue() {
        return previewHeightBlue;
    }

    public void setPreviewHeightBlue(Integer previewHeightBlue) {
        this.previewHeightBlue = previewHeightBlue;
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

    public List<FireGroupDTO> getFireGroupListRed() {
        return fireGroupListRed;
    }

    public void setFireGroupListRed(List<FireGroupDTO> fireGroupListRed) {
        this.fireGroupListRed = fireGroupListRed;
    }

    public List<FireGroupDTO> getFireGroupListBlue() {
        return fireGroupListBlue;
    }

    public void setFireGroupListBlue(List<FireGroupDTO> fireGroupListBlue) {
        this.fireGroupListBlue = fireGroupListBlue;
    }
}
