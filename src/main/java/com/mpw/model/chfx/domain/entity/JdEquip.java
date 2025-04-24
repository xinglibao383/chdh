package com.mpw.model.chfx.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * 数据库装备表对应的实体类
 */
public class JdEquip implements Serializable {
    /**
     * 装备编号id
     */
    @TableField("ID")
    private Long id;

    /**
     * 装备类型
     */
    @TableField("TYPE")
    private String type;

    /**
     * 半径
     */
    @TableField("RADIUS")
    private Double radius;

    /**
     * 越野机动速度
     */
    @TableField("CROSS_SPEED")
    private Double crossSpeed;

    public JdEquip() {
    }

    public JdEquip(Long id, String type, Double radius, Double crossSpeed) {
        this.id = id;
        this.type = type;
        this.radius = radius;
        this.crossSpeed = crossSpeed;
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

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getCrossSpeed() {
        return crossSpeed;
    }

    public void setCrossSpeed(Double crossSpeed) {
        this.crossSpeed = crossSpeed;
    }
}
