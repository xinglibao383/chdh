package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.entity.DemCsv;

import java.io.Serializable;
import java.util.List;

public class ViewVO implements Serializable {
    Double visualArea;
    Double totalArea;
    Double ratio;
    List<List<RangeVO>> rangeVOList;
    List<DemCsv> demCsvList;

    public ViewVO() {
    }

    public ViewVO(Double visualArea, Double totalArea, Double ratio, List<List<RangeVO>> rangeVOList) {
        this.visualArea = visualArea;
        this.totalArea = totalArea;
        this.ratio = ratio;
        this.rangeVOList = rangeVOList;
    }

    public ViewVO(Double visualArea, Double totalArea, Double ratio, List<List<RangeVO>> rangeVOList, List<DemCsv> demCsvList) {
        this.visualArea = visualArea;
        this.totalArea = totalArea;
        this.ratio = ratio;
        this.rangeVOList = rangeVOList;
        this.demCsvList = demCsvList;
    }

    public List<DemCsv> getDemCsvList() {
        return demCsvList;
    }

    public void setDemCsvList(List<DemCsv> demCsvList) {
        this.demCsvList = demCsvList;
    }

    public Double getVisualArea() {
        return visualArea;
    }

    public void setVisualArea(Double visualArea) {
        this.visualArea = visualArea;
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

    public List<List<RangeVO>> getRangeVOList() {
        return rangeVOList;
    }

    public void setRangeVOList(List<List<RangeVO>> rangeVOList) {
        this.rangeVOList = rangeVOList;
    }
}
