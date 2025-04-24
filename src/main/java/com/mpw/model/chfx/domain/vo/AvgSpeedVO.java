package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 平均速度VO
 */
public class AvgSpeedVO implements Serializable {
    /**
     * 装备编号id
     */
    private Long equipId;
    /**
     * 装备名称
     */
    private String equipName;
    /**
     * 装备的平均行驶速度
     */
    private Double avgSpeed;

    public AvgSpeedVO() {
    }

    public AvgSpeedVO(Long equipId, String equipName, Double avgSpeed) {
        this.equipId = equipId;
        this.equipName = equipName;
        this.avgSpeed = avgSpeed;
    }

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

    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }
}
