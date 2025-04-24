package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 伪装程度VO
 */
public class MaskDegreeAreaVO implements Serializable {
    /**
     * 伪装程度等级
     */
    private String degree;
    /**
     * 区域面积
     */
    private Double area;
    /**
     * 该伪装程度等级对应的地形
     */
    private List<String> typeList;

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public MaskDegreeAreaVO() {
    }

    public MaskDegreeAreaVO(String degree, Double area) {
        this.degree = degree;
        this.area = area;
    }

    public MaskDegreeAreaVO(String degree, Double area, List<String> typeList) {
        this.degree = degree;
        this.area = area;
        this.typeList = typeList;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}
