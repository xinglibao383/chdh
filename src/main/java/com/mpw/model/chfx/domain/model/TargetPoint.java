package com.mpw.model.chfx.domain.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 目标点类
 * 表示作战区域内的一个目标点，包括目标的地理位置信息（经纬度、高度）以及是否可射击等属性。
 * 
 * 该类使用 Builder 设计模式，使得在创建对象时可以灵活地设置必填字段和可选字段。
 */
public class TargetPoint implements Point{

    // =================== 🌐 位置与几何信息 ===================

    /** 射击点的纬度（单位：度） */
    private double latitude;

    /** 射击点的经度（单位：度） */
    private double longitude;

    /** 射击点的高度（单位：米） */
    private double height;

    /** 几何信息（BEM 格式，如 POLYGON 或 POINT） */
    private String geometry;

    // =================== 🎯 射击分析属性 ===================

    /** 是否可射击（true：可射击；false：不可射击） */
    private boolean shootable;

    /** 不可射击原因（如死区、遮挡） */
    private String reason;

    /** 命中概率（范围 0.0 ~ 1.0） */
    private double hitProbability;

    // =================== 🛡️ 隐蔽与遮蔽分析 ===================

    /** 隐蔽深度（影响目标被发现的难易程度） */
    private double concealmentDepth;

    /** 遮蔽深度（影响目标被打击的难易程度） */
    private double coverDepth;

    // =================== 👀 通视性分析 ===================

    /** 通视性（LOS，Line of Sight） */
    private boolean los;

    // =================== 🌍 DEM 数据 ===================

    /** 与射击点相关联的 DEM 数据（地形数据） */
    private Map<String, Object> demData;

    // =================== ⚙️ 构造方法 ===================

    /**
     * 构造方法（Builder模式）
     * 强制使用 Builder 创建对象，灵活设置必填和可选字段。
     *
     * @param builder Builder 对象，包含所有属性设置
     */
    private TargetPoint(Builder builder) {
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.height = builder.height;
        this.geometry = builder.geometry;
        this.shootable = builder.shootable;
        this.reason = builder.reason;
        this.hitProbability = builder.hitProbability;
        this.concealmentDepth = builder.concealmentDepth;
        this.coverDepth = builder.coverDepth;
        this.los = builder.los;
        this.demData = builder.demData;
    }

    /**
     * Builder 类用于构建 ShootingPoint 对象
     */
    public static class Builder {
        private double latitude;
        private double longitude;
        private double height;
        private String geometry;
        private boolean shootable;
        private String reason = "";
        private double hitProbability;
        private double concealmentDepth;
        private double coverDepth;
        private boolean los;
        private Map<String, Object> demData = new HashMap<>();

        /**
         * Builder 的构造方法（设置必填字段）
         *
         * @param latitude  射击点的纬度（单位：度）
         * @param longitude 射击点的经度（单位：度）
         * @param shootable 是否可射击（true 表示可射击）
         */
        public Builder(double latitude, double longitude, boolean shootable) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.shootable = shootable;
        }

        /**
         * 设置射击点的高度（单位：米）
         *
         * @param height 高度值
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder height(double height) {
            this.height = height;
            return this;
        }

        /**
         * 设置射击点的几何信息（BEM 格式）
         *
         * @param geometry 几何信息字符串
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder geometry(String geometry) {
            this.geometry = geometry;
            return this;
        }

        /**
         * 设置不可射击原因
         *
         * @param reason 不可射击的原因描述
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        /**
         * 设置命中概率（范围 0.0 ~ 1.0）
         *
         * @param hitProbability 命中概率
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder hitProbability(double hitProbability) {
            this.hitProbability = hitProbability;
            return this;
        }

        /**
         * 设置隐蔽深度
         *
         * @param concealmentDepth 隐蔽深度值
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder concealmentDepth(double concealmentDepth) {
            this.concealmentDepth = concealmentDepth;
            return this;
        }

        /**
         * 设置遮蔽深度
         *
         * @param coverDepth 遮蔽深度值
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder coverDepth(double coverDepth) {
            this.coverDepth = coverDepth;
            return this;
        }

        /**
         * 设置通视性（LOS）
         *
         * @param los 是否通视
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder los(boolean los) {
            this.los = los;
            return this;
        }

        /**
         * 设置 DEM 数据（地形数据）
         *
         * @param demData DEM 数据
         * @return 当前 Builder 对象（链式调用）
         */
        public Builder demData(Map<String, Object> demData) {
            this.demData = demData;
            return this;
        }

        /**
         * 构建 ShootingPoint 对象
         *
         * @return 构建完成的 ShootingPoint 实例
         */
        public TargetPoint build() {
            return new TargetPoint(this);
        }
    }

    // =================== 🌐 位置与几何信息 ===================

    /** 获取射击点的纬度 */
    @Override
    public double getLatitude() {
        return latitude;
    }

    /** 获取射击点的经度 */
    @Override
    public double getLongitude() {
        return longitude;
    }

    /** 获取射击点的高度 */
    @Override
    public double getHeight() {
        return height;
    }

    /** 获取几何信息 */
    @Override
    public String getGeometry() {
        return geometry;
    }

    /** 设置几何信息 */
    @Override
    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    // =================== 🎯 射击分析属性 ===================

    /** 判断是否可射击 */
    @Override
    public boolean isShootable() {
        return shootable;
    }

    /** 设置是否可射击 */
    @Override
    public void setShootable(boolean shootable) {
        this.shootable = shootable;
    }

    /** 获取不可射击原因 */
    @Override
    public String getReason() {
        return reason;
    }

    /** 设置不可射击原因 */
    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    /** 获取命中概率 */
    @Override
    public double getHitProbability() {
        return hitProbability;
    }

    /** 设置命中概率 */
    @Override
    public void setHitProbability(double hitProbability) {
        this.hitProbability = hitProbability;
    }

    // =================== 🛡️ 隐蔽与遮蔽分析 ===================

    /** 获取隐蔽深度 */
    @Override
    public double getConcealmentDepth() {
        return concealmentDepth;
    }

    /** 设置隐蔽深度 */
    @Override
    public void setConcealmentDepth(double concealmentDepth) {
        this.concealmentDepth = concealmentDepth;
    }

    /** 获取遮蔽深度 */
    @Override
    public double getCoverDepth() {
        return coverDepth;
    }

    /** 设置遮蔽深度 */
    @Override
    public void setCoverDepth(double coverDepth) {
        this.coverDepth = coverDepth;
    }

    /** 获取 DEM 数据 */
    @Override
    public Map<String, Object> getDemData() {
        return demData;
    }

    /** 设置 DEM 数据 */
    @Override
    public void setDemData(Map<String, Object> demData) {
        this.demData = demData;
    }

	@Override
	public boolean isLos() {
		// TODO Auto-generated method stub
		return los;
	}

	@Override
	public void setLos(boolean los) {
		// TODO Auto-generated method stub
		this.los=los;
		
	}
	/**
     * 返回目标点的详细信息字符串，方便调试和输出。
     *
     * @return 包含目标点所有属性的字符串。
     */
    @Override
    public String toString() {
        return "TargetPoint{" +
               "latitude=" + latitude +
               ", longitude=" + longitude +
               ", height=" + height +
               ", shootable=" + shootable +
               ", reason='" + reason + '\'' +
               ", geometry='" + geometry + '\'' +
               ", hitProbability=" + hitProbability +
               ", concealmentDepth=" + concealmentDepth +
               ", coverDepth=" + coverDepth +
               ", los=" + los +
               '}';
    }
}
