package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 装备速度系数
 */
@TableName("ROUTE.JD_EQUIP_SPEED")
public class EquipSpeedCoefficient {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 设备id
     */
    @TableField("EQUIP_ID")
    private Long equipId;

    /**
     * 地质类型
     */
    @TableField("DEM_TYPE")
    private Integer demType;

    /**
     * 速度系数
     */
    @TableField("COEFFICIENT")
    private Double coefficient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEquipId() {
        return equipId;
    }

    public void setEquipId(Long equipId) {
        this.equipId = equipId;
    }

    public Integer getDemType() {
        return demType;
    }

    public void setDemType(Integer demType) {
        this.demType = demType;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public EquipSpeedCoefficient(Long equipId, Integer demType, Double coefficient) {
        this.equipId = equipId;
        this.demType = demType;
        this.coefficient = coefficient;
    }

    public EquipSpeedCoefficient() {
    }
}
