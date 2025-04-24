package com.mpw.model.chfx.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * 数据库载重系数表对应的实体类
 */
public class ChTypeWeight implements Serializable {
    /**
     * 编号id
     */
    @TableField("ID")
    Long id;
    /**
     * 地质类型
     */
    @TableField("DEM_TYPE")
    String demType;
    /**
     * 道路类型
     */
    @TableField("ROUTE_FCLASS")
    String routeFclass;
    /**
     * 载重系数
     */
    @TableField("WEIGHT")
    Double weight;

    public ChTypeWeight() {
    }

    public ChTypeWeight(Long id, String demType, String routeFclass, Double weight) {
        this.id = id;
        this.demType = demType;
        this.routeFclass = routeFclass;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDemType() {
        return demType;
    }

    public void setDemType(String demType) {
        this.demType = demType;
    }

    public String getRouteFclass() {
        return routeFclass;
    }

    public void setRouteFclass(String routeFclass) {
        this.routeFclass = routeFclass;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
