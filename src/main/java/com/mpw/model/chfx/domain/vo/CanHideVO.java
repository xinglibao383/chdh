package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 是否隐蔽VO
 */
public class CanHideVO implements Serializable {
    /**
     * 可以隐蔽的区域面积
     */
    private Double canHideArea;
    /**
     * 不可以隐蔽的区域面积
     */
    private Double canNotHideArea;
    /**
     * 可以隐蔽的区域面积占区域总面积的比例
     */
    private Double ratio;
    /**
     * 三百六十度线段列表
     */
    private List<List<RangeVO>> rangeVOList;

    public CanHideVO() {
    }

    public CanHideVO(Double canHideArea, Double canNotHideArea, Double ratio, List<List<RangeVO>> rangeVOList) {
        this.canHideArea = canHideArea;
        this.canNotHideArea = canNotHideArea;
        this.ratio = ratio;
        this.rangeVOList = rangeVOList;
    }

    public Double getCanHideArea() {
        return canHideArea;
    }

    public void setCanHideArea(Double canHideArea) {
        this.canHideArea = canHideArea;
    }

    public Double getCanNotHideArea() {
        return canNotHideArea;
    }

    public void setCanNotHideArea(Double canNotHideArea) {
        this.canNotHideArea = canNotHideArea;
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
