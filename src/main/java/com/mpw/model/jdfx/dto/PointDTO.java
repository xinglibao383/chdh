package com.mpw.model.jdfx.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("点")
public class PointDTO implements Serializable {
    private static final long serialVersionUID = -1L;
    @ApiModelProperty("纬度")
    private Double lat;
    @ApiModelProperty("经度")
    private Double lng;
    private Integer isWater;

    @JsonIgnore
    private Double length;
    @JsonIgnore
    private String demType;

    public PointDTO() {
    }

    public PointDTO(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public PointDTO(Double lat, Double lng, Integer isWater, Double length, String demType) {
        this.lat = lat;
        this.lng = lng;
        this.isWater = isWater;
        this.length = length;
        this.demType = demType;
    }

    public PointDTO(Double length) {
        this.length = length;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getIsWater() {
        return isWater;
    }

    public void setIsWater(Integer isWater) {
        this.isWater = isWater;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getDemType() {
        return demType;
    }

    public void setDemType(String demType) {
        this.demType = demType;
    }
}
