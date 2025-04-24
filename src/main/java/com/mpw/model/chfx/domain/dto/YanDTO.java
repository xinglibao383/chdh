package com.mpw.model.chfx.domain.dto;

public class YanDTO {

    //深度
    private String penntrationDepth;
    //塌陷范围
    private String maxDamageRadius;
    //岩石破片破坏范围
    private String fragametRange;
    //地面破坏等级 . 毁伤等级
    private String groundDamage;
    //建筑破换类型 。毁伤等级
    private String buildingDamage;

    private String groundDamageWkt;

    private String buildingDamageWkt;

    public String getPenntrationDepth() {
        return penntrationDepth;
    }

    public void setPenntrationDepth(String penntrationDepth) {
        this.penntrationDepth = penntrationDepth;
    }

    public String getMaxDamageRadius() {
        return maxDamageRadius;
    }

    public void setMaxDamageRadius(String maxDamageRadius) {
        this.maxDamageRadius = maxDamageRadius;
    }

    public String getFragametRange() {
        return fragametRange;
    }

    public void setFragametRange(String fragametRange) {
        this.fragametRange = fragametRange;
    }

    public String getGroundDamage() {
        return groundDamage;
    }

    public void setGroundDamage(String groundDamage) {
        this.groundDamage = groundDamage;
    }

    public String getBuildingDamage() {
        return buildingDamage;
    }

    public void setBuildingDamage(String buildingDamage) {
        this.buildingDamage = buildingDamage;
    }

    public String getGroundDamageWkt() {
        return groundDamageWkt;
    }

    public void setGroundDamageWkt(String groundDamageWkt) {
        this.groundDamageWkt = groundDamageWkt;
    }

    public String getBuildingDamageWkt() {
        return buildingDamageWkt;
    }

    public void setBuildingDamageWkt(String buildingDamageWkt) {
        this.buildingDamageWkt = buildingDamageWkt;
    }
}
