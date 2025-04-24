package com.mpw.model.chfx.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.sql.Struct;

public class DemCsvXX implements Serializable {

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
     * 位于地图平面的X坐标
     */
    @TableField("XNUM")
    Integer xNum;

    /**
     * 位于地图平面的Y坐标
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
     * 区域对应的geometry对象的字符串形式
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
    Double gradient;

    /**
     * 水深
     */
    @TableField("DEPTH_WATER")
    Double depthWater;

    public DemCsvXX() {
    }

    public DemCsvXX(Integer id, Integer value, Integer xNum, Integer yNum, Integer isWater, String type, String geometryStr, Struct geom, Struct geomNew, Double gradient, Double depthWater) {
        this.id = id;
        this.value = value;
        this.xNum = xNum;
        this.yNum = yNum;
        this.isWater = isWater;
        this.type = type;
        this.geometryStr = geometryStr;
        this.geom = geom;
        this.geomNew = geomNew;
        this.gradient = gradient;
        this.depthWater = depthWater;
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

    public Double getGradient() {
        return gradient;
    }

    public void setGradient(Double gradient) {
        this.gradient = gradient;
    }

    public Double getDepthWater() {
        return depthWater;
    }

    public void setDepthWater(Double depthWater) {
        this.depthWater = depthWater;
    }
}
