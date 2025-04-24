package com.mpw.model.common.util;


import com.mpw.model.chfx.domain.model.*;
import dm.jdbc.filter.stat.json.JSONArray;
import dm.jdbc.filter.stat.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报告生成类 用于生成敌我双方的射击效能分析报告，包含死区、死界、命中概率、毁伤程度、隐蔽与遮蔽性等内容。
 */
public class ReportGeneratorV2 {

	/**
	 * 生成死界分析报告（详细版）
	 *
	 * @param shootingPoint 射击点
	 * @param equipment     武器装备
	 * @param targetPoint   目标点
	 * @param demData       DEM 数据
	 * @return 死界分析报告（JSON 格式）
	 */
	public static JSONObject generateDeadSectorReport(ShootingPoint shootingPoint, Equipment equipment, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
	    JSONObject report = new JSONObject();

	    // 计算死界扇形区域
	    List<double[]> deadSectorCoords = UtilsV2.calculateDeadSector(shootingPoint, equipment, targetPoint);

	    // 计算死界面积
	    double deadSectorArea = UtilsV2.calculateAreaFromCoordinates(deadSectorCoords);

	    // 获取 DEM 数据中死界区域内的地形信息
	    List<Map<String, Object>> filteredDEMData = UtilsV2.filterDEMDataWithinArea(demData, deadSectorCoords);

	    // 构建分析报告
	    report.put("deadSectorArea", String.format("%.6f 平方公里", deadSectorArea));
	    report.put("deadSectorBEM", UtilsV2.convertToBEMFormat(deadSectorCoords));
	    report.put("terrainData", filteredDEMData);

	    return report;
	}
	/**
	 * 生成死区分析报告
	 *
	 * @param shootingPoint 射击点
	 * @param equipment     武器装备
	 * @param area          作战区域
	 * @param demData       DEM 地形数据
	 * @param targetPoint   目标点（用于计算打击方位角）
	 * @return 死区分析报告（JSON 格式）
	 */
	public static JSONObject generateDeadZoneReport(ShootingPoint shootingPoint, Equipment equipment, Area area, List<Map<String, Object>> demData, ShootingPoint targetPoint) {
	    // 计算死区多边形
	    List<double[]> deadZone = UtilsV2.calculateDeadZone(shootingPoint, equipment, area, targetPoint);

	    // 计算死区面积
	    double deadZoneArea = UtilsV2.calculateAreaFromCoordinates(deadZone);

	    // 获取死区内的 DEM 数据
	    List<Map<String, Object>> filteredDEMData = UtilsV2.filterDEMDataWithinArea(demData, deadZone);

	    // 构建 JSON 报告
	    JSONObject report = new JSONObject();
	    report.put("deadZoneArea", String.format("%.6f 平方公里", deadZoneArea));
	    report.put("deadZoneBEM", UtilsV2.convertToBEMFormat(deadZone));
	    report.put("terrainData", filteredDEMData);

	    return report;
	}

	/**
	 * 3️⃣ 射击效能分析（命中率、毁伤能力、命中区域分析）
	 *
	 * @param shooter 射击点
	 * @param target  目标点
	 * @param weapon  武器装备信息
	 * @param weather 天气信息
	 * @param demData DEM 地形数据
	 * @return 射击效能分析结果（JSON 格式）
	 */
	public static JSONObject analyzeFireEffectiveness(ShootingPoint shooter, ShootingPoint target, Equipment weapon,
			Weather weather, List<Map<String, Object>> demData) {
		JSONObject effectivenessReport = new JSONObject();

		// ① 计算射击距离和角度
		double distance = UtilsV2.calculateDistance(shooter, target);
		double firingAngle = UtilsV2.calculateAzimuthAngle(shooter, target);

		// ② 计算命中概率
		double hitProbability = UtilsV2.calculateHitProbability(distance, weapon, weather);

		// ③ 毁伤分析（基于缩比距离计算冲击波超压和动压）
		double scaledDistance = UtilsV2.calculateScaledDistance(distance, weapon.getExplosivePower());
		double overpressure = UtilsV2.calculateOverpressure(scaledDistance);
		double dynamicPressure = UtilsV2.calculateDynamicPressure(overpressure);

		// ④ 命中区域分析（根据命中概率划分区域）
		Map<String, List<double[]>> hitZones = calculateHitZones(shooter, weapon, weather, demData);

		// ⑤ 命中区域面积计算
		double highHitArea = UtilsV2.calculateAreaFromCoordinates(hitZones.get("高命中区"));
		double mediumHitArea = UtilsV2.calculateAreaFromCoordinates(hitZones.get("中命中区"));
		double lowHitArea = UtilsV2.calculateAreaFromCoordinates(hitZones.get("低命中区"));

		// ⑥ 结果汇总
		effectivenessReport.put("射击角度", firingAngle + " 度");
		effectivenessReport.put("射击距离", distance + " 米");
		effectivenessReport.put("命中概率", String.format("%.2f%%", hitProbability * 100));
		effectivenessReport.put("缩比距离 Z", scaledDistance + " m/kg^(1/3)");
		effectivenessReport.put("冲击波超压", overpressure + " kPa");
		effectivenessReport.put("动压", dynamicPressure + " kPa");

		// ⑦ 命中区域与 BEM 数据
		JSONObject hitAreaReport = new JSONObject();
		hitAreaReport.put("高命中区面积", String.format("%.6f 平方公里", highHitArea));
		hitAreaReport.put("高命中区 BEM", UtilsV2.convertToBEMFormat(hitZones.get("高命中区")));
		hitAreaReport.put("中命中区面积", String.format("%.6f 平方公里", mediumHitArea));
		hitAreaReport.put("中命中区 BEM", UtilsV2.convertToBEMFormat(hitZones.get("中命中区")));
		hitAreaReport.put("低命中区面积", String.format("%.6f 平方公里", lowHitArea));
		hitAreaReport.put("低命中区 BEM", UtilsV2.convertToBEMFormat(hitZones.get("低命中区")));

		effectivenessReport.put("命中区域分析", hitAreaReport);

		return effectivenessReport;
	}

	/**
	 * 根据命中概率划分命中区域（高、中、低命中区）
	 *
	 * @param shooter 射击点
	 * @param weapon  武器装备信息
	 * @param weather 天气条件
	 * @param demData DEM 地形数据
	 * @return 命中区域划分结果（Map，包含高命中区、中命中区、低命中区的坐标）
	 */
	public static Map<String, List<double[]>> calculateHitZones(ShootingPoint shooter, Equipment weapon,
																Weather weather, List<Map<String, Object>> demData) {
		Map<String, List<double[]>> hitZones = new HashMap<>();

		// 初始化三个等级的命中区
		hitZones.put("高命中区", new ArrayList<>());
		hitZones.put("中命中区", new ArrayList<>());
		hitZones.put("低命中区", new ArrayList<>());

		double maxRange = weapon.getMaxRange();

		// 按一定的步长遍历目标区域，划分命中区（以 50 米为步长）
		double step = 50.0;
		for (double r = weapon.getMinRange(); r <= maxRange; r += step) {
			for (double angle = 0; angle < 360; angle += 10) {
				// 计算当前方向的目标点
				TargetPoint point = UtilsV2.calculateTargetPoint(shooter, angle, r);
				double hitProbability = UtilsV2.calculateHitProbability(r, weapon, weather);

				// 根据命中概率分类
				if (hitProbability >= 0.7) {
					hitZones.get("高命中区").add(new double[] { point.getLatitude(), point.getLongitude() });
				} else if (hitProbability >= 0.4) {
					hitZones.get("中命中区").add(new double[] { point.getLatitude(), point.getLongitude() });
				} else {
					hitZones.get("低命中区").add(new double[] { point.getLatitude(), point.getLongitude() });
				}
			}
		}
		return hitZones;
	}

	public static JSONObject generateComprehensibeDestructionReport(double distance, ShootingPoint shootingPoint, Equipment weapon, List<Map<String, Object>> demData, double maxRadius, double step){
		JSONObject report = new JSONObject();
		JSONObject zones = new JSONObject();
		JSONObject zoneReport = new JSONObject();
		double scaledDistance = UtilsV2.calculateScaledDistance(distance, weapon.getExplosivePower());;
		double impulse = UtilsV2.calculateImpulse(scaledDistance);
		double overpressure = UtilsV2.calculateOverpressure(distance,weapon.getExplosivePower(),"地面爆炸","空气");
		double dynamicPressurs = UtilsV2.calculateDynamicPressure(overpressure);
		double peakPressire = UtilsV2.calculatePeakPressure(distance,weapon.getExplosivePower());
		double penntrationDepth = UtilsV2.calculateRockPenetrationDepth(weapon.getExplosivePower(),overpressure,"花岗岩");
		// 2. 破片质量
		double fragmentMass = 1000; // 10g 的小破片
		// 3. 阻力系数 (Cd)
		double cD = 0.8; // 破片
		// 4. 迎风面积
		//若破片大约是个直径 1 cm 的小球，截面积 ~ π * (0.01/2)^2
		double crossSectionArea = Math.PI * Math.pow(0.005, 2);
		double fragametRange = UtilsV2.calculateRange(weapon.getExplosivePower() * 500, 1000, cD, crossSectionArea, 0.05, 45.0);
		JSONObject humanDamage = generateTargetDamageWithBEMV2("人员",shootingPoint,weapon,overpressure,distance,distance);
		JSONObject buildingDamage = generateTargetDamageWithBEMV2("建筑",shootingPoint,weapon,overpressure,distance,distance);
		JSONObject groundDamage = generateTargetDamageWithBEMV2("地面",shootingPoint,weapon,overpressure,distance,distance);
		zoneReport.put("radiuStep",/*radius + "_"+*/(distance));
		zoneReport.put("overpressure",overpressure);
		zoneReport.put("dynamicPressurs",dynamicPressurs);
		zoneReport.put("impulse",impulse );
		zoneReport.put("峰值压力",peakPressire);
		zoneReport.put("侵彻深度",penntrationDepth);
		zoneReport.put("破片破坏范围",fragametRange);
		zoneReport.put("人员毁伤评估",humanDamage);
		zoneReport.put("建筑毁伤评估",buildingDamage);
		zoneReport.put("地面毁伤评估",groundDamage);
		zones.put("zoneReport",zoneReport);
		report.put("爆炸中心点",new JSONObject().put("纬度",shootingPoint.getLatitude()).put("经度",shootingPoint.getLongitude()).put("高度",shootingPoint.getHeight()));
		report.put("武器信息", new JSONObject().put("武器类型",weapon.getType()).put("爆炸威力",weapon.getExplosivePower() + "kg TNT").put("最小射程",weapon.getMinRange()+"米").put("最大射程",weapon.getMaxRange()+"米"));
		report.put("最大分析半径",maxRadius+" 米");
		report.put("分析步长",step + " 米");
		report.put("毁伤区域分析",zones);

		System.out.println(report);

		return report;
	}
	public static JSONObject generateTargetDamageWithBEMV2(String targetType, ShootingPoint shootingPoint, Equipment weapon, double overpressure, double maxRadius, double step){
		JSONObject targetReport = new JSONObject();
		List<double[]> boundaryPoints = new ArrayList<>();
		double maxDamageRadius = step;
        /*double radius = 1.0;
        if (maxRadius < step) {
            radius = maxRadius;
            maxDamageRadius = maxRadius;
        }*/
		for (double radius = 0.0;radius <= maxRadius; radius += step) {
			double radiusOverpressure = UtilsV2.calculateOverpressure(radius,weapon.getExplosivePower(),"地面爆炸","空气");
			String currentDamageLevel = evaluateDamageLevel(targetType,radiusOverpressure);
			if(currentDamageLevel.equals("完全倒塌") || currentDamageLevel.equals("重伤")){
				maxDamageRadius = radius;
			}
		}
		boundaryPoints = genrateCircleBoundaryPoints(shootingPoint,maxDamageRadius);
		String bemFormat = UtilsV2.convertToBEMFormat(boundaryPoints);
		targetReport.put("毁伤范围半径",maxDamageRadius);
		targetReport.put("BEM 数据",bemFormat);
		targetReport.put("毁伤等级",evaluateDamageLevel(targetType,overpressure));
		return targetReport;
	}

	private static List<double[]> genrateCircleBoundaryPoints(ShootingPoint center, double radius){
		List<double []> points = new ArrayList<>();
		int segments =360;
		for (int angle  = 0; angle < segments; angle ++) {
			double radians =  Math.toRadians(angle);
			double latitudeOffset  =radius * Math.cos(radians) / 111320;
			double longtudeOffset  = radius * Math.sin(radians) / 111320 * Math.cos(Math.toRadians(center.getLatitude()));
			double [] point = {
					center.getLatitude() + latitudeOffset, center.getLongitude() + longtudeOffset
			};
			points.add(point);
		}
		return  points;
	}

	/**
	 * 4️⃣ 隐蔽与遮蔽力分析
	 *
	 * @param shootingPoint 射击点
	 * @param targetPoint   目标点
	 * @param demData       DEM 地形数据
	 * @return 隐蔽与遮蔽性分析结果（JSON 格式）
	 */
	public static JSONObject generateConcealmentReport(ShootingPoint shootingPoint, ShootingPoint targetPoint,
			List<Map<String, Object>> demData) {

		JSONObject concealmentReport = new JSONObject();

		// ① 计算总射击距离
		double totalDistance = UtilsV2.calculateDistance(shootingPoint, targetPoint);

		// ② 计算隐蔽距离（地形遮挡导致的不可视距离）
		double concealmentDistance = UtilsV2.calculateConcealmentDistance(shootingPoint, targetPoint, demData);

		// ③ 计算遮蔽深度（地形遮挡的深度）
		double coverDepth = UtilsV2.calculateCoverDepth(shootingPoint, targetPoint, demData);

		// ④ 计算隐蔽性和遮蔽性得分
		// 遮蔽
		Map<String, Object> concealmentScore = UtilsV2.calculateCoverScoreWithDetails(shootingPoint, targetPoint,
				demData);
		// 隱蔽
		Map<String, Object> coverScore = UtilsV2.calculateConcealmentScoreWithDetails(shootingPoint, targetPoint,
				demData);

		double concealmentScores = (double) concealmentScore.get("coverScore");
		double coverScores = (double) coverScore.get("concealmentScore");

		// ⑥ 组织分析报告
		concealmentReport.put("📏 总射击距离", String.format("%.2f 米", totalDistance));
		concealmentReport.put("📏 隐蔽距离", String.format("%.2f 米", concealmentDistance));
		concealmentReport.put("📏 遮蔽深度", String.format("%.2f 米", coverDepth));

		concealmentReport.put("🔹 隐蔽性得分", String.format("%.2f", concealmentScores));
		concealmentReport.put("🔹 遮蔽性得分", String.format("%.2f", coverScores));

		System.out.println("🔹 被遮挡点（隐蔽性）：");
		List<Map<String, Double>> concealedPointss = (List<Map<String, Double>>) concealmentScore.get("blockedPoints");
		JSONArray concealedArray = new JSONArray();
		for (Map<String, Double> point : concealedPointss) {
			JSONObject pointInfo = new JSONObject();
			pointInfo.put("纬度", point.get("latitude"));
			pointInfo.put("经度", point.get("longitude"));
			pointInfo.put("地形高度", String.format("%.2f m", point.get("terrainHeight")));
			concealedArray.put(pointInfo);
		}
		concealmentReport.put("🔹 被遮挡点（隐蔽性）", concealedArray);

		System.out.println("🔹 被遮挡点（遮蔽性）：");
		JSONArray blockedArray = new JSONArray();
		List<Map<String, Double>> blockedPoints = (List<Map<String, Double>>) coverScore.get("concealedPoints");
		for (Map<String, Double> point : blockedPoints) {
			JSONObject pointInfo = new JSONObject();
			pointInfo.put("纬度", point.get("latitude"));
			pointInfo.put("经度", point.get("longitude"));
			pointInfo.put("地形高度", String.format("%.2f m", point.get("terrainHeight")));
			blockedArray.put(pointInfo);
		}

		concealmentReport.put("🔹 被遮挡点（遮蔽性）", blockedArray);

		return concealmentReport;
	}

	/*** 4.5毀傷 ***/

	

	/**
	 * 针对不同目标类型（人员、建筑、地面）进行超压毁伤评估
	 *
	 * @param targetType   目标类型（人员、建筑、地面）
	 * @param overpressure 计算得到的冲击波超压（MPa）
	 * @return 毁伤等级（无伤、轻伤、中伤、重伤、致命）
	 */
	public static String evaluateDamageLevel(String targetType, double overpressure) {
		switch (targetType.toLowerCase()) {
		case "人员":
			if (overpressure > 0.1)
				return "致命";
			if (overpressure > 0.05)
				return "重伤（骨折或内出血）";
			if (overpressure > 0.03)
				return "中伤（内伤或耳膜破裂）";
			if (overpressure > 0.02)
				return "轻伤（耳鸣）";
			return "无伤";
		case "建筑":
			if (overpressure > 1.0)
				return "完全倒塌";
			if (overpressure > 0.5)
				return "严重破坏";
			if (overpressure > 0.2)
				return "中度破坏";
			return "轻微损伤";
		case "地面":
			if (overpressure > 2.0)
				return "深坑破坏";
			if (overpressure > 1.0)
				return "地面裂缝";
			return "无显著破坏";
		default:
			return "未知目标类型";
		}
	}

	/**
	 * 综合毁伤评估报告（含人员、建筑、地面评估，按半径分组）
	 *
	 * @param shootingPoint 射击点
	 * @param targetPoint   目标点
	 * @param weapon        武器信息
	 * @param demData       DEM 数据
	 * @param maxRadius     最大分析半径
	 * @param step          分析步长（米）
	 * @return 综合毁伤评估报告（JSON 格式）
	 */
	public static JSONObject generateComprehensiveDestructionReport(
	        ShootingPoint shootingPoint, ShootingPoint targetPoint,
	        Equipment weapon, List<Map<String, Object>> demData,
	        double maxRadius, double step) {

	    JSONObject report = new JSONObject(); // 总体报告
	    JSONArray zones = new JSONArray();    // 每个半径范围的评估数据

	    // 遍历每隔指定步长的半径，进行毁伤评估
	    for (double radius = 0.0; radius <= maxRadius; radius += step) {
	    	if(radius==0) {
	    		radius = 1;
	    	}
	        JSONObject zoneReport = new JSONObject(); // 当前半径范围的评估数据

	        //   计算射击距离
	        double distance = UtilsV2.calculateDistance(shootingPoint, targetPoint);

	        //   计算超压、动压、比冲量
	        double overpressure = UtilsV2.calculateOverpressure(radius, weapon.getExplosivePower(), "地面爆炸", "空气");
	        double dynamicPressure = UtilsV2.calculateDynamicPressure(overpressure);
	        double impulse = UtilsV2.calculateImpulse(overpressure);

	        //   峰值压力计算
	        double peakPressure = UtilsV2.calculatePeakPressure(radius, weapon.getExplosivePower());

	        //   岩石侵彻深度
	        double penetrationDepth = UtilsV2.calculateRockPenetrationDepth(weapon.getExplosivePower(), overpressure, "花岗岩");

	        //   破片/抛掷物分析
	        double fragmentRange = UtilsV2.calculateFragmentDamageRange(weapon.getExplosivePower(), 0.1);

	        //   毁伤评估
	        JSONObject humanDamage = generateTargetDamageWithBEM("人员", shootingPoint, weapon, overpressure, radius, step);
	        JSONObject buildingDamage = generateTargetDamageWithBEM("建筑", shootingPoint, weapon, overpressure, radius, step);
	        JSONObject groundDamage = generateTargetDamageWithBEM("地面", shootingPoint, weapon, overpressure, radius, step);

	        // 填充当前半径的评估数据
	        zoneReport.put("半径范围", radius + "-" + (radius + step) + " 米");
	        zoneReport.put("超压值", overpressure + " MPa");
	        zoneReport.put("动压值", dynamicPressure + " MPa");
	        zoneReport.put("比冲量", impulse + " s");
	        zoneReport.put("峰值压力", peakPressure + " MPa");
	        zoneReport.put("岩石侵彻深度", penetrationDepth + " 米");
	        zoneReport.put("破片破坏范围", fragmentRange + " 米");
	        zoneReport.put("人员毁伤评估", humanDamage);
	        zoneReport.put("建筑毁伤评估", buildingDamage);
	        zoneReport.put("地面毁伤评估", groundDamage);

	        // 将当前范围数据加入到 zones 中
	        zones.put(zoneReport);
	    }

	    // 填充总体报告
	    report.put("爆炸中心点", new JSONObject()
	            .put("纬度", shootingPoint.getLatitude())
	            .put("经度", shootingPoint.getLongitude())
	            .put("高度", shootingPoint.getHeight()));
	    report.put("目标点", new JSONObject()
	            .put("纬度", targetPoint.getLatitude())
	            .put("经度", targetPoint.getLongitude())
	            .put("高度", targetPoint.getHeight()));
	    report.put("武器信息", new JSONObject()
	            .put("武器类型", weapon.getType())
	            .put("爆炸威力", weapon.getExplosivePower() + " kg TNT")
	            .put("最小射程", weapon.getMinRange() + " 米")
	            .put("最大射程", weapon.getMaxRange() + " 米"));
	    report.put("最大分析半径", maxRadius + " 米");
	    report.put("分析步长", step + " 米");
	    report.put("毁伤区域分析", zones);

	    return report;
	}


	/**
	 * 针对特定目标类型生成毁伤评估及 BEM 数据
	 *
	 * @param targetType    目标类型（人员、建筑、地面）
	 * @param shootingPoint 射击点
	 * @param weapon        武器信息
	 * @param overpressure  当前射击超压
	 * @param maxRadius     最大分析半径
	 * @param step          分析步长
	 * @return 目标毁伤评估及 BEM 数据（JSON 格式）
	 */
	private static JSONObject generateTargetDamageWithBEM(String targetType, ShootingPoint shootingPoint,
			Equipment weapon, double overpressure, double maxRadius, double step) {

		JSONObject targetReport = new JSONObject();
		List<double[]> boundaryPoints = new ArrayList<>();
		double maxDamageRadius = 0.0;

		// 计算毁伤范围
		for (double radius = 0.0; radius <= maxRadius; radius += step) {
			double radiusOverpressure = UtilsV2.calculateOverpressure(radius, weapon.getExplosivePower(), "地面爆炸", "空气");
			String currentDamageLevel = evaluateDamageLevel(targetType, radiusOverpressure);

			if (currentDamageLevel.equals("完全倒塌") || currentDamageLevel.equals("重伤")) {
				maxDamageRadius = radius;
			}
		}

		// 生成边界点并转换为 BEM 数据
		boundaryPoints = generateCircleBoundaryPoints(shootingPoint, maxDamageRadius);
		String bemFormat = UtilsV2.convertToBEMFormat(boundaryPoints);

		// 写入评估结果
		//targetReport.put("毁伤范围半径", maxDamageRadius + " 米");
		targetReport.put("BEM 数据", bemFormat);
		targetReport.put("毁伤等级", evaluateDamageLevel(targetType, overpressure));

		return targetReport;
	}

	/**
	 * 生成圆形边界点
	 *
	 * @param center 中心点（经纬度）
	 * @param radius 半径（单位：米）
	 * @return 边界点列表（经纬度数组）
	 */
	private static List<double[]> generateCircleBoundaryPoints(ShootingPoint center, double radius) {
	    List<double[]> points = new ArrayList<>();
	    int segments = 360; // 每隔 1° 计算一个点

	    for (int angle = 0; angle < segments; angle++) {
	        double radians = Math.toRadians(angle);

	        // 根据半径和角度计算边界点
	        double latitudeOffset = radius * Math.cos(radians) / 111320; // 每经度的实际米数（简化）
	        double longitudeOffset = radius * Math.sin(radians) / (111320 * Math.cos(Math.toRadians(center.getLatitude())));

	        double[] point = {
	            center.getLatitude() + latitudeOffset,
	            center.getLongitude() + longitudeOffset
	        };
	        points.add(point);
	    }

	    return points;
	}
}
