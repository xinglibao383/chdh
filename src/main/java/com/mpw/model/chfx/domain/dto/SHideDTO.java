package com.mpw.model.chfx.domain.dto;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("隐蔽能力分析DTO")
public class SHideDTO implements Serializable {
    Double friendlyLng;
    Double friendlyLat;
    Double enemyLng;
    Double enemyLat;
    Double height;

    public SHideDTO() {
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getFriendlyLng() {
        return friendlyLng;
    }

    public void setFriendlyLng(Double friendlyLng) {
        this.friendlyLng = friendlyLng;
    }

    public Double getFriendlyLat() {
        return friendlyLat;
    }

    public void setFriendlyLat(Double friendlyLat) {
        this.friendlyLat = friendlyLat;
    }

    public Double getEnemyLng() {
        return enemyLng;
    }

    public void setEnemyLng(Double enemyLng) {
        this.enemyLng = enemyLng;
    }

    public Double getEnemyLat() {
        return enemyLat;
    }

    public void setEnemyLat(Double enemyLat) {
        this.enemyLat = enemyLat;
    }
}
