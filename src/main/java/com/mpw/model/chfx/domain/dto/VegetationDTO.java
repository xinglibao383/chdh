package com.mpw.model.chfx.domain.dto;

/**
 * 植被区域信息
 */
public class VegetationDTO {


    /**
     * 植被区域
     */
    private String vegetationName;
    /**
     *  植被高度
     */
    private Integer vegetationHeight;

    public String getVegetationName() {
        return vegetationName;
    }

    public void setVegetationName(String vegetationName) {
        this.vegetationName = vegetationName;
    }

    public Integer getVegetationHeight() {
        return vegetationHeight;
    }

    public void setVegetationHeight(Integer vegetationHeight) {
        this.vegetationHeight = vegetationHeight;
    }
}
