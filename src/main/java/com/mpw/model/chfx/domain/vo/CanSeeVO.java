package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 是否可以通视VO
 */
public class CanSeeVO implements Serializable {
    /**
     * 可以通视的面积
     */
    private Double canSeeArea;
    /**
     * 不可以通视的面积
     */
    private Double canNotSeeArea;
    /**
     * 可以通视的面积占总面积的比例
     */
    private Double ratio;
    /**
     * 三百六十度线段列表
     */
    private List<List<RangeVO>> rangeVOList;

    public CanSeeVO() {
    }

    public CanSeeVO(Double canSeeArea, Double canNotSeeArea, Double ratio, List<List<RangeVO>> rangeVOList) {
        this.canSeeArea = canSeeArea;
        this.canNotSeeArea = canNotSeeArea;
        this.ratio = ratio;
        this.rangeVOList = rangeVOList;
    }

    public Double getCanSeeArea() {
        return canSeeArea;
    }

    public void setCanSeeArea(Double canSeeArea) {
        this.canSeeArea = canSeeArea;
    }

    public Double getCanNotSeeArea() {
        return canNotSeeArea;
    }

    public void setCanNotSeeArea(Double canNotSeeArea) {
        this.canNotSeeArea = canNotSeeArea;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public List<List<RangeVO>> getRangeVOList() {
        return rangeVOList;
    }

    public void setRangeVOList(List<List<RangeVO>> rangeVOList) {
        this.rangeVOList = rangeVOList;
    }
}
