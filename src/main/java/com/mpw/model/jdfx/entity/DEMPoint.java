package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("ROUTE.DEM_POINT")
public class DEMPoint {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("DEGREE")
    private Double degree;

    @TableField("SX")
    private Double sx;

    @TableField("SY")
    private Double sy;

    @TableField("EX")
    private Double ex;

    @TableField("EY")
    private Double ey;

    @TableField("IS_WATER")
    private Integer isWater;

    @TableField("SNODE")
    private Integer sNode;

    @TableField("ENODE")
    private Integer eNode;

    @TableField("TYPE")
    private String type;

    @TableField("LENGTH")
    private Double length;

    public DEMPoint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDegree() {
        return degree;
    }

    public void setDegree(Double degree) {
        this.degree = degree;
    }

    public Double getSx() {
        return sx;
    }

    public void setSx(Double sx) {
        this.sx = sx;
    }

    public Double getSy() {
        return sy;
    }

    public void setSy(Double sy) {
        this.sy = sy;
    }

    public Double getEx() {
        return ex;
    }

    public void setEx(Double ex) {
        this.ex = ex;
    }

    public Double getEy() {
        return ey;
    }

    public void setEy(Double ey) {
        this.ey = ey;
    }

    public Integer getIsWater() {
        return isWater;
    }

    public void setIsWater(Integer isWater) {
        this.isWater = isWater;
    }

    public Integer getsNode() {
        return sNode;
    }

    public void setsNode(Integer sNode) {
        this.sNode = sNode;
    }

    public Integer geteNode() {
        return eNode;
    }

    public void seteNode(Integer eNode) {
        this.eNode = eNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
