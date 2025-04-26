package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.entity.DemCsv;

import java.util.List;

public class HidePossibilityVO {
    /**
     * 伪装程度VO列表
     */
    private List<MaskDegreeAreaVO> maskDegreeAreaVOListRed;
    /**
     * 伪装程度VO列表
     */
    private List<MaskDegreeVO> maskDegreeVOListRed;

    /**
     * 伪装程度VO列表
     */
    private List<MaskDegreeAreaVO> maskDegreeAreaVOListBlue;
    /**
     * 伪装程度VO列表
     */
    private List<MaskDegreeVO> maskDegreeVOListBlue;

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

    public HidePossibilityVO(List<MaskDegreeAreaVO> maskDegreeAreaVOListRed, List<MaskDegreeVO> maskDegreeVOListRed, List<MaskDegreeAreaVO> maskDegreeAreaVOListBlue, List<MaskDegreeVO> maskDegreeVOListBlue, Double canHideAreaRed, Double canNotHideAreaRed, Double totalAreaRed, List<DemCsv> demCsvListRed, Double ratioRed, Double canHideAreaBlue, Double canNotHideAreaBlue, Double totalAreaBlue, Double ratioBlue, List<DemCsv> demCsvListBlue) {
        this.maskDegreeAreaVOListRed = maskDegreeAreaVOListRed;
        this.maskDegreeVOListRed = maskDegreeVOListRed;
        this.maskDegreeAreaVOListBlue = maskDegreeAreaVOListBlue;
        this.maskDegreeVOListBlue = maskDegreeVOListBlue;
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

    public List<MaskDegreeAreaVO> getMaskDegreeAreaVOListRed() {
        return maskDegreeAreaVOListRed;
    }

    public void setMaskDegreeAreaVOListRed(List<MaskDegreeAreaVO> maskDegreeAreaVOListRed) {
        this.maskDegreeAreaVOListRed = maskDegreeAreaVOListRed;
    }

    public List<MaskDegreeVO> getMaskDegreeVOListRed() {
        return maskDegreeVOListRed;
    }

    public void setMaskDegreeVOListRed(List<MaskDegreeVO> maskDegreeVOListRed) {
        this.maskDegreeVOListRed = maskDegreeVOListRed;
    }

    public List<MaskDegreeAreaVO> getMaskDegreeAreaVOListBlue() {
        return maskDegreeAreaVOListBlue;
    }

    public void setMaskDegreeAreaVOListBlue(List<MaskDegreeAreaVO> maskDegreeAreaVOListBlue) {
        this.maskDegreeAreaVOListBlue = maskDegreeAreaVOListBlue;
    }

    public List<MaskDegreeVO> getMaskDegreeVOListBlue() {
        return maskDegreeVOListBlue;
    }

    public void setMaskDegreeVOListBlue(List<MaskDegreeVO> maskDegreeVOListBlue) {
        this.maskDegreeVOListBlue = maskDegreeVOListBlue;
    }

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
