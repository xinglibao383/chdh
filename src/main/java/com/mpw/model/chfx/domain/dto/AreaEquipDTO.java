package com.mpw.model.chfx.domain.dto;

import java.io.Serializable;

/**
 * 地图上一个区域和一个装备id的DTO
 */
public class AreaEquipDTO implements Serializable {
    /**
     * 地图上一个区域的wkt
     */
    private String areaRange;

    /**
     * 装备编号id
     */
    private Long equipId;

    public AreaEquipDTO() {
    }

    public AreaEquipDTO(String areaRange, Long equipId) {
        this.areaRange = areaRange;
        this.equipId = equipId;
    }

    public String getAreaRange() {
        return areaRange;
    }

    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }

    public Long getEquipId() {
        return equipId;
    }

    public void setEquipId(Long equipId) {
        this.equipId = equipId;
    }
}
