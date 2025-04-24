package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;

public class EffVO implements Serializable {
    Double highArea;
    String highAreaRange;
    Double mediumArea;
    String mediumAreaRange;
    Double lowArea;
    String lowAreaRange;

    public EffVO() {
    }

    public EffVO(Double highArea, Double mediumArea, Double lowArea) {
        this.highArea = highArea;
        this.mediumArea = mediumArea;
        this.lowArea = lowArea;
    }

    public String getHighAreaRange() {
        return highAreaRange;
    }

    public void setHighAreaRange(String highAreaRange) {
        this.highAreaRange = highAreaRange;
    }

    public String getMediumAreaRange() {
        return mediumAreaRange;
    }

    public void setMediumAreaRange(String mediumAreaRange) {
        this.mediumAreaRange = mediumAreaRange;
    }

    public String getLowAreaRange() {
        return lowAreaRange;
    }

    public void setLowAreaRange(String lowAreaRange) {
        this.lowAreaRange = lowAreaRange;
    }

    public EffVO(Double highArea, String highAreaRange, Double mediumArea, String mediumAreaRange, Double lowArea, String lowAreaRange) {
        this.highArea = highArea;
        this.highAreaRange = highAreaRange;
        this.mediumArea = mediumArea;
        this.mediumAreaRange = mediumAreaRange;
        this.lowArea = lowArea;
        this.lowAreaRange = lowAreaRange;
    }

    public Double getHighArea() {
        return highArea;
    }

    public void setHighArea(Double highArea) {
        this.highArea = highArea;
    }

    public Double getMediumArea() {
        return mediumArea;
    }

    public void setMediumArea(Double mediumArea) {
        this.mediumArea = mediumArea;
    }

    public Double getLowArea() {
        return lowArea;
    }

    public void setLowArea(Double lowArea) {
        this.lowArea = lowArea;
    }
}
