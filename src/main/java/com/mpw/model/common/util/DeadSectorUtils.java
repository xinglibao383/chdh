package com.mpw.model.common.util;

import com.mpw.model.chfx.domain.model.Equipment;
import com.mpw.model.chfx.domain.model.ShootingPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeadSectorUtils {
    private static final double EARTH_RADIUS = 6371 * 1000;  // 地球半径（米）
    private static final double GRAVITY = 9.81;              // 重力加速度（m/s²）

    public static List<double[]> calculateDeadSector(ShootingPoint shootingPoint, Equipment equipment, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        double azimuthAngle = UtilsV2.calculateAzimuthAngle(shootingPoint, targetPoint);
        equipment.setFiringAngle(azimuthAngle);
        double maxRange = UtilsV2.adjustRangeByPitch(equipment.getMaxRange(), equipment.getPitchAngle());
        double firingAngle = equipment.getFiringAngle();
        double halfAngle = firingAngle / 2.0;
        double start = (azimuthAngle - halfAngle + 360) % 360;
        int numSides = 36;
        double angleStep = firingAngle / numSides;
        List<double[]> doubles = new ArrayList<>();
        for (int i = 0; i < numSides; i++) {
            double angle = start + i * angleStep;
            List<Map<String, Object>> blockedPoints = calculateDeadSectorLineOfSightDetailed(shootingPoint, angle, maxRange, demData, equipment);
            for (Map<String, Object> point : blockedPoints) {
                doubles.add(new double[] {(double) point.get("latitude"), (double) point.get("longitude")});
            }
        }
        return doubles;
    }

    public static List<Map<String, Object>> calculateDeadSectorLineOfSightDetailed(ShootingPoint shootingPoint, double angle, double distance, List<Map<String, Object>> demData, Equipment equipment) {
        List<Map<String, Object>> points = new ArrayList<>();
        int steps = (int) Math.ceil(distance / 10.0);
        for (int i = 0; i < steps; i++) {
            double ratio = i / (double) steps;
            double currdistance = distance * ratio;
            ShootingPoint point = calculateDeadSectorTargetPoint(shootingPoint, angle, currdistance);
            double terrainH = UtilsV2.getTerrainHeight(point.getLatitude(), point.getLongitude(), demData);
            double flightH = calculateDeadSectorParaH(shootingPoint, currdistance, equipment);
            if (terrainH > flightH) {
                Map<String, Object> blockedPoint = new HashMap<>();
                blockedPoint.put("terrainHeight", terrainH);
                blockedPoint.put("flightHeight", flightH);
                blockedPoint.put("latitude", point.getLatitude());
                blockedPoint.put("longitude", point.getLongitude());
                blockedPoint.put("distanceFromShooter", currdistance);
                blockedPoint.put("demData", UtilsV2.getClosestGrid(point, demData));
                points.add(blockedPoint);
            }
        }
        return points;
    }

    public static ShootingPoint calculateDeadSectorTargetPoint(ShootingPoint shootingPoint, double angle, double distance) {
        double angular = distance / EARTH_RADIUS;
        double angleRad = Math.toRadians(angle);
        double lat1 = Math.toRadians(shootingPoint.getLatitude());
        double lng1 = Math.toRadians(shootingPoint.getLongitude());
        double targetlat = Math.asin(Math.sin(lat1) * Math.cos(angular) + Math.cos(lat1) * Math.sin(angular) * Math.cos(angleRad));

        double targetLon = lng1 + Math.atan2(Math.sin(angleRad) * Math.sin(angular) * Math.cos(lat1), Math.cos(angular) - Math.sin(lat1) * Math.sin(targetlat));
        return new ShootingPoint.Builder(Math.toDegrees(targetlat), Math.toDegrees(targetLon), true).build();
    }

    public static double calculateDeadSectorParaH(ShootingPoint shootingPoint, double distance, Equipment equipment) {
        double inital = Math.sqrt(equipment.getMaxRange() * GRAVITY);
        double angle = Math.toRadians(equipment.getPitchAngle());
        double time = distance / (inital * Math.cos(angle));
        return shootingPoint.getHeight() + inital * Math.sin(angle) * time - 0.5 * GRAVITY * time * time;
    }
}
