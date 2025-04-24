package com.mpw.model.chfx.constants;
/**
 * 武器状态枚举
 * 表示武器的当前状态（可用或不可用）。
 * 适用于射击分析系统中，武器状态的判断和切换。
 */
public enum WeaponStatus {
    /**
     * 表示武器当前状态为可用
     */
    ACTIVE("可用"),

    /**
     * 表示武器当前状态为不可用
     */
    DISABLED("不可用");

    private final String description; // 武器状态的描述（适用于多语言）

    /**
     * 构造方法
     * 初始化枚举值时设置对应的描述
     * @param description 武器状态的描述
     */
    WeaponStatus(String description) {
        this.description = description;
    }

    /**
     * 获取武器状态的描述信息
     * @return 武器状态的描述（如："可用" 或 "不可用"）
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据字符串输入获取对应的武器状态
     * 支持状态的动态解析，用于从用户输入或文件中加载状态
     * @param value 状态描述（如："可用" 或 "不可用"）
     * @return 对应的 WeaponStatus 枚举值；如果未匹配到，则返回 null
     */
    public static WeaponStatus fromDescription(String value) {
        for (WeaponStatus status : WeaponStatus.values()) {
            if (status.getDescription().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null; // 未找到匹配状态时返回 null
    }

    @Override
    public String toString() {
        return description; // 重写 toString 方法返回状态描述
    }
}
