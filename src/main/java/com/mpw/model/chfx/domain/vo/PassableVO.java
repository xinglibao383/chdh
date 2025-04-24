package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 可通行性与速度分析VO
 */
public class PassableVO implements Serializable {
    /**
     * 装备编号id
     */
    Long equipId;

    /**
     * 载具名称
     */
    String equipName;
    /**
     * 是否可通行
     */
    Integer whetherPassable;
    /**
     * 可以通行的面积
     */
    Double canPassAcreage;
    /**
     * 可以通行的面积占总面积的比例
     */
    Double canPassRatio;
    /**
     * 可通行数量
     */
    Integer passageQuantity;
    /**
     * 可以通行的数量占总数量的比例
     */
    Double passageQuantityProportion;
    /**
     * 装备的平均行驶速度
     */
    private Double avgSpeed;

    public Long getEquipId() {
        return equipId;
    }

    public void setEquipId(Long equipId) {
        this.equipId = equipId;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public Integer getWhetherPassable() {
        return whetherPassable;
    }

    public void setWhetherPassable(Integer whetherPassable) {
        this.whetherPassable = whetherPassable;
    }

    public Double getCanPassAcreage() {
        return canPassAcreage;
    }

    public void setCanPassAcreage(Double canPassAcreage) {
        this.canPassAcreage = canPassAcreage;
    }

    public Double getCanPassRatio() {
        return canPassRatio;
    }

    public void setCanPassRatio(Double canPassRatio) {
        this.canPassRatio = canPassRatio;
    }

    public Integer getPassageQuantity() {
        return passageQuantity;
    }

    public void setPassageQuantity(Integer passageQuantity) {
        this.passageQuantity = passageQuantity;
    }

    public Double getPassageQuantityProportion() {
        return passageQuantityProportion;
    }

    public void setPassageQuantityProportion(Double passageQuantityProportion) {
        this.passageQuantityProportion = passageQuantityProportion;
    }

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
