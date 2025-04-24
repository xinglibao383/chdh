package com.mpw.model.chfx.constants;
/**
 * 弹道类型枚举类
 * 表示武器的弹道类型，包括直线和抛物线。
 * 不同的弹道类型用于区分武器的射击轨迹特性，例如直瞄武器（LINEAR）和间瞄武器（PARABOLIC）。
 */
public enum TrajectoryType {
    /**
     * 直线弹道（适用于直瞄武器，如步枪、狙击枪）。
     */
    LINEAR("直线弹道", "适用于直瞄武器，如步枪、狙击枪等。轨迹近似为直线，射程较短但命中精度高。"),

    /**
     * 抛物线弹道（适用于间瞄武器，如迫击炮、火箭弹）。
     */
    PARABOLIC("抛物线弹道", "适用于间瞄武器，如迫击炮、火箭弹等。轨迹为抛物线，可用于越障射击。");

    private final String description; // 弹道类型的描述
    private final String details; // 弹道类型的详细说明

    /**
     * 构造方法，用于初始化弹道类型枚举的描述和详细信息
     * @param description 弹道类型的简要描述
     * @param details 弹道类型的详细说明
     */
    TrajectoryType(String description, String details) {
        this.description = description; // 初始化描述
        this.details = details; // 初始化详细说明
    }

    /**
     * 获取弹道类型的简要描述
     * @return 弹道类型的简要描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取弹道类型的详细说明
     * @return 弹道类型的详细说明
     */
    public String getDetails() {
        return details;
    }

    /**
     * 根据弹道类型返回其对应的特性
     * 可用于程序中动态生成武器特性说明或调试信息。
     * @return 弹道类型的完整特性信息
     */
    @Override
    public String toString() {
        return "TrajectoryType{" +
                "description='" + description + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
