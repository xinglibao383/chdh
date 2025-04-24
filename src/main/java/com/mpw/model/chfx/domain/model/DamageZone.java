package com.mpw.model.chfx.domain.model;
/**
 * 表示弹药的杀伤区（Damage Zone）
 */
public class DamageZone {
    private double radius;      // 杀伤半径
    private double damageLevel; // 杀伤强度（百分比）
    private String geometry;    // BEM 格式的 geometry

    public DamageZone(double radius, double damageLevel, String geometry) {
        this.radius = radius;
        this.damageLevel = damageLevel;
        this.geometry = geometry;
    }

    public double getRadius() {
        return radius;
    }

    public double getDamageLevel() {
        return damageLevel;
    }

    public String getGeometry() {
        return geometry;
    }
}
