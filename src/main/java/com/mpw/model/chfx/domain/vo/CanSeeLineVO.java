package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 一条线上是否可以通视VO
 */
public class CanSeeLineVO implements Serializable {
    /**
     * 一条线上可以通视的长度
     */
    private Double canSeeDistance;
    /**
     * 一条线上不可以通视的长度
     */
    private Double canNotSeeDistance;
    /**
     * 一条线上可以通视的长度占总长度的比例
     */
    private Double ratio;
    /**
     * 一条线上线段列表
     */
    private List<RangeVO> rangeVOList;

    public CanSeeLineVO() {
    }

    public CanSeeLineVO(Double canSeeDistance, Double canNotSeeDistance, Double ratio, List<RangeVO> rangeVOList) {
        this.canSeeDistance = canSeeDistance;
        this.canNotSeeDistance = canNotSeeDistance;
        this.ratio = ratio;
        this.rangeVOList = rangeVOList;
    }

    public Double getCanSeeDistance() {
        return canSeeDistance;
    }

    public void setCanSeeDistance(Double canSeeDistance) {
        this.canSeeDistance = canSeeDistance;
    }

    public Double getCanNotSeeDistance() {
        return canNotSeeDistance;
    }

    public void setCanNotSeeDistance(Double canNotSeeDistance) {
        this.canNotSeeDistance = canNotSeeDistance;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public List<RangeVO> getRangeVOList() {
        return rangeVOList;
    }

    public void setRangeVOList(List<RangeVO> rangeVOList) {
        this.rangeVOList = rangeVOList;
    }
}
