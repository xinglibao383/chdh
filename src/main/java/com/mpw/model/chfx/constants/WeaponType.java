package com.mpw.model.chfx.constants;
/**
 * 武器类型枚举
 * 表示武器是直瞄还是间瞄武器。
 * 用于射击分析系统中的武器分类和处理逻辑。
 */
public enum WeaponType {
    /**
     * 直瞄武器
     * 武器射击路径是直线，例如步枪或直射火炮。
     */
    DIRECT_FIRE("直瞄"),
    
    /**
     * 间瞄武器
     * 武器射击路径是抛物线，例如迫击炮或火箭炮。
     */
    INDIRECT_FIRE("间瞄");

    private final String description; // 武器类型的描述

    /**
     * 构造方法
     * 初始化武器类型的描述
     * 
     * @param description 武器类型的描述信息
     */
    WeaponType(String description) {
        this.description = description;
    }

    /**
     * 获取武器类型的描述
     * 
     * @return 武器类型的描述（如："直瞄" 或 "间瞄"）
     */
    public String getDescription() {
        return description;
    }

    /**
     * 根据字符串描述获取对应的武器类型
     * 用于动态解析用户输入或配置文件中的武器类型。
     * 
     * @param value 武器类型描述（如："直瞄" 或 "间瞄"）
     * @return 对应的 WeaponType 枚举值；如果未找到匹配项，返回 null
     */
    public static WeaponType fromDescription(String value) {
        for (WeaponType type : WeaponType.values()) {
            if (type.getDescription().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null; // 如果未找到匹配项，则返回 null
    }

    @Override
    public String toString() {
        return description; // 重写 toString 方法，返回武器类型的描述
    }
}
