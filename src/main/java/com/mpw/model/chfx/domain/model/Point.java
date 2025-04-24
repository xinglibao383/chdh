package com.mpw.model.chfx.domain.model;

import java.util.Map;

/**
 * 通用点接口（Point）
 * 用于统一表示射击点（ShootingPoint）和目标点（TargetPoint），
 * 支持射击分析、隐蔽性与遮蔽性分析等功能。
 */
public interface Point {

    // =================== 🌐 位置与几何信息 ===================

    /**
     * 获取点的纬度（单位：度）
     * @return 纬度值
     */
    double getLatitude();

    /**
     * 获取点的经度（单位：度）
     * @return 经度值
     */
    double getLongitude();

    /**
     * 获取点的高度（单位：米）
     * @return 高度值
     */
    double getHeight();

    /**
     * 获取点的几何信息（如 BEM 格式的 POLYGON 或 POINT）
     * @return 几何信息字符串
     */
    String getGeometry();

    /**
     * 设置点的几何信息（如 BEM 格式的 POLYGON 或 POINT）
     * @param geometry 几何信息字符串
     */
    void setGeometry(String geometry);

    // =================== 🎯 射击分析属性 ===================

    /**
     * 判断该点是否可以被射击
     * @return true：可射击；false：不可射击
     */
    boolean isShootable();

    /**
     * 设置该点是否可以被射击
     * @param shootable true：可射击；false：不可射击
     */
    void setShootable(boolean shootable);

    /**
     * 获取不可射击的原因（如：死区、遮挡）
     * @return 不可射击的原因描述
     */
    String getReason();

    /**
     * 设置不可射击的原因（如：死区、遮挡）
     * @param reason 原因描述
     */
    void setReason(String reason);

    /**
     * 获取命中概率（0.0 ~ 1.0）
     * @return 命中概率
     */
    double getHitProbability();

    /**
     * 设置命中概率（0.0 ~ 1.0）
     * @param hitProbability 命中概率
     */
    void setHitProbability(double hitProbability);

    // =================== 🛡️ 隐蔽与遮蔽分析 ===================

    /**
     * 获取隐蔽深度（影响目标被发现的难易程度）
     * @return 隐蔽深度值
     */
    double getConcealmentDepth();

    /**
     * 设置隐蔽深度（影响目标被发现的难易程度）
     * @param concealmentDepth 隐蔽深度值
     */
    void setConcealmentDepth(double concealmentDepth);

    /**
     * 获取遮蔽深度（影响目标被打击的难易程度）
     * @return 遮蔽深度值
     */
    double getCoverDepth();

    /**
     * 设置遮蔽深度（影响目标被打击的难易程度）
     * @param coverDepth 遮蔽深度值
     */
    void setCoverDepth(double coverDepth);

    // =================== 👀 通视性分析 ===================

    /**
     * 获取点的通视性（LOS，Line of Sight）
     * @return true：有通视性（无遮挡）；false：无通视性（有遮挡）
     */
    boolean isLos();

    /**
     * 设置点的通视性（LOS，Line of Sight）
     * @param los true：有通视性（无遮挡）；false：无通视性（有遮挡）
     */
    void setLos(boolean los);

    // =================== 🌍 DEM 地形数据 ===================

    /**
     * 获取与该点相关联的 DEM 数据（数字高程模型）
     * @return DEM 数据（Map 结构）
     */
    Map<String, Object> getDemData();

    /**
     * 设置与该点相关联的 DEM 数据（数字高程模型）
     * @param demData DEM 数据（Map 结构）
     */
    void setDemData(Map<String, Object> demData);
}
