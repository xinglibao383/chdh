package com.mpw.model.jdfx.controller;

import com.mpw.model.common.constant.Constant;
import com.mpw.model.jdfx.dto.NavQueryDTO;
import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.dto.PointVO;
import com.mpw.model.jdfx.dto.RoadVO;
import com.mpw.model.jdfx.entity.RouteLine;
import com.mpw.model.jdfx.service.IDEMRoutingService;
import com.mpw.model.jdfx.service.IDStarRoutingService;
import com.mpw.model.jdfx.service.IEquipService;
import com.mpw.model.jdfx.service.IRoadNetworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("nav")
@AllArgsConstructor
@Api(tags = "路径规划")
public class NavController {
    @Autowired
    private IDEMRoutingService routingService;
    @Autowired
    private IRoadNetworkService roadNetworkService;
    @Autowired
    private IEquipService equipService;
    @Autowired
    private IDStarRoutingService dStarRoutingService;

    @PostMapping("getPath")
    @ApiOperation("推荐DEM线路 距离优先")
    public PointVO getPath(@RequestBody NavQueryDTO query) {
        long start = System.currentTimeMillis();
        List<PointDTO> points;
        if (Constant.A_STAR.equals(query.getType())) {
            points = routingService.AStarShortestPath(query.getStart(), query.getEnd());
        } else if (Constant.DIJKSTRA.equals(query.getType())) {
            points = routingService.DijkstraShortestPath(query.getStart(), query.getEnd());
        } else {
            points = dStarRoutingService.demShortestPath(query.getStart(), query.getEnd(), false);
        }
        return getPointVO(query, start, points);
    }

    @PostMapping("getPathNoWater")
    @ApiOperation("推荐DEM线路 避免涉水")
    public PointVO getPathNoWater(@RequestBody NavQueryDTO query) {
        long start = System.currentTimeMillis();
        List<PointDTO> points;
        if (Constant.A_STAR.equals(query.getType())) {
            points = routingService.AStarShorNoWaterPath(query.getStart(), query.getEnd());
        } else if (Constant.D_STAR.equals(query.getType())) {
            points = dStarRoutingService.demShortestPath(query.getStart(), query.getEnd(), true);
        } else {
            points = routingService.DijkstraNoWaterPath(query.getStart(), query.getEnd());
        }
        return getPointVO(query, start, points);
    }

    //时间优先 为每个装备在每种地形上设置速度系数  真实速度 = 系数 * 设备表速度
    @ApiOperation("推荐DEM线路 时间优先")
    @PostMapping("/getPathTimeFirst")
    public PointVO getPathTimeFirst(@RequestBody NavQueryDTO query) {
        if (Objects.isNull(query.getEquipId())) {
            throw new IllegalArgumentException("参数缺少装备id");
        }
        long start = System.currentTimeMillis();
        List<PointDTO> points = routingService.getPathTimeFirst(query);
        return getPointVO(query, start, points);
    }

    private PointVO getPointVO(NavQueryDTO query, long start, List<PointDTO> points) {
        //装备id不为空，计算路线耗时
        double speed;
        if (Objects.isNull(query.getEquipId()))
            speed = 30;
        else
            speed = equipService.getById(query.getEquipId()).getCrossSpeed();
        //公里每小时 转换为 米每秒
        speed = speed / 3.6;
        double calcTime = System.currentTimeMillis() - start;

        PointDTO startPoint = query.getStart();
        PointDTO endPoint = query.getEnd();

        if (Objects.isNull(points)) {
            return new PointVO(Collections.EMPTY_LIST, startPoint, endPoint, calcTime, speed, query.getType(), false);
        }
        //给起终设置demType length
        startPoint.setDemType(points.get(0).getDemType());
        endPoint.setDemType(points.get(points.size() - 1).getDemType());
        startPoint.setLength(30d);
        endPoint.setLength(30d);

        List<PointDTO> res = new ArrayList<>();
        res.add(endPoint);
        res.addAll(points);
        res.add(startPoint);
        return new PointVO(res, startPoint, endPoint, calcTime, speed, query.getType(), true);
    }


    @PostMapping("getRoadPath")
    @ApiOperation("推荐路网线路")
    public RoadVO getRoadPath(@RequestBody NavQueryDTO query) {
        long start = System.currentTimeMillis();
        RoadVO vo;
        if (Constant.A_STAR.equals(query.getType())) {
            vo = roadNetworkService.AStarShortestPath(query.getStart(), query.getEnd(), false);
        } else if (Constant.DIJKSTRA.equals(query.getType())) {
            vo = roadNetworkService.DijkstraShortNoWaterPath(query.getStart(), query.getEnd(), false);
        } else {
            vo = dStarRoutingService.routeShortestPath(query.getStart(), query.getEnd(), false);
        }
        return getRoadVO(query, start, vo);
    }

    private RoadVO getRoadVO(NavQueryDTO query, long start, RoadVO vo) {
        if (Objects.isNull(vo)) {
            vo = new RoadVO();
            vo.setSuccess(false);
            vo.setLines(Collections.EMPTY_LIST);
        }
        vo.setSuccess(true);
        double calcTime = System.currentTimeMillis() - start;
        vo.setCalcTime(calcTime);
        vo.setStart(query.getStart());
        vo.setEnd(query.getEnd());

        List<RouteLine> lines = vo.getLines();
        //总长度
        double length = lines.stream().mapToDouble(RouteLine::getLength).sum();
        vo.setLength(length);
        double speed;
        if (Objects.isNull(query.getEquipId()))
            speed = 30;
        else
            speed = equipService.getById(query.getEquipId()).getRoadSpeed();
        //公里每小时 转换为 米每秒
        speed = speed / 3.6;
        //线路总耗时
        vo.setPathTime(length / speed);
        return vo;
    }


    @PostMapping("getRoadPathNoBridgeAndTunnel")
    @ApiOperation("推荐路网线路 不走桥梁和隧道")
    public RoadVO getRoadPathNoBridgeAndTunnel(@RequestBody NavQueryDTO query) {
        long start = System.currentTimeMillis();
        RoadVO vo;
        if (Constant.A_STAR.equals(query.getType())) {
            vo = roadNetworkService.AStarShortestPath(query.getStart(), query.getEnd(), true);
        } else {
            vo = roadNetworkService.DijkstraShortNoWaterPath(query.getStart(), query.getEnd(), true);
        }
        return getRoadVO(query, start, vo);
    }

}
