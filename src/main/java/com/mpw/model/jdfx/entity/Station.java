package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("ROUTE.JD_STATION")
public class Station {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("NAME")
    private String name;

    @TableField("TYPE")
    private String type;

    @TableField("LAT")
    private Double lat;

    @TableField("LNG")
    private Double lng;

    @TableField("GEOM")
    private String geom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Station() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", geom='" + geom + '\'' +
                '}';
    }
}
