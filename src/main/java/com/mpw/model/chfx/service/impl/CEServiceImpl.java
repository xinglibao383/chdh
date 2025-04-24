package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.dto.AreaEquipDTO;
import com.mpw.model.chfx.domain.entity.CHSpeed;
import com.mpw.model.chfx.domain.entity.Speed;
import com.mpw.model.chfx.domain.entity.ChRoutePoint;
import com.mpw.model.chfx.domain.entity.ChTypeWeight;
import com.mpw.model.chfx.domain.entity.DemCsvXX;
import com.mpw.model.chfx.service.ICEService;
import com.mpw.model.chfx.domain.entity.JdEquip;
import com.mpw.model.chfx.domain.vo.*;
import com.mpw.model.chfx.mapper.*;
import com.mpw.model.common.enums.DEMType1Enum;
import com.mpw.model.common.util.GeoUtil;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机动影响分析的接口实现类
 */
@Service
public class CEServiceImpl implements ICEService {

    private static final double POLE = 2.003750834E7D;

    @Autowired
    private DemCsvXXMapper demCsvMapper;

    @Autowired
    private XJdEquipSpeedMapper speedMapper;

    @Autowired
    private CHSpeedMapper chSpeedMapper;

    @Autowired
    private JdEquipMapper jdEquipMapper;

    @Autowired
    private ChTypeWeightMapper chTypeWeightMapper;

    @Autowired
    private ChRoutePointMapper chRoutePointMapper;

    @Autowired
    private XSyfxServiceImpl xSyfxService;

    private Double find1(List<Speed> list, Long id1, Integer id2) {
        for (Speed speed : list) {
            if (speed.getEquipId() == id1 && speed.getDemType() == id2) {
                return speed.getCoefficient();
            }
        }
        return -1.0;
    }

    private Double find2(List<CHSpeed> list, Long id1, Integer id2) {
        for (CHSpeed speed : list) {
            if (speed.getEquipId() == id1 && speed.getDemType() == id2) {
                return speed.getCoefficient();
            }
        }
        return -1.0;
    }

    private Double find2(List<JdEquip> list, Long id) {
        for (JdEquip equip : list) {
            if (equip.getId() == id) {
                return equip.getCrossSpeed();
            }
        }
        return -1.0;
    }

    /**
     *机动能力分析
     * @param areaRange 区域WKT
     * @return
     */
    @Override
    public MotorAbilityVO motorAbilityAnalysis(String areaRange) {
        xSyfxService.checkArea(areaRange);
        //该区域平均水深
        double waterAverageDepth = 0.0;
        //总面积
        double totalAcreage = 0.0;
        MotorAbilityVO motorAbilityVO=new MotorAbilityVO();
        //根据区域查询该区域所有的网格
        List<DemCsvXX> demCsvList = demCsvMapper.selectByArea(areaRange);
        if (!demCsvList.isEmpty()) {
            //总面积
            totalAcreage = demCsvList.size() * 0.03 * 0.03;
            motorAbilityVO.setTotalAcreage(totalAcreage);

            //越野机动分析VO列表
            double maxGradient = demCsvList.get(0).getGradient();
            for (DemCsvXX demCsv : demCsvList) {
                maxGradient = Math.max(demCsv.getGradient(), maxGradient);
            }

            //坡度等级及面积占比
            List<WildDegreeVO> degreeVOList = new ArrayList<>();
            for (int i = 0; i < maxGradient / 5; i++) {
                double start = i * 5.0;
                double end = (i + 1) * 5.0;
                List<DemCsvXX> subList = demCsvList.stream()
                        .filter(demCsv -> demCsv.getGradient() >= start && demCsv.getGradient() < end)
                        .collect(Collectors.toList());
                double temp = subList.size() * 0.03 * 0.03;
                degreeVOList.add(new WildDegreeVO(i, start, end, totalAcreage, temp, temp / totalAcreage));
            }
            motorAbilityVO.setDegreeVOList(degreeVOList);

            List<DemCsvXX> green = demCsvList.stream()
                    .filter(demCsv -> isGreen(demCsv.getType()))
                    .collect(Collectors.toList());
            if (!green.isEmpty()) {
                //植被面积
                motorAbilityVO.setGreenAcreage(green.size() * 0.03 * 0.03);
                //植被面积占总面积的比例
                motorAbilityVO.setGreenRatio(motorAbilityVO.getGreenAcreage() / totalAcreage);
            }else{
                //植被面积
                motorAbilityVO.setGreenAcreage(0.0);
                //植被面积占总面积的比例
                motorAbilityVO.setGreenRatio(0.0);
            }

            List<DemCsvXX> live = demCsvList.stream()
                    .filter(demCsv -> isLive(demCsv.getType()))
                    .collect(Collectors.toList());
            if (!live.isEmpty()) {
                //居民区面积
                motorAbilityVO.setLiveAcreage(live.size() * 0.03 * 0.03);
                //居民区面积占总面积的比例
                motorAbilityVO.setLiveRatio(motorAbilityVO.getLiveAcreage() / totalAcreage);
            }else{
                //居民区面积
                motorAbilityVO.setLiveAcreage(0.0);
                //居民区面积占总面积的比例
                motorAbilityVO.setLiveRatio(0.0);
            }

            //是否为水域(1为水域)
            List<DemCsvXX> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> demCsv.getIsWater() == 1)
                    .collect(Collectors.toList());
            if (!subDemCsvList.isEmpty()) {
                //该区域水域面积
                double waterAcreage=subDemCsvList.size() * 0.03 * 0.03;
                motorAbilityVO.setWaterAcreage(waterAcreage);
                //该区域水域面积占总面积的比例
                motorAbilityVO.setRatio(waterAcreage / totalAcreage);
                //该区域平均水深
                for (DemCsvXX demCsv : subDemCsvList) {
                    waterAverageDepth += demCsv.getDepthWater();
                }
                waterAverageDepth /= subDemCsvList.size();
                motorAbilityVO.setWaterAverageDepth(waterAverageDepth);
            }
        }


        List<ChRoutePoint> routePointList = chRoutePointMapper.selectByArea(areaRange);
        List<ChTypeWeight> typeWeightList = chTypeWeightMapper.selectAll();

        //计算区域平均梯度
        double avgGradient = demCsvList.stream()
                .mapToDouble(DemCsvXX::getGradient)
                .average().getAsDouble();

        motorAbilityVO.setAreaAvgGradient(avgGradient);

        //计算区域平均载重系数
        double routeAvgLoadWeight = -1.0;
        double areaAvgLoadWeight = -1.0;

        Map<String, List<DemCsvXX>> map1 = demCsvList.stream()
                .collect(Collectors.groupingBy(DemCsvXX::getType));
        int cnt1 = 0;
        double total1 = 0.0;
        for (Map.Entry<String, List<DemCsvXX>> entry : map1.entrySet()) {
            Double weight = find(typeWeightList, entry.getKey());
            if (weight != -1.0) {
                cnt1 += entry.getValue().size();
                total1 += entry.getValue().size() * weight;
            }
        }
        if (cnt1 != 0) {
            areaAvgLoadWeight = total1 / cnt1;
        }
        motorAbilityVO.setAreaAvgLoadWeight(areaAvgLoadWeight);
        //计算道路平均载重
        Map<String, List<ChRoutePoint>> map2 = routePointList.stream()
                .collect(Collectors.groupingBy(ChRoutePoint::getfClass));
        int cnt2 = 0;
        double total2 = 0.0;
        for (Map.Entry<String, List<ChRoutePoint>> entry : map2.entrySet()) {
            Double weight = find(typeWeightList, entry.getKey());
            if (weight != -1.0) {
                cnt2 += entry.getValue().size();
                total2 += entry.getValue().size() * weight;
            }
        }
        if (cnt2 != 0) {
            routeAvgLoadWeight = total2 / cnt2;
        }
        motorAbilityVO.setRouteAvgLoadWeight(routeAvgLoadWeight);

        Double maxGradient = Double.MIN_VALUE;
        Double minGradient = Double.MAX_VALUE;
        Double maxLoadWeight = Double.MIN_VALUE;
        Double minLoadWeight = Double.MAX_VALUE;
        for (DemCsvXX demCsvXX : demCsvList) {
            //最大坡度及经纬度
            if (demCsvXX.getGradient() > maxGradient) {
                maxGradient = demCsvXX.getGradient();
                motorAbilityVO.setMaxGradient(maxGradient);
                Point centroid = getPoint(demCsvXX);
                motorAbilityVO.setMaxGradientLng(centroid.getX());
                motorAbilityVO.setMaxGradientLat(centroid.getY());
            }
            //最小坡度及经纬度
            if (demCsvXX.getGradient() < minGradient) {
                minGradient = demCsvXX.getGradient();
                motorAbilityVO.setMinGradient(minGradient);
                Point centroid = getPoint(demCsvXX);
                motorAbilityVO.setMinGradientLng(centroid.getX());
                motorAbilityVO.setMinGradientLat(centroid.getY());
            }
        }

        for (Map.Entry<String, List<DemCsvXX>> entry : map1.entrySet()) {
            Double weight = find(typeWeightList, entry.getKey());
            if (weight != -1.0) {
                //最小载重及经纬度
                if (weight < minLoadWeight) {
                    minLoadWeight = weight;
                    motorAbilityVO.setMinLoadWeight(minLoadWeight);
                    Point centroid = getPoint(entry.getValue().get(0));
                    motorAbilityVO.setMinLoadWeightLng(centroid.getX());
                    motorAbilityVO.setMinLoadWeightLat(centroid.getY());
                }
                //最大载重及经纬度
                if (weight > maxLoadWeight) {
                    maxLoadWeight = weight;
                    motorAbilityVO.setMaxLoadWeight(maxLoadWeight);
                    Point centroid = getPoint(entry.getValue().get(0));
                    motorAbilityVO.setMaxLoadWeightLng(centroid.getX());
                    motorAbilityVO.setMaxLoadWeightLat(centroid.getY());
                }
            }
        }


        Geometry geometry = GeoUtil.getGeometry(areaRange);

        // 获取多边形内桥梁、隧道等
        List<ChRoutePoint> builds = chRoutePointMapper.selectByArea(areaRange);

        //交通道路
        List<ChRoutePoint> notRoadList = builds.stream()
                .filter(build -> !(build.getBridge().equals("F") && build.getTunnel().equals("F")))
                .collect(Collectors.toList());

        int bridgeHorizontal = 0, bridgeVertical = 0, tunnelHorizontal = 0, tunnelVertical = 0;
        for (ChRoutePoint build : notRoadList) {
            boolean isHorizontal = judgeIsHorizontal(GeoUtil.getGeometry(build.getGeometry()));
            if (build.getBridge().equals("T")) {
                if (isHorizontal) {
                    bridgeHorizontal += 1;
                } else {
                    bridgeVertical += 1;
                }
            } else if (build.getTunnel().equals("T")) {
                if (isHorizontal) {
                    tunnelHorizontal += 1;
                } else {
                    tunnelVertical += 1;
                }
            }
        }

        List<ChRoutePoint> roadList = builds.stream()
                .filter(build -> build.getBridge().equals("F") && build.getTunnel().equals("F"))
                .collect(Collectors.toList());

        List<ChRoutePoint> horizontalRoadList = roadList.stream()
                .filter(road -> judgeIsHorizontal(GeoUtil.getGeometry(road.getGeometry())))
                .collect(Collectors.toList());

        List<ChRoutePoint> verticalRoadList = roadList.stream()
                .filter(road -> !judgeIsHorizontal(GeoUtil.getGeometry(road.getGeometry())))
                .collect(Collectors.toList());

        List<ChRoutePoint> throughRoadList = roadList.stream()
                .filter(build -> geometry.contains(GeoUtil.getGeometry(build.getGeometry())))
                .collect(Collectors.toList());

        //水平桥梁数量
        motorAbilityVO.setBridgeHorizontal(bridgeHorizontal);
        //垂直桥梁数量
        motorAbilityVO.setBridgeVertical(bridgeVertical);
        //水平隧道数量
        motorAbilityVO.setTunnelHorizontal(tunnelHorizontal);
        //垂直隧道数量
        motorAbilityVO.setTunnelVertical(tunnelVertical);
        //水平道路数量
        motorAbilityVO.setRoadHorizontal(horizontalRoadList.size());
        //垂直道路数量
        motorAbilityVO.setRoadVertical(verticalRoadList.size());
        //可通行道路数量
        motorAbilityVO.setThroughRoad(throughRoadList.size());
        //水平道路总长度
        motorAbilityVO.setRoadHorizontalSumLength(horizontalRoadList.stream().mapToDouble(ChRoutePoint::getLength).sum() / 1000);
        //垂直道路总长度
        motorAbilityVO.setRoadVerticalSumLength(verticalRoadList.stream().mapToDouble(ChRoutePoint::getLength).sum() / 1000);

        //可通行性与速度分析
        List<PassableVO> passableVOList = new ArrayList<>();
        //获取载具数据
        List<JdEquip> equipList = jdEquipMapper.selectAll();
        //获取装备在不同地质速度系数
        List<CHSpeed> cHSpeedList = chSpeedMapper.selectAll();
        //查询所有的装备速度系数
        List<Speed> speedList = speedMapper.selectAll();
        for (JdEquip jdEquip : equipList) {
            double canPassAcreage = 0.0;
            PassableVO passableVO=new PassableVO();
            //装备编号id
            passableVO.setEquipId(jdEquip.getId());
            //载具名称
            passableVO.setEquipName(jdEquip.getType());
            //是否可通行
            passableVO.setWhetherPassable(0);
            //可以通行的面积
            if (!demCsvList.isEmpty()) {
                //是否可通行(0:不可通行;1:可通行)
                passableVO.setWhetherPassable(1);
                int canPassCnt = 0;
                Map<String, List<DemCsvXX>> map = demCsvList.stream()
                        .collect(Collectors.groupingBy(DemCsvXX::getType));

                for (Map.Entry<String, List<DemCsvXX>> entry : map.entrySet()) {

                    Double coefficient = find2(cHSpeedList, jdEquip.getId(), getDegreeByType(entry.getKey()));
                    if (coefficient != -1.0) {
                        canPassCnt += entry.getValue().size();
                    }
                }
                canPassAcreage = canPassCnt * 0.03 * 0.03;
                passableVO.setCanPassAcreage(canPassAcreage);
                passableVO.setCanPassRatio(canPassAcreage / totalAcreage);

                //装备的平均行驶速度
                Double speed = find2(equipList, jdEquip.getId());
                double temp = 0.0;
                int temp2 = 0;
                for (Map.Entry<String, List<DemCsvXX>> entry : map.entrySet()) {
                    Double coefficient = find1(speedList, jdEquip.getId(), getDegreeByType(entry.getKey()));
                    if (coefficient != -1.0) {
                        temp += entry.getValue().size() * speed * coefficient;
                        temp2 += entry.getValue().size();
                    }
                }
                if (temp2 != 0) {
                    passableVO.setAvgSpeed(temp / temp2);
                } else {
                    passableVO.setAvgSpeed(0.0);
                }
                //问题
                passableVO.setPassageQuantity(0);
                passableVO.setPassageQuantityProportion(0.0);
            }
            passableVOList.add(passableVO);
        }

        //可通行性与速度分析
        motorAbilityVO.setPassableVOList(passableVOList);

        try {
            boolean has = XSyfxServiceImpl.hasNull(motorAbilityVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return motorAbilityVO;
    }

    /**
     * 行车速度计算: 平均通行速度（所有装备的）
     *
     * @param areaRange 区域WKT
     * @return 行車速度計算的返回结果实体
     */
    @Override
    public SpeedVO speed(String areaRange) {
        xSyfxService.checkArea(areaRange);

        List<AvgSpeedVO> avgSpeedVOList = new ArrayList<>();
        List<DemCsvXX> demCsvList = demCsvMapper.selectByArea(areaRange);

        List<JdEquip> equipList = jdEquipMapper.selectAll();
        List<Speed> speedList = speedMapper.selectAll();

        for (DemCsvXX demCsvXX : demCsvList) {
            if (demCsvXX.getIsWater() == 1) {
                demCsvXX.setType("water");
            }
        }
        Map<String, List<DemCsvXX>> map = demCsvList.stream()
                .collect(Collectors.groupingBy(DemCsvXX::getType));

        for (JdEquip equip : equipList) {
            Double speed = find2(equipList, equip.getId());
            double temp = 0.0;
            int temp2 = 0;
            for (Map.Entry<String, List<DemCsvXX>> entry : map.entrySet()) {
                // Double coefficient = find1(speedList, equip.getId(), DemTypeEnum.getByName(entry.getKey()).getNum());
                Double coefficient = find1(speedList, equip.getId(), getDegreeByType(entry.getKey()));
                if (coefficient != -1.0) {
                    temp += entry.getValue().size() * speed * coefficient;
                    temp2 += entry.getValue().size();
                }
            }
            if (temp2 != 0) {
                avgSpeedVOList.add(new AvgSpeedVO(equip.getId(), equip.getType(), temp / temp2));
            } else {
                avgSpeedVOList.add(new AvgSpeedVO(equip.getId(), equip.getType(), 0.0));
            }
        }

        SpeedVO res = new SpeedVO(avgSpeedVOList);

        try {
            boolean has = XSyfxServiceImpl.hasNull(res);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 水路机动分析: 涉水平均深度，涉水面积
     *
     * @param areaRange 区域WKT
     * @return 水路機動分析的返回结果实体
     */
    @Override
    public WaterVO water(String areaRange) {
        xSyfxService.checkArea(areaRange);

        double waterAverageDepth = 0.0;
        double totalAcreage = 0.0;
        double waterAcreage = 0.0;
        double ratio = 0.0;

        List<DemCsvXX> demCsvList = demCsvMapper.selectByArea(areaRange);
        if (!demCsvList.isEmpty()) {
            totalAcreage = demCsvList.size() * 0.03 * 0.03;
            List<DemCsvXX> subDemCsvList = demCsvList.stream()
                    .filter(demCsv -> demCsv.getIsWater() == 1)
                    .collect(Collectors.toList());
            if (!subDemCsvList.isEmpty()) {
                waterAcreage = subDemCsvList.size() * 0.03 * 0.03;
                ratio = waterAcreage / totalAcreage;
                for (DemCsvXX demCsv : subDemCsvList) {
                    waterAverageDepth += demCsv.getDepthWater();
                }
                waterAverageDepth /= subDemCsvList.size();
            }
        }

        WaterVO res = new WaterVO(waterAverageDepth, totalAcreage, waterAcreage, ratio);

        try {
            boolean has = XSyfxServiceImpl.hasNull(res);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static Integer getDegreeByType(String type) {
        for (DEMType1Enum value : DEMType1Enum.values()) {
            if (value.getStr().equals(type)) {
                return value.getNum();
            }
        }
        return DEMType1Enum.UNCLASSIFIED.getNum();
    }


    /**
     * 可通行性分析: 总面积 可通行占比 可通行面积
     *
     * @param areaEquipDTO 可通行性分析的请求体
     * @return 可通行性分析的返回结果实体
     */
    @Override
    public PassVO pass(AreaEquipDTO areaEquipDTO) {
        xSyfxService.checkArea(areaEquipDTO.getAreaRange());

        double totalAcreage = 0.0;
        double canPassAcreage = 0.0;

        List<DemCsvXX> demCsvList = demCsvMapper.selectByArea(areaEquipDTO.getAreaRange());
        for (DemCsvXX demCsv : demCsvList) {
            if (demCsv.getIsWater() == 1) {
                demCsv.setType("water");
            }
        }

        List<CHSpeed> speedList = chSpeedMapper.selectAll();
        List<JdEquip> equipList = jdEquipMapper.selectAll();

        JdEquip equip = null;
        for (JdEquip jdEquip : equipList) {
            if (jdEquip.getId() == areaEquipDTO.getEquipId()) {
                equip = jdEquip;
                break;
            }
        }

        if (!demCsvList.isEmpty()) {
            totalAcreage = demCsvList.size() * 0.03 * 0.03;
            if (equip != null) {
                int canPassCnt = 0;
                Map<String, List<DemCsvXX>> map = demCsvList.stream()
                        .collect(Collectors.groupingBy(DemCsvXX::getType));
                for (Map.Entry<String, List<DemCsvXX>> entry : map.entrySet()) {
                    Double coefficient = find2(speedList, equip.getId(), getDegreeByType(entry.getKey()));
                    if (coefficient != -1.0) {
                        canPassCnt += entry.getValue().size();
                    }
                }
                canPassAcreage = canPassCnt * 0.03 * 0.03;
            }
        }

        PassVO res = new PassVO(totalAcreage, canPassAcreage, canPassAcreage / totalAcreage);

        try {
            boolean has = XSyfxServiceImpl.hasNull(res);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 越野機動分析
     *
     * @param areaRange 区域WKT
     * @return 越野機動分析的返回结果实体
     */
    @Override
    public WildVO wild(String areaRange) {
        xSyfxService.checkArea(areaRange);

        WildVO wildVO = new WildVO();

        List<DemCsvXX> demCsvList = demCsvMapper.selectByArea(areaRange);
        if (!demCsvList.isEmpty()) {
            double totalArea = demCsvList.size() * 0.03 * 0.03;
            wildVO.setTotalAcreage(totalArea);
            double maxGradient = demCsvList.get(0).getGradient();
            for (DemCsvXX demCsv : demCsvList) {
                maxGradient = Math.max(demCsv.getGradient(), maxGradient);
            }

            List<WildDegreeVO> degreeVOList = new ArrayList<>();
            for (int i = 0; i < maxGradient / 5; i++) {
                double start = i * 5.0;
                double end = (i + 1) * 5.0;
                List<DemCsvXX> subList = demCsvList.stream()
                        .filter(demCsv -> demCsv.getGradient() >= start && demCsv.getGradient() < end)
                        .collect(Collectors.toList());
                double temp = subList.size() * 0.03 * 0.03;
                degreeVOList.add(new WildDegreeVO(i, start, end, totalArea, temp, temp / totalArea));
            }
            wildVO.setDegreeVOList(degreeVOList);

            List<DemCsvXX> green = demCsvList.stream()
                    .filter(demCsv -> isGreen(demCsv.getType()))
                    .collect(Collectors.toList());
            wildVO.setGreenAcreage(green.size() * 0.03 * 0.03);
            wildVO.setGreenRatio(wildVO.getGreenAcreage() / totalArea);

            List<DemCsvXX> live = demCsvList.stream()
                    .filter(demCsv -> isLive(demCsv.getType()))
                    .collect(Collectors.toList());
            wildVO.setLiveAcreage(live.size() * 0.03 * 0.03);
            wildVO.setLiveRatio(wildVO.getLiveAcreage() / totalArea);
        }

        try {
            boolean has = XSyfxServiceImpl.hasNull(wildVO);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return wildVO;
    }

    /**
     * 越野機動分析
     *
     * @param areaRange 区域WKT
     * @return 通行能力分析的返回结果实体
     */
    @Override
    public PassAbilityVO passAbility(String areaRange) {
        xSyfxService.checkArea(areaRange);

        Double maxGradient = Double.MIN_VALUE;
        Double minGradient = Double.MAX_VALUE;
        Double maxLoadWeight = Double.MIN_VALUE;
        Double minLoadWeight = Double.MAX_VALUE;
        List<DemCsvXX> demCsvList = demCsvMapper.selectByArea(areaRange);
        List<ChRoutePoint> routePointList = chRoutePointMapper.selectByArea(areaRange);
        List<ChTypeWeight> typeWeightList = chTypeWeightMapper.selectAll();

        double avgGradient = demCsvList.stream()
                .mapToDouble(DemCsvXX::getGradient)
                .average().getAsDouble();

        double routeAvgLoadWeight = -1.0;
        double areaAvgLoadWeight = -1.0;

        Map<String, List<DemCsvXX>> map1 = demCsvList.stream()
                .collect(Collectors.groupingBy(DemCsvXX::getType));
        int cnt1 = 0;
        double total1 = 0.0;
        for (Map.Entry<String, List<DemCsvXX>> entry : map1.entrySet()) {
            Double weight = find(typeWeightList, entry.getKey());
            if (weight != -1.0) {
                cnt1 += entry.getValue().size();
                total1 += entry.getValue().size() * weight;
            }
        }
        if (cnt1 != 0) {
            areaAvgLoadWeight = total1 / cnt1;
        }

        Map<String, List<ChRoutePoint>> map2 = routePointList.stream()
                .collect(Collectors.groupingBy(ChRoutePoint::getfClass));
        int cnt2 = 0;
        double total2 = 0.0;
        for (Map.Entry<String, List<ChRoutePoint>> entry : map2.entrySet()) {
            Double weight = find(typeWeightList, entry.getKey());
            if (weight != -1.0) {
                cnt2 += entry.getValue().size();
                total2 += entry.getValue().size() * weight;
            }
        }
        if (cnt2 != 0) {
            routeAvgLoadWeight = total2 / cnt2;
        }
        PassAbilityVO result = new PassAbilityVO(routeAvgLoadWeight, areaAvgLoadWeight, avgGradient);

        for (DemCsvXX demCsvXX : demCsvList) {
            if (demCsvXX.getGradient() > maxGradient) {
                maxGradient = demCsvXX.getGradient();
                result.setMaxGradient(maxGradient);
                Point centroid = getPoint(demCsvXX);
                result.setMaxGradientLng(centroid.getX());
                result.setMaxGradientLat(centroid.getY());
            }
            if (demCsvXX.getGradient() < minGradient) {
                minGradient = demCsvXX.getGradient();
                result.setMinGradient(minGradient);
                Point centroid = getPoint(demCsvXX);
                result.setMinGradientLng(centroid.getX());
                result.setMinGradientLat(centroid.getY());
            }
        }

        for (Map.Entry<String, List<DemCsvXX>> entry : map1.entrySet()) {
            Double weight = find(typeWeightList, entry.getKey());
            if (weight != -1.0) {
                if (weight < minLoadWeight) {
                    minLoadWeight = weight;
                    result.setMinLoadWeight(minLoadWeight);
                    Point centroid = getPoint(entry.getValue().get(0));
                    result.setMinLoadWeightLng(centroid.getX());
                    result.setMinLoadWeightLat(centroid.getY());
                }
                if (weight > maxLoadWeight) {
                    maxLoadWeight = weight;
                    result.setMaxLoadWeight(maxLoadWeight);
                    Point centroid = getPoint(entry.getValue().get(0));
                    result.setMaxLoadWeightLng(centroid.getX());
                    result.setMaxLoadWeightLat(centroid.getY());
                }
            }
        }

        try {
            boolean has = XSyfxServiceImpl.hasNull(result);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据DemCsvXX获取Point
     *
     * @param demCsvXX 区域对应的DemCsvXX
     * @return 中心点
     */
    private Point getPoint(DemCsvXX demCsvXX) {
        String geometryStr = demCsvXX.getGeometryStr();
        Geometry geometry = GeoUtil.getGeometry(geometryStr);
        return geometry.getCentroid();
    }

    /**
     * 在所有的载重系数中查找需要的载重系数
     *
     * @param typeWeightList 载重系数列表
     * @param name           名称
     * @return 载重系数
     */
    private static Double find(List<ChTypeWeight> typeWeightList, String name) {
        for (ChTypeWeight chTypeWeight : typeWeightList) {
            if (chTypeWeight.getDemType() != null && chTypeWeight.getDemType().equals(name)) {
                return chTypeWeight.getWeight();
            }

            if (chTypeWeight.getRouteFclass() != null && chTypeWeight.getRouteFclass().equals(name)) {
                return chTypeWeight.getWeight();
            }
        }
        return -1.0;
    }

    /**
     * 判断是否是植被
     *
     * @param type 区域类型
     * @return 是否是植被
     */
    private static boolean isGreen(String type) {
        List<String> list = new ArrayList<>();
        list.add("allotments");
        list.add("farmland");
        list.add("forest");
        list.add("grass");
        list.add("scrub");

        for (String s : list) {
            if (type.equals(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否是居民区
     *
     * @param type 区域类型
     * @return 是否是居民区
     */
    private static boolean isLive(String type) {
        List<String> list = new ArrayList<>();
        list.add("cemetery");
        list.add("industrial");
        list.add("military");
        list.add("park");
        list.add("residential");

        for (String s : list) {
            if (type.equals(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将平面坐标转为经纬度
     *
     * @param xdouble x
     * @param ydouble y
     * @return 经纬度
     */
    public static double[] inverseMercator(double xdouble, double ydouble) {
        double[] result = new double[2];
        result[0] = 180.0D * xdouble / POLE;
        result[1] = 57.29577951308232D * (2.0D * Math.atan(Math.exp(ydouble / POLE * 3.141592653589793D)) - 1.5707963267948966D);
        return result;
    }

    /**
     * 道路機動分析
     *
     * @param areaRange 区域WKT
     * @return 道路機動分析的返回结果实体
     */
    @Override
    public MoveVO move(String areaRange) {
        xSyfxService.checkArea(areaRange);

        Geometry geometry = GeoUtil.getGeometry(areaRange);

        // 获取多边形内桥梁、隧道等
        List<ChRoutePoint> builds = chRoutePointMapper.selectByArea(areaRange);

        List<ChRoutePoint> notRoadList = builds.stream()
                .filter(build -> !(build.getBridge().equals("F") && build.getTunnel().equals("F")))
                .collect(Collectors.toList());

        int bridgeHorizontal = 0, bridgeVertical = 0, tunnelHorizontal = 0, tunnelVertical = 0;
        for (ChRoutePoint build : notRoadList) {
            boolean isHorizontal = judgeIsHorizontal(GeoUtil.getGeometry(build.getGeometry()));
            if (build.getBridge().equals("T")) {
                if (isHorizontal) {
                    bridgeHorizontal += 1;
                } else {
                    bridgeVertical += 1;
                }
            } else if (build.getTunnel().equals("T")) {
                if (isHorizontal) {
                    tunnelHorizontal += 1;
                } else {
                    tunnelVertical += 1;
                }
            }
        }

        List<ChRoutePoint> roadList = builds.stream()
                .filter(build -> build.getBridge().equals("F") && build.getTunnel().equals("F"))
                .collect(Collectors.toList());

        List<ChRoutePoint> horizontalRoadList = roadList.stream()
                .filter(road -> judgeIsHorizontal(GeoUtil.getGeometry(road.getGeometry())))
                .collect(Collectors.toList());

        List<ChRoutePoint> verticalRoadList = roadList.stream()
                .filter(road -> !judgeIsHorizontal(GeoUtil.getGeometry(road.getGeometry())))
                .collect(Collectors.toList());

        List<ChRoutePoint> throughRoadList = roadList.stream()
                .filter(build -> geometry.contains(GeoUtil.getGeometry(build.getGeometry())))
                .collect(Collectors.toList());

        MoveVO result = new MoveVO();
        result.setBridgeHorizontal(bridgeHorizontal);
        result.setBridgeVertical(bridgeVertical);
        result.setTunnelHorizontal(tunnelHorizontal);
        result.setTunnelVertical(tunnelVertical);
        result.setRoadHorizontal(horizontalRoadList.size());
        result.setRoadVertical(verticalRoadList.size());
        result.setThroughRoad(throughRoadList.size());
        result.setRoadHorizontalSumLength(horizontalRoadList.stream().mapToDouble(ChRoutePoint::getLength).sum() / 1000);
        result.setRoadVerticalSumLength(verticalRoadList.stream().mapToDouble(ChRoutePoint::getLength).sum() / 1000);

        try {
            boolean has = XSyfxServiceImpl.hasNull(result);
            if (has) {
                throw new RuntimeException("未计算出结果。");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 判断是否水平
     *
     * @param geometry Geometry对象
     * @return 是否水平
     */
    private static boolean judgeIsHorizontal(Geometry geometry) {
        boolean isHorizontal = true;

        Point centroid = geometry.getCentroid();
        double baseLng = centroid.getX();
        double baseLat = centroid.getY();

        Coordinate[] coordinates = geometry.getBoundary().getCoordinates();
        double targetLng = coordinates[0].x;
        double targetLat = coordinates[0].y;

        // 将两个经纬度转为平面坐标
        double[] baseXY = inverseMercator(baseLng, baseLat);
        double baseX = baseXY[0];
        double baseY = baseXY[1];

        double[] targetXY = inverseMercator(targetLng, targetLat);
        double targetX = targetXY[0];
        double targetY = targetXY[1];

        // 计算tan
        double tan = (targetY - baseY) / (targetX - baseX);

        // 根据tan判断横纵
        if (tan > -1 && tan < 1) {
            isHorizontal = false;
        }

        return isHorizontal;
    }
}
