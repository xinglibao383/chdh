package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 伪装VO
 */
public class MaskVO implements Serializable {
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
    /**
     * 该区域可以伪装各个装备的数量
     */
    private List<EquipMaskNumVO> equipMaskNumVOList;
    /**
     * 伪装程度VO列表
     */
    private List<MaskDegreeAreaVO> maskDegreeAreaVOList;
    /**
     * 伪装程度VO列表
     */
    private List<MaskDegreeVO> maskDegreeVOList;

    public MaskVO() {
    }

    public MaskVO(Double canHideArea, Double canNotHideArea, Double totalArea, Double ratio, List<EquipMaskNumVO> equipMaskNumVOList, List<MaskDegreeAreaVO> maskDegreeAreaVOList, List<MaskDegreeVO> maskDegreeVOList) {
        this.canHideArea = canHideArea;
        this.canNotHideArea = canNotHideArea;
        this.totalArea = totalArea;
        this.ratio = ratio;
        this.equipMaskNumVOList = equipMaskNumVOList;
        this.maskDegreeAreaVOList = maskDegreeAreaVOList;
        this.maskDegreeVOList = maskDegreeVOList;
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

    public List<EquipMaskNumVO> getEquipMaskNumVOList() {
        return equipMaskNumVOList;
    }

    public void setEquipMaskNumVOList(List<EquipMaskNumVO> equipMaskNumVOList) {
        this.equipMaskNumVOList = equipMaskNumVOList;
    }

    public List<MaskDegreeAreaVO> getMaskDegreeAreaVOList() {
        return maskDegreeAreaVOList;
    }

    public void setMaskDegreeAreaVOList(List<MaskDegreeAreaVO> maskDegreeAreaVOList) {
        this.maskDegreeAreaVOList = maskDegreeAreaVOList;
    }

    public List<MaskDegreeVO> getMaskDegreeVOList() {
        return maskDegreeVOList;
    }

    public void setMaskDegreeVOList(List<MaskDegreeVO> maskDegreeVOList) {
        this.maskDegreeVOList = maskDegreeVOList;
    }
}
