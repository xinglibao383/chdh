package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 可通行能力VO
 */
public class PassAbilityVO implements Serializable {
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

    public PassAbilityVO() {
    }

    public PassAbilityVO(Double routeAvgLoadWeight, Double areaAvgLoadWeight, Double areaAvgGradient) {
        this.routeAvgLoadWeight = routeAvgLoadWeight;
        this.areaAvgLoadWeight = areaAvgLoadWeight;
        this.areaAvgGradient = areaAvgGradient;
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

    Double maxGradient;
    Double maxGradientLng;
    Double maxGradientLat;
    Double minGradient;
    Double minGradientLng;
    Double minGradientLat;
    Double maxLoadWeight;
    Double maxLoadWeightLng;
    Double maxLoadWeightLat;
    Double minLoadWeight;
    Double minLoadWeightLng;
    Double minLoadWeightLat;

    public PassAbilityVO(Double routeAvgLoadWeight, Double areaAvgLoadWeight, Double areaAvgGradient, Double maxGradient, Double maxGradientLng, Double maxGradientLat, Double minGradient, Double minGradientLng, Double minGradientLat, Double maxLoadWeight, Double maxLoadWeightLng, Double maxLoadWeightLat, Double minLoadWeight, Double minLoadWeightLng, Double minLoadWeightLat) {
        this.routeAvgLoadWeight = routeAvgLoadWeight;
        this.areaAvgLoadWeight = areaAvgLoadWeight;
        this.areaAvgGradient = areaAvgGradient;
        this.maxGradient = maxGradient;
        this.maxGradientLng = maxGradientLng;
        this.maxGradientLat = maxGradientLat;
        this.minGradient = minGradient;
        this.minGradientLng = minGradientLng;
        this.minGradientLat = minGradientLat;
        this.maxLoadWeight = maxLoadWeight;
        this.maxLoadWeightLng = maxLoadWeightLng;
        this.maxLoadWeightLat = maxLoadWeightLat;
        this.minLoadWeight = minLoadWeight;
        this.minLoadWeightLng = minLoadWeightLng;
        this.minLoadWeightLat = minLoadWeightLat;
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
}
