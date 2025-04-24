package com.mpw.model.chfx.domain.entity;

import java.io.Serializable;

/**
 * 地形实体类
 */
public class DemPoint implements Serializable {
    /**
     * 编号id
     */
    Long id;

    /**
     * 地质类型
     */
    String type;

    public DemPoint() {
    }

    public DemPoint(Long id, String type) {
        this.id = id;
        this.type = type;
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
}
