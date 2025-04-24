package com.mpw.model.chfx.domain.dto;

import java.io.Serializable;

public class XDeadAreaDTO implements Serializable {
    private String friendlyArea;
    private String enemyArea;
    private Double friendlyPitchAngle;

    private Long friendlyWeaponId;
    private Long enemyWeaponId;
    private Double enemyPitchAngle;

    public Double getFriendlyPitchAngle() {
        return friendlyPitchAngle;
    }

    public void setFriendlyPitchAngle(Double friendlyPitchAngle) {
        this.friendlyPitchAngle = friendlyPitchAngle;
    }

    public Double getEnemyPitchAngle() {
        return enemyPitchAngle;
    }

    public void setEnemyPitchAngle(Double enemyPitchAngle) {
        this.enemyPitchAngle = enemyPitchAngle;
    }

    public XDeadAreaDTO(String friendlyArea, String enemyArea, Double friendlyPitchAngle, Long friendlyWeaponId, Long enemyWeaponId, Double enemyPitchAngle) {
        this.friendlyArea = friendlyArea;
        this.enemyArea = enemyArea;
        this.friendlyPitchAngle = friendlyPitchAngle;
        this.friendlyWeaponId = friendlyWeaponId;
        this.enemyWeaponId = enemyWeaponId;
        this.enemyPitchAngle = enemyPitchAngle;
    }

    public XDeadAreaDTO() {
    }

    public XDeadAreaDTO(String friendlyArea, String enemyArea, Long friendlyWeaponId, Long enemyWeaponId) {
        this.friendlyArea = friendlyArea;
        this.enemyArea = enemyArea;
        this.friendlyWeaponId = friendlyWeaponId;
        this.enemyWeaponId = enemyWeaponId;
    }

    public String getFriendlyArea() {
        return friendlyArea;
    }

    public void setFriendlyArea(String friendlyArea) {
        this.friendlyArea = friendlyArea;
    }

    public String getEnemyArea() {
        return enemyArea;
    }

    public void setEnemyArea(String enemyArea) {
        this.enemyArea = enemyArea;
    }

    public Long getFriendlyWeaponId() {
        return friendlyWeaponId;
    }

    public void setFriendlyWeaponId(Long friendlyWeaponId) {
        this.friendlyWeaponId = friendlyWeaponId;
    }

    public Long getEnemyWeaponId() {
        return enemyWeaponId;
    }

    public void setEnemyWeaponId(Long enemyWeaponId) {
        this.enemyWeaponId = enemyWeaponId;
    }
}
