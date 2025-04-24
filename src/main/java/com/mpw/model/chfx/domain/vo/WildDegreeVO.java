package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 越野机动分析VO
 */
public class WildDegreeVO implements Serializable {
    /**
     * 等级
     */
    private Integer level;
    /**
     * 最小梯度
     */
    private Double gradientStart;
    /**
     * 最大梯度
     */
    private Double gradientEnd;
    /**
     * 总面积
     */
    private Double totalAcreage;
    /**
     * 该等级的梯度的面积
     */
    private Double acreage;
    /**
     * 该等级的梯度占总面积的比例
     */
    private Double ratio;

    public WildDegreeVO() {
    }

    public WildDegreeVO(Integer level, Double gradientStart, Double gradientEnd, Double totalAcreage, Double acreage, Double ratio) {
        this.level = level;
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.totalAcreage = totalAcreage;
        this.acreage = acreage;
        this.ratio = ratio;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getGradientStart() {
        return gradientStart;
    }

    public void setGradientStart(Double gradientStart) {
        this.gradientStart = gradientStart;
    }

    public Double getGradientEnd() {
        return gradientEnd;
    }

    public void setGradientEnd(Double gradientEnd) {
        this.gradientEnd = gradientEnd;
    }

    public Double getTotalAcreage() {
        return totalAcreage;
    }

    public void setTotalAcreage(Double totalAcreage) {
        this.totalAcreage = totalAcreage;
    }

    public Double getAcreage() {
        return acreage;
    }

    public void setAcreage(Double acreage) {
        this.acreage = acreage;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
