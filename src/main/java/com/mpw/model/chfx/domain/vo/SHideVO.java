package com.mpw.model.chfx.domain.vo;

import com.mpw.model.chfx.domain.vo.RangeVO;

import java.util.List;

public class SHideVO {

    //总 射击距离
    private Double totalDistance;
    //隐蔽距离
    private Double concealmentDistance;
    //隐蔽深度
    private Double coverDepth;
    private List<RangeVO> subLines;

    public SHideVO(Double totalDistance, Double concealmentDistance, Double coverDepth, List<RangeVO> subLines) {
        this.totalDistance = totalDistance;
        this.concealmentDistance = concealmentDistance;
        this.coverDepth = coverDepth;
        this.subLines = subLines;
    }

    public SHideVO() {
    }

    public List<RangeVO> getSubLines() {
        return subLines;
    }

    public void setSubLines(List<RangeVO> subLines) {
        this.subLines = subLines;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Double getConcealmentDistance() {
        return concealmentDistance;
    }

    public void setConcealmentDistance(Double concealmentDistance) {
        this.concealmentDistance = concealmentDistance;
    }

    public Double getCoverDepth() {
        return coverDepth;
    }

    public void setCoverDepth(Double coverDepth) {
        this.coverDepth = coverDepth;
    }
}
