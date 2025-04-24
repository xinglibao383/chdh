package com.mpw.model.chfx.domain.vo;

/**
 * 范围VO
 */
public class RangeVO {
    /**
     * 角度
     */
    private Double range;
    /**
     * 起始位置经度
     */
    private Double startLng;
    /**
     * 起始位置纬度
     */
    private Double startLat;
    /**
     * 终点位置经度
     */
    private Double endLng;
    /**
     * 重点位置纬度
     */
    private Double endLat;
    /**
     * 是否可以通视
     */
    private Integer canSee;
    /**
     * 是否可以隐蔽
     */
    private Integer canHide;

    public RangeVO() {
    }

    public RangeVO(Double range, Double startLng, Double startLat, Double endLng, Double endLat, Integer canSee, Integer canHide) {
        this.range = range;
        this.startLng = startLng;
        this.startLat = startLat;
        this.endLng = endLng;
        this.endLat = endLat;
        this.canSee = canSee;
        this.canHide = canHide;
    }

    public RangeVO(Double range, Double startLng, Double startLat, Integer canSee, Integer canHide) {
        this.range = range;
        this.startLng = startLng;
        this.startLat = startLat;
        this.canSee = canSee;
        this.canHide = canHide;
    }

    public Double getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public Double getStartLng() {
        return startLng;
    }

    public void setStartLng(Double startLng) {
        this.startLng = startLng;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getEndLng() {
        return endLng;
    }

    public void setEndLng(Double endLng) {
        this.endLng = endLng;
    }

    public Double getEndLat() {
        return endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public Integer getCanSee() {
        return canSee;
    }

    public void setCanSee(Integer canSee) {
        this.canSee = canSee;
    }

    public Integer getCanHide() {
        return canHide;
    }

    public void setCanHide(Integer canHide) {
        this.canHide = canHide;
    }
}