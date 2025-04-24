package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 道路机动分析VO
 */
public class MoveVO implements Serializable {
    /**
     * 水平桥梁数量
     */
    Integer bridgeHorizontal;
    /**
     * 垂直桥梁数量
     */
    Integer bridgeVertical;
    /**
     * 水平隧道数量
     */
    Integer tunnelHorizontal;
    /**
     * 垂直隧道数量
     */
    Integer tunnelVertical;
    /**
     * 水平道路数量
     */
    Integer roadHorizontal;
    /**
     * 垂直道路数量
     */
    Integer roadVertical;
    /**
     * 可通行道路数量
     */
    Integer throughRoad;
    /**
     * 水平道路总长度
     */
    Double roadHorizontalSumLength;
    /**
     * 垂直道路总长度
     */
    Double roadVerticalSumLength;

    public MoveVO() {
    }

    public MoveVO(Integer bridgeHorizontal, Integer bridgeVertical, Integer tunnelHorizontal, Integer tunnelVertical, Integer roadHorizontal, Integer roadVertical, Integer throughRoad, Double roadHorizontalSumLength, Double roadVerticalSumLength) {
        this.bridgeHorizontal = bridgeHorizontal;
        this.bridgeVertical = bridgeVertical;
        this.tunnelHorizontal = tunnelHorizontal;
        this.tunnelVertical = tunnelVertical;
        this.roadHorizontal = roadHorizontal;
        this.roadVertical = roadVertical;
        this.throughRoad = throughRoad;
        this.roadHorizontalSumLength = roadHorizontalSumLength;
        this.roadVerticalSumLength = roadVerticalSumLength;
    }

    public Integer getBridgeHorizontal() {
        return bridgeHorizontal;
    }

    public void setBridgeHorizontal(Integer bridgeHorizontal) {
        this.bridgeHorizontal = bridgeHorizontal;
    }

    public Integer getBridgeVertical() {
        return bridgeVertical;
    }

    public void setBridgeVertical(Integer bridgeVertical) {
        this.bridgeVertical = bridgeVertical;
    }

    public Integer getTunnelHorizontal() {
        return tunnelHorizontal;
    }

    public void setTunnelHorizontal(Integer tunnelHorizontal) {
        this.tunnelHorizontal = tunnelHorizontal;
    }

    public Integer getTunnelVertical() {
        return tunnelVertical;
    }

    public void setTunnelVertical(Integer tunnelVertical) {
        this.tunnelVertical = tunnelVertical;
    }

    public Integer getRoadHorizontal() {
        return roadHorizontal;
    }

    public void setRoadHorizontal(Integer roadHorizontal) {
        this.roadHorizontal = roadHorizontal;
    }

    public Integer getRoadVertical() {
        return roadVertical;
    }

    public void setRoadVertical(Integer roadVertical) {
        this.roadVertical = roadVertical;
    }

    public Integer getThroughRoad() {
        return throughRoad;
    }

    public void setThroughRoad(Integer throughRoad) {
        this.throughRoad = throughRoad;
    }

    public Double getRoadHorizontalSumLength() {
        return roadHorizontalSumLength;
    }

    public void setRoadHorizontalSumLength(Double roadHorizontalSumLength) {
        this.roadHorizontalSumLength = roadHorizontalSumLength;
    }

    public Double getRoadVerticalSumLength() {
        return roadVerticalSumLength;
    }

    public void setRoadVerticalSumLength(Double roadVerticalSumLength) {
        this.roadVerticalSumLength = roadVerticalSumLength;
    }
}
