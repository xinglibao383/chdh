package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 越野机动分析VO
 */
public class WildVO implements Serializable {
    /**
     * 总面积
     */
    Double totalAcreage;
    /**
     * 越野机动分析VO列表
     */
    List<WildDegreeVO> degreeVOList;
    /**
     * 植被面积
     */
    Double greenAcreage;
    /**
     * 植被面积占总面积的比例
     */
    Double greenRatio;

    /**
     * 居民区面积
     */
    Double liveAcreage;

    /**
     * 居民区面积占总面积的比例
     */
    Double liveRatio;

    public WildVO() {
    }

    public WildVO(Double totalAcreage, List<WildDegreeVO> degreeVOList, Double greenAcreage, Double greenRatio, Double liveAcreage, Double liveRatio) {
        this.totalAcreage = totalAcreage;
        this.degreeVOList = degreeVOList;
        this.greenAcreage = greenAcreage;
        this.greenRatio = greenRatio;
        this.liveAcreage = liveAcreage;
        this.liveRatio = liveRatio;
    }

    public Double getTotalAcreage() {
        return totalAcreage;
    }

    public void setTotalAcreage(Double totalAcreage) {
        this.totalAcreage = totalAcreage;
    }

    public List<WildDegreeVO> getDegreeVOList() {
        return degreeVOList;
    }

    public void setDegreeVOList(List<WildDegreeVO> degreeVOList) {
        this.degreeVOList = degreeVOList;
    }

    public Double getGreenAcreage() {
        return greenAcreage;
    }

    public void setGreenAcreage(Double greenAcreage) {
        this.greenAcreage = greenAcreage;
    }

    public Double getGreenRatio() {
        return greenRatio;
    }

    public void setGreenRatio(Double greenRatio) {
        this.greenRatio = greenRatio;
    }

    public Double getLiveAcreage() {
        return liveAcreage;
    }

    public void setLiveAcreage(Double liveAcreage) {
        this.liveAcreage = liveAcreage;
    }

    public Double getLiveRatio() {
        return liveRatio;
    }

    public void setLiveRatio(Double liveRatio) {
        this.liveRatio = liveRatio;
    }
}
