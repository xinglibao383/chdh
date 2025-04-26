package com.mpw.model.chfx.domain.dto;

import java.io.Serializable;

public class GreenDTO implements Serializable {
    private String area;

    public GreenDTO() {
    }

    public GreenDTO(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
