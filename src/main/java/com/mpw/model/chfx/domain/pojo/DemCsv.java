package com.mpw.model.chfx.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.vividsolutions.jts.geom.Geometry;

import java.io.Serializable;
import java.sql.Struct;

/**
 * 地图区域网格实体类
 */
public class DemCsv implements Serializable {

    /**
     * 编号id
     */
    @TableField("ID")
    Integer id;

    /**
     * 海拔高度
     */
    @TableField("VALUE")
    Integer value;

    /**
     * 位于地图上的平面坐标X
     */
    @TableField("XNUM")
    Integer xNum;

    /**
     * 位于地图上的平面坐标Y
     */
    @TableField("YNUM")
    Integer yNum;

    /**
     * 是否为水域
     */
    @TableField("ISWATER")
    Integer isWater;

    /**
     * 地质类型
     */
    @TableField("TYPE")
    String type;

    /**
     * 区域对应的Geometry对象字符串
     */
    @TableField("GEOMETRYSTR")
    String geometryStr;

    @TableField("GEOM")
    Struct geom;

    @TableField("GEOMNEW")
    Struct geomNew;

    /**
     * 梯度
     */
    @TableField("GRADIENT")
    Double gradientFT;

    /**
     * 水深深度
     */
    @TableField("DEPTH_WATER")
    Double depthWater;

    /**
     * 区域对应的Geometry对象
     */
    Geometry geometry;

    /**
     * 区域中心点的纬度
     */
    Double centerLat;

    /**
     * 区域中心点的经度
     */
    Double centerLng;

    /**
     * 相对目标点的距离
     */
    Double distance;

    /**
     * 相对目标点的梯度
     */
    Double gradient;

    /**
     * 相对目标点的方向
     */
    Double direction;

    /**
     * 是否通视
     */
    Integer canSee = 0;

    /**
     * 是否可以隐蔽
     */
    Integer canHide = 0;

    /**
     * 隐蔽成都等级数值
     */
    Integer maskDegree;

    /**
     * 隐蔽程度等级名称
     */
    String maskDegreeName;

    public DemCsv() {
    }

    public DemCsv(Integer id, Integer value, Integer xNum, Integer yNum, Integer isWater, String type, String geometryStr, Struct geom, Struct geomNew, Double gradientFT, Double depthWater) {
        this.id = id;
        this.value = value;
        this.xNum = xNum;
        this.yNum = yNum;
        this.isWater = isWater;
        this.type = type;
        this.geometryStr = geometryStr;
        this.geom = geom;
        this.geomNew = geomNew;
        this.gradientFT = gradientFT;
        this.depthWater = depthWater;
    }

    public DemCsv(Integer id, Integer value, Integer xNum, Integer yNum, Integer isWater, String type, String geometryStr, Struct geom, Struct geomNew, Double gradientFT, Double depthWater, Geometry geometry, Double centerLat, Double centerLng, Double distance, Double gradient, Double direction, Integer canSee, Integer canHide, Integer maskDegree, String maskDegreeName) {
        this.id = id;
        this.value = value;
        this.xNum = xNum;
        this.yNum = yNum;
        this.isWater = isWater;
        this.type = type;
        this.geometryStr = geometryStr;
        this.geom = geom;
        this.geomNew = geomNew;
        this.gradientFT = gradientFT;
        this.depthWater = depthWater;
        this.geometry = geometry;
        this.centerLat = centerLat;
        this.centerLng = centerLng;
        this.distance = distance;
        this.gradient = gradient;
        this.direction = direction;
        this.canSee = canSee;
        this.canHide = canHide;
        this.maskDegree = maskDegree;
        this.maskDegreeName = maskDegreeName;
    }

    public String getMaskDegreeName() {
        return maskDegreeName;
    }

    public void setMaskDegreeName(String maskDegreeName) {
        this.maskDegreeName = maskDegreeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getxNum() {
        return xNum;
    }

    public void setxNum(Integer xNum) {
        this.xNum = xNum;
    }

    public Integer getyNum() {
        return yNum;
    }

    public void setyNum(Integer yNum) {
        this.yNum = yNum;
    }

    public Integer getIsWater() {
        return isWater;
    }

    public void setIsWater(Integer isWater) {
        this.isWater = isWater;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGeometryStr() {
        return geometryStr;
    }

    public void setGeometryStr(String geometryStr) {
        this.geometryStr = geometryStr;
    }

    public Struct getGeom() {
        return geom;
    }

    public void setGeom(Struct geom) {
        this.geom = geom;
    }

    public Struct getGeomNew() {
        return geomNew;
    }

    public void setGeomNew(Struct geomNew) {
        this.geomNew = geomNew;
    }

    public Double getGradientFT() {
        return gradientFT;
    }

    public void setGradientFT(Double gradientFT) {
        this.gradientFT = gradientFT;
    }

    public Double getDepthWater() {
        return depthWater;
    }

    public void setDepthWater(Double depthWater) {
        this.depthWater = depthWater;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Double getCenterLng() {
        return centerLng;
    }

    public void setCenterLng(Double centerLng) {
        this.centerLng = centerLng;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getGradient() {
        return gradient;
    }

    public void setGradient(Double gradient) {
        this.gradient = gradient;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    public Integer getCanSee() {
        return canSee;
    }

    public void setCanSee(Integer canSee) {
        this.canSee = canSee;
    }

    public Integer getCanHide() {
        return canHide;
    }

    public void setCanHide(Integer canHide) {
        this.canHide = canHide;
    }

    public Integer getMaskDegree() {
        return maskDegree;
    }

    public void setMaskDegree(Integer maskDegree) {
        this.maskDegree = maskDegree;
    }
}
