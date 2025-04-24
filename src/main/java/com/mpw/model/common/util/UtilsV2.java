package com.mpw.model.common.util;

import com.mpw.model.chfx.domain.model.*;
import dm.jdbc.filter.stat.json.JSONArray;
import dm.jdbc.filter.stat.json.JSONObject;

import java.util.*;

/**
 * 工具类（Utils）
 * - 死界（Dead Zone）计算
 * - 死区（Blind Zone）判断
 * - 命中率与毁伤分析
 * - 间瞄射击（抛物线轨迹）
 */
public class UtilsV2 {

    private static final double EARTH_RADIUS = 6371 * 1000;  // 地球半径（米）
    private static final double GRAVITY = 9.81;              // 重力加速度（m/s²）
    private static final double AIR_DENSITY = 1.225; // 空气密度 (kg/m^3)

    /**
     * 计算两点之间的球面距离（米）
     *
     * @param shootingPoint 射击点（包含经纬度信息）
     * @param targetPoint   目标点（包含经纬度信息）
     * @return 射击点与目标点之间的球面距离（单位：米）
     */
    public static double calculateDistance(ShootingPoint shootingPoint, ShootingPoint targetPoint) {
        return calculateDistance(shootingPoint.getLatitude(), shootingPoint.getLongitude(),
                targetPoint.getLatitude(), targetPoint.getLongitude());
    }

    /**
     * 计算两点之间的球面距离（米）
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 两点之间的球面距离（单位：米）
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDiff = Math.toRadians(lat2 - lat1);
        double lonDiff = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * 判断目标点是否处于死界（Dead Zone）
     *
     * @param shootingPoint 射击点
     * @param targetPoint   目标点
     * @param equipment     武器装备（包含最小射程参数）
     * @return 如果目标点在死界内，返回 true；否则返回 false
     */
    public static boolean isInDeadZone(ShootingPoint shootingPoint, ShootingPoint targetPoint, Equipment equipment) {
        double distance = calculateDistance(shootingPoint, targetPoint);
        return distance < equipment.getMinRange();
    }

    /**
     * 判断目标点是否处于死区（Blind Zone）
     *
     * @param shootingPoint 射击点
     * @param targetPoint   目标点
     * @param demData       DEM 地形数据（包含地形高度）
     * @return 如果目标点在死区内，返回 true；否则返回 false
     */
    public static boolean isInBlindZone(ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        return !calculateLineOfSight(shootingPoint, targetPoint, demData);
    }

    /**
     * 判断射击点与目标点之间是否存在遮挡（通视性 LOS 分析）
     *
     * @param shootingPoint 射击点
     * @param targetPoint   目标点
     * @param demData       DEM 地形数据
     * @return 如果存在遮挡，返回 false；否则返回 true
     */
    public static boolean calculateLineOfSight(ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        double totalDistance = calculateDistance(shootingPoint, targetPoint);
        int steps = (int) Math.ceil(totalDistance / 10.0);

        for (int i = 1; i <= steps; i++) {
            double ratio = i / (double) steps;
            double currentLat = shootingPoint.getLatitude() + (targetPoint.getLatitude() - shootingPoint.getLatitude()) * ratio;
            double currentLon = shootingPoint.getLongitude() + (targetPoint.getLongitude() - shootingPoint.getLongitude()) * ratio;
            double terrainHeight = getTerrainHeight(currentLat, currentLon, demData);

            double shootingHeight = shootingPoint.getHeight();
            double targetHeight = targetPoint.getHeight();
            double shootingLineHeight = shootingHeight + (targetHeight - shootingHeight) * ratio;

            if (terrainHeight > shootingLineHeight) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取 DEM 数据中的地形高度
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param demData   DEM 数据
     * @return 该点的地形高度（单位：米）
     */
    public static double getTerrainHeight(double latitude, double longitude, List<Map<String, Object>> demData) {
        double closestDistance = Double.MAX_VALUE;
        double terrainHeight = 0.0;

        for (Map<String, Object> grid : demData) {
            double centerLat = ((Number) grid.get("centerLat")).doubleValue();
            double centerLon = ((Number) grid.get("centerLon")).doubleValue();
            double distance = calculateDistance(latitude, longitude, centerLat, centerLon);

            if (distance < closestDistance) {
                closestDistance = distance;
                terrainHeight = ((Number) grid.get("value")).doubleValue();
            }
        }
        return terrainHeight;
    }

    /**
     * 计算从射击点到目标点的方位角（单位：度，0度表示正北，顺时针方向增加）
     *
     * @param shooter 射击点
     * @param target  目标点
     * @return 方位角（单位：度）
     */
    public static double calculateAzimuthAngle(ShootingPoint shooter, ShootingPoint target) {
        double lat1 = Math.toRadians(shooter.getLatitude());
        double lon1 = Math.toRadians(shooter.getLongitude());
        double lat2 = Math.toRadians(target.getLatitude());
        double lon2 = Math.toRadians(target.getLongitude());

        double deltaLon = lon2 - lon1;
        double x = Math.sin(deltaLon) * Math.cos(lat2);
        double y = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
        double initialBearing = Math.atan2(x, y);
        double bearingDegrees = (Math.toDegrees(initialBearing) + 360) % 360;

        return bearingDegrees;
    }


    /**
     * 根据俯仰角调整射程，使用经典抛体运动公式
     *
     * @param maxRange   武器在最大度俯仰角下的最大射程（米）
     * @param pitchAngle 俯仰角（单位：度），范围应为0到90度
     * @return 调整后的有效射程（米）
     * @throws IllegalArgumentException 如果俯仰角不在0到90度之间
     */
    public static double adjustRangeByPitch(double maxRange, double pitchAngle) {
        if (pitchAngle < 0 || pitchAngle > 90) {
            throw new IllegalArgumentException("俯仰角必须在0到90度之间");
        }

        // 重力加速度（米/秒²）
        final double GRAVITY = 9.81;

        // 计算初速度，假设最大射程在最大俯仰角度时
        double initialVelocity = Math.sqrt(maxRange * GRAVITY);

        // 将俯仰角转换为弧度
        double pitchRad = Math.toRadians(pitchAngle);

        // 计算射程，使用抛体运动公式
        double range = (Math.pow(initialVelocity, 2) * Math.sin(2 * pitchRad)) / GRAVITY;

        // 确保射程不为负
        return Math.max(range, 0);
    }


    /**
     * 计算命中概率（考虑距离、武器精度和天气影响）
     *
     * @param distance  目标点与射击点的距离（单位：米）
     * @param equipment 武器装备（包含精度参数）
     * @param weather   天气条件（影响命中率）
     * @return 命中概率（0.0 ~ 1.0）
     */
    public static double calculateHitProbability(double distance, Equipment equipment, Weather weather) {


        double baseAccuracy = equipment.getBaseAccuracy();  // 武器基础精度

        // 距离影响（随着距离增加命中率降低）
        double distanceFactor = Math.max(0, 1 - (distance / equipment.getMaxRange()));

        // 天气影响（风速 + 能见度）
        double weatherFactor = 1 - (weather.getWindSpeed() * 0.01 + weather.getVisibilityFactor() * 0.01);

        // 综合命中率（武器精度 * 距离衰减 * 天气影响）
        return baseAccuracy * distanceFactor * weatherFactor;
    }


    /**
     * 计算冲击波超压（kPa）
     *
     * @param scaledDistance 缩比距离（Hopkinson-Cranz 定律）
     * @return 超压值（单位：kPa）
     */
    public static double calculateOverpressure(double scaledDistance) {
        if (scaledDistance <= 0) throw new IllegalArgumentException("缩比距离必须为正值");
        return 101.3 * (1 + 6.7 / Math.pow(scaledDistance, 2) - 0.27 / Math.pow(scaledDistance, 3));
    }

    /**
     * 计算比冲量（Pa·s）
     *
     * @param scaledDistance 缩比距离
     * @return 比冲量（Pa·s）
     */
    public static double calculateImpulse(double scaledDistance) {
        if (scaledDistance <= 0) throw new IllegalArgumentException("缩比距离必须为正值");
        return 180 / scaledDistance;
    }

    /**
     * 计算从射击点到目标点的方位角（单位：度，0度表示正北，顺时针方向增加）
     *
     * @param shooter 射击点
     * @param target  目标点
     * @return 方位角（单位：度）
     */
    public static double calculateFiringDirection(ShootingPoint shooter, ShootingPoint target) {
        double lat1 = Math.toRadians(shooter.getLatitude());
        double lon1 = Math.toRadians(shooter.getLongitude());
        double lat2 = Math.toRadians(target.getLatitude());
        double lon2 = Math.toRadians(target.getLongitude());

        double deltaLon = lon2 - lon1;
        double x = Math.sin(deltaLon) * Math.cos(lat2);
        double y = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
        double initialBearing = Math.atan2(x, y);
        double bearingDegrees = (Math.toDegrees(initialBearing) + 360) % 360;

        return bearingDegrees;
    }

    /**
     * 计算死区（Dead Zone），考虑俯仰角、打击方位角、以及避免重复/异常坐标。
     *
     * @param shootingPoint 射击点（我方/敌方中心）
     * @param equipment     武器装备（含 deadZone, pitchAngle, firingAngle 等）
     * @param area          作战区域（若需裁剪扇形到该区域，可在循环中判断 isInside）
     * @param targetPoint   用于计算方位角的对方中心点
     * @return 死区多边形（List<double[]>，每个数组 [lat, lon]），未做 BEM 转换
     */
    public static List<double[]> calculateDeadZone(
            ShootingPoint shootingPoint,
            Equipment equipment,
            Area area,
            ShootingPoint targetPoint
    ) {
        // 1) 俯仰角安全校验
        double pitchAngle = equipment.getPitchAngle();
        if (pitchAngle < 0 || pitchAngle > 90) {
            throw new IllegalArgumentException("俯仰角必须在 [0, 90] 之间");
        }

        // 2) 调整后死区射程
        double baseDeadZone = equipment.getDeadZone();
        double deadZoneRadius = adjustRangeByPitch(baseDeadZone, pitchAngle);
        // 若 deadZoneRadius < 1e-8，可视为 0；此时死区无意义，直接返回一个“带射击点本身”的空多边形
        if (deadZoneRadius < 1e-8) {
            return Collections.singletonList(
                    new double[]{shootingPoint.getLatitude(), shootingPoint.getLongitude()}
            );
        }

        // 3) 计算射击点 -> 目标点的方位角
        double azimuthAngle = calculateAzimuthAngle(shootingPoint, targetPoint);
        equipment.setFiringAngle(azimuthAngle);
        // 4) 扇形角度
        double halfFiringAngle = equipment.getFiringAngle() / 2.0;
        // 若 firingAngle 超出合理范围，可先做校正或抛异常
        if (equipment.getFiringAngle() <= 0 || equipment.getFiringAngle() > 360) {
            throw new IllegalArgumentException("firingAngle 不在 (0, 360] 范围内");
        }
        // 让 startAngle, endAngle 落在 [0,360)
        double startAngle = (azimuthAngle - halfFiringAngle + 360) % 360;
        double endAngle = (azimuthAngle + halfFiringAngle + 360) % 360;

        // 5) 以多边形近似扇形
        int numSides = 36;
        double angleStep = equipment.getFiringAngle() / numSides;

        List<double[]> polygon = new ArrayList<>();
        // 添加射击点
        polygon.add(new double[]{shootingPoint.getLatitude(), shootingPoint.getLongitude()});

        // 6) 顺时针/逆时针扫射
        if (endAngle < startAngle) {
            endAngle += 360;
        }
        double currentAngle = startAngle;
        for (int i = 0; i <= numSides; i++) {
            double angle = currentAngle + i * angleStep;
            // angle 可能超出 360，这里可做 angle = angle % 360
            double realAngle = (angle % 360);

            TargetPoint point = calculateTargetPoint(shootingPoint, realAngle, deadZoneRadius);

            // 若需要裁剪到 area，则判断 area.isInside(...)
            boolean inside = area.isInside(point.getLatitude(), point.getLongitude());
            if (inside) {
                polygon.add(new double[]{point.getLatitude(), point.getLongitude()});
            } else {
                // 可选择跳过 or 仅在边缘做插值
                polygon.add(new double[]{point.getLatitude(), point.getLongitude()});
            }
        }

        // 7) 闭合扇形
        polygon.add(new double[]{shootingPoint.getLatitude(), shootingPoint.getLongitude()});

        // 8) 去重/简化邻接点
        return deduplicatePolygon(polygon, /* optional epsilon */ 1e-10);
    }


    /**
     * 计算死界（Dead Sector）扇形区域多边形（[lat, lon]），并在过程中打印关键计算数据。
     * <p>
     * 说明：
     * 1) 中轴方位 = 射击点→目标点的方位角
     * 2) 射击扇形角度（firingAngle）为固定值
     * 3) 最小/最大射程 + 俯仰角修正
     * 4) 打印中间过程数据，用于调试观察
     * @param shootingPoint 我方射击点（含经纬度、高度等）
     * @param equipment     武器装备信息（含最小射程、最大射程、俯仰角、固定射击扇形角度等）
     * @param targetPoint   敌方目标点（用于计算射击中轴方位角）
     * @return 扇形区域的多边形坐标列表，格式为 [lat, lon]，首尾含射击点
     */
    public static List<double[]> calculateDeadSector(
            ShootingPoint shootingPoint,
            Equipment equipment,
            ShootingPoint targetPoint
    ) {
        // 1. 射击中轴方位角
        double azimuthAngle = UtilsV2.calculateAzimuthAngle(shootingPoint, targetPoint);
        System.out.println("[DEBUG] 射击中轴方位角(azimuthAngle) = " + azimuthAngle + " 度");

        // 2. 修正最小/最大射程
        double minRange = UtilsV2.adjustRangeByPitch(equipment.getMinRange(), equipment.getPitchAngle());
        double maxRange = UtilsV2.adjustRangeByPitch(equipment.getMaxRange(), equipment.getPitchAngle());
        System.out.println("[DEBUG] minRange = " + minRange + "；maxRange=" + maxRange);
        // 3. 扇形角度
        double firingAngle = equipment.getFiringAngle();
        double halfAngle = firingAngle / 2.0;
        double startAngle = (azimuthAngle - halfAngle + 360) % 360;
        double endAngle = (azimuthAngle + halfAngle + 360) % 360;
        if (endAngle < startAngle) {
            endAngle += 360;
        }
        System.out.println("[DEBUG] 射击扇形角度(firingAngle) = " + firingAngle + "°");
        System.out.println("[DEBUG] 扇形范围 startAngle=" + startAngle + "  endAngle=" + endAngle);
        // 4. 构建扇形多边形
        List<double[]> polygon = new ArrayList<>();
        // 4.1 将射击点添加
        double shooterLat = shootingPoint.getLatitude();
        double shooterLon = shootingPoint.getLongitude();
        polygon.add(new double[]{shooterLat, shooterLon});
        System.out.println("[DEBUG] 射击点(纬lat,经lon) = " + shooterLat + ", " + shooterLon);

        int numSides = 36;
        double step = firingAngle / numSides;

        // 4.2 最小射程弧
        for (int i = 0; i <= numSides; i++) {
            double angle = startAngle + i * step;
            double realAngle = angle % 360;

            TargetPoint pMin = UtilsV2.calculateTargetPoint(shootingPoint, realAngle, minRange);
            polygon.add(new double[]{pMin.getLatitude(), pMin.getLongitude()});
            System.out.println(String.format("[DEBUG] 最小i=%d, angle=%.2f, (lat=%.6f, lon=%.6f)", i, realAngle, pMin.getLatitude(), pMin.getLongitude()));
        }

        // 4.3 最大射程弧(逆序)
        for (int i = numSides; i >= 0; i--) {
            double angle = startAngle + i * step;
            double realAngle = angle % 360;

            TargetPoint pMax = UtilsV2.calculateTargetPoint(shootingPoint, realAngle, maxRange);
            polygon.add(new double[]{pMax.getLatitude(), pMax.getLongitude()});
            System.out.println(String.format("[DEBUG] 最大i=%d, angle=%.2f, (lat=%.6f, lon=%.6f)", i, realAngle, pMax.getLatitude(), pMax.getLongitude()));
        }

        // 4.4 再次加入射击点，闭合
        polygon.add(new double[]{shooterLat, shooterLon});
        System.out.println("[DEBUG] 再次加入射击点以闭合扇形，多边形顶点数=" + polygon.size());

        // 5. 返回多边形 (lat, lon) ！
        return polygon;
    }


    /**
     * 将多边形坐标中“相邻重复或非常接近”的点去除，
     * 避免 BEM 结果出现大量相同坐标。
     *
     * @param polygon [lat, lon] 的列表
     * @param epsilon 判断“相同点”的阈值（可设1e-8或1e-10等）
     * @return 去重后的多边形坐标列表
     */
    public static List<double[]> deduplicatePolygon(List<double[]> polygon, double epsilon) {
        if (polygon.size() < 2) return polygon;
        List<double[]> result = new ArrayList<>();
        double[] last = null;
        for (double[] p : polygon) {
            if (last == null) {
                last = p;
                result.add(p);
            } else {
                if (!isSamePoint(last, p, epsilon)) {
                    result.add(p);
                    last = p;
                }
            }
        }
        // 若首尾点相同，也可自行决定是否移除
        if (result.size() > 1) {
            double[] first = result.get(0);
            double[] tail = result.get(result.size() - 1);
            if (isSamePoint(first, tail, epsilon)) {
                // 保留首点 → 去掉尾巴
                result.remove(result.size() - 1);
            }
        }
        return result;
    }

    private static boolean isSamePoint(double[] a, double[] b, double eps) {
        return (Math.abs(a[0] - b[0]) < eps) && (Math.abs(a[1] - b[1]) < eps);
    }

    /**
     * 判断点与多边形边界是否相交（辅助方法）
     *
     * @param p1  线段起点
     * @param p2  线段终点
     * @param lat 点的纬度
     * @param lon 点的经度
     * @return true：相交；false：不相交
     */
    private static boolean isIntersecting(double[] p1, double[] p2, double lat, double lon) {
        double lat1 = p1[0], lon1 = p1[1];
        double lat2 = p2[0], lon2 = p2[1];

        if (lat1 == lat2) return false;  // 跳过水平边界

        if (lat < Math.min(lat1, lat2) || lat >= Math.max(lat1, lat2)) return false;

        double intersectLon = lon1 + (lat - lat1) * (lon2 - lon1) / (lat2 - lat1);
        return intersectLon > lon;
    }

    /**
     * 计算多边形的面积（单位：平方公里）
     * 使用 Shoelace 公式计算平面多边形的面积。
     *
     * @param geometry 多边形的坐标点列表
     * @return 计算得到的面积（单位：平方公里）
     */
    public static double calculateAreaFromCoordinates(List<double[]> geometry) {
        if (geometry == null || geometry.size() < 3) {
            return 0.0; // 少于三个点无法构成多边形
        }

        double area = 0.0;
        int n = geometry.size();

        for (int i = 0; i < n; i++) {
            double[] current = geometry.get(i);
            double[] next = geometry.get((i + 1) % n);
            area += current[1] * next[0] - next[1] * current[0];
        }

        // Shoelace 公式计算平面面积，并转换为地球表面积（单位：平方公里）
        return Math.abs(area / 2.0) * (Math.PI / 180.0) * Math.pow(EARTH_RADIUS, 2) / 1_000_000.0;
    }

    /**
     * 根据射击点的角度和距离计算目标点的经纬度
     *
     * @param shootingPoint 射击点
     * @param angle         射击角度（单位：度）
     * @param distance      距离（单位：米）
     * @return 目标点（包含经纬度）
     */
    public static TargetPoint calculateTargetPoint(ShootingPoint shootingPoint, double angle, double distance) {
        double angularDistance = distance / EARTH_RADIUS;
        double angleRad = Math.toRadians(angle);

        double lat1 = Math.toRadians(shootingPoint.getLatitude());
        double lon1 = Math.toRadians(shootingPoint.getLongitude());

        double targetLatRad = Math.asin(
                Math.sin(lat1) * Math.cos(angularDistance) +
                        Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(angleRad)
        );

        double targetLonRad = lon1 + Math.atan2(
                Math.sin(angleRad) * Math.sin(angularDistance) * Math.cos(lat1),
                Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(targetLatRad)
        );

        double targetLat = Math.toDegrees(targetLatRad);
        double targetLon = ((Math.toDegrees(targetLonRad) + 540) % 360) - 180;

        return new TargetPoint.Builder(targetLat, targetLon, true).build();
    }


    /**
     * 计算缩比距离 Z（用于爆炸影响计算）
     * Hopkinson-Cranz 缩比定律：Z = R / W^(1/3)
     *
     * @param distance        爆炸中心到目标点的距离（单位：米）
     * @param explosiveWeight 爆炸当量（单位：kg TNT）
     * @return 缩比距离 Z（单位：m/kg^(1/3)）
     */
    public static double calculateScaledDistance(double distance, double explosiveWeight) {
        if (explosiveWeight <= 0) {
            throw new IllegalArgumentException("爆炸当量必须为正数");
        }
        return distance / Math.cbrt(explosiveWeight); // 缩比距离 Z
    }

    /**
     * 计算动压（Dynamic Pressure）
     * 动压是冲击波超压的 30%，用于评估结构破坏和冲击效应。
     *
     * @param overpressure 冲击波超压（单位：kPa）
     * @return 动压（单位：kPa）
     */
    public static double calculateDynamicPressure(double overpressure) {
        if (overpressure < 0) {
            throw new IllegalArgumentException("超压值必须为非负数");
        }
        return overpressure * 0.3; // 动压为超压的 30%
    }

    /**
     * 将 TargetPoint 转换为 BEM 格式
     *
     * @param point 目标点
     * @return BEM 格式字符串（POINT）
     */
    public static String convertToBEMFormat(TargetPoint point) {
        return "POINT (" + point.getLongitude() + " " + point.getLatitude() + ")";
    }


    /**
     * 计算目标点列表形成区域的总面积（平方公里）
     *
     * @param targetPoints 目标点列表
     * @return 区域总面积（单位：平方公里）
     */
    public static double calculateAreaFromTargetPoints(List<TargetPoint> targetPoints) {
        if (targetPoints == null || targetPoints.size() < 3) {
            return 0.0;  // 少于3个点无法形成多边形
        }

        List<double[]> coordinates = new ArrayList<>();
        for (TargetPoint point : targetPoints) {
            coordinates.add(new double[]{point.getLatitude(), point.getLongitude()});
        }

        return calculateAreaFromCoordinates(coordinates);  // 调用 Shoelace 公式计算面积
    }

    /**
     * 计算命中概率分布（根据命中概率区间分类）
     *
     * @param shootZones        可射击目标点
     * @param shootingPoint     射击点
     * @param equipment         武器装备信息
     * @param weather           天气条件
     * @param probabilityRanges 命中概率区间（Map<String, double[]>）
     * @param targetPoint       目标点
     * @param area              作战区域（用于边界判断）
     * @return 分类后的命中概率区间（Map<String, List<ShootingPoint>>）
     */
    public static Map<String, List<ShootingPoint>> calculateHitProbabilityDistribution(
            List<ShootingPoint> shootZones, ShootingPoint shootingPoint, Equipment equipment,
            Weather weather, Map<String, double[]> probabilityRanges,
            ShootingPoint targetPoint, Area area) {

        Map<String, List<ShootingPoint>> probabilityZones = new HashMap<>();

        // 初始化命中概率区间
        for (String rangeName : probabilityRanges.keySet()) {
            probabilityZones.put(rangeName, new ArrayList<>());
        }

        // 动态计算打击方位角（射击点指向目标点的方向）
        double azimuthAngle = calculateAzimuthAngle(shootingPoint, targetPoint);

        // 定义射击扇形角度范围（基于武器射击方位角和射击角度）
        double halfFiringAngle = equipment.getFiringAngle() / 2.0;
        double minAngle = (azimuthAngle - halfFiringAngle + 360) % 360; // 射击扇形起始角
        double maxAngle = (azimuthAngle + halfFiringAngle + 360) % 360; // 射击扇形终止角

        // 定义死界扇形区域（考虑俯仰角调整射程范围）
        List<double[]> deadSector = calculateDeadSector(shootingPoint, equipment, targetPoint);
        Area deadSectorArea = new Area(deadSector, Area.AreaType.BATTLEFIELD);

        // 遍历射击区域中的每个点
        for (ShootingPoint point : shootZones) {
            // 检查点是否在死界扇形区域内
            boolean inDeadSector = deadSectorArea.isInside(point.getLatitude(), point.getLongitude());
            if (!inDeadSector) {
                continue; // 点不在有效射击范围内，跳过
            }

            // 计算目标点的方位角
            double targetAzimuth = calculateAzimuthAngle(shootingPoint, point);

            // 检查目标点是否在射击扇形范围内
            if (!isWithinAzimuthRange(targetAzimuth, minAngle, maxAngle)) {
                continue; // 点不在武器的射击方向范围内，跳过
            }

            // 计算到目标点的距离
            double distance = calculateDistance(shootingPoint, point);

            // 判断是否在武器的有效射程范围内
            if (distance < equipment.getMinRange() || distance > equipment.getMaxRange()) {
                continue;
            }

            // 计算命中概率（考虑距离、武器精度、天气影响）
            double probability = calculateHitProbability(distance, equipment, weather);

            // 分类到命中概率区间
            for (Map.Entry<String, double[]> entry : probabilityRanges.entrySet()) {
                double minProb = entry.getValue()[0];
                double maxProb = entry.getValue()[1];

                if (probability >= minProb && probability <= maxProb) {
                    probabilityZones.get(entry.getKey()).add(point);
                    break;
                }
            }

            // 更新点的命中概率
            point.setHitProbability(probability);
        }

        return probabilityZones;
    }


    /**
     * 判断目标点的方位角是否在射击扇形范围内
     *
     * @param targetAzimuth 目标点的方位角（单位：度）
     * @param minAngle      射击扇形的起始角度（单位：度）
     * @param maxAngle      射击扇形的终止角度（单位：度）
     * @return true 如果目标点的方位角在范围内；否则返回 false
     */
    private static boolean isWithinAzimuthRange(double targetAzimuth, double minAngle, double maxAngle) {
        if (minAngle <= maxAngle) {
            return targetAzimuth >= minAngle && targetAzimuth <= maxAngle;
        } else {
            // 跨越 0 度的情况
            return targetAzimuth >= minAngle || targetAzimuth <= maxAngle;
        }
    }

    /**
     * 解析格子边界
     *
     * @param geometry POLYGON 字符串
     * @return 边界数组 [minLat, maxLat, minLon, maxLon]
     */
    public static double[] parseGridBounds(String geometry) {
        geometry = geometry.replace("POLYGON ((", "").replace("))", "").trim(); // 移除多余字符
        String[] points = geometry.split(","); // 分割点
        double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = -Double.MAX_VALUE;

        for (String point : points) {
            String[] coords = point.trim().split(" ");
            double lon = Double.parseDouble(coords[0]); // 经度
            double lat = Double.parseDouble(coords[1]); // 纬度
            minLat = Math.min(minLat, lat);
            maxLat = Math.max(maxLat, lat);
            minLon = Math.min(minLon, lon);
            maxLon = Math.max(maxLon, lon);
        }

        return new double[]{minLat, maxLat, minLon, maxLon};
    }


    /**
     * 获取区域边界与连线的交点（最前沿点）
     *
     * @param area         区域（敌方或我方）
     * @param targetCenter 射击点（目标区域中心）
     * @return 交点坐标 [纬度, 经度]
     */
    public static double[] getFrontlineIntersectionPoint(Area area, ShootingPoint targetCenter) {
        List<double[]> boundary = area.getBoundaryPolygon();  // 区域边界（多边形）
        double[] areaCenter = {area.getCenterPoint().getLatitude(), area.getCenterPoint().getLongitude()};
        double[] targetPoint = {targetCenter.getLatitude(), targetCenter.getLongitude()};

        double[] intersectionPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < boundary.size() - 1; i++) {
            double[] edgeStart = boundary.get(i);
            double[] edgeEnd = boundary.get(i + 1);

            // ➡️ 确保连线的起点和终点不一样
            if (Arrays.equals(areaCenter, targetPoint)) {
                System.out.println("[WARN] 连线起点和终点相同，无法计算交点！");
                continue;
            }

            double[] intersection = calculateLineIntersection(areaCenter, targetPoint, edgeStart, edgeEnd);

            if (intersection != null) {
                double distance = UtilsV2.calculateDistance(areaCenter[0], areaCenter[1], intersection[0], intersection[1]);
                if (distance < minDistance) {
                    minDistance = distance;
                    intersectionPoint = intersection;
                }
            }
        }

        if (intersectionPoint == null) {
            System.out.println("[WARN] 未找到交点，检查多边形是否闭合或存在计算误差。");
        }
        return intersectionPoint;
    }


    /**
     * 计算目标的遮蔽性得分（0~1），返回遮挡点的坐标和高度
     *
     * @param shootingPoint 射击点
     * @param targetPoint   目标点
     * @param demData       DEM 地形数据
     * @return Map，包含遮蔽性得分和遮挡点列表
     */
    public static Map<String, Object> calculateCoverScoreWithDetails(ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Double>> blockedPoints = new ArrayList<>();

        double totalDistance = calculateDistance(shootingPoint, targetPoint);
        int steps = (int) Math.ceil(totalDistance / 10.0);
        int blockedCount = 0;

        for (int i = 1; i <= steps; i++) {
            double ratio = i / (double) steps;

            // 计算连线上的采样点
            double currentLat = shootingPoint.getLatitude() + (targetPoint.getLatitude() - shootingPoint.getLatitude()) * ratio;
            double currentLon = shootingPoint.getLongitude() + (targetPoint.getLongitude() - shootingPoint.getLongitude()) * ratio;

            double terrainHeight = getTerrainHeight(currentLat, currentLon, demData);
            double shootingLineHeight = shootingPoint.getHeight() + (targetPoint.getHeight() - shootingPoint.getHeight()) * ratio;

            // 判断是否被遮挡
            if (terrainHeight > shootingLineHeight) {
                blockedCount++;

                Map<String, Double> point = new HashMap<>();
                point.put("latitude", currentLat);
                point.put("longitude", currentLon);
                point.put("terrainHeight", terrainHeight);

                blockedPoints.add(point);
            }
        }

        double coverScore = (double) blockedCount / steps;

        result.put("coverScore", coverScore);
        result.put("blockedPoints", blockedPoints);

        return result;
    }


    /**
     * 计算目标的隐蔽性得分（0~1），返回遮挡点的坐标和高度
     *
     * @param shootingPoint 射击点
     * @param targetPoint   目标点
     * @param demData       DEM 地形数据
     * @return Map，包含隐蔽性得分和遮挡点列表
     */
    public static Map<String, Object> calculateConcealmentScoreWithDetails(ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Double>> concealedPoints = new ArrayList<>();

        double totalDistance = calculateDistance(shootingPoint, targetPoint);
        int steps = (int) Math.ceil(totalDistance / 10.0);
        int concealedCount = 0;

        for (int i = 1; i <= steps; i++) {
            double ratio = i / (double) steps;

            // 计算连线上的采样点
            double currentLat = shootingPoint.getLatitude() + (targetPoint.getLatitude() - shootingPoint.getLatitude()) * ratio;
            double currentLon = shootingPoint.getLongitude() + (targetPoint.getLongitude() - shootingPoint.getLongitude()) * ratio;

            double terrainHeight = getTerrainHeight(currentLat, currentLon, demData);
            double lineOfSightHeight = shootingPoint.getHeight() + (targetPoint.getHeight() - shootingPoint.getHeight()) * ratio;

            // 判断是否被遮挡
            if (terrainHeight > lineOfSightHeight) {
                concealedCount++;

                Map<String, Double> point = new HashMap<>();
                point.put("latitude", currentLat);
                point.put("longitude", currentLon);
                point.put("terrainHeight", terrainHeight);

                concealedPoints.add(point);
            }
        }

        double concealmentScore = (double) concealedCount / steps;

        result.put("concealmentScore", concealmentScore);
        result.put("concealedPoints", concealedPoints);

        return result;
    }


    /**
     * 获取射击路径上关联的 DEM 数据（严格基于射击路径）
     */
    public static List<Map<String, Object>> getPathDEMData(ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        List<Map<String, Object>> pathDEMData = new ArrayList<>();

        double totalDistance = calculateDistance(shootingPoint, targetPoint);
        int steps = (int) Math.ceil(totalDistance / 10.0);

        for (int i = 1; i <= steps; i++) {
            double ratio = i / (double) steps;
            double lat = shootingPoint.getLatitude() + (targetPoint.getLatitude() - shootingPoint.getLatitude()) * ratio;
            double lon = shootingPoint.getLongitude() + (targetPoint.getLongitude() - shootingPoint.getLongitude()) * ratio;

            Map<String, Object> grid = getClosestGrid(new ShootingPoint.Builder(lat, lon, true).build(), demData);
            if (grid != null && !pathDEMData.contains(grid)) {
                pathDEMData.add(grid);
            }
        }
        return pathDEMData;
    }



    /**
     * 从 DEM 数据中提取 BEM 格式数据
     */
    public static JSONArray extractBEMFromDEM(List<Map<String, Object>> demData) {
        JSONArray bemArray = new JSONArray();
        for (Map<String, Object> grid : demData) {
            bemArray.put(grid.get("geometry"));
        }
        return bemArray;
    }


    /**
     * 生成死区（Dead Zone）的 BEM 格式
     *
     * @param shootingPoint 射击点
     * @param equipment     武器装备
     * @param area          作战区域（用于边界判断）
     * @param targetPoint   目标点（用于计算打击方位角）
     * @return 死区的 BEM 格式 JSON 对象
     */
    public static JSONObject generateDeadZoneBEM(ShootingPoint shootingPoint, Equipment equipment, Area area, ShootingPoint targetPoint) {
        // 计算死区多边形
        List<double[]> deadZone = calculateDeadZone(shootingPoint, equipment, area, targetPoint);

        // 计算死区面积
        double deadZoneArea = calculateAreaFromCoordinates(deadZone);

        // 构建 JSON 报告
        JSONObject report = new JSONObject();
        report.put("deadZoneArea", String.format("%.6f 平方公里", deadZoneArea));
        report.put("deadZoneBEM", convertToBEMFormat(deadZone));

        return report;
    }

    /**
     * 对外暴露的方法：生成死界BEM，及面积信息。
     *
     * @param shooter   射击点
     * @param equipment 武器装备
     * @param target    用于计算方位角
     * @return JSON: { "deadSectorBEM": "...", "deadSectorArea": "xxx 平方公里" }
     */
    public static JSONObject generateDeadSectorBEM(ShootingPoint shooter, Equipment equipment, ShootingPoint target) {
        JSONObject result = new JSONObject();

        // 1) 生成死界扇形多边形（经纬度坐标）
        List<double[]> polygon = calculateDeadSector(shooter, equipment, target);

        // 2) 使用本地投影计算多边形面积
        double refLat = shooter.getLatitude();
        double refLon = shooter.getLongitude();
        double areaKm2 = UtilsV2.calculateAreaLocalProjectionKm2(polygon, refLat, refLon);

        // 3) 转换成 BEM 格式
        String bemString = UtilsV2.convertToBEMFormat(polygon);

        result.put("deadSectorBEM", bemString);
        result.put("deadSectorArea", String.format("%.6f 平方公里", areaKm2));
        return result;
    }


    /**
     * 使用“本地投影”将 [lat, lon] → [x, y](单位：米)
     *
     * @param polygon 多边形顶点（[lat, lon]）
     * @param refLat  参考纬度（可用射击点）
     * @param refLon  参考经度（可用射击点）
     * @return 该多边形的面积，单位：平方公里
     */
    public static double calculateAreaLocalProjectionKm2(List<double[]> polygon, double refLat, double refLon) {
        List<double[]> meterCoords = new ArrayList<>();
        double degToMeterLat = 111_000.0;
        double degToMeterLon = 111_000.0 * Math.cos(Math.toRadians(refLat));

        for (double[] latlon : polygon) {
            double dLat = latlon[0] - refLat;
            double dLon = latlon[1] - refLon;
            double x = dLon * degToMeterLon;
            double y = dLat * degToMeterLat;
            meterCoords.add(new double[]{x, y});
        }

        double areaM2 = shoelaceArea(meterCoords);
        return areaM2 / 1_000_000.0; // 转换为平方公里
    }


    /**
     * Shoelace 公式 (x,y) 计算多边形面积 (m²)
     */
    private static double shoelaceArea(List<double[]> coords) {
        double area = 0.0;
        int n = coords.size();
        for (int i = 0; i < n; i++) {
            double[] p1 = coords.get(i);
            double[] p2 = coords.get((i + 1) % n);
            area += p1[0] * p2[1] - p2[0] * p1[1];
        }
        return Math.abs(area) / 2.0;
    }


    /**
     * 获取 DEM 数据中的地形高度（使用 HashMap 快速定位）
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param demData   DEM 数据（使用网格索引）
     * @return 地形高度（米）
     */
    public static double getOptimizedTerrainHeight(double latitude, double longitude, Map<String, Double> demData) {
        String key = String.format("%.5f_%.5f", latitude, longitude);  // 格式化为键
        return demData.getOrDefault(key, 0.0);  // 快速查询，默认 0.0
    }

    /**
     * 根据多边形区域筛选 DEM 数据
     *
     * @param demData         DEM 数据（List<Map<String, Object>>）
     * @param boundaryPolygon 区域边界（多边形顶点坐标）
     * @return 区域内的 DEM 数据
     */
    public static List<Map<String, Object>> filterDEMDataWithinArea(List<Map<String, Object>> demData, List<double[]> boundaryPolygon) {

        for (int i = 0; i < Math.min(5, demData.size()); i++) {
            System.out.println("[DEBUG] DEM 数据点: " + demData.get(i));
        }


        List<Map<String, Object>> filteredData = new ArrayList<>();

        // 遍历 DEM 数据，判断每个网格是否在多边形区域内
        for (Map<String, Object> grid : demData) {
            double lat = (double) grid.get("centerLat");
            double lon = (double) grid.get("centerLon");

            if (isPointInPolygon(lat, lon, boundaryPolygon)) {
                filteredData.add(grid);
            }
        }
        return filteredData;
    }


    /**
     * 确保多边形闭合（首尾点一致）
     *
     * @param polygon 多边形顶点列表
     * @return 闭合后的多边形
     */
    public static List<double[]> ensurePolygonClosed(List<double[]> polygon) {
        if (polygon.isEmpty()) {
            throw new IllegalArgumentException("多边形点集不能为空");
        }

        // 转换为可变列表（避免 UnsupportedOperationException）
        List<double[]> closedPolygon = new ArrayList<>(polygon);

        double[] firstPoint = closedPolygon.get(0);
        double[] lastPoint = closedPolygon.get(closedPolygon.size() - 1);

        // 如果首尾点不同，则闭合多边形
        if (firstPoint[0] != lastPoint[0] || firstPoint[1] != lastPoint[1]) {
            closedPolygon.add(new double[]{firstPoint[0], firstPoint[1]});
        }

        return closedPolygon;
    }


    /**
     * 计算隐蔽距离（目标点在地形遮挡下不可视的距离）
     *
     * @param shooter 射击点
     * @param target  目标点
     * @param demData DEM 数据
     * @return 隐蔽距离（单位：米）
     */
    public static double calculateConcealmentDistance(ShootingPoint shooter, ShootingPoint target, List<Map<String, Object>> demData) {
        double totalDistance = calculateDistance(shooter, target);
        int steps = (int) Math.ceil(totalDistance / 10.0);
        double concealmentDistance = 0.0;

        for (int i = 1; i <= steps; i++) {
            double ratio = i / (double) steps;
            double lat = shooter.getLatitude() + (target.getLatitude() - shooter.getLatitude()) * ratio;
            double lon = shooter.getLongitude() + (target.getLongitude() - shooter.getLongitude()) * ratio;
            double terrainHeight = getTerrainHeight(lat, lon, demData);

            double lineHeight = shooter.getHeight() + (target.getHeight() - shooter.getHeight()) * ratio;

            if (terrainHeight > lineHeight) {
                concealmentDistance += totalDistance / steps;
            }
        }

        return concealmentDistance;
    }

    /**
     * 计算遮蔽深度（被地形遮挡的深度）
     *
     * @param shooter 射击点
     * @param target  目标点
     * @param demData DEM 数据
     * @return 遮蔽深度（单位：米）
     */
    public static double calculateCoverDepth(ShootingPoint shooter, ShootingPoint target, List<Map<String, Object>> demData) {
        double maxCoverDepth = 0.0;
        double totalDistance = calculateDistance(shooter, target);
        int steps = (int) Math.ceil(totalDistance / 10.0);

        for (int i = 1; i <= steps; i++) {
            double ratio = i / (double) steps;
            double lat = shooter.getLatitude() + (target.getLatitude() - shooter.getLatitude()) * ratio;
            double lon = shooter.getLongitude() + (target.getLongitude() - shooter.getLongitude()) * ratio;
            double terrainHeight = getTerrainHeight(lat, lon, demData);

            double lineHeight = shooter.getHeight() + (target.getHeight() - shooter.getHeight()) * ratio;

            if (terrainHeight > lineHeight) {
                double depth = terrainHeight - lineHeight;
                if (depth > maxCoverDepth) {
                    maxCoverDepth = depth;
                }
            }
        }

        return maxCoverDepth;
    }


    /**
     * 计算爆炸冲击波超压（考虑多介质和爆炸类型）
     *
     * @param distance      爆炸点到目标点的距离（单位：米）
     * @param explosiveMass 爆炸物TNT当量（单位：kg）
     * @param explosionType 爆炸类型（空气爆炸、地面爆炸、地下爆炸）
     * @param medium        爆炸介质（空气、岩石、混凝土）
     * @return 超压值（单位：MPa）
     */
    /*public static double calculateOverpressure(double distance, double explosiveMass, String explosionType, String medium) {
        // 1️⃣ 计算缩比距离 Z = R / W^(1/3)
        double scaledDistance = distance / Math.cbrt(explosiveMass);

        // 2️ 根据缩比距离计算基础超压值（MPa）
        double baseOverpressure;
        if (scaledDistance < 1.0) {
            baseOverpressure = 50.0 / scaledDistance;  // 强冲击
        } else if (scaledDistance < 10.0) {
            baseOverpressure = 5.0 / scaledDistance;   // 中等冲击
        } else {
            baseOverpressure = 0.5 / scaledDistance;   // 弱冲击
        }

        // 3️ 介质修正系数
        double mediumFactor = switch (medium.toLowerCase()) {
            case "空气" -> 1.0;
            case "岩石" -> 1.2;
            case "混凝土" -> 1.5;
            default -> 1.0;
        };

        // 4️  爆炸类型修正（考虑空气、地面、地下爆炸）
        double explosionFactor = switch (explosionType.toLowerCase()) {
            case "空气爆炸" -> 0.8;
            case "地面爆炸" -> 1.0;
            case "地下爆炸" -> 1.3;
            default -> 1.0;
        };

        // 5️ 计算最终超压值
        return baseOverpressure * mediumFactor * explosionFactor;
    }*/

    /**
     * 计算冲击波随时间和距离的超压衰减（动态曲线）
     *
     * @param explosiveMass 爆炸物TNT当量（kg）
     * @param maxDistance   最大计算距离（m）
     * @return 超压衰减曲线（Map：距离 -> 超压）
     */
    public static Map<Double, Double> calculateDynamicOverpressure(double explosiveMass, double maxDistance) {
        Map<Double, Double> overpressureCurve = new LinkedHashMap<>();

        for (double distance = 1.0; distance <= maxDistance; distance += 1.0) {
            double overpressure = calculateOverpressure(distance, explosiveMass, "地面爆炸", "空气");
            overpressureCurve.put(distance, overpressure);
        }

        return overpressureCurve;
    }

    /**
     * 计算破片和抛掷物的飞行距离及破坏效果。
     *
     * @param explosivePower
     * @param fragmentMass
     * @return
     */
    public static double calculateFragmentDamageRange(double explosivePower, double fragmentMass) {
        double initialVelocity = Math.sqrt(2 * explosivePower * 4184 / fragmentMass);
        double airResistance = 0.47; // 球形阻力系数
        double airDensity = 1.225;   // kg/m³

        return (Math.pow(initialVelocity, 2) / (airResistance * airDensity * 9.81)); // 单位：米
    }
















    public static double calculateTime(double distancenlncrement){
        double timeSeconds = distancenlncrement / 343.0;//声速
        return timeSeconds * 1000;
    }
    /**
     * 计算两点之间的直线距离（米）
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 计算两点之间的直线距离（单位：米）
     */
    public static double calculateDistance2(double lat1, double lon1, double lat2, double lon2) {
        double dLon = lon2 - lon1;
        double distance = EARTH_RADIUS * Math.acos(
                Math.sin(lat1) * Math.sin(lat2)
                        + Math.cos(lat1) * Math.cos(lat2) * Math.cos(dLon) );
        return distance;
    }


    /**
     * 计算射击角度（俯仰角 + 方位角）
     *
     * @param shootingPoint 射击点
     * @param targetPoint 目标点
     * @return 俯仰角（单位：度）
     */
    public static double calculateShootingAngle(ShootingPoint shootingPoint, ShootingPoint targetPoint) {
        double dx = targetPoint.getLongitude() - shootingPoint.getLongitude();
        double dy = targetPoint.getLatitude() - shootingPoint.getLatitude();
        double dz = targetPoint.getHeight() - shootingPoint.getHeight();

        double horizontalDistance = Math.sqrt(dx * dx + dy * dy);
        return Math.toDegrees(Math.atan2(dz, horizontalDistance));
    }





    /**
     * 将坐标点集合转换为 BEM 格式
     *
     * @param coordinates 坐标点集合
     * @return BEM 格式字符串
     */
    public static String convertToBEMFormat(List<double[]> coordinates) {
        StringBuilder bem = new StringBuilder("POLYGON ((");
        for (double[] coord : coordinates) {
            bem.append(coord[1]).append(" ").append(coord[0]).append(", ");
        }
        bem.append(coordinates.get(0)[1]).append(" ").append(coordinates.get(0)[0]).append("))");
        return bem.toString();
    }

    /**
     * 计算死区（Dead Zone），并在控制台打印输出调试信息
     * 死区是指在死界距离（Dead Zone）范围内无法攻击的区域。
     *
     * @param shootingPoint 射击点
     * @param equipment     武器装备（包含死界距离等信息）
     * @param area          作战区域（用于边界判断）
     * @return 死区的几何坐标列表（近似为圆形）
     */
    public static List<double[]> calculateDeadZone(ShootingPoint shootingPoint, Equipment equipment, Area area) {

        double deadZoneRadius = equipment.getDeadZone(); // 获取武器的死区半径（米）
        System.out.println("[DEBUG] 武器死区半径: " + deadZoneRadius + " meters"+";作战区域"+area.toString());

        int numSides = 36; // 用多边形近似圆形，边数越多越精确
        List<double[]> deadZonePolygon = new ArrayList<>();

        for (int i = 0; i < numSides; i++) {
            // 计算每个顶点的角度
            double angle = i * (360.0 / numSides);
            // 计算该角度下，距离射击点deadZoneRadius处的目标点坐标
            TargetPoint point = calculateTargetPoint(shootingPoint, angle, deadZoneRadius);

            // 判断该坐标是否在作战区域内
            boolean inside = area.isInside(point.getLatitude(), point.getLongitude());
            if (inside) {
                deadZonePolygon.add(new double[]{point.getLatitude(), point.getLongitude()});
            }
        }

        return deadZonePolygon;
    }


    /**
     * 解析 POLYGON 字符串为坐标点列表
     * 解析格式示例：POLYGON ((121.4 25.0, 121.5 25.1, 121.6 25.0, 121.4 25.0))
     *
     * @param geometry POLYGON 格式字符串
     * @return 坐标点列表，每个点是 double[] 格式 [纬度, 经度]
     */
    public static List<double[]> parsePolygonCoordinates(String geometry) {
        List<double[]> coordinates = new ArrayList<>();

        if (geometry == null || !geometry.startsWith("POLYGON")) {
            throw new IllegalArgumentException("无效的 POLYGON 数据: " + geometry);
        }

        // 移除 POLYGON 前缀和多余括号
        geometry = geometry.replace("POLYGON ((", "").replace("))", "").trim();

        // 按逗号分割坐标点
        String[] points = geometry.split(",");

        for (String point : points) {
            String[] latLon = point.trim().split(" ");
            double lon = Double.parseDouble(latLon[0]);  // 经度
            double lat = Double.parseDouble(latLon[1]);  // 纬度
            coordinates.add(new double[]{lat, lon});
        }

        return coordinates;
    }

    /**
     * 判断点是否在多边形内（射线法）
     *
     * @param lat             点的纬度
     * @param lon             点的经度
     * @param boundaryPolygon 区域边界（多边形）
     * @return true：点在多边形内；false：点在多边形外
     */
    private static boolean isPointInPolygon(double lat, double lon, List<double[]> boundaryPolygon) {
        int intersectCount = 0;
        int n = boundaryPolygon.size();

        for (int i = 0; i < n - 1; i++) {
            double[] p1 = boundaryPolygon.get(i);
            double[] p2 = boundaryPolygon.get(i + 1);

            // 判断射线是否与多边形边界相交
            if (isIntersecting(p1, p2, lat, lon)) {
                intersectCount++;
            }
        }

        // 奇数次相交 -> 点在多边形内；偶数次相交 -> 点在多边形外
        return (intersectCount % 2) == 1;
    }

    /**
     * 计算死界（Dead Sector）扇形区域
     * 死界是根据武器发射方向和射程限制计算的扇形区域
     *
     * @param shootingPoint 射击点
     * @param weapon        武器装备（包含最大射程、最小射程、射击角度）
     * @return 死界扇形区域的多边形坐标点
     */
    public static List<double[]> calculateDeadSector(ShootingPoint shootingPoint, Equipment weapon) {
        double minRange = weapon.getMinRange();  // 最小射程
        double maxRange = weapon.getMaxRange();  // 最大射程
        double firingAngle = weapon.getFiringAngle();  // 射击扇形角度（度）

        // 定义扇形角度范围（中心朝正北方向）
        double startAngle = -firingAngle / 2;  // 起始角度
        double endAngle = firingAngle / 2;     // 结束角度

        int numSides = 36;  // 扇形分段数，越多越平滑
        double angleStep = firingAngle / numSides;

        List<double[]> deadSectorPolygon = new ArrayList<>();

        // 添加射击点（扇形顶点）
        deadSectorPolygon.add(new double[]{shootingPoint.getLatitude(), shootingPoint.getLongitude()});

        // 计算最小射程弧线（死区内部）
        for (int i = 0; i <= numSides; i++) {
            double angle = Math.toRadians(startAngle + i * angleStep);
            TargetPoint innerPoint = calculateTargetPoint(shootingPoint, Math.toDegrees(angle), minRange);
            deadSectorPolygon.add(new double[]{innerPoint.getLatitude(), innerPoint.getLongitude()});
        }

        // 计算最大射程弧线（死界外部）
        for (int i = numSides; i >= 0; i--) {
            double angle = Math.toRadians(startAngle + i * angleStep);
            TargetPoint outerPoint = calculateTargetPoint(shootingPoint, Math.toDegrees(angle), maxRange);
            deadSectorPolygon.add(new double[]{outerPoint.getLatitude(), outerPoint.getLongitude()});
        }

        // 闭合扇形
        deadSectorPolygon.add(new double[]{shootingPoint.getLatitude(), shootingPoint.getLongitude()});

        return deadSectorPolygon;
    }

    /**
     * 计算两条线段是否相交，并返回交点
     *
     * @param line1Start 第1条线段的起点 [纬度, 经度]
     * @param line1End   第1条线段的终点 [纬度, 经度]
     * @param line2Start 第2条线段的起点 [纬度, 经度]
     * @param line2End   第2条线段的终点 [纬度, 经度]
     * @return 交点 [纬度, 经度]；如果不相交，返回 null
     */
    public static double[] calculateLineIntersection(double[] line1Start, double[] line1End, double[] line2Start, double[] line2End) {
        double x1 = line1Start[1], y1 = line1Start[0];
        double x2 = line1End[1], y2 = line1End[0];
        double x3 = line2Start[1], y3 = line2Start[0];
        double x4 = line2End[1], y4 = line2End[0];

        double denominator = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);



        if (denominator == 0) {
            System.out.println("[WARN] 两条线段平行或重合，无法计算交点。");
            return null; // 平行或重合
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denominator;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denominator;

        if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
            double intersectionX = x1 + ua * (x2 - x1);
            double intersectionY = y1 + ua * (y2 - y1);
            return new double[]{intersectionY, intersectionX};
        }

        return null;
    }

    /**
     * 获取最接近的格子
     *
     * @param targetPoint 目标点
     * @param validGrids DEM 数据列表
     * @return 最接近的格子
     */
    public static Map<String, Object> getClosestGrid(ShootingPoint targetPoint, List<Map<String, Object>> validGrids) {
        double closestDistance = Double.MAX_VALUE;
        Map<String, Object> closestGrid = null;

        for (Map<String, Object> grid : validGrids) {
            double centerLat = ((Number) grid.get("centerLat")).doubleValue();
            double centerLon = ((Number) grid.get("centerLon")).doubleValue();
            double distance = calculateDistance(targetPoint.getLatitude(), targetPoint.getLongitude(), centerLat, centerLon);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestGrid = grid;
            }
        }

        return closestGrid;
    }

    /**
     * 计算爆炸冲击波的峰值压力。
     * @param distance      爆炸点到目标点的距离（单位：米）
     * @param explosivePower
     * @return
     */
    public static double calculatePeakPressure(double distance, double explosivePower) {
        double scaledDistance = distance / Math.cbrt(explosivePower);
        return 1000 / Math.pow(scaledDistance, 2.5); // 单位：MPa
    }

    /**
     * 计算爆炸冲击波超压（考虑多介质和爆炸类型）
     *
     * @param distance      爆炸点到目标点的距离（单位：米）
     * @param explosiveMass 爆炸物TNT当量（单位：kg）
     * @param explosionType 爆炸类型（空气爆炸、地面爆炸、地下爆炸）
     * @param medium        爆炸介质（空气、岩石、混凝土）
     * @return 超压值（单位：MPa）
     */
    public static double calculateOverpressure(double distance, double explosiveMass, String explosionType, String medium) {
        //判断当前爆炸点 不算目标距离 默认为 0.1
        if(distance == 0.0){
            distance = 0.1;
        }
        // 1️⃣ 计算缩比距离 Z = R / W^(1/3)
        double scaledDistance = distance / Math.cbrt(explosiveMass);

        // 2️⃣ 根据缩比距离计算基础超压值（MPa）
        double baseOverpressure;
        if (scaledDistance < 1.0) {
            baseOverpressure = 50.0 / scaledDistance;  // 强冲击
        } else if (scaledDistance < 10.0) {
            baseOverpressure = 5.0 / scaledDistance;   // 中等冲击
        } else {
            baseOverpressure = 0.5 / scaledDistance;   // 弱冲击
        }
        return baseOverpressure * 1.0 * 1.0 ;//* mediumFactor * explosionFactor;
    }

    public static double calculateRockPenetrationDepth(double explosiveMass,double overpressure,String rockType){
        double rockDensity;
        switch (rockType){
            case "花岗岩":
                rockDensity = 2.7;
                break;
            case "砂岩":
                rockDensity = 2.2;
                break;
            default:
                rockDensity = 2.5;
        }
        return (explosiveMass * 4148) / (rockDensity * 1e6 * overpressure);
    }



    /**
     * 计算破片最大射程（简化弹道模型-数值迭代版本）。
     *
     * @param explosivePower 炸药能量当量 (kcal)，比如 25 kg TNT 可以约略转换成 kcal；或者直接在外部折算成 kJ/kcal
     * @param fragmentMass   破片质量 (kg)
     * @param cD            阻力系数（取 0.4~1.0 之间，视形状而定）
     * @param crossSectionArea 破片迎风面积 (m^2)，需根据破片外形大致估算
     * @param energyFactor  能量分配系数（0~1），假设只有这部分能量传递给破片
     * @param launchAngle   发射角度 (度)，若要计算最大射程，可尝试 30°~45°
     * @return 破片水平射程 (米)
     */
    public static double calculateRange(
            double explosivePower,
            double fragmentMass,
            double cD,
            double crossSectionArea,
            double energyFactor,
            double launchAngle
    ) {
        // 1. 炸药能量(焦耳)：explosivePower 以 kcal 为单位 -> 乘以 4184 转成 J
        double totalEnergyJoules = explosivePower * 4184;

        // 2. 仅将一部分能量分配给破片 (energyFactor)
        double fragmentEnergy = totalEnergyJoules * energyFactor;

        // 3. 计算破片初速 v0 = sqrt(2E/m)
        double initialVelocity = Math.sqrt((2 * fragmentEnergy) / fragmentMass);

        // 4. 根据发射角计算初始速度在 x, y 方向上的分量
        double angleRad = Math.toRadians(launchAngle);
        double vX = initialVelocity * Math.cos(angleRad);
        double vY = initialVelocity * Math.sin(angleRad);

        // 5. 迭代模拟运动
        double x = 0.0;  // 水平位置
        double y = 0.0;  // 垂直高度
        double dt = 0.01; // 时间步长，秒
        while (true) {
            // 当前速度大小
            double v = Math.sqrt(vX * vX + vY * vY);

            // 计算阻力大小 = 1/2 * Cd * rho * A * v^2
            // 方向与速度矢量相反
            double drag = 0.5 * cD * AIR_DENSITY * crossSectionArea * v * v;

            // 分解阻力在 x, y 方向上的分量
            // 注意速度为 0 时要避免除以 0，若 v=0，可直接 break
            if (v < 1e-8) {
                break;
            }
            double dragX = drag * (vX / v);
            double dragY = drag * (vY / v);

            // 计算加速度 (牛顿第二定律 F = m*a)
            double ax = -dragX / fragmentMass;
            double ay = - GRAVITY - (dragY / fragmentMass);

            // 更新速度
            vX += ax * dt;
            vY += ay * dt;

            // 更新位置
            x += vX * dt;
            y += vY * dt;

            // 若破片落到地面以下，结束模拟
            if (y <= 0.0) {
                break;
            }
        }

        return x;  // 返回水平位移
    }

}
