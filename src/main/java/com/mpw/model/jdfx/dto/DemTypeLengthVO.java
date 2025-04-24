package com.mpw.model.jdfx.dto;

public class DemTypeLengthVO {

    private String demType;

    private Double length;

    public DemTypeLengthVO(String demType, Double length) {
        this.demType = demType;
        this.length = length;
    }

    public String getDemType() {
        return demType;
    }

    public void setDemType(String demType) {
        this.demType = demType;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public DemTypeLengthVO() {
    }
}
