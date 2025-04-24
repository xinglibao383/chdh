package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 机动能力分析VO
 */
public class MotorAbilityVO implements Serializable {
    /**
     * 总面积
     */
    Double totalAcreage;
    /**
     * 坡度等级及面积占比
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
     * 建筑物面积
     */
    Double liveAcreage;

    /**
     * 建筑物面积占总面积的比例
     */
    Double liveRatio;

    /**
     * 道路平均载重
     */
    Double routeAvgLoadWeight;
    /**
     * 区域平均载重系数
     */
    Double areaAvgLoadWeight;
    /**
     * 区域平均梯度
     */
    Double areaAvgGradient;

    /**
     *最大坡度
     */
    Double maxGradient;
    /**
     *最大坡度位置经度
     */
    Double maxGradientLng;
    /**
     *最大坡度位置纬度
     */
    Double maxGradientLat;
    /**
     *最小坡度
     */
    Double minGradient;
    /**
     *最小坡度位置经度
     */
    Double minGradientLng;
    /**
     *最小坡度位置纬度
     */
    Double minGradientLat;
    /**
     *最大载重
     */
    Double maxLoadWeight;
    /**
     *最大载重位置经度
     */
    Double maxLoadWeightLng;
    /**
     *最大载重位置纬度
     */
    Double maxLoadWeightLat;
    /**
     *最小载重
     */
    Double minLoadWeight;
    /**
     *最小载重位置经度
     */
    Double minLoadWeightLng;
    /**
     *最小载重位置纬度
     */
    Double minLoadWeightLat;

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

    /**
     * 该区域平均水深
     */
    Double waterAverageDepth;

    /**
     * 该区域水域面积
     */
    Double waterAcreage;
    /**
     * 该区域水域面积占总面积的比例
     */
    Double ratio;

    /**
     * 可通行性与速度分析
     */
    List<PassableVO> passableVOList;


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

    public Double getRouteAvgLoadWeight() {
        return routeAvgLoadWeight;
    }

    public void setRouteAvgLoadWeight(Double routeAvgLoadWeight) {
        this.routeAvgLoadWeight = routeAvgLoadWeight;
    }

    public Double getAreaAvgLoadWeight() {
        return areaAvgLoadWeight;
    }

    public void setAreaAvgLoadWeight(Double areaAvgLoadWeight) {
        this.areaAvgLoadWeight = areaAvgLoadWeight;
    }

    public Double getAreaAvgGradient() {
        return areaAvgGradient;
    }

    public void setAreaAvgGradient(Double areaAvgGradient) {
        this.areaAvgGradient = areaAvgGradient;
    }

    public Double getMaxGradient() {
        return maxGradient;
    }

    public void setMaxGradient(Double maxGradient) {
        this.maxGradient = maxGradient;
    }

    public Double getMaxGradientLng() {
        return maxGradientLng;
    }

    public void setMaxGradientLng(Double maxGradientLng) {
        this.maxGradientLng = maxGradientLng;
    }

    public Double getMaxGradientLat() {
        return maxGradientLat;
    }

    public void setMaxGradientLat(Double maxGradientLat) {
        this.maxGradientLat = maxGradientLat;
    }

    public Double getMinGradient() {
        return minGradient;
    }

    public void setMinGradient(Double minGradient) {
        this.minGradient = minGradient;
    }

    public Double getMinGradientLng() {
        return minGradientLng;
    }

    public void setMinGradientLng(Double minGradientLng) {
        this.minGradientLng = minGradientLng;
    }

    public Double getMinGradientLat() {
        return minGradientLat;
    }

    public void setMinGradientLat(Double minGradientLat) {
        this.minGradientLat = minGradientLat;
    }

    public Double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(Double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
    }

    public Double getMaxLoadWeightLng() {
        return maxLoadWeightLng;
    }

    public void setMaxLoadWeightLng(Double maxLoadWeightLng) {
        this.maxLoadWeightLng = maxLoadWeightLng;
    }

    public Double getMaxLoadWeightLat() {
        return maxLoadWeightLat;
    }

    public void setMaxLoadWeightLat(Double maxLoadWeightLat) {
        this.maxLoadWeightLat = maxLoadWeightLat;
    }

    public Double getMinLoadWeight() {
        return minLoadWeight;
    }

    public void setMinLoadWeight(Double minLoadWeight) {
        this.minLoadWeight = minLoadWeight;
    }

    public Double getMinLoadWeightLng() {
        return minLoadWeightLng;
    }

    public void setMinLoadWeightLng(Double minLoadWeightLng) {
        this.minLoadWeightLng = minLoadWeightLng;
    }

    public Double getMinLoadWeightLat() {
        return minLoadWeightLat;
    }

    public void setMinLoadWeightLat(Double minLoadWeightLat) {
        this.minLoadWeightLat = minLoadWeightLat;
    }

    public Integer getBridgeVertical() {
        return bridgeVertical;
    }

    public void setBridgeVertical(Integer bridgeVertical) {
        this.bridgeVertical = bridgeVertical;
    }

    public Integer getBridgeHorizontal() {
        return bridgeHorizontal;
    }

    public void setBridgeHorizontal(Integer bridgeHorizontal) {
        this.bridgeHorizontal = bridgeHorizontal;
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

    public Double getWaterAverageDepth() {
        return waterAverageDepth;
    }

    public void setWaterAverageDepth(Double waterAverageDepth) {
        this.waterAverageDepth = waterAverageDepth;
    }

    public Double getWaterAcreage() {
        return waterAcreage;
    }

    public void setWaterAcreage(Double waterAcreage) {
        this.waterAcreage = waterAcreage;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public List<PassableVO> getPassableVOList() {
        return passableVOList;
    }

    public void setPassableVOList(List<PassableVO> passableVOList) {
        this.passableVOList = passableVOList;
    }
}
