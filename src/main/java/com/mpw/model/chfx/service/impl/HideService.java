package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.dto.*;
import com.mpw.model.chfx.domain.entity.DemCsv;
import com.mpw.model.chfx.domain.entity.GreenArea;
import com.mpw.model.chfx.domain.vo.*;
import com.mpw.model.chfx.mapper.JdEquipMapper;
import com.mpw.model.chfx.mapper.XDemCsvMapper;
import com.mpw.model.chfx.service.IGreenAreaService;
import com.mpw.model.common.util.GeoUtil;
import com.mpw.model.common.util.PointUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HideService {

    @Autowired
    private XDemCsvMapper demCsvMapper;

    @Autowired
    private JdEquipMapper jdEquipMapper;

    @Autowired
    private IGreenAreaService greenAreaService;

    @Autowired
    private XSyfxServiceImpl xSyfxService;

    public void judge(int weaponPureHeight, String deployArea, List<DemCsv> demCsvList, Double lng, Double lat) {
        DemCsv demCsv = demCsvMapper.selectByPoint(CanSeeServiceImpl.getPointStr(lng, lat));
        if (demCsv == null) {
            throw new RuntimeException("敌方火力点处无地形数据，无法计算。");
        }

        fillDemCsv(weaponPureHeight, lng, lat, demCsvList);
        double maxDistance = -1;
        for (DemCsv demCsv1 : demCsvList) {
            maxDistance = Math.max(maxDistance, demCsv1.getDistance());
        }

        Geometry area = GeoUtil.getGeometry(deployArea);
        Geometry point = CanHideServiceImpl.getPoint(lng, lat);
        boolean inArea = area.contains(point);

        for (int i = 0; i < 720; i++) {
            double angle = 2 * Math.PI / 720 * i;
            Geometry line = CanHideServiceImpl.getLineByAngleAndPlusRadius(lng, lat, angle, maxDistance);

            // 找到与半径直线相交的網格，并根据网格与观察点的距离对网格进行排序
            List<DemCsv> subDemCsvList = demCsvList.stream()
                    .filter(temp -> line.intersects(temp.getGeometry()))
                    .sorted(Comparator.comparing(DemCsv::getDistance))
                    .collect(Collectors.toList());

            List<RangeVO> subResult = new ArrayList<>();
            if (!subDemCsvList.isEmpty()) {
                int currentCanHide = 0;
                double currentStartLng = lng, currentStartLat = lat;
                double maxGradient = subDemCsvList.get(0).getGradient();

                for (int j = 0; j < subDemCsvList.size(); j++) {
                    DemCsv targetDemCsv = subDemCsvList.get(j);
                    if (j == 0) {
                        targetDemCsv.setCanHide(0);
                        if (!inArea) {
                            currentStartLng = targetDemCsv.getCenterLng();
                            currentStartLat = targetDemCsv.getCenterLat();
                            Geometry boundary = GeoUtil.getGeometry(deployArea).getBoundary();
                            double min = Double.MAX_VALUE;
                            Geometry intersection = line.intersection(boundary);
                            Coordinate[] coordinates = intersection.getCoordinates();
                            if (coordinates.length > 0) {
                                for (Coordinate coordinate : coordinates) {
                                    double len = PointUtil.calcDistanceMeter(lat, lng, coordinate.y, coordinate.x);
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
                        subResult.add(CanSeeServiceImpl.fillRangeVO(lat, lng, line, targetDemCsv.getGeometry(), new RangeVO(angle, currentStartLng, currentStartLat, lineCentroid.getX(), lineCentroid.getY(), -1, currentCanHide)));
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
                    Geometry boundary = GeoUtil.getGeometry(deployArea).getBoundary();
                    double max = -1.0;
                    Geometry intersection = line.intersection(boundary);
                    Coordinate[] coordinates = intersection.getCoordinates();
                    if (coordinates.length > 0) {
                        for (Coordinate coordinate : coordinates) {
                            double len = PointUtil.calcDistanceMeter(lat, lng, coordinate.y, coordinate.x);
                            if (len > max) {
                                max = len;
                                rangeVO.setEndLat(coordinate.y);
                                rangeVO.setEndLng(coordinate.x);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 隐蔽概率分析
     * 判断地图上一个区域是否可以隐蔽
     *
     * @param query 判断地图上一个区域是否可以隐蔽的查询请求体
     * @return 判断地图上一个区域是否可以隐蔽的返回结果
     */
    public HidePossibilityVO hiddenProbabilityAnalysis(ConcealmentDTO query) {
        HidePossibilityVO result = new HidePossibilityVO();

        //校验红方任务区域
        xSyfxService.checkArea(query.getRedDeployRange());
        //校验蓝方任务区域
        xSyfxService.checkArea(query.getBlueDeployRange());

        for (FireGroupDTO fireGroupDTO : query.getFireGroupListRed()) {
            xSyfxService.checkPoint(fireGroupDTO.getBaseLng(), fireGroupDTO.getBaseLat());
        }

        for (FireGroupDTO fireGroupDTO : query.getFireGroupListBlue()) {
            xSyfxService.checkPoint(fireGroupDTO.getBaseLng(), fireGroupDTO.getBaseLat());
        }

        HidePossibilityPreDTO preDTO = query.getPossibilityPreDTO();
        List<GreenArea> greenAreas = new ArrayList<>();
        greenAreas.addAll(preDTO.getBlueGreenAreas());
        greenAreas.addAll(preDTO.getRedGreenAreas());

        for (FireGroupDTO fireGroupDTO : query.getFireGroupListBlue()) {
            fireGroupDTO.setWeaponPureHeight(fireGroupDTO.getWeaponPureHeight() + getBaseHeight(fireGroupDTO.getBaseLng(), fireGroupDTO.getBaseLat()));
        }

        for (FireGroupDTO fireGroupDTO : query.getFireGroupListRed()) {
            fireGroupDTO.setWeaponPureHeight(fireGroupDTO.getWeaponPureHeight() + getBaseHeight(fireGroupDTO.getBaseLng(), fireGroupDTO.getBaseLat()));
        }

        // 根据装备部署范围查询网格列表
        List<DemCsv> redDemCsvList = demCsvMapper.selectByArea(query.getRedDeployRange());
        if (redDemCsvList == null || redDemCsvList.isEmpty()) {
            throw new RuntimeException("作战区域处无地形数据，无法计算。");
        } else {
            if (query.getSeason().equals("summer_fall")) {
                for (int i = 0; i < redDemCsvList.size(); i++) {
                    DemCsv temp = redDemCsvList.get(i);
                    for (GreenArea greenArea : greenAreas) {
                        if (GeoUtil.getGeometry(greenArea.getArea()).contains(GeoUtil.getGeometry(temp.getGeometryStr()))) {
                            temp.setValue((int) (temp.getValue() + greenArea.getHeight()));
                        }
                    }
                }
            }

            for (FireGroupDTO fireGroupDTO : query.getFireGroupListBlue()) {
                judge(fireGroupDTO.getWeaponPureHeight(), query.getRedDeployRange(), redDemCsvList, fireGroupDTO.getBaseLng(), fireGroupDTO.getBaseLat());
            }

            int count = 0;
            for (DemCsv demCsv : redDemCsvList) {
                if (demCsv.getCanHide() == 1) ++count;
            }

            result.setCanHideAreaRed(count * 0.03 * 0.03);
            result.setCanNotHideAreaRed((redDemCsvList.size() - count) * 0.03 * 0.03);
            result.setTotalAreaRed(redDemCsvList.size() * 0.03 * 0.03);
            result.setRatioRed(result.getCanHideAreaRed() / result.getTotalAreaRed());
            result.setDemCsvListRed(redDemCsvList);
        }

        // 根据装备部署范围查询网格列表
        List<DemCsv> blueDemCsvList = demCsvMapper.selectByArea(query.getBlueDeployRange());
        if (blueDemCsvList == null || blueDemCsvList.isEmpty()) {
            throw new RuntimeException("作战区域处无地形数据，无法计算。");
        } else {
            for (int i = 0; i < blueDemCsvList.size(); i++) {
                DemCsv temp = blueDemCsvList.get(i);
                if (query.getSeason().equals("summer_fall")) {
                    for (GreenArea greenArea : greenAreas) {
                        if (GeoUtil.getGeometry(greenArea.getArea()).contains(GeoUtil.getGeometry(temp.getGeometryStr()))) {
                            temp.setValue((int) (temp.getValue() + greenArea.getHeight()));
                        }
                    }
                }

                for (FireGroupDTO fireGroupDTO : query.getFireGroupListRed()) {
                    judge(fireGroupDTO.getWeaponPureHeight(), query.getBlueDeployRange(), blueDemCsvList, fireGroupDTO.getBaseLng(), fireGroupDTO.getBaseLat());
                }
            }

            int count = 0;
            for (DemCsv demCsv : blueDemCsvList) {
                if (demCsv.getCanHide() == 1) ++count;
            }

            result.setCanHideAreaBlue(count * 0.03 * 0.03);
            result.setCanNotHideAreaBlue((redDemCsvList.size() - count) * 0.03 * 0.03);
            result.setTotalAreaBlue(redDemCsvList.size() * 0.03 * 0.03);
            result.setRatioBlue(result.getCanHideAreaBlue() / result.getTotalAreaBlue());
            result.setDemCsvListBlue(blueDemCsvList);
        }

        return result;
    }

    public Integer getBaseHeight(Double baseLng, Double baseLat) {
        String pointStr = String.format("POINT(%f %f)", baseLng, baseLat);
        DemCsv demCsv = demCsvMapper.selectByPoint(pointStr);
        if (demCsv != null) {
            return demCsv.getValue();
        }
        return 0;
    }

    public static double calculateDirection(Double baseLng, Double baseLat, Double targetLng, Double targetLat) {
        GlobalCoordinates base = new GlobalCoordinates(baseLat, baseLng);
        GlobalCoordinates target = new GlobalCoordinates(targetLat, targetLng);
        GeodeticCurve curve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, base, target);
        return curve.getAzimuth();
    }

    public static void fillDemCsv(Integer pureHeight, Double baseLng, Double baseLat, List<DemCsv> demCsvList) {
        for (DemCsv demCsv : demCsvList) {
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            demCsv.setGeometry(geometry);
        }

        for (DemCsv demCsv : demCsvList) {
            Point centroid = demCsv.getGeometry().getCentroid();
            demCsv.setCenterLng(centroid.getX());
            demCsv.setCenterLat(centroid.getY());
            demCsv.setDistance(PointUtil.calcDistanceMeter(baseLat, baseLng, demCsv.getCenterLat(), demCsv.getCenterLng()));
            demCsv.setGradient((demCsv.getValue() - pureHeight) / demCsv.getDistance());
            demCsv.setDirection(calculateDirection(baseLng, baseLat, demCsv.getCenterLng(), demCsv.getCenterLat()));
        }
    }
}
