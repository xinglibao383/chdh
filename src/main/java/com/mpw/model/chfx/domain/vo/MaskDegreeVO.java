package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.entity.DemCsv;

import java.io.Serializable;
import java.util.List;

/**
 * 伪装程度VO
 */
public class MaskDegreeVO implements Serializable {
    /**
     * 伪装程度等级（数值）
     */
    private Integer maskDegree;
    /**
     * 该伪装程度等级对应的地图上的区域网格列表
     */
    private List<DemCsv> demCsvList;

    public MaskDegreeVO() {
    }

    public MaskDegreeVO(Integer maskDegree, List<DemCsv> demCsvList) {
        this.maskDegree = maskDegree;
        this.demCsvList = demCsvList;
    }

    public Integer getMaskDegree() {
        return maskDegree;
    }

    public void setMaskDegree(Integer maskDegree) {
        this.maskDegree = maskDegree;
    }

    public List<DemCsv> getDemCsvList() {
        return demCsvList;
    }

    public void setDemCsvList(List<DemCsv> demCsvList) {
        this.demCsvList = demCsvList;
    }
}
