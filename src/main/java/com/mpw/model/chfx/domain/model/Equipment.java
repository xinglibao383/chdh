package com.mpw.model.chfx.domain.model;


import com.mpw.model.chfx.constants.TrajectoryType;
import com.mpw.model.chfx.constants.WeaponStatus;
import com.mpw.model.chfx.constants.WeaponType;

/**
 * 装备类（Equipment）
 *
 * 表示作战中的装备属性，包括武器类型、观察线高度、最小射程、最大射程、射击死界、命中精度和爆炸威力等。 支持计算命中概率、毁伤程度等功能。
 */
public class Equipment {
	/**
	 * 点的角色（区分我方/敌方）
	 */
	public enum PointRole {
		FRIENDLY,  // 我方
		ENEMY      // 敌方
	}
	private WeaponType type; // 武器类型（直瞄 DIRECT_FIRE 或 间瞄 INDIRECT_FIRE）
	private double observationHeight; // 观察线高度（单位：米）
	private double minRange; // 最小射程（单位：米）
	private double maxRange; // 最大射程（单位：米）
	private double deadZone; // 射击死界距离（单位：米）
	private double baseAccuracy; // 基础命中精度（0~1 范围）
	private double accuracy; // 实时命中精度（受环境影响）
	private WeaponStatus status; // 武器状态（ACTIVE、DISABLED）
	private TrajectoryType trajectoryType; // 弹道类型（直线、抛物线）
	private double explosivePower; // 武器爆炸威力（单位：kg TNT）
	private double rangNum; //毁伤半径
	private double pitchAngle;        //俯仰角（单位：度）
	private PointRole pointRole;

	public PointRole getPointRole() {
		return pointRole;
	}

	public void setPointRole(PointRole pointRole) {
		this.pointRole = pointRole;
	}

	public double getPitchAngle() {
		return pitchAngle;
	}

	public void setPitchAngle(double pitchAngle) {
		this.pitchAngle = pitchAngle;
	}

	public double getRangNum() {
		return rangNum;
	}

	public void setRangNum(double rangNum) {
		this.rangNum = rangNum;
	}

	private double firingAngle;
	// ============================== 1️⃣ 构造方法 ==============================

	public void setFiringAngle(double firingAngle) {
		this.firingAngle = firingAngle;
	}

	/**
	 * 全参构造方法：初始化装备属性
	 *
	 * @param type              武器类型（直瞄或间瞄）
	 * @param observationHeight 观察线高度（单位：米）
	 * @param minRange          最小射程（单位：米）
	 * @param maxRange          最大射程（单位：米）
	 * @param deadZone          射击死界（单位：米）
	 * @param baseAccuracy      基础命中精度（0~1）
	 * @param status            武器状态（ACTIVE、DISABLED）
	 * @param trajectoryType    弹道类型（直线、抛物线）
	 * @param explosivePower    爆炸威力（单位：kg TNT）
	 */
	public Equipment(WeaponType type, double observationHeight, double minRange, double maxRange, double deadZone,
					 double baseAccuracy, WeaponStatus status, TrajectoryType trajectoryType, double explosivePower,
					 double rangNum, double pitchAngle, PointRole pointRole) {

		// 参数校验
		if (type == null)
			throw new IllegalArgumentException("武器类型不能为空");
		if (status == null)
			throw new IllegalArgumentException("武器状态不能为空");
		if (trajectoryType == null)
			throw new IllegalArgumentException("弹道类型不能为空");
		if (minRange < 0 || maxRange <= minRange)
			throw new IllegalArgumentException("射程参数不合法");
		if (deadZone < 0 || deadZone >= maxRange)
			throw new IllegalArgumentException("死界设置不合法");
		if (baseAccuracy < 0 || baseAccuracy > 1)
			throw new IllegalArgumentException("命中精度必须在 0~1 之间");
		if (explosivePower < 0)
			throw new IllegalArgumentException("爆炸威力必须为正数");

		this.type = type;
		this.observationHeight = observationHeight;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.deadZone = deadZone;
		this.baseAccuracy = baseAccuracy;
		this.accuracy = baseAccuracy;
		this.status = status;
		this.trajectoryType = trajectoryType;
		this.explosivePower = explosivePower;
		this.rangNum = rangNum;
		this.pitchAngle = pitchAngle;
		this.pointRole = pointRole;
	}

	public Equipment(WeaponType type, double observationHeight, double minRange, double maxRange, double deadZone,
					 double baseAccuracy, WeaponStatus status, TrajectoryType trajectoryType, double explosivePower) {

		// 参数校验
		if (type == null)
			throw new IllegalArgumentException("武器类型不能为空");
		if (status == null)
			throw new IllegalArgumentException("武器状态不能为空");
		if (trajectoryType == null)
			throw new IllegalArgumentException("弹道类型不能为空");
		if (minRange < 0 || maxRange <= minRange)
			throw new IllegalArgumentException("射程参数不合法");
		if (deadZone < 0 || deadZone >= maxRange)
			throw new IllegalArgumentException("死界设置不合法");
		if (baseAccuracy < 0 || baseAccuracy > 1)
			throw new IllegalArgumentException("命中精度必须在 0~1 之间");
		if (explosivePower < 0)
			throw new IllegalArgumentException("爆炸威力必须为正数");

		this.type = type;
		this.observationHeight = observationHeight;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.deadZone = deadZone;
		this.baseAccuracy = baseAccuracy;
		this.accuracy = baseAccuracy;
		this.status = status;
		this.trajectoryType = trajectoryType;
		this.explosivePower = explosivePower;
	}
	public Equipment(double rangNum, double explosivePower) {

		if (explosivePower < 0)
			throw new IllegalArgumentException("爆炸威力必须为正数");
		this.rangNum = rangNum;
		this.explosivePower = explosivePower;
	}

	/**
	 * 默认构造方法（方便测试）
	 */
	public Equipment() {
		this(WeaponType.DIRECT_FIRE, 1.5, 100, 5000, 50, 0.8, WeaponStatus.ACTIVE, TrajectoryType.PARABOLIC, 100.0,500,50, PointRole.FRIENDLY);
	}

	// ============================== 2️⃣ Getter 和 Setter
	// ==============================

	public WeaponType getType() {
		return type;
	}

	public void setType(WeaponType type) {
		this.type = type;
	}

	public double getObservationHeight() {
		return observationHeight;
	}

	public void setObservationHeight(double observationHeight) {
		this.observationHeight = observationHeight;
	}

	public double getMinRange() {
		return minRange;
	}

	public void setMinRange(double minRange) {
		this.minRange = minRange;
	}

	public double getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(double maxRange) {
		this.maxRange = maxRange;
	}

	public double getDeadZone() {
		return deadZone;
	}

	public void setDeadZone(double deadZone) {
		this.deadZone = deadZone;
	}

	public double getBaseAccuracy() {
		return baseAccuracy;
	}

	public void setBaseAccuracy(double baseAccuracy) {
		this.baseAccuracy = baseAccuracy;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public WeaponStatus getStatus() {
		return status;
	}

	public void setStatus(WeaponStatus status) {
		this.status = status;
	}

	public TrajectoryType getTrajectoryType() {
		return trajectoryType;
	}

	public void setTrajectoryType(TrajectoryType trajectoryType) {
		this.trajectoryType = trajectoryType;
	}

	public double getExplosivePower() {
		return explosivePower;
	}

	public void setExplosivePower(double explosivePower) {
		this.explosivePower = explosivePower;
	}

	// ============================== 3️⃣ 核心算法 ==============================

	/**
	 * 判断是否在有效射程范围内（考虑最小射程、最大射程和死界）
	 *
	 * @param distance 目标距离（单位：米）
	 * @return true 表示目标在有效射程内；false 表示目标超出射程
	 */
	public boolean isWithinEffectiveRange(double distance) {
		return distance >= minRange && distance <= maxRange && distance >= deadZone;
	}

	/**
	 * 计算命中概率（线性衰减模型）
	 *
	 * @param distance 目标距离（单位：米）
	 * @return 命中概率（0~1）
	 */
	public double calculateHitProbability(double distance) {
		if (!isWithinEffectiveRange(distance))
			return 0.0;

		double normalizedDistance = (distance - deadZone) / (maxRange - deadZone);
		return accuracy * (1.0 - normalizedDistance);
	}

	/**
	 * 计算毁伤程度（综合考虑命中概率、爆炸威力、天气影响和目标遮蔽）
	 *
	 * @param distance    目标距离（单位：米）
	 * @param weather     天气条件（风速、湿度、温度）
	 * @param targetPoint 目标点（用于计算遮蔽与隐蔽效果）
	 * @param demData     DEM 数据（用于获取目标点的地形信息）
	 * @return 毁伤程度（0~1）
	 *//*
	 * public double calculateDamage(ShootingPoint shotpoint, double distance,
	 * Weather weather, ShootingPoint targetPoint, List<Map<String, Object>>
	 * demData) { // 1️⃣ 计算命中概率 double hitProbability =
	 * calculateHitProbability(distance); if (hitProbability == 0.0) return 0.0; //
	 * 命中概率为 0，直接返回无毁伤
	 *
	 * // 2️⃣ 考虑天气因素（风速、湿度、温度） double weatherFactor = 1 - (weather.getWindSpeed() *
	 * 0.01 + weather.getHumidity() * 0.005); weatherFactor *= (1 -
	 * (Math.abs(weather.getTemperature() - 25) * 0.002)); // 温度偏差对爆炸威力的影响
	 *
	 * // 3️⃣ 计算缩比距离（Hopkinson-Cranz 缩比定律） double scaledDistance =
	 * Utils.calculateScaledDistance(distance, this.explosivePower);
	 *
	 * // 4️⃣ 计算冲击波超压（kPa） double overpressure =
	 * Utils.calculateOverpressure(scaledDistance);
	 *
	 * // 5️⃣ 替代计算【隐蔽力】（Concealment）：目标点与地形高度差 Map<String, Object> concealmentScore
	 * = Utils.calculateConcealmentScoreWithDetails(shotpoint,targetPoint, demData);
	 *
	 * // 6️⃣ 替代计算【遮蔽力】（Cover）：射击点与目标点之间是否被地形遮挡（LOS分析） boolean isCovered =
	 * !Utils.calculateLineOfSight(shotpoint, targetPoint, demData); double
	 * coverScore = isCovered ? 0.8 : 0.0; // 有遮蔽 → 遮蔽力 0.8，否则为 0
	 *
	 * // 7️⃣ 综合毁伤计算（命中概率 * 爆炸威力 * 天气影响 * 遮蔽/隐蔽） double damage = hitProbability *
	 * Math.min(1.0, (overpressure / 100.0) * weatherFactor); damage *= (1 -
	 * concealmentScore) * (1 - coverScore); // 遮蔽和隐蔽降低毁伤
	 *
	 * return Math.max(0.0, Math.min(1.0, damage)); // 限制结果在 0~1 之间 }
	 */

	// ============================== 4️⃣ 信息输出 ==============================

	@Override
	public String toString() {
		return "装备信息 {" + "\n  武器类型：" + type + "\n  最大射程：" + maxRange + " 米" + "\n  爆炸威力：" + explosivePower + " kg TNT"
				+ "\n  实时命中精度：" + accuracy + "\n}";
	}

	public double getFiringAngle() {
		// TODO Auto-generated method stub
		return firingAngle;
	}
}
