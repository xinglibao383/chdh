package com.mpw.model.chfx.domain.model;
/**
 * 功能：描述射击目标的类。
 * 包含目标的类型（如建筑物、装备、人员等）、地理位置（经纬度）、高度等属性。
 * 用于路径分析、射击影响分析或爆炸破坏分析中的目标参数。
 */
public class Target {
    private TargetType type; // 目标类型（如建筑物、装备、人员等）
    private double latitude; // 目标的纬度（单位：度）
    private double longitude; // 目标的经度（单位：度）
    private double height; // 目标的高度（单位：米）

    /**
     * 枚举类型：目标类型
     * HUMAN: 人类目标
     * VEHICLE: 车辆目标
     * BUILDING: 建筑目标
     */
    public enum TargetType {
        HUMAN,     // 人类
        VEHICLE,   // 车辆
        BUILDING   // 建筑
    }

    /**
     * 构造方法，用于初始化目标信息
     * 
     * @param type 目标类型（枚举类型，如 HUMAN、VEHICLE、BUILDING）
     * @param latitude 目标的纬度（单位：度）
     * @param longitude 目标的经度（单位：度）
     * @param height 目标的高度（单位：米）
     */
    public Target(TargetType type, double latitude, double longitude, double height) {
        this.type = type; // 初始化目标类型
        this.latitude = latitude; // 初始化目标纬度
        this.longitude = longitude; // 初始化目标经度
        this.height = height; // 初始化目标高度
    }

    /**
     * 获取目标的类型
     * 
     * @return 目标类型（枚举类型，如 HUMAN、VEHICLE、BUILDING）
     */
    public TargetType getType() {
        return type;
    }

    /**
     * 设置目标的类型
     * 
     * @param type 目标类型（枚举类型，如 HUMAN、VEHICLE、BUILDING）
     */
    public void setType(TargetType type) {
        this.type = type;
    }

    /**
     * 获取目标的纬度
     * 
     * @return 目标的纬度（单位：度）
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * 设置目标的纬度
     * 
     * @param latitude 目标的纬度（单位：度）
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取目标的经度
     * 
     * @return 目标的经度（单位：度）
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 设置目标的经度
     * 
     * @param longitude 目标的经度（单位：度）
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取目标的高度
     * 
     * @return 目标的高度（单位：米）
     */
    public double getHeight() {
        return height;
    }

    /**
     * 设置目标的高度
     * 
     * @param height 目标的高度（单位：米）
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * 判断目标点是否有效
     * 目标点经纬度需在合法范围内：
     * - 纬度范围：[-90, 90]
     * - 经度范围：[-180, 180]
     * 
     * @return true 如果目标点有效，否则返回 false
     */
    public boolean isValid() {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }

    /**
     * 根据目标类型获取其重要属性的权重
     * 例如：
     * - 人类目标通常更容易受打击影响
     * - 建筑目标更难摧毁
     * 
     * @return 目标类型的权重（如 HUMAN 返回 1.5，VEHICLE 返回 1.2，BUILDING 返回 0.8）
     */
    public double getImpactWeight() {
        switch (this.type) {
            case HUMAN:
                return 1.5; // 人类目标权重
            case VEHICLE:
                return 1.2; // 车辆目标权重
            case BUILDING:
                return 0.8; // 建筑目标权重
            default:
                return 1.0; // 默认权重
        }
    }

    /**
     * 将目标的基本信息转换为字符串，便于调试和输出
     * 
     * @return 目标的字符串描述
     */
    @Override
    public String toString() {
        return "Target{" +
               "type=" + type +
               ", latitude=" + latitude +
               ", longitude=" + longitude +
               ", height=" + height +
               '}';
    }
}
