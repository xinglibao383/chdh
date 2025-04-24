package com.mpw.model.chfx.domain.model;
/**
 * 植被类
 * 描述射击路径上的植被信息，包括植被类型（如森林、草地等）、覆盖度（百分比）和高度（米）。
 * 用于评估植被对射击路径的遮挡影响。
 */
public class Vegetation {
    private String type; // 植被类型（如森林、草地等）
    private double coverage; // 植被覆盖度（单位：百分比，范围：0-100）
    private double height; // 植被高度（单位：米）

    /**
     * 构造方法
     * 用于初始化植被信息
     *
     * @param type 植被类型（如森林、草地等）
     * @param coverage 植被覆盖度（单位：百分比，范围：0-100）
     * @param height 植被高度（单位：米）
     */
    public Vegetation(String type, double coverage, double height) {
        this.type = type; // 初始化植被类型
        this.coverage = coverage; // 初始化植被覆盖度
        this.height = height; // 初始化植被高度
    }

    /**
     * 获取植被的类型
     * @return 植被类型（如森林、草地等）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置植被的类型
     * @param type 植被类型（如森林、草地等）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取植被覆盖度
     * @return 植被覆盖度（单位：百分比，范围：0-100）
     */
    public double getCoverage() {
        return coverage;
    }

    /**
     * 设置植被覆盖度
     * @param coverage 植被覆盖度（单位：百分比，范围：0-100）
     */
    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    /**
     * 获取植被高度
     * @return 植被高度（单位：米）
     */
    public double getHeight() {
        return height;
    }

    /**
     * 设置植被高度
     * @param height 植被高度（单位：米）
     */
    public void setHeight(double height) {
        this.height = height;
    }
    
  

}
