package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 水路機動分析VO
 */
public class WaterVO implements Serializable {
    /**
     * 该区域平均水深
     */
    Double waterAverageDepth;
    /**
     * 该区域总面积
     */
    Double totalAcreage;
    /**
     * 该区域水域面积
     */
    Double waterAcreage;
    /**
     * 该区域水域面积占总面积的比例
     */
    Double ratio;

    public WaterVO() {
    }

    public WaterVO(Double waterAverageDepth, Double totalAcreage, Double waterAcreage, Double ratio) {
        this.waterAverageDepth = waterAverageDepth;
        this.totalAcreage = totalAcreage;
        this.waterAcreage = waterAcreage;
        this.ratio = ratio;
    }

    public Double getWaterAverageDepth() {
        return waterAverageDepth;
    }

    public void setWaterAverageDepth(Double waterAverageDepth) {
        this.waterAverageDepth = waterAverageDepth;
    }

    public Double getTotalAcreage() {
        return totalAcreage;
    }

    public void setTotalAcreage(Double totalAcreage) {
        this.totalAcreage = totalAcreage;
    }

    public Double getWaterAcreage() {
        return waterAcreage;
    }

    public void setWaterAcreage(Double waterAcreage) {
        this.waterAcreage = waterAcreage;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
