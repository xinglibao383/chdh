package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.dto.ConcealmentDTO;
import com.mpw.model.chfx.domain.dto.DasqueradeDTO;
import com.mpw.model.chfx.domain.dto.HideDTO;
import com.mpw.model.chfx.domain.dto.VegetationDTO;
import com.mpw.model.chfx.domain.entity.DemCsv;
import com.mpw.model.chfx.domain.entity.JdEquip;
import com.mpw.model.chfx.domain.vo.*;
import com.mpw.model.chfx.mapper.XDemCsvMapper;
import com.mpw.model.chfx.mapper.JdEquipMapper;
import com.mpw.model.chfx.service.ICanHideService;
import com.mpw.model.common.enums.DEMType1Enum;
import com.mpw.model.common.util.GeoUtil;
import com.mpw.model.common.util.PointUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 判断是否隐蔽的服务接口的实现类
 */
@Service
public class CanHideServiceImpl implements ICanHideService {

    private static final double POLE = 2.003750834E7D;

    @Autowired
    private XDemCsvMapper demCsvMapper;

    @Autowired
    private JdEquipMapper jdEquipMapper;

    public static Geometry getPoint(Double lng, Double lat) {
        // POINT(_ _)
        return GeoUtil.getGeometry(String.format("POINT(%f %f)", lng, lat));
    }

    public static Geometry getLine(Double baseLng, Double baseLat, Double targetLng, Double targetLat) {
        // LINESTRING(_ _, _ _)
        return GeoUtil.getGeometry(String.format("LINESTRING(%f %f, %f %f)", baseLng, baseLat, targetLng, targetLat));
    }

    public static Geometry getLineByAngleRadius(Double baseLng, Double baseLat, Double angle, Double radius) {
        double[] baseXY = GeoUtil.wgs84ToMercator(baseLng, baseLat);
        double targetX = baseXY[0] + radius * Math.sin(angle);
        double targetY = baseXY[1] + radius * Math.cos(angle);
        double[] targetLngLat = inverseMercator(targetX, targetY);
        return getLine(baseLng, baseLat, targetLngLat[0], targetLngLat[1]);
    }

    public static Geometry getLineByAngleAndPlusRadius(Double baseLng, Double baseLat, Double angle, Double radius) {
        return getLineByAngleRadius(baseLng, baseLat, angle, radius * 2);
    }

    public static double[] inverseMercator(double xdouble, double ydouble) {
        double[] result = new double[2];
        result[0] = 180.0D * xdouble / POLE;
        result[1] = 57.29577951308232D * (2.0D * Math.atan(Math.exp(ydouble / POLE * 3.141592653589793D)) - 1.5707963267948966D);
        return result;
    }

    public static Integer getDegreeByType(String type) {
        for (DEMType1Enum value : DEMType1Enum.values()) {
            if (value.getStr().equals(type)) {
                return value.getNum();
            }
        }
        return DEMType1Enum.UNCLASSIFIED.getNum();
    }

    public Integer getBaseHeight(Double baseLng, Double baseLat) {
        String pointStr = String.format("POINT(%f %f)", baseLng, baseLat);
        DemCsv demCsv = demCsvMapper.selectByPoint(pointStr);
        if (demCsv != null) {
            return demCsv.getValue();
        }
        return 0;
    }

    @Autowired
    private XSyfxServiceImpl xSyfxService;

    /**
     * 判断是否隐蔽
     *
     * @param query 判断是否隐蔽的查询实体类
     * @return 判断是否隐蔽的返回结果
     */
    @Override
    public CanHideVO judge(HideDTO query) {
        xSyfxService.checkArea(query.getDeployRange());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        CanHideVO canHideVO = new CanHideVO();
        List<List<RangeVO>> result = new ArrayList<>();

        DemCsv demCsv = demCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(query.getBaseLng(), query.getBaseLat()));
        if (demCsv == null) {
            throw new RuntimeException("敌方火力点处无地形数据，无法计算。");
        }

        // 根据装备部署范围查询网格列表
        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getDeployRange());
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("作战区域处无地形数据，无法计算。");
        }

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));

        // 计算每个网格与观察点的距离等，并找到最大距离
        CanSeeServiceImpl.fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), query.getGreen(), query.getBuilding(), demCsvList);
        double maxDistance = -1;
        for (DemCsv demCsv1 : demCsvList) {
            maxDistance = Math.max(maxDistance, demCsv1.getDistance());
        }

        Geometry area = GeoUtil.getGeometry(query.getDeployRange());
        Geometry point = getPoint(query.getBaseLng(), query.getBaseLat());
        boolean inArea = area.contains(point);

        for (int i = 0; i < 720; i++) {
            double angle = 2 * Math.PI / 720 * i;
            // Geometry line = getLineByAngleRadius(query.getBaseLng(), query.getBaseLat(), angle, maxDistance);
            Geometry line = getLineByAngleAndPlusRadius(query.getBaseLng(), query.getBaseLat(), angle, maxDistance);

            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(temp -> line.intersects(temp.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            List<RangeVO> subResult = new ArrayList<>();
            if (!subDemCsvList.isEmpty()) {
                int currentCanHide = 0;
                double currentStartLng = query.getBaseLng(), currentStartLat = query.getBaseLat();
                double maxGradient = subDemCsvList.get(0).getGradient();

                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanHide(0);
                        if (!inArea) {
                            currentStartLng = targetDemCsv.getCenterLng();
                            currentStartLat = targetDemCsv.getCenterLat();
                            Geometry boundary = GeoUtil.getGeometry(query.getDeployRange()).getBoundary();
                            double min = Double.MAX_VALUE;
                            Geometry intersection = line.intersection(boundary);
                            Coordinate[] coordinates = intersection.getCoordinates();
                            if (coordinates.length > 0) {
                                for (Coordinate coordinate : coordinates) {
                                    double len = PointUtil.calcDistanceMeter(query.getBaseLat(), query.getBaseLng(), coordinate.y, coordinate.x);
                                    if (len < min) {
                                        min = len;
                                        currentStartLng = coordinate.x;
                                        currentStartLat = coordinate.y;
                                    }
                                }
                            }
                        }
                    } else {
                        if (targetDemCsv.getGradient() <= maxGradient) {
                            targetDemCsv.setCanHide(1);
                        }
                        maxGradient = Math.max(targetDemCsv.getGradient(), maxGradient);
                    }

                    Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
                    if (j == subDemCsvList.size() - 1) {
                        // subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide));
                        subResult.add(CanSeeServiceImpl.fillRangeVO(query.getBaseLat(), query.getBaseLng(), line, targetDemCsv.getGeometry(), new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide)));
                    } else if (j != 0) {
                        if (targetDemCsv.getCanHide() != currentCanHide) {
                            subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide));
                            currentCanHide = targetDemCsv.getCanHide();
                            currentStartLng = lineCentroid.getX();
                            currentStartLat = lineCentroid.getY();
                        }
                    }
                }

                if (subResult.size() > 0) {
                    RangeVO rangeVO = subResult.get(subResult.size() - 1);
                    Geometry boundary = GeoUtil.getGeometry(query.getDeployRange()).getBoundary();
                    double max = -1.0;
                    Geometry intersection = line.intersection(boundary);
                    Coordinate[] coordinates = intersection.getCoordinates();
                    if (coordinates.length > 0) {
                        for (Coordinate coordinate : coordinates) {
                            double len = PointUtil.calcDistanceMeter(query.getBaseLat(), query.getBaseLng(), coordinate.y, coordinate.x);
                            if (len > max) {
                                max = len;
                                rangeVO.setEndLat(coordinate.y);
                                rangeVO.setEndLng(coordinate.x);
                            }
                        }
                    }
                }
            }
            result.add(subResult);
        }

        Double ratio = calculateRatio(result);
        double areaSize = demCsvList.size() * 0.03 * 0.03;
        canHideVO.setCanHideArea(ratio * areaSize);
        canHideVO.setCanNotHideArea((1 - ratio) * areaSize);
        canHideVO.setRatio(ratio);
        canHideVO.setRangeVOList(result);

        try {
            boolean has = XSyfxServiceImpl.hasNull(canHideVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return canHideVO;
    }

    /**
     * 隐蔽概率分析
     * 判断地图上一个区域是否可以隐蔽
     *
     * @param query 判断地图上一个区域是否可以隐蔽的查询请求体
     * @return 判断地图上一个区域是否可以隐蔽的返回结果
     */
    @Override
    public ConcealmentVO hiddenProbabilityAnalysis(ConcealmentDTO query) {
        //校验红方任务区域
        xSyfxService.checkArea(query.getRedDeployRange());
        //校验蓝方任务区域
        xSyfxService.checkArea(query.getBlueDeployRange());
        //校验红方火力点
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());
        //校验蓝方火力点
        xSyfxService.checkPoint(query.getTargetLng(), query.getTargetLat());

        ConcealmentVO concealmentVO = new ConcealmentVO();
        List<List<RangeVO>> result = new ArrayList<>();

        DemCsv demCsvRed = demCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(query.getBaseLng(), query.getBaseLat()));
        if (demCsvRed == null) {
            throw new RuntimeException("红方火力点处无地形数据，无法计算。");
        }
        DemCsv demCsvBlue = demCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(query.getTargetLng(), query.getTargetLat()));
        if (demCsvBlue == null) {
            throw new RuntimeException("蓝方火力点处无地形数据，无法计算。");
        }

        // 根据装备部署范围查询网格列表
        List<DemCsv> demCsvListRed = demCsvMapper.selectByArea(query.getRedDeployRange());
        if (demCsvListRed == null || demCsvListRed.isEmpty()) {
            throw new RuntimeException("红方作战区域处无地形数据，无法计算。");
        }
        List<DemCsv> demCsvListBlue = demCsvMapper.selectByArea(query.getBlueDeployRange());
        if (demCsvListBlue == null || demCsvListBlue.isEmpty()) {
            throw new RuntimeException("蓝方作战区域处无地形数据，无法计算。");
        }
        // 单位面积
        double unitArea = 0.03 * 0.03 / GeoUtil.getGeometry(demCsvListRed.get(0).getGeometryStr()).getArea();

        // +海拔
        query.setPreviewHeightRed(query.getPreviewHeightBlue() + getBaseHeight(query.getTargetLng(), query.getTargetLat()));

        //考虑季节
        //夏秋
        if (query.getSeason().equals("summer_fall")) {
            for (VegetationDTO vegetationDTO : query.getVegetationDTOList()) {
                // 计算每个网格与观察点的距离等，并找到最大距离
                CanSeeServiceImpl.fillVisualDomainDemCsv(query.getPreviewHeightBlue(), query.getTargetLng(), query.getTargetLat(), 1, 0, demCsvListRed, vegetationDTO.getVegetationHeight());
            }
        } else {
            // 计算每个网格与观察点的距离等，并找到最大距离
            CanSeeServiceImpl.fillDemCsv(query.getPreviewHeightBlue(), query.getTargetLng(), query.getTargetLat(), 1, 0, demCsvListRed);
        }

        double maxDistance = -1;
        for (DemCsv demCsv1 : demCsvListRed) {
            maxDistance = Math.max(maxDistance, demCsv1.getDistance());
        }

        Geometry area = GeoUtil.getGeometry(query.getRedDeployRange());
        Geometry point = getPoint(query.getTargetLng(), query.getTargetLat());
        boolean inArea = area.contains(point);

        for (int i = 0; i < 720; i++) {
            double angle = 2 * Math.PI / 720 * i;
            Geometry line = getLineByAngleAndPlusRadius(query.getTargetLng(), query.getTargetLat(), angle, maxDistance);

            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvListRed.stream()
                    .filter(temp -> line.intersects(temp.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            List<RangeVO> subResult = new ArrayList<>();
            if (!subDemCsvList.isEmpty()) {
                int currentCanHide = 0;
                double currentStartLng = query.getTargetLng(), currentStartLat = query.getTargetLat();
                double maxGradient = subDemCsvList.get(0).getGradient();

                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanHide(0);
                        if (!inArea) {
                            currentStartLng = targetDemCsv.getCenterLng();
                            currentStartLat = targetDemCsv.getCenterLat();
                            Geometry boundary = GeoUtil.getGeometry(query.getRedDeployRange()).getBoundary();
                            double min = Double.MAX_VALUE;
                            Geometry intersection = line.intersection(boundary);
                            Coordinate[] coordinates = intersection.getCoordinates();
                            if (coordinates.length > 0) {
                                for (Coordinate coordinate : coordinates) {
                                    double len = PointUtil.calcDistanceMeter(query.getTargetLat(), query.getTargetLng(), coordinate.y, coordinate.x);
                                    if (len < min) {
                                        min = len;
                                        currentStartLng = coordinate.x;
                                        currentStartLat = coordinate.y;
                                    }
                                }
                            }
                        }
                    } else {
                        if (targetDemCsv.getGradient() <= maxGradient) {
                            targetDemCsv.setCanHide(1);
                        }
                        maxGradient = Math.max(targetDemCsv.getGradient(), maxGradient);
                    }

                    Point lineCentroid = line.intersection(targetDemCsv.getGeometry()).getCentroid();
                    if (j == subDemCsvList.size() - 1) {
                        // subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide));
                        subResult.add(CanSeeServiceImpl.fillRangeVO(query.getTargetLat(), query.getTargetLng(), line, targetDemCsv.getGeometry(), new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide)));
                    } else if (j != 0) {
                        if (targetDemCsv.getCanHide() != currentCanHide) {
                            subResult.add(new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide));
                            currentCanHide = targetDemCsv.getCanHide();
                            currentStartLng = lineCentroid.getX();
                            currentStartLat = lineCentroid.getY();
                        }
                    }
                }

                if (subResult.size() > 0) {
                    RangeVO rangeVO = subResult.get(subResult.size() - 1);
                    Geometry boundary = GeoUtil.getGeometry(query.getRedDeployRange()).getBoundary();
                    double max = -1.0;
                    Geometry intersection = line.intersection(boundary);
                    Coordinate[] coordinates = intersection.getCoordinates();
                    if (coordinates.length > 0) {
                        for (Coordinate coordinate : coordinates) {
                            double len = PointUtil.calcDistanceMeter(query.getTargetLat(), query.getTargetLng(), coordinate.y, coordinate.x);
                            if (len > max) {
                                max = len;
                                rangeVO.setEndLat(coordinate.y);
                                rangeVO.setEndLng(coordinate.x);
                            }
                        }
                    }
                }
            }
            result.add(subResult);
        }

        Double ratio = calculateRatio(result);
        double totalArea = demCsvListRed.size() * 0.03 * 0.03;
        concealmentVO.setTotalArea(totalArea);
        concealmentVO.setCanHideArea(ratio * totalArea);
        concealmentVO.setCanNotHideArea((1 - ratio) * totalArea);
        concealmentVO.setRatio(ratio);
        concealmentVO.setRangeVOList(result);

        // 硬编码，可以根据需要修改
        String[] list0 = {"森林", "工厂", "军队", "公园", "居民区", "矮树"};
        String[] list1 = {"菜地", "墓地", "农田", "草坪", "水"};
        String[] list2 = {"其他", "未分类"};
        Map<Integer, List<DemCsv>> maskDegree2DemCsvListMap = demCsvListRed.stream()
                .collect(Collectors.groupingBy(DemCsv::getMaskDegree));
        List<MaskDegreeAreaVO> maskDegreeAreaVOList = new LinkedList<>();
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("absolute", 0.0, new ArrayList<>()));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("high", 0.0, Arrays.asList(list0)));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("low", 0.0, Arrays.asList(list1)));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("uncertain", 0.0, Arrays.asList(list2)));
        for (Map.Entry<Integer, List<DemCsv>> entry : maskDegree2DemCsvListMap.entrySet()) {
            int index = 3, num = entry.getKey();
            if (num == -1) {
                index = 0;
            } else if (num == 3 || num == 5 || num == 6 || num == 8 || num == 9 || num == 10) {
                index = 1;
            } else if (num == 0 || num == 1 || num == 2 || num == 4 || num == 12) {
                index = 2;
            }

            // 判断隐蔽等级，并且计算面积
            double areaSize = 0.0;
            for (DemCsv csv : entry.getValue()) {
                areaSize += unitArea * GeoUtil.getGeometry(csv.getGeometryStr()).getArea();
                if (index == 0) {
                    csv.setMaskDegreeName("absolute");
                } else if (index == 1) {
                    csv.setMaskDegreeName("high");
                } else if (index == 2) {
                    csv.setMaskDegreeName("low");
                } else if (index == 3) {
                    csv.setMaskDegreeName("uncertain");
                }
            }

            MaskDegreeAreaVO maskDegreeAreaVO = maskDegreeAreaVOList.get(index);
            maskDegreeAreaVO.setArea(maskDegreeAreaVO.getArea() + areaSize);
        }
        concealmentVO.setMaskDegreeAreaVOList(maskDegreeAreaVOList);

        try {
            boolean has = XSyfxServiceImpl.hasNull(concealmentVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return concealmentVO;
    }

    /**
     * 判断伪装程度
     *
     * @param query 判断伪装程度的查询实体类
     * @return
     */
    @Override
    public MaskVO judgeMask(HideDTO query) {
        xSyfxService.checkArea(query.getDeployRange());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        MaskVO maskVO = new MaskVO();

        DemCsv demCsvv = demCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(query.getBaseLng(), query.getBaseLat()));
        if (demCsvv == null) {
            throw new RuntimeException("敌方火力点处无地形数据，无法计算。");
        }

        // 根据部署范围查询网格列表（交叉）
        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getDeployRange());
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("作战区域处无地形数据，无法计算。");
        }

        // 单位面积
        double unitArea = 0.03 * 0.03 / GeoUtil.getGeometry(demCsvList.get(0).getGeometryStr()).getArea();

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));
        // 计算每个网格与观察点的距离等
        CanSeeServiceImpl.fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), query.getGreen(), query.getBuilding(), demCsvList);

        // 找到指定方向範圍內的網格
        for (double i = 0; i < 360; i++) {
            double startAngle = i;
            double endAngle = i + 1;
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> demCsv.getDirection() >= startAngle && demCsv.getDirection() < endAngle)
                    .collect(Collectors.toList());

            if (!subDemCsvList.isEmpty()) {
                boolean firstFlag = true;
                double maxGradient = Double.MIN_VALUE;
                // 找到指定方向範圍內的網格的最大距离，依次找到扇形區域內三十米內的網格
                double maxDistance = subDemCsvList.get(subDemCsvList.size() - 1).getDistance();
                for (int j = 0; j < maxDistance / 30; j++) {
                    double startDistance = j * 30.0;
                    double endDistance = (j + 1) * 30.0;
                    List<DemCsv> subSubDemCsvList = subDemCsvList.stream()
                            .filter(demCsv -> demCsv.getDistance() >= startDistance && demCsv.getDistance() < endDistance)
                            .collect(Collectors.toList());

                    if (!subSubDemCsvList.isEmpty()) {
                        // 计算扇形区域內三十米內的网格的平均高度
                        double averageHeight = subSubDemCsvList.stream()
                                .mapToDouble(DemCsv::getValue)
                                .average().getAsDouble();

                        double gradient = (averageHeight - query.getWeaponPureHeight()) / endDistance;

                        if (firstFlag) {
                            maxGradient = gradient;
                            for (DemCsv demCsv : subSubDemCsvList) {
                                demCsv.setCanHide(1);
                            }
                            firstFlag = false;
                        } else {
                            if (gradient <= maxGradient) {
                                for (DemCsv demCsv : subSubDemCsvList) {
                                    demCsv.setCanHide(1);
                                }
                            } else {
                                maxGradient = gradient;
                                for (DemCsv demCsv : subSubDemCsvList) {
                                    demCsv.setCanHide(0);
                                }
                            }
                        }
                    }
                }
            }
        }

        double canHideAreaSize = 0.0, totalAreaSize = 0.0;
        Geometry area = GeoUtil.getGeometry(query.getDeployRange());
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = demCsv.getGeometry();
            if (!area.contains(geometry) && area.intersects(geometry)) {
                demCsv.setGeometryStr(getGeometryStr(area.intersection(geometry)));
                demCsv.setGeometry(GeoUtil.getGeometry(demCsv.getGeometryStr()));
            }

            totalAreaSize += unitArea * demCsv.getGeometry().getArea();
            if (demCsv.getCanHide() == 1) {
                canHideAreaSize += unitArea * demCsv.getGeometry().getArea();
                demCsv.setMaskDegree(-1);
            } else {
                demCsv.setMaskDegree(getDegreeByType(demCsv.getType()));
            }

            demCsv.setCanSee(-1);
            demCsv.setGeometry(null);
        }

        maskVO.setCanHideArea(canHideAreaSize);
        maskVO.setCanNotHideArea(totalAreaSize - canHideAreaSize);
        maskVO.setTotalArea(totalAreaSize);
        maskVO.setRatio(maskVO.getCanHideArea() / totalAreaSize);

        List<EquipMaskNumVO> equipMaskNumVOList = new ArrayList<>();
        Map<String, Integer> equipMaskNumMap = new HashMap<>();
        for (JdEquip equip : jdEquipMapper.selectAll()) {
            int num = Math.max(1, ((int) ((maskVO.getCanHideArea() * 1000 * 1000) / (Math.PI * equip.getRadius() * equip.getRadius()))));
            equipMaskNumMap.put(equip.getType(), num);
        }
        for (Map.Entry<String, Integer> entry : equipMaskNumMap.entrySet()) {
            equipMaskNumVOList.add(new EquipMaskNumVO(entry.getKey(), entry.getValue()));
        }
        maskVO.setEquipMaskNumVOList(equipMaskNumVOList);

        List<MaskDegreeVO> maskDegreeVOList = new ArrayList<>();
        Map<Integer, List<DemCsv>> maskDegree2DemCsvListMap = demCsvList.stream()
                .collect(Collectors.groupingBy(DemCsv::getMaskDegree));
        for (Map.Entry<Integer, List<DemCsv>> entry : maskDegree2DemCsvListMap.entrySet()) {
            maskDegreeVOList.add(new MaskDegreeVO(entry.getKey(), entry.getValue()));
        }
        maskVO.setMaskDegreeVOList(maskDegreeVOList);

        // 硬编码，可以根据需要修改
        String[] list0 = {"森林", "工厂", "军队", "公园", "居民区", "矮树"};
        String[] list1 = {"菜地", "墓地", "农田", "草坪", "水"};
        String[] list2 = {"其他", "未分类"};

        List<MaskDegreeAreaVO> maskDegreeAreaVOList = new LinkedList<>();
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("absolute", 0.0, new ArrayList<>()));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("high", 0.0, Arrays.asList(list0)));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("low", 0.0, Arrays.asList(list1)));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("uncertain", 0.0, Arrays.asList(list2)));
        for (Map.Entry<Integer, List<DemCsv>> entry : maskDegree2DemCsvListMap.entrySet()) {
            int index = 3, num = entry.getKey();
            if (num == -1) {
                index = 0;
            } else if (num == 3 || num == 5 || num == 6 || num == 8 || num == 9 || num == 10) {
                index = 1;
            } else if (num == 0 || num == 1 || num == 2 || num == 4 || num == 12) {
                index = 2;
            }

            // 判断隐蔽等级，并且计算面积
            double areaSize = 0.0;
            for (DemCsv csv : entry.getValue()) {
                areaSize += unitArea * GeoUtil.getGeometry(csv.getGeometryStr()).getArea();
                if (index == 0) {
                    csv.setMaskDegreeName("absolute");
                } else if (index == 1) {
                    csv.setMaskDegreeName("high");
                } else if (index == 2) {
                    csv.setMaskDegreeName("low");
                } else if (index == 3) {
                    csv.setMaskDegreeName("uncertain");
                }
            }

            MaskDegreeAreaVO maskDegreeAreaVO = maskDegreeAreaVOList.get(index);
            maskDegreeAreaVO.setArea(maskDegreeAreaVO.getArea() + areaSize);
        }
        maskVO.setMaskDegreeAreaVOList(maskDegreeAreaVOList);

        try {
            boolean has = XSyfxServiceImpl.hasNull(maskVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return maskVO;
    }

    /**
     * 伪装程度分析
     * @param query 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的查询请求体
     * @return 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的返回结果
     */
    @Override
    public MaskVO camouflageAnalysis(DasqueradeDTO query) {
        xSyfxService.checkArea(query.getAppointmentArea());
        xSyfxService.checkPoint(query.getBaseLng(), query.getBaseLat());

        MaskVO maskVO = new MaskVO();

        DemCsv demCsvv = demCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(query.getBaseLng(), query.getBaseLat()));
        if (demCsvv == null) {
            throw new RuntimeException("敌方火力点处无地形数据，无法计算。");
        }

        // 根据部署范围查询网格列表（交叉）
        List<DemCsv> demCsvList = demCsvMapper.selectByArea(query.getAppointmentArea());
        if (demCsvList == null || demCsvList.isEmpty()) {
            throw new RuntimeException("作战区域处无地形数据，无法计算。");
        }

        // 单位面积
        double unitArea = 0.03 * 0.03 / GeoUtil.getGeometry(demCsvList.get(0).getGeometryStr()).getArea();

        // +海拔
        query.setWeaponPureHeight(query.getWeaponPureHeight() + getBaseHeight(query.getBaseLng(), query.getBaseLat()));


        //考虑季节
        //夏秋
        if (query.getSeason().equals("summer_fall")) {
            for (VegetationDTO vegetationDTO : query.getVegetationDTOList()) {
                // 计算每个网格与观察点的距离等，并找到最大距离
                CanSeeServiceImpl.fillVisualDomainDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), 1, 0, demCsvList, vegetationDTO.getVegetationHeight());
            }
        } else {
            // 计算每个网格与观察点的距离等
            CanSeeServiceImpl.fillDemCsv(query.getWeaponPureHeight(), query.getBaseLng(), query.getBaseLat(), 1, 0, demCsvList);
        }

        // 找到指定方向範圍內的網格
        for (double i = 0; i < 360; i++) {
            double startAngle = i;
            double endAngle = i + 1;
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> demCsv.getDirection() >= startAngle && demCsv.getDirection() < endAngle)
                    .collect(Collectors.toList());

            if (!subDemCsvList.isEmpty()) {
                boolean firstFlag = true;
                double maxGradient = Double.MIN_VALUE;
                // 找到指定方向範圍內的網格的最大距离，依次找到扇形區域內三十米內的網格
                double maxDistance = subDemCsvList.get(subDemCsvList.size() - 1).getDistance();
                for (int j = 0; j < maxDistance / 30; j++) {
                    double startDistance = j * 30.0;
                    double endDistance = (j + 1) * 30.0;
                    List<DemCsv> subSubDemCsvList = subDemCsvList.stream()
                            .filter(demCsv -> demCsv.getDistance() >= startDistance && demCsv.getDistance() < endDistance)
                            .collect(Collectors.toList());

                    if (!subSubDemCsvList.isEmpty()) {
                        // 计算扇形区域內三十米內的网格的平均高度
                        double averageHeight = subSubDemCsvList.stream()
                                .mapToDouble(DemCsv::getValue)
                                .average().getAsDouble();

                        double gradient = (averageHeight - query.getWeaponPureHeight()) / endDistance;

                        if (firstFlag) {
                            maxGradient = gradient;
                            for (DemCsv demCsv : subSubDemCsvList) {
                                demCsv.setCanHide(1);
                            }
                            firstFlag = false;
                        } else {
                            if (gradient <= maxGradient) {
                                for (DemCsv demCsv : subSubDemCsvList) {
                                    demCsv.setCanHide(1);
                                }
                            } else {
                                maxGradient = gradient;
                                for (DemCsv demCsv : subSubDemCsvList) {
                                    demCsv.setCanHide(0);
                                }
                            }
                        }
                    }
                }
            }
        }

        double canHideAreaSize = 0.0, totalAreaSize = 0.0;
        Geometry area = GeoUtil.getGeometry(query.getAppointmentArea());
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = demCsv.getGeometry();
            if (!area.contains(geometry) && area.intersects(geometry)) {
                demCsv.setGeometryStr(getGeometryStr(area.intersection(geometry)));
                demCsv.setGeometry(GeoUtil.getGeometry(demCsv.getGeometryStr()));
            }

            totalAreaSize += unitArea * demCsv.getGeometry().getArea();
            if (demCsv.getCanHide() == 1) {
                canHideAreaSize += unitArea * demCsv.getGeometry().getArea();
                demCsv.setMaskDegree(-1);
            } else {
                demCsv.setMaskDegree(getDegreeByType(demCsv.getType()));
            }

            demCsv.setCanSee(-1);
            demCsv.setGeometry(null);
        }

        maskVO.setCanHideArea(canHideAreaSize);
        maskVO.setCanNotHideArea(totalAreaSize - canHideAreaSize);
        maskVO.setTotalArea(totalAreaSize);
        maskVO.setRatio(maskVO.getCanHideArea() / totalAreaSize);

        List<EquipMaskNumVO> equipMaskNumVOList = new ArrayList<>();
        Map<String, Integer> equipMaskNumMap = new HashMap<>();
        for (JdEquip equip : jdEquipMapper.selectAll()) {
            int num = Math.max(1, ((int) ((maskVO.getCanHideArea() * 1000 * 1000) / (Math.PI * equip.getRadius() * equip.getRadius()))));
            equipMaskNumMap.put(equip.getType(), num);
        }
        for (Map.Entry<String, Integer> entry : equipMaskNumMap.entrySet()) {
            equipMaskNumVOList.add(new EquipMaskNumVO(entry.getKey(), entry.getValue()));
        }
        maskVO.setEquipMaskNumVOList(equipMaskNumVOList);

        List<MaskDegreeVO> maskDegreeVOList = new ArrayList<>();
        Map<Integer, List<DemCsv>> maskDegree2DemCsvListMap = demCsvList.stream()
                .collect(Collectors.groupingBy(DemCsv::getMaskDegree));
        for (Map.Entry<Integer, List<DemCsv>> entry : maskDegree2DemCsvListMap.entrySet()) {
            maskDegreeVOList.add(new MaskDegreeVO(entry.getKey(), entry.getValue()));
        }
        maskVO.setMaskDegreeVOList(maskDegreeVOList);

        // 硬编码，可以根据需要修改
        String[] list0 = {"森林", "工厂", "军队", "公园", "居民区", "矮树"};
        String[] list1 = {"菜地", "墓地", "农田", "草坪", "水"};
        String[] list2 = {"其他", "未分类"};

        List<MaskDegreeAreaVO> maskDegreeAreaVOList = new LinkedList<>();
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("absolute", 0.0, new ArrayList<>()));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("high", 0.0, Arrays.asList(list0)));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("low", 0.0, Arrays.asList(list1)));
        maskDegreeAreaVOList.add(new MaskDegreeAreaVO("uncertain", 0.0, Arrays.asList(list2)));
        for (Map.Entry<Integer, List<DemCsv>> entry : maskDegree2DemCsvListMap.entrySet()) {
            int index = 3, num = entry.getKey();
            if (num == -1) {
                index = 0;
            } else if (num == 3 || num == 5 || num == 6 || num == 8 || num == 9 || num == 10) {
                index = 1;
            } else if (num == 0 || num == 1 || num == 2 || num == 4 || num == 12) {
                index = 2;
            }

            // 判断隐蔽等级，并且计算面积
            double areaSize = 0.0;
            for (DemCsv csv : entry.getValue()) {
                areaSize += unitArea * GeoUtil.getGeometry(csv.getGeometryStr()).getArea();
                if (index == 0) {
                    csv.setMaskDegreeName("absolute");
                } else if (index == 1) {
                    csv.setMaskDegreeName("high");
                } else if (index == 2) {
                    csv.setMaskDegreeName("low");
                } else if (index == 3) {
                    csv.setMaskDegreeName("uncertain");
                }
            }

            MaskDegreeAreaVO maskDegreeAreaVO = maskDegreeAreaVOList.get(index);
            maskDegreeAreaVO.setArea(maskDegreeAreaVO.getArea() + areaSize);
        }
        maskVO.setMaskDegreeAreaVOList(maskDegreeAreaVOList);

        try {
            boolean has = XSyfxServiceImpl.hasNull(maskVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return maskVO;
    }

    /**
     * 计算通视/隐蔽线段占比
     *
     * @param result 360度方向上的每条线
     * @return 通视/隐蔽线段占比
     */
    public static Double calculateRatio(List<List<RangeVO>> result) {
        double total = 0.0, flag = 0.0;
        // 遍历每一个方向
        for (List<RangeVO> rangeVOList : result) {
            // 遍历每一个方向上的线段
            for (int i = 0; i < rangeVOList.size(); i++) {
                RangeVO rangeVO = rangeVOList.get(i);
                double[] baseXY = GeoUtil.wgs84ToMercator(rangeVO.getStartLng(), rangeVO.getStartLat());
                double[] targetXY = GeoUtil.wgs84ToMercator(rangeVO.getEndLng(), rangeVO.getEndLat());
                // 计算相对长度
                double length = Math.sqrt(Math.pow(baseXY[0] - targetXY[0], 2) + (Math.pow(baseXY[1] - targetXY[1], 2)));
                total += length;
                if (rangeVO.getCanSee() == 1 || rangeVO.getCanHide() == 1) {
                    flag += length;
                }
            }
        }
        if (total == 0) {
            return 0.0;
        }
        return flag / total;
    }

    /**
     * 获取Geometry的wkt字符串
     *
     * @param geometry Geometry对象
     * @return Geometry的wkt字符串
     */
    public static String getGeometryStr(Geometry geometry) {
        Geometry boundary = geometry.getBoundary();
        Coordinate[] coordinates = boundary.getCoordinates();
        if (coordinates.length == 0) {
            return "POLYGON (())";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("POLYGON ((");
        // 遍历每一个坐标
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate coordinate = coordinates[i];
            sb.append(coordinate.x + " " + coordinate.y + ", ");
        }
        sb.append(coordinates[0].x + " " + coordinates[0].y + "))");
        return sb.toString();
    }
}
