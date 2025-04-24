package com.mpw.model.chfx.domain.model;

import dm.jdbc.filter.stat.json.JSONObject;

/**
 * 爆炸目标类
 * 
 * 该类用于存储爆炸影响分析中每个目标点的详细信息，包括地理位置、高度、
 * 与爆炸中心的距离、冲击波超压、比冲量和动压值等。
 * 
 * 适用于在爆炸分析中评估每个目标点受到的影响程度。
 */
public class ExplosionTarget {
    private double latitude;           // 目标点的纬度（单位：度）
    private double longitude;          // 目标点的经度（单位：度）
    private double height;             // 目标点的高度（单位：米）
    private double distance;           // 爆炸中心到目标点的距离（单位：米）
    private double overpressure;       // 冲击波超压（单位：千帕）
    private double impulse;            // 比冲量（单位：帕秒）
    private double dynamicPressure;    // 动压值（单位：千帕）
    private String damageLevel;     // 毁伤等级（重度毁伤、中度毁伤、轻度毁伤）

    /**
     * 构造方法
     */
    public ExplosionTarget(double latitude, double longitude, double height, double distance,
                           double overpressure, double impulse, double dynamicPressure) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.distance = distance;
        this.overpressure = overpressure;
        this.impulse = impulse;
        this.dynamicPressure = dynamicPressure;
        this.damageLevel = "未分类"; // 默认值
    }


    // Getter 方法

    /**
     * 获取目标点的纬度
     * 
     * @return 目标点的纬度（单位：度）
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * 获取目标点的经度
     * 
     * @return 目标点的经度（单位：度）
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 获取目标点的高度
     * 
     * @return 目标点的高度（单位：米）
     */
    public double getHeight() {
        return height;
    }

    /**
     * 获取爆炸中心到目标点的距离
     * 
     * @return 爆炸中心到目标点的距离（单位：米）
     */
    public double getDistance() {
        return distance;
    }

    /**
     * 获取冲击波超压
     * 
     * @return 冲击波超压（单位：千帕）
     */
    public double getOverpressure() {
        return overpressure;
    }

    /**
     * 获取比冲量
     * 
     * @return 比冲量（单位：帕秒）
     */
    public double getImpulse() {
        return impulse;
    }

    /**
     * 获取动压值
     * 
     * @return 动压值（单位：千帕）
     */
    public double getDynamicPressure() {
        return dynamicPressure;
    }

    // Setter 方法（如果需要修改字段值，可以根据需要添加）

    /**
     * 设置目标点的纬度
     * 
     * @param latitude 目标点的纬度（单位：度）
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 设置目标点的经度
     * 
     * @param longitude 目标点的经度（单位：度）
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 设置目标点的高度
     * 
     * @param height 目标点的高度（单位：米）
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * 设置爆炸中心到目标点的距离
     * 
     * @param distance 爆炸中心到目标点的距离（单位：米）
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * 设置冲击波超压
     * 
     * @param overpressure 冲击波超压（单位：千帕）
     */
    public void setOverpressure(double overpressure) {
        this.overpressure = overpressure;
    }

    /**
     * 设置比冲量
     * 
     * @param impulse 比冲量（单位：帕秒）
     */
    public void setImpulse(double impulse) {
        this.impulse = impulse;
    }

    /**
     * 设置动压值
     * 
     * @param dynamicPressure 动压值（单位：千帕）
     */
    public void setDynamicPressure(double dynamicPressure) {
        this.dynamicPressure = dynamicPressure;
    }

    /**
     * 将 ExplosionTarget 对象转换为 JSON 格式
     * 
     * @return JSON 对象，包含目标点的所有属性
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("latitude", this.latitude);
        json.put("longitude", this.longitude);
        json.put("height", this.height);
        json.put("distance", this.distance);
        json.put("overpressure", this.overpressure);
        json.put("impulse", this.impulse);
        json.put("dynamicPressure", this.dynamicPressure);
        return json;
    }

    /**
     * 将 ExplosionTarget 对象转换为字符串表示
     * 
     * @return 包含所有属性的字符串描述
     */
    @Override
    public String toString() {
        return "ExplosionTarget{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", height=" + height +
                ", distance=" + distance +
                ", overpressure=" + overpressure +
                ", impulse=" + impulse +
                ", dynamicPressure=" + dynamicPressure +
                '}';
    }

    /**
     * 设置毁伤等级
     * @param damageLevel 毁伤等级（重度毁伤、中度毁伤、轻度毁伤）
     */
    public void setDamageLevel(String damageLevel) {
        this.damageLevel = damageLevel;
    }

    /**
     * 获取毁伤等级
     * @return 毁伤等级
     */
    public String getDamageLevel() {
        return damageLevel;
    }
}
