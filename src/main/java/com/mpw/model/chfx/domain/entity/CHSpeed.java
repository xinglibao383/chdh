package com.mpw.model.chfx.domain.entity;

import java.io.Serializable;

/**
 * 装备速度系数表对应的存储实体
 */
public class CHSpeed implements Serializable {

    /**
     * 编号
     */
    private Long id;

    /**
     * 装备编号id
     */
    private Long equipId;

    /**
     * 地形
     */
    private Integer demType;

    /**
     * 速度系数
     */
    private Double coefficient;

    public CHSpeed() {
    }

    public CHSpeed(Long id, Long equipId, Integer demType, Double coefficient) {
        this.id = id;
        this.equipId = equipId;
        this.demType = demType;
        this.coefficient = coefficient;
    }

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
}
