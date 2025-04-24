package com.mpw.model.chfx.domain.dto;

import java.io.Serializable;

/**
 * 地图上一个区域的DTO
 */
public class AreaDTO implements Serializable {
    /**
     * 地图上一个区域的wkt
     */
    private String areaRange;

    public AreaDTO() {
    }

    public AreaDTO(String areaRange) {
        this.areaRange = areaRange;
    }

    public String getAreaRange() {
        return areaRange;
    }

    public void setAreaRange(String areaRange) {
        this.areaRange = areaRange;
    }
}
