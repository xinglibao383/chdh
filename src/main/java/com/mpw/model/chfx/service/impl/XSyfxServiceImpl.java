package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.entity.DemCsv;
import com.mpw.model.chfx.domain.vo.RangeVO;
import com.mpw.model.chfx.mapper.XDemCsvMapper;
import com.mpw.model.common.util.GeoUtil;
import com.mpw.model.common.util.PointUtil;
import com.mpw.model.common.util.ReportGeneratorV2;
import com.mpw.model.common.util.UtilsV2;
import com.mpw.model.chfx.constants.TrajectoryType;
import com.mpw.model.chfx.constants.WeaponStatus;
import com.mpw.model.chfx.constants.WeaponType;
import com.mpw.model.chfx.domain.dto.EffDTO;
import com.mpw.model.chfx.domain.dto.SHideDTO;
import com.mpw.model.chfx.domain.dto.XDeadAreaDTO;
import com.mpw.model.chfx.domain.entity.XWeapon;
import com.mpw.model.chfx.mapper.XWeaponMapper;
import com.mpw.model.chfx.domain.model.Area;
import com.mpw.model.chfx.domain.model.Equipment;
import com.mpw.model.chfx.domain.model.ShootingPoint;
import com.mpw.model.chfx.domain.model.Weather;
import com.mpw.model.chfx.service.IXSyfxService;
import com.mpw.model.chfx.domain.vo.DeadVO;
import com.mpw.model.chfx.domain.vo.EffVO;
import com.mpw.model.chfx.domain.vo.SHideVO;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class XSyfxServiceImpl implements IXSyfxService {

    @Autowired
    private XDemCsvMapper xDemCsvMapper;

    @Autowired
    private XWeaponMapper xWeaponMapper;

    @Override
    public DeadVO dead(XDeadAreaDTO xDeadAreaDTO) {
        checkArea(xDeadAreaDTO.getEnemyArea());
        checkArea(xDeadAreaDTO.getFriendlyArea());

        DeadVO deadVO = new DeadVO();
        String fullFightArea = getTotalArea();
        try {
            Coordinate[] coordinates1 = GeoUtil.getGeometry(fullFightArea).getCoordinates();
            List<double[]> battlefieldPolygon = new ArrayList<>();
            for (Coordinate coordinate : coordinates1) {
                battlefieldPolygon.add(new double[]{coordinate.y, coordinate.x});
            }
            Area battlefieldArea = new Area(battlefieldPolygon, Area.AreaType.BATTLEFIELD);

            Coordinate[] coordinates2 = GeoUtil.getGeometry(xDeadAreaDTO.getFriendlyArea()).getCoordinates();
            List<double[]> friendlyPolygon = new ArrayList<>();
            for (Coordinate coordinate : coordinates2) {
                friendlyPolygon.add(new double[]{coordinate.y, coordinate.x});
            }
            Area friendlyArea = new Area(friendlyPolygon, Area.AreaType.FRIENDLY);

            Coordinate[] coordinates3 = GeoUtil.getGeometry(xDeadAreaDTO.getEnemyArea()).getCoordinates();
            List<double[]> enemyPolygon = new ArrayList<>();
            for (Coordinate coordinate : coordinates3) {
                enemyPolygon.add(new double[]{coordinate.y, coordinate.x});
            }
            Area enemyArea = new Area(enemyPolygon, Area.AreaType.ENEMY);

            // ✅ 检查敌我区域是否包含在总体作战区域内
            if (!(battlefieldArea.contains(friendlyArea) && battlefieldArea.contains(enemyArea))) {
                System.err.println("[ERROR] 敌我区域未完全包含在总体作战区域内！");
                return deadVO;
            }

            // ✅ 计算总体作战区域面积
            double battlefieldAreaSize = UtilsV2.calculateAreaFromCoordinates(battlefieldArea.getBoundaryPolygon());
            deadVO.setTotalArea(battlefieldAreaSize);
            // System.out.printf("[INFO] 总体作战区域面积: %.6f 平方公里\n", battlefieldAreaSize);
            // ✅ 计算总体作战区域面积
            double friendlyAreaSize = UtilsV2.calculateAreaFromCoordinates(friendlyArea.getBoundaryPolygon());
            deadVO.setFriendlyTotalArea(friendlyAreaSize);
            // System.out.printf("[INFO] 我方作战区域面积: %.6f 平方公里\n", friendlyAreaSize);

            // ✅ 计算总体作战区域面积
            double enemyAreaSize = UtilsV2.calculateAreaFromCoordinates(enemyArea.getBoundaryPolygon());
            deadVO.setEnemyTotalArea(enemyAreaSize);
            // System.out.printf("[INFO] 敌方作战区域面积: %.6f 平方公里\n", enemyAreaSize);

            // 4️⃣ 获取敌我区域的中心点
            ShootingPoint friendlyCenter = friendlyArea.getCenterPoint();
            ShootingPoint enemyCenter = enemyArea.getCenterPoint();

            // 5️⃣ 计算敌我区域边界与中心线的交点（最前沿射击点）
            double[] friendlyIntersection = UtilsV2.getFrontlineIntersectionPoint(friendlyArea, enemyCenter);
            double[] enemyIntersection = UtilsV2.getFrontlineIntersectionPoint(enemyArea, friendlyCenter);

            // 6️⃣ 设置敌我射击点（A 和 B 点）
            ShootingPoint friendlyShootingPoint = new ShootingPoint.Builder(friendlyIntersection[0], friendlyIntersection[1], true)
                    .height(getBaseHeight(friendlyIntersection[1], friendlyIntersection[0]))
                    .build();
            ShootingPoint enemyShootingPoint = new ShootingPoint.Builder(enemyIntersection[0], enemyIntersection[1], true)
                    .height(getBaseHeight(enemyIntersection[1], enemyIntersection[0]))
                    .build();

            Equipment friendlyWeapon = getWeapon(xDeadAreaDTO.getFriendlyWeaponId(), xDeadAreaDTO.getFriendlyPitchAngle(), Equipment.PointRole.FRIENDLY);
            Equipment enemyWeapon = getWeapon(xDeadAreaDTO.getEnemyWeaponId(), xDeadAreaDTO.getEnemyPitchAngle(), Equipment.PointRole.ENEMY);

            List<double[]> friendlyDeadZone = UtilsV2.calculateDeadZone(friendlyShootingPoint, enemyWeapon, battlefieldArea, enemyShootingPoint);
            deadVO.setFriendlyDeadZone(friendlyDeadZone);
            deadVO.setFriendlyDeadZoneArea(UtilsV2.calculateAreaFromCoordinates(friendlyDeadZone));

            List<double[]> friendlyDeadZoneSector = UtilsV2.calculateDeadSector(friendlyShootingPoint, enemyWeapon, enemyShootingPoint);
            // List<double[]> friendlyDeadZoneSector = DeadSectorUtils.calculateDeadSector(friendlyShootingPoint, enemyWeapon, enemyShootingPoint, getDemData(xDeadAreaDTO.getFriendlyArea()));
            // List<double[]> friendlyDeadZoneSector = DeadSectorUtils.calculateDeadSector(friendlyShootingPoint, enemyWeapon, enemyShootingPoint, getDemData(xDeadAreaDTO.getEnemyArea()));
            deadVO.setFriendlyDeadZoneSector(friendlyDeadZoneSector);
            deadVO.setFriendlyDeadZoneSectorArea(UtilsV2.calculateAreaFromCoordinates(friendlyDeadZoneSector));

            List<double[]> enemyDeadZone = UtilsV2.calculateDeadZone(enemyShootingPoint, friendlyWeapon, battlefieldArea, friendlyShootingPoint);
            deadVO.setEnemyDeadZone(enemyDeadZone);
            deadVO.setEnemyDeadZoneArea(UtilsV2.calculateAreaFromCoordinates(enemyDeadZone));

            List<double[]> enemyDeadZoneSector = UtilsV2.calculateDeadSector(enemyShootingPoint, friendlyWeapon, friendlyShootingPoint);
            // List<double[]> enemyDeadZoneSector = DeadSectorUtils.calculateDeadSector(enemyShootingPoint, friendlyWeapon, friendlyShootingPoint, getDemData(xDeadAreaDTO.getEnemyArea()));
            // List<double[]> enemyDeadZoneSector = DeadSectorUtils.calculateDeadSector(enemyShootingPoint, friendlyWeapon, friendlyShootingPoint, getDemData(xDeadAreaDTO.getFriendlyArea()));
            deadVO.setEnemyDeadZoneSector(enemyDeadZoneSector);
            deadVO.setEnemyDeadZoneSectorArea(UtilsV2.calculateAreaFromCoordinates(enemyDeadZoneSector));

            deadVO.setFriendlyDeadZoneAreaRatio(deadVO.getFriendlyDeadZoneArea() / deadVO.getFriendlyTotalArea());
            deadVO.setEnemyDeadZoneAreaRatio(deadVO.getEnemyDeadZoneArea() / deadVO.getEnemyTotalArea());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            boolean has = hasNull(deadVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return deadVO;
    }

    public static boolean hasNull(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return true;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EffVO eff(EffDTO effDTO) {
        EffVO effVO = new EffVO();
        try {
            ShootingPoint friendlyShootingPoint = new ShootingPoint.Builder(effDTO.getLat(), effDTO.getLng(), true)
                    .height(effDTO.getHeight() + getBaseHeight(effDTO.getLng(), effDTO.getLat()))
                    .build();

            Weather weather = new Weather(3.0, "东南风", 25.0, 60.0);

            analyzeFireEffectiveness(effVO, friendlyShootingPoint, null, getWeapon(effDTO.getId()), weather, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            boolean has = hasNull(effVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return effVO;
    }

    public static void analyzeFireEffectiveness(EffVO effVO, ShootingPoint shooter, ShootingPoint target, Equipment weapon, Weather weather, List<Map<String, Object>> demData) {
        Map<String, List<double[]>> hitZones = ReportGeneratorV2.calculateHitZones(shooter, weapon, weather, demData);

        double highHitArea = UtilsV2.calculateAreaFromCoordinates(hitZones.get("高命中区"));
        double mediumHitArea = UtilsV2.calculateAreaFromCoordinates(hitZones.get("中命中区"));
        double lowHitArea = UtilsV2.calculateAreaFromCoordinates(hitZones.get("低命中区"));

        effVO.setHighArea(highHitArea);
        effVO.setHighAreaRange(UtilsV2.convertToBEMFormat(hitZones.get("高命中区")));

        effVO.setMediumArea(mediumHitArea);
        effVO.setMediumAreaRange(UtilsV2.convertToBEMFormat(hitZones.get("中命中区")));

        effVO.setLowArea(lowHitArea);
        effVO.setLowAreaRange(UtilsV2.convertToBEMFormat(hitZones.get("低命中区")));
    }


    @Override
    public SHideVO hide(SHideDTO hideDTO) {
        SHideVO sHideVO = new SHideVO();
        try {
            List<Map<String, Object>> demData = getDemData();
            ShootingPoint friendlyShootingPoint = new ShootingPoint.Builder(hideDTO.getFriendlyLat(), hideDTO.getFriendlyLng(), true)
                    //.height(hideDTO.getHeight() + getBaseHeight(hideDTO.getFriendlyLng(), hideDTO.getFriendlyLat()))
                    .height(10.0)

                    .build();
            ShootingPoint enemyShootingPoint = new ShootingPoint.Builder(hideDTO.getEnemyLat(), hideDTO.getEnemyLng(), true)
                    //.height(getBaseHeight(hideDTO.getEnemyLng(), hideDTO.getEnemyLat()))
                    .height(10.0)
                    .build();

            ShootingPoint friendlyTarget = new ShootingPoint.Builder(friendlyShootingPoint.getLatitude(), friendlyShootingPoint.getLongitude(), true).height(friendlyShootingPoint.getHeight()).build();
            ShootingPoint enemyTarget = new ShootingPoint.Builder(enemyShootingPoint.getLatitude(), enemyShootingPoint.getLongitude(), true).height(enemyShootingPoint.getHeight()).build();

            generateConcealmentReport(sHideVO, friendlyTarget, enemyTarget, demData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            boolean has = hasNull(sHideVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return sHideVO;
    }

    public void generateConcealmentReport(SHideVO sHideVO, ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Object>> demData) {
        // ① 计算总射击距离
        double totalDistance = UtilsV2.calculateDistance(shootingPoint, targetPoint);

        // ② 计算隐蔽距离（地形遮挡导致的不可视距离）
        double concealmentDistance = UtilsV2.calculateConcealmentDistance(shootingPoint, targetPoint, demData);

        // ③ 计算遮蔽深度（地形遮挡的深度）
        double coverDepth = UtilsV2.calculateCoverDepth(shootingPoint, targetPoint, demData);

        sHideVO.setCoverDepth(coverDepth);
        sHideVO.setConcealmentDistance(concealmentDistance);
        sHideVO.setTotalDistance(totalDistance);

        // ④ 计算隐蔽性和遮蔽性得分
        //遮蔽
        Map<String, Object> concealmentScore = UtilsV2.calculateCoverScoreWithDetails(shootingPoint, targetPoint, demData);
        List<Map<String, Double>> concealedPointss = (List<Map<String, Double>>) concealmentScore.get("blockedPoints");

        sHideVO.setSubLines(getSubLines(shootingPoint, targetPoint, concealedPointss));
    }

    /**
     * 获取DemCsv到Map<String, Object>的映射
     *
     * @return DemCsv到Map<String, Object>的映射
     */
    public List<Map<String, Object>> getDemData() {
        return map(xDemCsvMapper.selectAll());
    }

    public List<Map<String, Object>> getDemData(String area) {
        return map(xDemCsvMapper.selectByArea(area));
    }

    public List<Map<String, Object>> map(List<DemCsv> demCsvList) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            Point centroid = geometry.getCentroid();
            Map<String, Object> map = new HashMap<>();
            map.put("centerLat", centroid.getY());
            map.put("centerLon", centroid.getX());
            map.put("value", demCsv.getValue());
            result.add(map);
        }
        return result;
    }

    // 根据id获取武器
    public Equipment getWeapon(Long id, Double pitchAngle, Equipment.PointRole pointRole) {
        List<XWeapon> weaponList = xWeaponMapper.selectAll();
        for (XWeapon weapon : weaponList) {
            if (weapon.getId() == id) {
                return new Equipment(WeaponType.fromDescription(weapon.getWeaponType()),
                        weapon.getObserveLineHigh(),
                        weapon.getMinRange(),
                        weapon.getMaxRange(),
                        weapon.getDeadZoneRange(),
                        weapon.getWeaponPrecision(),
                        WeaponStatus.fromDescription(weapon.getWeaponStatus()),
                        fromDescription(weapon.getBallisticType()),
                        weapon.getExplosiveYield(), weapon.getRangNum(), pitchAngle, pointRole);
            }
        }
        return null;
    }

    /**
     * 根据id获取Equipment
     *
     * @param id 武器编号id
     * @return 武器对应的Equipment类的实体
     */
    public Equipment getWeapon(Long id) {
        List<XWeapon> weaponList = xWeaponMapper.selectAll();
        for (XWeapon weapon : weaponList) {
            if (weapon.getId() == id) {
                return new Equipment(WeaponType.fromDescription(weapon.getWeaponType()),
                        weapon.getObserveLineHigh(),
                        weapon.getMinRange(),
                        weapon.getMaxRange(),
                        weapon.getDeadZoneRange(),
                        weapon.getWeaponPrecision(),
                        WeaponStatus.fromDescription(weapon.getWeaponStatus()),
                        fromDescription(weapon.getBallisticType()),
                        weapon.getExplosiveYield());
            }
        }
        return null;
    }

    /**
     * 根据描述获取TrajectoryType
     *
     * @param value
     * @return
     */
    public static TrajectoryType fromDescription(String value) {
        for (TrajectoryType type : TrajectoryType.values()) {
            if (type.getDescription().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取地图四个点的经纬度的wkt
     *
     * @return
     */
    public String getTotalArea() {
        double minLng = Double.MAX_VALUE;
        double maxLng = Double.MIN_VALUE;
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        List<DemCsv> demCsvList = xDemCsvMapper.selectAll();
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            Coordinate[] coordinates = geometry.getCoordinates();
            for (Coordinate coordinate : coordinates) {
                minLng = Math.min(minLng, coordinate.x);
                maxLng = Math.max(maxLng, coordinate.x);
                minLat = Math.min(minLat, coordinate.y);
                maxLat = Math.max(maxLat, coordinate.y);
            }
        }
        return String.format("POLYGON ((%f %f, %f %f, %f %f, %f %f, %f %f))", minLng, minLat, maxLng, minLat, maxLng, maxLat, minLng, maxLat, minLng, minLat);
    }

    /**
     * 海拔
     *
     * @param baseLng
     * @param baseLat
     * @return
     */
    public Integer getBaseHeight(Double baseLng, Double baseLat) {
        String pointStr = String.format("POINT(%f %f)", baseLng, baseLat);
        DemCsv demCsv = xDemCsvMapper.selectByPoint(pointStr);
        if (demCsv != null) {
            return demCsv.getValue();
        }
        return 0;
    }

    public static Geometry getLine(Double baseLng, Double baseLat, Double targetLng, Double targetLat) {
        // LINESTRING(_ _, _ _)
        return GeoUtil.getGeometry(String.format("LINESTRING(%f %f, %f %f)", baseLng, baseLat, targetLng, targetLat));
    }

    public List<RangeVO> getSubLines(ShootingPoint shootingPoint, ShootingPoint targetPoint, List<Map<String, Double>> points) {
        List<DemCsv> demCsvList = xDemCsvMapper.selectByLine(String.format("LINESTRING(%f %f, %f %f)", shootingPoint.getLongitude(), shootingPoint.getLatitude(), targetPoint.getLongitude(), targetPoint.getLatitude()));
        for (DemCsv demCsv : demCsvList) {
            demCsv.setCanSee(-1);
            demCsv.setCanHide(0);
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            demCsv.setCenterLng(geometry.getCentroid().getX());
            demCsv.setCenterLat(geometry.getCentroid().getY());
            demCsv.setDistance(PointUtil.calcDistanceMeter(shootingPoint.getLatitude(), shootingPoint.getLongitude(), demCsv.getCenterLat(), demCsv.getCenterLng()));
            demCsv.setGeometry(geometry);
        }
        for (Map<String, Double> point : points) {
            DemCsv demCsv = xDemCsvMapper.selectByPoint(String.format("POINT(%f %f)", point.get("longitude"), point.get("latitude")));
            if (demCsv != null) {
                Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
                for (DemCsv temp : demCsvList) {
                    if (temp.getCenterLng() == geometry.getCentroid().getX() && temp.getCenterLat() == geometry.getCentroid().getY()) {
                        temp.setCanHide(1);
                        continue; // 优化性能
                    }
                }
            }
        }
        List<RangeVO> subResult = new ArrayList<>();
        if (!demCsvList.isEmpty()) {
            // 根据距离排序
            demCsvList = demCsvList.stream().sorted(Comparator.comparing(DemCsv::getDistance)).collect(Collectors.toList());
            // 武器面向的第一个网格肯定是不可以隐蔽的
            int currentCanHide = 0;
            double currentStartLng = shootingPoint.getLongitude(), currentStartLat = shootingPoint.getLatitude();
            Geometry line = getLine(shootingPoint.getLongitude(), shootingPoint.getLatitude(), targetPoint.getLongitude(), targetPoint.getLatitude());
            for (int j = 0; j < demCsvList.size(); j++) {
                DemCsv targetDemCsv = demCsvList.get(j);
                Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
                if (j == demCsvList.size() - 1) {
                    subResult.add(new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide));
                } else if (j != 0) {
                    if (targetDemCsv.getCanHide() != currentCanHide) {
                        subResult.add(new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide));
                        currentCanHide = targetDemCsv.getCanHide();
                        currentStartLng = lineCentroid.getX();
                        currentStartLat = lineCentroid.getY();
                    }
                }
            }
        }
        return subResult;
    }

    public void checkArea(String area) {
        List<DemCsv> demCsvList = xDemCsvMapper.selectByArea(area);
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("区域内无地形数据，无法计算。");
        }
    }

    public void checkPoint(Double lng, Double lat) {
        DemCsv demCsv = xDemCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(lng, lat));
        if (demCsv == null) {
            throw new RuntimeException("所选观测点无地形数据，无法计算。");
        }
    }
}
