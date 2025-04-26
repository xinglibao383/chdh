package com.mpw.model.chfx.domain.dto;

import java.io.Serializable;

public class GreenDTOV2 implements Serializable {
    private Double startLng;
    private Double startLat;
    private Double endLng;
    private Double endLat;

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

    public GreenDTOV2(Double startLng, Double startLat, Double endLng, Double endLat) {
        this.startLng = startLng;
        this.startLat = startLat;
        this.endLng = endLng;
        this.endLat = endLat;
    }

    public GreenDTOV2() {
    }
}
