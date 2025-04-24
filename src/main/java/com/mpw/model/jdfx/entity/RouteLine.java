package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@TableName("ROUTE.ROUTE_POINT")
public class RouteLine {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("FCLASS")
    private String fClass;

    @TableField("NAME")
    private String name;

    @TableField("MAX_SPEED")
    private Integer maxSpeed;

    @TableField("BRIDGE")
    private String bridge;

    @TableField("TUNNEL")
    private String tunnel;

    @TableField("SNODE")
    private Integer sNode;

    @TableField("ENODE")
    private Integer eNode;

    @TableField("LENGTH")
    private Double length;

    @TableField("GEOMETRY")
    private String geometry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfClass() {
        return fClass;
    }

    public void setfClass(String fClass) {
        this.fClass = fClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getTunnel() {
        return tunnel;
    }

    public void setTunnel(String tunnel) {
        this.tunnel = tunnel;
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

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public RouteLine() {
    }
}
