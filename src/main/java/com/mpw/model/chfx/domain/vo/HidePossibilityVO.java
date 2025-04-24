package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.entity.DemCsv;

import java.util.List;

public class HidePossibilityVO {
    /**
     * 可以隐蔽的区域面积
     */
    private Double canHideAreaRed;
    /**
     * 不可以隐蔽的区域面积
     */
    private Double canNotHideAreaRed;
    /**
     * 总面积
     */
    private Double totalAreaRed;

    List<DemCsv> DemCsvListRed;

    /**
     * 可以隐蔽的区域面积占区域总面积的比例
     */
    private Double ratioRed;

    /**
     * 可以隐蔽的区域面积
     */
    private Double canHideAreaBlue;
    /**
     * 不可以隐蔽的区域面积
     */
    private Double canNotHideAreaBlue;
    /**
     * 总面积
     */
    private Double totalAreaBlue;
    /**
     * 可以隐蔽的区域面积占区域总面积的比例
     */
    private Double ratioBlue;

    List<DemCsv> DemCsvListBlue;

    public Double getCanHideAreaRed() {
        return canHideAreaRed;
    }

    public void setCanHideAreaRed(Double canHideAreaRed) {
        this.canHideAreaRed = canHideAreaRed;
    }

    public Double getCanNotHideAreaRed() {
        return canNotHideAreaRed;
    }

    public void setCanNotHideAreaRed(Double canNotHideAreaRed) {
        this.canNotHideAreaRed = canNotHideAreaRed;
    }

    public Double getTotalAreaRed() {
        return totalAreaRed;
    }

    public void setTotalAreaRed(Double totalAreaRed) {
        this.totalAreaRed = totalAreaRed;
    }

    public List<DemCsv> getDemCsvListRed() {
        return DemCsvListRed;
    }

    public void setDemCsvListRed(List<DemCsv> demCsvListRed) {
        DemCsvListRed = demCsvListRed;
    }

    public Double getRatioRed() {
        return ratioRed;
    }

    public void setRatioRed(Double ratioRed) {
        this.ratioRed = ratioRed;
    }

    public Double getCanHideAreaBlue() {
        return canHideAreaBlue;
    }

    public void setCanHideAreaBlue(Double canHideAreaBlue) {
        this.canHideAreaBlue = canHideAreaBlue;
    }

    public Double getCanNotHideAreaBlue() {
        return canNotHideAreaBlue;
    }

    public void setCanNotHideAreaBlue(Double canNotHideAreaBlue) {
        this.canNotHideAreaBlue = canNotHideAreaBlue;
    }

    public Double getTotalAreaBlue() {
        return totalAreaBlue;
    }

    public void setTotalAreaBlue(Double totalAreaBlue) {
        this.totalAreaBlue = totalAreaBlue;
    }

    public Double getRatioBlue() {
        return ratioBlue;
    }

    public void setRatioBlue(Double ratioBlue) {
        this.ratioBlue = ratioBlue;
    }

    public List<DemCsv> getDemCsvListBlue() {
        return DemCsvListBlue;
    }

    public void setDemCsvListBlue(List<DemCsv> demCsvListBlue) {
        DemCsvListBlue = demCsvListBlue;
    }

    public HidePossibilityVO() {
    }

    public HidePossibilityVO(Double canHideAreaRed, Double canNotHideAreaRed, Double totalAreaRed, List<DemCsv> demCsvListRed, Double ratioRed, Double canHideAreaBlue, Double canNotHideAreaBlue, Double totalAreaBlue, Double ratioBlue, List<DemCsv> demCsvListBlue) {
        this.canHideAreaRed = canHideAreaRed;
        this.canNotHideAreaRed = canNotHideAreaRed;
        this.totalAreaRed = totalAreaRed;
        DemCsvListRed = demCsvListRed;
        this.ratioRed = ratioRed;
        this.canHideAreaBlue = canHideAreaBlue;
        this.canNotHideAreaBlue = canNotHideAreaBlue;
        this.totalAreaBlue = totalAreaBlue;
        this.ratioBlue = ratioBlue;
        DemCsvListBlue = demCsvListBlue;
    }
}
