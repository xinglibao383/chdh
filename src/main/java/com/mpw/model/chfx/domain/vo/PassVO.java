package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

/**
 * 可通行性分析VO
 */
public class PassVO implements Serializable {
    /**
     * 总面积
     */
    Double totalAcreage;
    /**
     * 可以通行的面积
     */
    Double canPassAcreage;
    /**
     * 可以通行的面积占总面积的比例
     */
    Double ratio;

    public PassVO() {
    }

    public PassVO(Double totalAcreage, Double canPassAcreage, Double ratio) {
        this.totalAcreage = totalAcreage;
        this.canPassAcreage = canPassAcreage;
        this.ratio = ratio;
    }

    public Double getTotalAcreage() {
        return totalAcreage;
    }

    public void setTotalAcreage(Double totalAcreage) {
        this.totalAcreage = totalAcreage;
    }

    public Double getCanPassAcreage() {
        return canPassAcreage;
    }

    public void setCanPassAcreage(Double canPassAcreage) {
        this.canPassAcreage = canPassAcreage;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }
}
