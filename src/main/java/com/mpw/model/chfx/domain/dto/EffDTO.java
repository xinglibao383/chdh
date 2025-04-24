package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("射击效能DTO")
public class EffDTO implements Serializable {
    @ApiModelProperty("精度")
    private Double lng;
    @ApiModelProperty("纬度")
    private Double lat;
    @ApiModelProperty("武器id")
    private Long id;
    @ApiModelProperty("射击高度")
    private Double height;

    /*@ApiModelProperty("敌方经度")
    private Double enemyLng;
    @ApiModelProperty("敌方纬度")
    private Double enemyLat;*/

    public EffDTO() {
    }

    /*public Double getEnemyLng() {
        return enemyLng;
    }

    public void setEnemyLng(Double enemyLng) {
        this.enemyLng = enemyLng;
    }

    public Double getEnemyLat() {
        return enemyLat;
    }

    public void setEnemyLat(Double enemyLat) {
        this.enemyLat = enemyLat;
    }*/

    /*public EffDTO(Double lng, Double lat, Long id, Double height, Double enemyLng, Double enemyLat) {
        this.lng = lng;
        this.lat = lat;
        this.id = id;
        this.height = height;
        this.enemyLng = enemyLng;
        this.enemyLat = enemyLat;
    }*/

    public EffDTO(Double lng, Double lat, Long id) {
        this.lng = lng;
        this.lat = lat;
        this.id = id;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
