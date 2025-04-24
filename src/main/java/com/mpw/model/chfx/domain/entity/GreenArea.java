package com.mpw.model.chfx.domain.entity;

public class GreenArea {
    private Integer id;
    private String area;
    private Double height;

    public GreenArea() {
    }

    public GreenArea(Integer id, String area) {
        this.id = id;
        this.area = area;
    }

    public GreenArea(Integer id, String area, Double height) {
        this.id = id;
        this.area = area;
        this.height = height;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
