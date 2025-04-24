package com.mpw.model.chfx.domain.model;

/**
 * 气象类
 * 描述射击时的气象条件，包括风速、风向、温度、湿度和可见度等参数。
 * 用于分析气象对射击路径和命中率的影响。
 */
public class Weather {
    private double windSpeed;         // 风速（单位：米/秒）
    private String windDirection;     // 风向（如：北风、东南风等）
    private double temperature;       // 温度（单位：摄氏度）
    private double humidity;          // 湿度（单位：百分比，范围：0-100）
    private double visibilityFactor;  // 可见度因子（0-1，影响命中率）

    /**
     * 构造方法
     * 初始化气象条件并计算可见度因子
     *
     * @param windSpeed     风速（单位：米/秒）
     * @param windDirection 风向（如：北风、东南风等）
     * @param temperature   温度（单位：摄氏度）
     * @param humidity      湿度（单位：百分比，范围：0-100）
     */
    public Weather(double windSpeed, String windDirection, double temperature, double humidity) {
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.temperature = temperature;
        this.humidity = humidity;
        this.visibilityFactor = calculateVisibilityFactor();  // 计算可见度因子
    }

    // --------------------- Getter 和 Setter ----------------------

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
        this.visibilityFactor = calculateVisibilityFactor();  // 更新可见度因子
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
        this.visibilityFactor = calculateVisibilityFactor();  // 更新可见度因子
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
        this.visibilityFactor = calculateVisibilityFactor();  // 更新可见度因子
    }

    /**
     * 获取可见度因子（影响命中率）
     * @return 可见度因子（0-1）
     */
    public double getVisibilityFactor() {
        return visibilityFactor;
    }

    public void setVisibilityFactor(double visibilityFactor) {
        if (visibilityFactor < 0.0 || visibilityFactor > 1.0) {
            throw new IllegalArgumentException("可见度因子必须在 0 和 1 之间");
        }
        this.visibilityFactor = visibilityFactor;
    }

    // --------------------- 功能方法 ----------------------

    /**
     * 计算可见度因子（Visibility Factor）
     * 根据风速、湿度、温度等气象条件动态调整
     *
     * @return 可见度因子（0-1）
     */
    private double calculateVisibilityFactor() {
        double factor = 1.0;

        if (windSpeed > 20) {
            factor -= 0.2;  // 大风降低可见度
        }
        if (humidity > 80) {
            factor -= 0.3;  // 高湿度（雾气）降低可见度
        }
        if (temperature < -10 || temperature > 40) {
            factor -= 0.1;  // 极端温度降低可见度
        }

        return Math.max(0.0, factor);  // 保证因子最小为 0
    }
}
