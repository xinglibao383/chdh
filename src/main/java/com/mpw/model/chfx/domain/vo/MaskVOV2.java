package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.entity.DemCsv;

import java.io.Serializable;
import java.util.List;

/**
 * 伪装VO
 */
public class MaskVOV2 implements Serializable {
    /**
     * 可以隐蔽的面积
     */
    private Double canHideArea;
    /**
     * 不可以隐蔽的面积
     */
    private Double canNotHideArea;
    /**
     * 总面积
     */
    private Double totalArea;
    /**
     * 可以隐蔽的面积占总面积的比例
     */
    private Double ratio;

    List<DemCsv> demCsvList;

    public List<DemCsv> getDemCsvList() {
        return demCsvList;
    }

    public void setDemCsvList(List<DemCsv> demCsvList) {
        this.demCsvList = demCsvList;
    }

    public MaskVOV2(Double canHideArea, Double canNotHideArea, Double totalArea, Double ratio, List<DemCsv> demCsvList) {
        this.canHideArea = canHideArea;
        this.canNotHideArea = canNotHideArea;
        this.totalArea = totalArea;
        this.ratio = ratio;
        this.demCsvList = demCsvList;
    }

    public MaskVOV2() {
    }

    public MaskVOV2(Double canHideArea, Double canNotHideArea, Double totalArea, Double ratio) {
        this.canHideArea = canHideArea;
        this.canNotHideArea = canNotHideArea;
        this.totalArea = totalArea;
        this.ratio = ratio;
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

    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
