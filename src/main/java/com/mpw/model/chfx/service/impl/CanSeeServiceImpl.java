package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.dto.*;
import com.mpw.model.chfx.domain.entity.DemCsv;
import com.mpw.model.chfx.domain.vo.*;
import com.mpw.model.chfx.mapper.XDemCsvMapper;
import com.mpw.model.chfx.service.ICanSeeService;
import com.mpw.model.common.util.GeoUtil;
import com.mpw.model.common.util.PointUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 判断是否可以通视的服务接口实现类
 */
@Service
public class CanSeeServiceImpl implements ICanSeeService {

    private static WKTWriter writer = new WKTWriter();

    public static final int ADDITIONAL_HEIGHT_GREEN = 6;

    public static final int ADDITIONAL_HEIGHT_BUILDING = 60;

    private static final double POLE = 2.003750834E7D;

    @Autowired
    private XDemCsvMapper demCsvMapper;

    @Autowired
    private XSyfxServiceImpl xSyfxService;

    public static Geometry getLine(Double baseLng, Double baseLat, Double targetLng, Double targetLat) {
        // LINESTRING(_ _, _ _)
        return GeoUtil.getGeometry(getLineStr(baseLng, baseLat, targetLng, targetLat));
    }

    public static String getLineStr(Double baseLng, Double baseLat, Double targetLng, Double targetLat) {
        // LINESTRING(_ _, _ _)
        return String.format("LINESTRING(%f %f, %f %f)", baseLng, baseLat, targetLng, targetLat);
    }

    public static Geometry getPoint(Double lng, Double lat) {
        // POINT(_ _)
        return GeoUtil.getGeometry(String.format("POINT(%f %f)", lng, lat));
    }

    public static String getPointStr(Double lng, Double lat) {
        return String.format("POINT(%f %f)", lng, lat);
    }


    /*public static boolean intersect(Geometry line, Double lng, Double lat) {
        // POINT(_ _)
        Geometry point = GeoUtil.getGeometry(String.format("POINT(%f %f)", lng, lat));
        return line.intersects(point);
    }*/

    /**
     * 平面坐标转经纬度
     *
     * @param xdouble 平面坐标x
     * @param ydouble 平面坐标y
     * @return
     */
    public static double[] inverseMercator(double xdouble, double ydouble) {
        double[] result = new double[2];
        result[0] = 180.0D * xdouble / POLE;
        result[1] = 57.29577951308232D * (2.0D * Math.atan(Math.exp(ydouble / POLE * 3.141592653589793D)) - 1.5707963267948966D);
        return result;
    }

    /**
     * 计算方向
     *
     * @param baseLng   经度
     * @param baseLat   纬度
     * @param targetLng 经度
     * @param targetLat 纬度
     * @return
     */
    public static double calculateDirection(Double baseLng, Double baseLat, Double targetLng, Double targetLat) {
        GlobalCoordinates base = new GlobalCoordinates(baseLat, baseLng);
        GlobalCoordinates target = new GlobalCoordinates(targetLat, targetLng);
        GeodeticCurve curve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, base, target);
        return curve.getAzimuth();
    }

    /**
     * 判断一个区域是否tongshi
     *
     * @param query 判断一个区域的通视情况的请求体
     * @return
     */
    @Override
    public CanSeeVO judge(CanSeeDTO query) {
        xSyfxService.checkArea(query.getFightRange());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        // 根据作战范围查询网格列表
        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getFightRange());
        if (demCsvList == null || demCsvList.isEmpty()) {
            return new CanSeeVO();
        }
        List<List<RangeVO>> result = new ArrayList<>();

        Double baseLng = query.getBaseLng();
        Double baseLat = query.getBaseLat();
        double[] baseXY = GeoUtil.wgs84ToMercator(baseLng, baseLat);
        double baseX = baseXY[0];
        double baseY = baseXY[1];

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));

        // 计算每个网格与观察点的距离等
        fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), query.getGreen(), query.getBuilding(), demCsvList);

        for (double i = 0; i < 720; i++) {
            List<RangeVO> subResult = new ArrayList<>();

            double angle = 2 * Math.PI / 720 * i;
            double targetX = baseX + query.getRadius() * Math.sin(angle);
            double targetY = baseY + query.getRadius() * Math.cos(angle);
            double[] targetLngLat = inverseMercator(targetX, targetY);
            double targetLng = targetLngLat[0];
            double targetLat = targetLngLat[1];
            Geometry radiusLine = getLine(baseLng, baseLat, targetLng, targetLat);

            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> radiusLine.intersects(demCsv.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            if (!subDemCsvList.isEmpty()) {
                int currentCanSee = 1;
                double currentStartLng = baseLng, currentStartLat = baseLat;
                double maxGradient = subDemCsvList.get(0).getGradient();

                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanSee(1);
                    } else {
                        if (targetDemCsv.getGradient() > maxGradient) {
                            targetDemCsv.setCanSee(1);
                            maxGradient = targetDemCsv.getGradient();
                        }
                    }

                    Point lineCentroid = radiusLine.intersection(targetDemCsv.getGeometry()).getCentroid();
                    if (j == subDemCsvList.size() - 1) {
                        subResult.add(fillRangeVO(baseLat, baseLng, radiusLine, targetDemCsv.getGeometry(), new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1)));
                    } else if (j != 0) {
                        if (targetDemCsv.getCanSee() != currentCanSee) {
                            subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
                            currentCanSee = targetDemCsv.getCanSee();
                            currentStartLng = lineCentroid.getX();
                            currentStartLat = lineCentroid.getY();
                        }
                    }
                }
            }
            result.add(subResult);
        }

        Double ratio = CanHideServiceImpl.calculateRatio(result);
        Double areaSize = Math.PI * query.getRadius() / 1000.0 * query.getRadius() / 1000.0;
        CanSeeVO canSeeVO = new CanSeeVO();
        canSeeVO.setCanSeeArea(ratio * areaSize);
        canSeeVO.setCanNotSeeArea((1 - ratio) * areaSize);
        canSeeVO.setRatio(ratio);
        canSeeVO.setRangeVOList(result);

        try {
            boolean has = XSyfxServiceImpl.hasNull(canSeeVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return canSeeVO;
    }

    private static Callable<Void> computeTask(double index, List<List<RangeVO>> result, CanSeeDTO query, List<DemCsv> demCsvList) {
        return () -> {
            List<RangeVO> subResult = new ArrayList<>();

            Double baseLng = query.getBaseLng();
            Double baseLat = query.getBaseLat();
            double[] baseXY = GeoUtil.wgs84ToMercator(baseLng, baseLat);
            double baseX = baseXY[0];
            double baseY = baseXY[1];

            double angle = 2 * Math.PI / 720 * index;
            double targetX = baseX + query.getRadius() * Math.sin(angle);
            double targetY = baseY + query.getRadius() * Math.cos(angle);
            double[] targetLngLat = inverseMercator(targetX, targetY);
            Geometry radiusLine = new WKTReader().read(String.format("LINESTRING(%f %f, %f %f)", baseLng, baseLat, targetLngLat[0], targetLngLat[1]));
            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> radiusLine.intersects(demCsv.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            if (!subDemCsvList.isEmpty()) {
                int currentCanSee = 1;
                double currentStartLng = baseLng, currentStartLat = baseLat;
                double maxGradient = subDemCsvList.get(0).getGradient();

                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanSee(1);
                    } else {
                        if (targetDemCsv.getGradient() > maxGradient) {
                            targetDemCsv.setCanSee(1);
                            maxGradient = targetDemCsv.getGradient();
                        }
                    }

                    Point lineCentroid = radiusLine.intersection(targetDemCsv.getGeometry()).getCentroid();
                    if (j == subDemCsvList.size() - 1) {
                        subResult.add(fillRangeVO(baseLat, baseLng, radiusLine, targetDemCsv.getGeometry(), new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1)));
                    } else if (j != 0) {
                        if (targetDemCsv.getCanSee() != currentCanSee) {
                            subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
                            currentCanSee = targetDemCsv.getCanSee();
                            currentStartLng = lineCentroid.getX();
                            currentStartLat = lineCentroid.getY();
                        }
                    }
                }
            }

            synchronized (result) {
                result.set((int) index, subResult);
            }

            return null;
        };
    }

    private static ExecutorService threadPool = Executors.newFixedThreadPool(8);

    @Override
    public CanSeeVO judgePall(CanSeeDTO query) {
        xSyfxService.checkArea(query.getFightRange());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getFightRange());
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("所选观测点及周围区域无地形数据。");
        }
        List<List<RangeVO>> result = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 720; i++) {
            result.add(null);
        }
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));

        fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), query.getGreen(), query.getBuilding(), demCsvList);
        List<Future<?>> futures = new ArrayList<>();
        for (double i = 0; i < 720; i++) {
            Callable<Void> task = computeTask(i, result, query, demCsvList);
            Future<Void> future = threadPool.submit(task);
            futures.add(future);
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException("计算失败");
            }
        }

        Double ratio = CanHideServiceImpl.calculateRatio(result);
        Double areaSize = Math.PI * query.getRadius() / 1000.0 * query.getRadius() / 1000.0;
        CanSeeVO canSeeVO = new CanSeeVO();
        canSeeVO.setCanSeeArea(ratio * areaSize);
        canSeeVO.setCanNotSeeArea((1 - ratio) * areaSize);
        canSeeVO.setRatio(ratio);
        canSeeVO.setRangeVOList(result);

        try {
            boolean has = XSyfxServiceImpl.hasNull(canSeeVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return canSeeVO;
    }

    /**
     * 判断地图上一个区域是否可通视
     *
     * @param query 判断地图上一个区域是否通视的查询请求
     * @return 判断地图上一个区域是否通视的返回结果
     */
    @Override
    public VisualDomainVO visualDomainAnalysis(VisualDomainDTO query) {
        //检查作战区域与坐标
        xSyfxService.checkArea(query.getAppointmentArea());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        VisualDomainVO visualDomainVO = new VisualDomainVO();

        //获取作战区域信息
        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getAppointmentArea());
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("所选任务区域无地形数据。");
        }
        List<List<RangeVO>> result = new ArrayList<>();

        //区域面积
        double unitAreaSize = 0.03 * 0.03 / GeoUtil.getGeometry(demCsvList.get(0).getGeometryStr()).getArea();

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));

        //判断环境影响因素
        //考虑植被
        int vegetation = 0;
        //考虑建筑
        int building = 0;
        for (String Item : query.getEnvironmentalValue()) {
            //考虑植被
            if (Item.equals("vegetation")) {
                vegetation = 1;
            }
            //考虑建筑
            if (Item.equals("building")) {
                building = 1;
            }
        }

        //考虑季节
        //夏秋
        if (query.getSeason().equals("summer_fall")) {
            for (VegetationDTO vegetationDTO : query.getVegetationDTOList()) {
                // 计算每个网格与观察点的距离等，并找到最大距离
                fillVisualDomainDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), vegetation, building, demCsvList, vegetationDTO.getVegetationHeight());
            }
        } else {
            fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), vegetation, building, demCsvList);
        }

        double maxDistance = -1;
        for (DemCsv demCsv : demCsvList) {
            maxDistance = Math.max(maxDistance, demCsv.getDistance());
        }

        Geometry area = GeoUtil.getGeometry(query.getAppointmentArea());
        //根据点查询地图网格列表
        DemCsv point = demCsvMapper.selectByPoint(getPointStr(query.getBaseLng(), query.getBaseLat()));
        boolean inArea = area.contains(GeoUtil.getGeometry(point.getGeometryStr()));

        for (int i = 0; i < 720; i++) {
            double angle = 2 * Math.PI / 720 * i;
            Geometry line = CanHideServiceImpl.getLineByAngleAndPlusRadius(query.getBaseLng(), query.getBaseLat(), angle, maxDistance);

            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> line.intersects(demCsv.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            Coordinate[] coordinates = area.intersection(line).getCoordinates();
            double minD = Double.MAX_VALUE, maxD = Double.MIN_VALUE, endLng = query.getBaseLng(), endLat = query.getBaseLat();
            double currentStartLng = query.getBaseLng(), currentStartLat = query.getBaseLat();
            if (coordinates.length > 0) {
                for (Coordinate coordinate : coordinates) {
                    double dis = PointUtil.calcDistanceMeter(query.getBaseLat(), query.getBaseLng(), coordinate.y, coordinate.x);
                    if (!inArea && dis < minD) {
                        minD = dis;
                        currentStartLng = coordinate.x;
                        currentStartLat = coordinate.y;
                    }
                    if (dis > maxD) {
                        maxD = dis;
                        endLng = coordinate.x;
                        endLat = coordinate.y;
                    }
                }
            }

            List<RangeVO> subResult = new ArrayList<>();
            if (!subDemCsvList.isEmpty() && coordinates.length > 0) {
                int currentCanSee = 1;
                double maxGradient = subDemCsvList.get(0).getGradient();
                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanSee(1);
                    } else {
                        if (targetDemCsv.getGradient() > maxGradient) {
                            targetDemCsv.setCanSee(1);
                            maxGradient = targetDemCsv.getGradient();
                        } else {
                            targetDemCsv.setCanSee(0);
                        }
                    }
                    if (j == subDemCsvList.size() - 1) {
                        subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, endLng, endLat, currentCanSee, -1));
                    } else if (j != 0) {
                        Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
                        if (targetDemCsv.getCanSee() != currentCanSee) {
                            subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
                            currentCanSee = targetDemCsv.getCanSee();
                            currentStartLng = lineCentroid.getX();
                            currentStartLat = lineCentroid.getY();
                        }
                    }
                }
            }
            result.add(subResult);
        }


        Double ratio = CanHideServiceImpl.calculateRatio(result);
        double areaSize = GeoUtil.getGeometry(query.getAppointmentArea()).getArea() * unitAreaSize;

        visualDomainVO.setCanSeeArea(areaSize * ratio);
        visualDomainVO.setCanNotSeeArea(areaSize * (1 - ratio));
        visualDomainVO.setRatio(ratio);
        visualDomainVO.setRangeVOList(result);
        visualDomainVO.setDemCsvList(demCsvList);

        for (DemCsv demCsv : visualDomainVO.getDemCsvList()) {
            demCsv.setGeometry(null);
            demCsv.setCanHide(-1);
        }
        try {
            boolean has = XSyfxServiceImpl.hasNull(visualDomainVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return visualDomainVO;
    }


    /**
     * 计算每个网格与观察点的距离等，并找到最大距离
     * @param pureHeight 观测高度
     * @param baseLng 经度
     * @param baseLat 维度
     * @param green 环境因素：建筑
     * @param building 环境因素：植被
     * @param demCsvList 作战区域信息
     * @param vegetationHeight 植被高度
     */
    public static void fillVisualDomainDemCsv(Integer pureHeight, Double baseLng, Double baseLat, Integer green, Integer building, List<DemCsv> demCsvList, Integer vegetationHeight) {
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            demCsv.setGeometry(geometry);
        }

        for (DemCsv demCsv : demCsvList) {
            /**
             * 考虑建筑物
             */
            if (building == 1 && demCsv.getType().equals("building")) {
                demCsv.setValue(demCsv.getValue() + ADDITIONAL_HEIGHT_BUILDING);
            }
            /**
             * 考虑植被
             */
            Integer vegetationHeightNew = ADDITIONAL_HEIGHT_BUILDING;
            if (vegetationHeight != null) {
                vegetationHeightNew = vegetationHeight;
            }
            if (green == 1 && demCsv.getType().equals("forest")) {
                demCsv.setValue(demCsv.getValue() + vegetationHeightNew);
            }

            Point centroid = demCsv.getGeometry().getCentroid();
            demCsv.setCenterLng(centroid.getX());
            demCsv.setCenterLat(centroid.getY());
            demCsv.setDistance(PointUtil.calcDistanceMeter(baseLat, baseLng, demCsv.getCenterLat(), demCsv.getCenterLng()));
            demCsv.setGradient((demCsv.getValue() - pureHeight) / demCsv.getDistance());
            demCsv.setDirection(calculateDirection(baseLng, baseLat, demCsv.getCenterLng(), demCsv.getCenterLat()));
        }
    }

    @Override
    public CanSeeLineVO judgeLine(CanSeeLineDTO query) {
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        CanSeeLineVO canSeeLineVO = new CanSeeLineVO();

        DemCsv demCsv1 = demCsvMapper.selectByPoint(getPointStr(query.getBaseLng(), query.getBaseLat()));
        DemCsv demCsv2 = demCsvMapper.selectByPoint(getPointStr(query.getTargetLng(), query.getTargetLat()));
        if (demCsv1 != null && demCsv2 != null) {
            Point centroid1 = GeoUtil.getGeometry(demCsv1.getGeometryStr()).getCentroid();
            Point centroid2 = GeoUtil.getGeometry(demCsv2.getGeometryStr()).getCentroid();
            if (centroid1.getX() == centroid2.getX() && centroid1.getY() == centroid2.getY()) {
                throw new RuntimeException("观测点和目标点距离过近，无法进行计算。");
            }
        }

        Geometry line = getLine(query.getBaseLng(), query.getBaseLat(), query.getTargetLng(), query.getTargetLat());
        List<DemCsv> demCsvList = demCsvMapper.selectByLine(getWKTByGeometry(line));
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("所选观测点或目标点无地形数据。");
        }

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));

        // 计算每个网格与观察点的距离等
        fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), query.getGreen(), query.getBuilding(), demCsvList);

        // 根据网格与观察点的距离对网格进行排序
        List<DemCsv> subDemCsvList = demCsvList.stream()
                .sorted(Comparator.comparing(DemCsv::getDistance))
                .collect(Collectors.toList());

        int currentCanSee = 1;
        double currentStartLng = query.getBaseLng(), currentStartLat = query.getBaseLat();
        double maxGradient = subDemCsvList.get(0).getGradient();

        List<RangeVO> result = new ArrayList<>();
        for (int j = 0; j < subDemCsvList.size(); j++) {
            DemCsv targetDemCsv = subDemCsvList.get(j);
            if (j == 0) {
                targetDemCsv.setCanSee(1);
            } else {
                if (targetDemCsv.getGradient() > maxGradient) {
                    targetDemCsv.setCanSee(1);
                    maxGradient = targetDemCsv.getGradient();
                }
            }

            Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
            if (j == subDemCsvList.size() - 1) {
                result.add(fillRangeVO(query.getBaseLat(), query.getBaseLng(), line, targetDemCsv.getGeometry(), new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1)));
                // result.add(new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
            } else if (j != 0) {
                if (targetDemCsv.getCanSee() != currentCanSee) {
                    result.add(new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
                    currentCanSee = targetDemCsv.getCanSee();
                    currentStartLng = lineCentroid.getX();
                    currentStartLat = lineCentroid.getY();
                }
            }
        }

        double length = PointUtil.calcDistanceMeter(query.getBaseLat(), query.getBaseLng(), query.getTargetLat(), query.getTargetLng()) / 1000;
        Double ratio = calculateLineRatio(result);
        canSeeLineVO.setCanSeeDistance(ratio * length);
        canSeeLineVO.setCanNotSeeDistance((1 - ratio) * length);
        canSeeLineVO.setRatio(ratio);
        canSeeLineVO.setRangeVOList(result);

        try {
            boolean has = XSyfxServiceImpl.hasNull(canSeeLineVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return canSeeLineVO;
    }

    /**
     * 通视性分析
     * 判断地图上一条线是否通视
     *
     * @param query 判断地图上一条线是否通视的查询请求
     * @return 判断地图上一条线是否通视的返回结果
     */
    @Override
    public CanSeeLineVO linearSight(LinearSightDTO query) {
        //检查观测点坐标
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());
        //检查目标点坐标
        xSyfxService.checkPoint(query.getTargetLng(), query.getTargetLat());

        CanSeeLineVO canSeeLineVO = new CanSeeLineVO();

        //获取观测点信息
        DemCsv demCsv1 = demCsvMapper.selectByPoint(getPointStr(query.getBaseLng(), query.getBaseLat()));
        //获取目标点信息
        DemCsv demCsv2 = demCsvMapper.selectByPoint(getPointStr(query.getTargetLng(), query.getTargetLat()));
        //判断观测点与目标点是否在同坐标
        if (demCsv1 != null && demCsv2 != null) {
            Point centroid1 = GeoUtil.getGeometry(demCsv1.getGeometryStr()).getCentroid();
            Point centroid2 = GeoUtil.getGeometry(demCsv2.getGeometryStr()).getCentroid();
            if (centroid1.getX() == centroid2.getX() && centroid1.getY() == centroid2.getY()) {
                throw new RuntimeException("观测点和目标点距离过近，无法进行计算。");
            }
        }

        //获取地形数据
        Geometry line = getLine(query.getBaseLng(), query.getBaseLat(), query.getTargetLng(), query.getTargetLat());
        List<DemCsv> demCsvList = demCsvMapper.selectByLine(getWKTByGeometry(line));
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("所选观测点或目标点无地形数据。");
        }

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));
        query.setTargetHeight(query.getTargetHeight() + getBaseHeight(query.getTargetLng(), query.getTargetLat()));

        //判断环境影响因素
        //考虑植被
        int vegetation = 0;
        //考虑建筑
        int building = 0;
        for (String Item : query.getEnvironmentalValue()) {
            //考虑植被
            if (Item.equals("vegetation")) {
                vegetation = 1;
            }
            //考虑建筑
            if (Item.equals("building")) {
                building = 1;
            }
        }

        //考虑季节
        //夏秋
        if (query.getSeason().equals("summer_fall")) {
            for (VegetationDTO vegetationDTO : query.getVegetationDTOList()) {
                // 计算每个网格与观察点的距离等，并找到最大距离
                fillVisualDomainDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), vegetation, building, demCsvList, vegetationDTO.getVegetationHeight());
            }
        } else {
            // 计算每个网格与观察点的距离等
            fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), vegetation, building, demCsvList);
        }
        // 根据网格与观察点的距离对网格进行排序
        List<DemCsv> subDemCsvList = demCsvList.stream()
                .sorted(Comparator.comparing(DemCsv::getDistance))
                .collect(Collectors.toList());

        int currentCanSee = 1;
        double currentStartLng = query.getBaseLng(), currentStartLat = query.getBaseLat();
        double maxGradient = subDemCsvList.get(0).getGradient();

        List<RangeVO> result = new ArrayList<>();
        for (int j = 0; j < subDemCsvList.size(); j++) {
            DemCsv targetDemCsv = subDemCsvList.get(j);
            if (j == 0) {
                targetDemCsv.setCanSee(1);
            } else {
                if (targetDemCsv.getGradient() > maxGradient) {
                    targetDemCsv.setCanSee(1);
                    maxGradient = targetDemCsv.getGradient();
                }
            }

            Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
            if (j == subDemCsvList.size() - 1) {
                result.add(fillRangeVO(query.getBaseLat(), query.getBaseLng(), line, targetDemCsv.getGeometry(), new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1)));
                // result.add(new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
            } else if (j != 0) {
                if (targetDemCsv.getCanSee() != currentCanSee) {
                    result.add(new RangeVO(-1.0, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
                    currentCanSee = targetDemCsv.getCanSee();
                    currentStartLng = lineCentroid.getX();
                    currentStartLat = lineCentroid.getY();
                }
            }
        }

        double length = PointUtil.calcDistanceMeter(query.getBaseLat(), query.getBaseLng(), query.getTargetLat(), query.getTargetLng()) / 1000;
        Double ratio = calculateLineRatio(result);
        canSeeLineVO.setCanSeeDistance(ratio * length);
        canSeeLineVO.setCanNotSeeDistance((1 - ratio) * length);
        canSeeLineVO.setRatio(ratio);
        canSeeLineVO.setRangeVOList(result);

        try {
            boolean has = XSyfxServiceImpl.hasNull(canSeeLineVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return canSeeLineVO;
    }

    public static void fillDemCsv(Integer pureHeight, Double baseLng, Double baseLat, Integer green, Integer building, List<DemCsv> demCsvList) {
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            demCsv.setGeometry(geometry);
        }

        for (DemCsv demCsv : demCsvList) {
            /**
             * 考虑建筑物
             */
            if (building == 1 && demCsv.getType().equals("building")) {
                demCsv.setValue(demCsv.getValue() + ADDITIONAL_HEIGHT_BUILDING);
            }
            /**
             * 考虑植被
             */
            if (green == 1 && demCsv.getType().equals("forest")) {
                demCsv.setValue(demCsv.getValue() + ADDITIONAL_HEIGHT_GREEN);
            }

            Point centroid = demCsv.getGeometry().getCentroid();
            demCsv.setCenterLng(centroid.getX());
            demCsv.setCenterLat(centroid.getY());
            demCsv.setDistance(PointUtil.calcDistanceMeter(baseLat, baseLng, demCsv.getCenterLat(), demCsv.getCenterLng()));
            demCsv.setGradient((demCsv.getValue() - pureHeight) / demCsv.getDistance());
            demCsv.setDirection(calculateDirection(baseLng, baseLat, demCsv.getCenterLng(), demCsv.getCenterLat()));
        }
    }

    public Integer getBaseHeight(Double baseLng, Double baseLat) {
        String pointStr = getPointStr(baseLng, baseLat);
        DemCsv demCsv = demCsvMapper.selectByPoint(pointStr);
        if (demCsv != null) {
            return demCsv.getValue();
        }
        return 0;
    }

    public static String getWKTByGeometry(Geometry geometry) {
        if (writer == null) {
            writer = new WKTWriter();
        }
        return writer.write(geometry);
    }

    public static RangeVO fillRangeVO(Double baseLat, Double baseLng, Geometry line, Geometry point, RangeVO rangeVO) {
        Point lineCentroid = line.intersection(point).getCentroid();
        double finalLng = lineCentroid.getX(), finalLat = lineCentroid.getY();
        Geometry intersection = line.intersection(point);
        if (intersection != null) {
            Coordinate[] coordinates = intersection.getBoundary().getCoordinates();
            double maxLength = -1.0;
            if (coordinates.length > 0) {
                for (Coordinate coordinate : coordinates) {
                    double length = PointUtil.calcDistanceMeter(baseLat, baseLng, coordinate.y, coordinate.x);
                    if (length > maxLength) {
                        maxLength = length;
                        finalLng = coordinate.x;
                        finalLat = coordinate.y;
                    }
                }
            }
        }
        rangeVO.setEndLng(finalLng);
        rangeVO.setEndLat(finalLat);
        return rangeVO;
    }

    public static Double calculateLineRatio(List<RangeVO> result) {
        double total = 0.0, flag = 0.0;
        if (result == null || result.isEmpty()) {
            return 0.0;
        } else {
            for (RangeVO rangeVO : result) {
                double[] baseXY = GeoUtil.wgs84ToMercator(rangeVO.getStartLng(), rangeVO.getStartLat());
                double[] targetXY = GeoUtil.wgs84ToMercator(rangeVO.getEndLng(), rangeVO.getEndLat());
                double length = Math.sqrt(Math.pow(baseXY[0] - targetXY[0], 2) + (Math.pow(baseXY[1] - targetXY[1], 2)));
                total += length;
                if (rangeVO.getCanSee() == 1 || rangeVO.getCanHide() == 1) {
                    flag += length;
                }
            }
        }
        return flag / total;
    }


    /**
     * 战场视野分析
     *
     * @param query
     * @return
     */
    @Override
    public ViewVO view(XViewDTO query) {
        xSyfxService.checkArea(query.getDeployRange());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        ViewVO viewVO = new ViewVO();

        List<List<RangeVO>> result = new ArrayList<>();

        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getDeployRange());
        if (demCsvList == null || demCsvList.isEmpty()) {
            return viewVO;
        }

        double unitAreaSize = 0.03 * 0.03 / GeoUtil.getGeometry(demCsvList.get(0).getGeometryStr()).getArea();

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));

        // 计算每个网格与观察点的距离等，并找到最大距离
        CanSeeServiceImpl.fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), query.getGreen(), query.getBuilding(), demCsvList);
        double maxDistance = -1;
        for (DemCsv demCsv : demCsvList) {
            maxDistance = Math.max(maxDistance, demCsv.getDistance());
        }

        Geometry area = GeoUtil.getGeometry(query.getDeployRange());
        DemCsv point = demCsvMapper.selectByPoint(getPointStr(query.getBaseLng(), query.getBaseLat()));
        boolean inArea = area.contains(GeoUtil.getGeometry(point.getGeometryStr()));

        for (int i = 0; i < 720; i++) {
            double angle = 2 * Math.PI / 720 * i;
            Geometry line = CanHideServiceImpl.getLineByAngleAndPlusRadius(query.getBaseLng(), query.getBaseLat(), angle, maxDistance);

            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> line.intersects(demCsv.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            Coordinate[] coordinates = area.intersection(line).getCoordinates();
            double minD = Double.MAX_VALUE, maxD = Double.MIN_VALUE, endLng = query.getBaseLng(), endLat = query.getBaseLat();
            double currentStartLng = query.getBaseLng(), currentStartLat = query.getBaseLat();
            if (coordinates.length > 0) {
                for (Coordinate coordinate : coordinates) {
                    double dis = PointUtil.calcDistanceMeter(query.getBaseLat(), query.getBaseLng(), coordinate.y, coordinate.x);
                    if (!inArea && dis < minD) {
                        minD = dis;
                        currentStartLng = coordinate.x;
                        currentStartLat = coordinate.y;
                    }
                    if (dis > maxD) {
                        maxD = dis;
                        endLng = coordinate.x;
                        endLat = coordinate.y;
                    }
                }
            }

            List<RangeVO> subResult = new ArrayList<>();
            if (!subDemCsvList.isEmpty() && coordinates.length > 0) {
                int currentCanSee = 1;
                double maxGradient = subDemCsvList.get(0).getGradient();
                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanSee(1);
                    } else {
                        if (targetDemCsv.getGradient() > maxGradient) {
                            targetDemCsv.setCanSee(1);
                            maxGradient = targetDemCsv.getGradient();
                        } else {
                            targetDemCsv.setCanSee(0);
                        }
                    }
                    if (j == subDemCsvList.size() - 1) {
                        subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, endLng, endLat, currentCanSee, -1));
                    } else if (j != 0) {
                        Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
                        if (targetDemCsv.getCanSee() != currentCanSee) {
                            subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), currentCanSee, -1));
                            currentCanSee = targetDemCsv.getCanSee();
                            currentStartLng = lineCentroid.getX();
                            currentStartLat = lineCentroid.getY();
                        }
                    }
                }
            }
            result.add(subResult);
        }

        Double ratio = CanHideServiceImpl.calculateRatio(result);
        double areaSize = GeoUtil.getGeometry(query.getDeployRange()).getArea() * unitAreaSize;

        viewVO.setVisualArea(areaSize * ratio);
        viewVO.setTotalArea(areaSize);
        viewVO.setRatio(ratio);
        viewVO.setRangeVOList(result);
        viewVO.setDemCsvList(demCsvList);

        for (DemCsv demCsv : viewVO.getDemCsvList()) {
            demCsv.setGeometry(null);
            demCsv.setCanHide(-1);
        }

        return viewVO;
    }
}
