package com.mpw.model.chfx.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

/**
 * 交通道路表
 */
public class ChRoutePoint implements Serializable {
    /**
     * 编号id
     */
    @TableField("ID")
    private Long id;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 最大速度
     */
    @TableField("MAX_SPEED")
    private Integer maxSpeed;

    /**
     * 是否是桥
     */
    @TableField("BRIDGE")
    private String bridge;

    /**
     * 是否是隧道
     */
    @TableField("TUNNEL")
    private String tunnel;

    @TableField("SNODE")
    private Integer sNode;

    @TableField("ENODE")
    private Integer eNode;

    /**
     * 长度
     */
    @TableField("LENGTH")
    private Double length;

    @TableField("FCLASS")
    private String fClass;

    /**
     * 字符串形式的GEOMETRY
     */
    @TableField("GEOMETRY")
    private String geometry;

    @TableField("GEOM")
    private String geom;

    public ChRoutePoint() {
    }

    public ChRoutePoint(Long id, String name, Integer maxSpeed, String bridge, String tunnel, Integer sNode, Integer eNode, Double length, String fClass, String geometry, String geom) {
        this.id = id;
        this.name = name;
        this.maxSpeed = maxSpeed;
        this.bridge = bridge;
        this.tunnel = tunnel;
        this.sNode = sNode;
        this.eNode = eNode;
        this.length = length;
        this.fClass = fClass;
        this.geometry = geometry;
        this.geom = geom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getfClass() {
        return fClass;
    }

    public void setfClass(String fClass) {
        this.fClass = fClass;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}
