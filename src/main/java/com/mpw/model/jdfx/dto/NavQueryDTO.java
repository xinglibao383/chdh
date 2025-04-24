package com.mpw.model.jdfx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("路径规划请求对象")
public class NavQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("起点")
    private PointDTO start;
    @ApiModelProperty("终点")
    private PointDTO end;

    @ApiModelProperty("算法类型 aStar dijkstra")
    private String type;

    @ApiModelProperty("装备id")
    private Long equipId;


    public PointDTO getStart() {
        return start;
    }

    public void setStart(PointDTO start) {
        this.start = start;
    }

    public PointDTO getEnd() {
        return end;
    }

    public void setEnd(PointDTO end) {
        this.end = end;
    }

    public NavQueryDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getEquipId() {
        return equipId;
    }

    public void setEquipId(Long equipId) {
        this.equipId = equipId;
    }
}
