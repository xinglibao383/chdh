package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("ROUTE.DEM_CSV")
public class DEMCsv {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("VALUE")
    private Integer value;

    @TableField("XNUM")
    private Integer xNum;

    @TableField("YNUM")
    private Integer yNum;

    @TableField("ISWATER")
    private Integer isWater;

    @TableField("TYPE")
    private String type;

    @TableField("GEOMETRYSTR")
    private String geometryStr;

    @TableField("GEOM")
    private String geom;

    @TableField("GRADIENT")
    private Double gradient;

    @TableField("DEPTH_WATER")
    private Double depthWater;

    public Double getDepthWater() {
        return depthWater;
    }

    public void setDepthWater(Double depthWater) {
        this.depthWater = depthWater;
    }

    public Double getGradient() {
        return gradient;
    }

    public void setGradient(Double gradient) {
        this.gradient = gradient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }


    public DEMCsv() {
    }

    @Override
    public String toString() {
        return "DEMCsv{" +
                "id=" + id +
                ", value=" + value +
                ", xNum=" + xNum +
                ", yNum=" + yNum +
                ", isWater=" + isWater +
                ", type='" + type + '\'' +
                ", geometryStr='" + geometryStr + '\'' +
                ", geom='" + geom + '\'' +
                ", gradient=" + gradient +
                '}';
    }
}
