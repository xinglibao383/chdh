package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("ROUTE.JD_EQUIP")
public class Equip {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("TYPE")
    private String type;

    /**
     * 越野速度
     */
    @TableField("CROSS_SPEED")
    private Double crossSpeed;

    /**
     * 公路速度
     */
    @TableField("ROAD_SPEED")
    private Double roadSpeed;

    /**
     * 装备净高
     */
    @TableField("HEIGHT")
    private Double height;

    /**
     * 战斗全重
     */
    @TableField("WIGHT")
    private Double wight;

    /**
     * 半径
     */
    @TableField("RADIUS")
    private Double radius;

    /**
     * 履带长度
     */
    @TableField("TRACK_LENGTH")
    private Double trackLength;

    /**
     * 履带宽度
     */
    @TableField("TRACK_WIDTH")
    private Double trackWIDTH;

    /**
     * 轮胎半径
     */
    @TableField("TYRE_RADIUS")
    private Double tyreRadius;

    /**
     * 轮胎宽度
     */
    @TableField("TYRE_WIDTH")
    private Double tyreWidth;

    /**
     * 作用面积
     */
    @TableField("EFFECT_AREA")
    private Double effectArea;

    public Double getEffectArea() {
        return effectArea;
    }

    public void setEffectArea(Double effectArea) {
        this.effectArea = effectArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCrossSpeed() {
        return crossSpeed;
    }

    public void setCrossSpeed(Double crossSpeed) {
        this.crossSpeed = crossSpeed;
    }

    public Double getRoadSpeed() {
        return roadSpeed;
    }

    public void setRoadSpeed(Double roadSpeed) {
        this.roadSpeed = roadSpeed;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWight() {
        return wight;
    }

    public void setWight(Double wight) {
        this.wight = wight;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(Double trackLength) {
        this.trackLength = trackLength;
    }

    public Double getTrackWIDTH() {
        return trackWIDTH;
    }

    public void setTrackWIDTH(Double trackWIDTH) {
        this.trackWIDTH = trackWIDTH;
    }

    public Double getTyreRadius() {
        return tyreRadius;
    }

    public void setTyreRadius(Double tyreRadius) {
        this.tyreRadius = tyreRadius;
    }

    public Double getTyreWidth() {
        return tyreWidth;
    }

    public void setTyreWidth(Double tyreWidth) {
        this.tyreWidth = tyreWidth;
    }

    public Equip() {
    }
}
