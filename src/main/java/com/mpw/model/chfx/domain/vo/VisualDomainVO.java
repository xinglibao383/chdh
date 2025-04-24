package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.entity.DemCsv;

import java.io.Serializable;
import java.util.List;

/**
 * 可视域VO
 */
public class VisualDomainVO implements Serializable {
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
    /**
     * 作战区域数据
     */
    List<DemCsv> demCsvList;

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

    public List<DemCsv> getDemCsvList() {
        return demCsvList;
    }

    public void setDemCsvList(List<DemCsv> demCsvList) {
        this.demCsvList = demCsvList;
    }
}
