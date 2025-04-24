package com.mpw.model.jdfx.service.impl;

import com.mpw.model.common.util.PointUtil;
import com.mpw.model.jdfx.dstar.DGraph;
import com.mpw.model.jdfx.dstar.DNode;
import com.mpw.model.jdfx.dstar.DStar;
import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.dto.RoadVO;
import com.mpw.model.jdfx.entity.DEMPoint;
import com.mpw.model.jdfx.entity.RouteLine;
import com.mpw.model.jdfx.service.IDEMPointService;
import com.mpw.model.jdfx.service.IDStarRoutingService;
import com.mpw.model.jdfx.service.IRouteLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DStarRoutingServiceImpl implements IDStarRoutingService {
    private Logger log = LoggerFactory.getLogger(DStarRoutingServiceImpl.class);

    @Autowired
    private IDEMPointService demPointService;

    @Override
    public List<PointDTO> demShortestPath(PointDTO start, PointDTO end, boolean noWater) {
        List<DEMPoint> points = demPointService.list();

        DGraph graph = new DGraph();
        graph.initGraph(points, noWater);
        DStar dStar = new DStar(graph);

        //寻找离终点和起点最近的点
        int sNode = closePointIndex(start, graph.getNodeMap());
        int eNode = closePointIndex(end, graph.getNodeMap());
        List<DNode> nodes = dStar.run(sNode, eNode);
        //计算线路
        if (nodes == null) {
            log.error("未找到线路");
            return null;
        }
        Collections.reverse(nodes);
        return nodes.stream().map(x -> (PointDTO) x.getObj()).collect(Collectors.toList());
    }


    //查询离点最近的节点
    public static int closePointIndex(PointDTO start, Map<Integer , DNode> list) {
        int idx = 0;
        double dis = 999999d;

        for (Map.Entry<Integer, DNode> entry : list.entrySet()) {
            Integer key = entry.getKey();
            DNode node = entry.getValue();
            PointDTO point = (PointDTO) node.getObj();
            if (point.getIsWater().equals(1)) continue;
            double distance = PointUtil.calcDistanceMeter(start, point);
            if (distance < dis) {
                dis = distance;
                idx = key;
            }
        }
        return idx;
    }

    @Autowired
    private IRouteLineService routePointService;

    @Override
    public RoadVO routeShortestPath(PointDTO start, PointDTO end, boolean noBridgeAndTunnel) {
        List<RouteLine> lines = routePointService.list();
        DGraph graph = new DGraph();
        graph.initRouteGraph(lines, false);
        DStar dStar = new DStar(graph);

        //寻找离终点和起点最近的点
        //寻找离终点和起点最近的点
        int sNode = PointUtil.closeLineIndex(start, lines, "s");
        int eNode = PointUtil.closeLineIndex(end, lines, "e");
        List<DNode> nodes = dStar.run(sNode, eNode);
        //计算线路
        if (nodes == null) {
            log.error("未找到线路");
            return null;
        }
        nodes.forEach(System.out::println);
        return null;
    }
}
