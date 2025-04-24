package com.mpw.model.jdfx.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpw.model.common.constant.Constant;
import com.mpw.model.common.util.PointUtil;
import com.mpw.model.jdfx.dto.NavQueryDTO;
import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.entity.DEMPoint;
import com.mpw.model.jdfx.entity.Equip;
import com.mpw.model.jdfx.entity.EquipSpeedCoefficient;
import com.mpw.model.common.enums.DemTypeEnum;
import com.mpw.model.jdfx.service.IDEMPointService;
import com.mpw.model.jdfx.service.IDEMRoutingService;
import com.mpw.model.jdfx.service.IEquipService;
import com.mpw.model.jdfx.service.IEquipSpeedCoefficientService;
import lombok.AllArgsConstructor;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.build.basic.BasicGraphGenerator;
import org.geotools.graph.path.AStarShortestPathFinder;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicEdge;
import org.geotools.graph.traverse.standard.AStarIterator;
import org.geotools.graph.traverse.standard.DijkstraIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DEMRoutingServiceImpl implements IDEMRoutingService {
    private final Logger log = LoggerFactory.getLogger(DEMRoutingServiceImpl.class);

    @Autowired
    private IDEMPointService demPointService;

    @Autowired
    private IEquipService equipService;

    @Autowired
    private IEquipSpeedCoefficientService coefficientService;

    public List<PointDTO> AStarShorNoWaterPath(PointDTO start, PointDTO end) {
        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        BasicGraphBuilder basicGraphBuilder = new BasicGraphBuilder();

        List<DEMPoint> points = demPointService.list();

        Map<String, Node> map = getStarNodeMap(points, basicGraphGenerator, basicGraphBuilder);
        //寻找离终点和起点最近的点
        int sNode = PointUtil.closePointIndex(start, map);
        int eNode = PointUtil.closePointIndex(end, map);
        //计算线路
        String nodeIds = AStarCalcPath(basicGraphBuilder.getGraph(), map.get(Integer.toString(sNode)), map.get(Integer.toString(eNode)));
        if (nodeIds == null) {
            return null;
        }

        List<String> list = JSONUtil.toList(nodeIds, String.class);
        return list.stream().map(x -> (PointDTO) map.get(x).getObject()).collect(Collectors.toList());
    }

    //创建node节点 构造边
    private Map<String, Node> getStarNodeMap(List<DEMPoint> points, BasicGraphGenerator basicGraphGenerator, GraphBuilder graphBuilder) {
        //创建node节点 构造边
        Map<String, Node> map = new HashMap<>();
        for (DEMPoint point : points) {
            if (point.getIsWater().equals(Constant.IS_WATER)) {
                continue;
            }
            Integer sNode = point.getsNode();
            Integer eNode = point.geteNode();
            Node startNode;
            Node endNode;
            if (!map.containsKey(sNode.toString())) {
                startNode = createNode(basicGraphGenerator, new PointDTO(point.getSy(), point.getSx(), point.getIsWater(), point.getLength(), point.getType()), sNode);
                map.put(sNode.toString(), startNode);
                basicGraphGenerator.getGraphBuilder().addNode(startNode);
            } else {
                startNode = map.get(sNode.toString());
            }

            if (!map.containsKey(eNode.toString())) {
                endNode = createNode(basicGraphGenerator, new PointDTO(point.getEy(), point.getEx(), point.getIsWater(), point.getLength(), point.getType()), eNode);
                map.put(eNode.toString(), endNode);
                basicGraphGenerator.getGraphBuilder().addNode(endNode);
            } else {
                endNode = map.get(eNode.toString());
            }
            //构造边
            getEdge(graphBuilder, point, startNode, endNode);
            getEdge(graphBuilder, point, endNode, startNode);
        }
        return map;
    }

    @Override
    public List<PointDTO> getPathTimeFirst(NavQueryDTO query) {
        PointDTO start = query.getStart();
        PointDTO end = query.getEnd();
        //装备 及其对应速度系数
        Equip equip = equipService.getById(query.getEquipId());
        List<EquipSpeedCoefficient> equipSpeedCoefficients = coefficientService.list(new QueryWrapper<EquipSpeedCoefficient>().lambda()
                .eq(EquipSpeedCoefficient::getEquipId, equip.getId()));

        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        BasicGraphBuilder basicGraphBuilder = new BasicGraphBuilder();

        List<DEMPoint> points = demPointService.list();

        //创建node节点 构造边
        Map<String, Node> map = getStarNodeMap(points, basicGraphGenerator, basicGraphBuilder);

        //寻找离终点和起点最近的点
        int sNode = PointUtil.closePointIndex(start, map);
        int eNode = PointUtil.closePointIndex(end, map);
        //计算线路
        String nodeIds;
        if (Constant.DIJKSTRA.equals(query.getType())) {
            nodeIds = dijkstraCalcPathTimeFirst(basicGraphBuilder.getGraph(),
                    map.get(Integer.toString(sNode)),
                    map.get(Integer.toString(eNode)),
                    equip, equipSpeedCoefficients);
        } else {
            nodeIds = AStarCalcPathTimeFirst(basicGraphBuilder.getGraph(),
                    map.get(Integer.toString(sNode)),
                    map.get(Integer.toString(eNode)),
                    equip, equipSpeedCoefficients);
        }
        if (nodeIds == null) {
            return null;
        }

        List<String> list = JSONUtil.toList(nodeIds, String.class);
        return list.stream().map(x -> (PointDTO) map.get(x).getObject()).collect(Collectors.toList());
    }

    //aStar计算时间优先
    private String AStarCalcPathTimeFirst(Graph graph, Node startNode, Node endNode, Equip equip, List<EquipSpeedCoefficient> equipSpeedCoefficients) {
        AStarIterator.AStarFunctions aStarFunction = new AStarIterator.AStarFunctions(endNode) {
            @Override
            public double cost(AStarIterator.AStarNode aStarNode, AStarIterator.AStarNode aStarNode1) {
                //依据地形速度系数 计算权重
                Edge edge = aStarNode.getNode().getEdge(aStarNode1.getNode());
                DEMPoint demPoint = (DEMPoint) edge.getObject();

                //查找地形编号
                Integer demNum = DemTypeEnum.getByName(demPoint.getType()).getNum();

                //过滤系数
                Optional<EquipSpeedCoefficient> coefficientOptional = equipSpeedCoefficients.stream()
                        .filter(x -> x.getDemType().equals(demNum)).findFirst();
                EquipSpeedCoefficient coefficient = coefficientOptional.get();

                Double length = demPoint.getLength();
                Double speed = equip.getCrossSpeed() * coefficient.getCoefficient();

                return length / speed;
            }

            @Override
            public double h(Node node) {
                PointDTO pws = (PointDTO) startNode.getObject();
                PointDTO pwd = (PointDTO) node.getObject();
                double distance = PointUtil.calcDistanceMeter(pws, pwd);
                return distance / equip.getCrossSpeed();
            }
        };
        AStarShortestPathFinder aStarPf = new AStarShortestPathFinder(graph, startNode, endNode, aStarFunction);
        try {
            aStarPf.calculate();
            return aStarPf.getPath().toString();
        } catch (Exception e) {
            log.error("路线不存在 {}", e.getMessage());
        }
        return null;
    }

    //dijkstra算法计算时间优先
    private String dijkstraCalcPathTimeFirst(Graph graph, Node startNode, Node endNode, Equip equip, List<EquipSpeedCoefficient> equipSpeedCoefficients) {
        DijkstraIterator.EdgeWeighter weighter = edge -> {
            DEMPoint demPoint = (DEMPoint) edge.getObject();
            //查找地形编号
            Integer demNum = DemTypeEnum.getByName(demPoint.getType()).getNum();
            //过滤系数
            Optional<EquipSpeedCoefficient> coefficientOptional = equipSpeedCoefficients.stream()
                    .filter(x -> x.getDemType().equals(demNum)).findFirst();
            EquipSpeedCoefficient coefficient = coefficientOptional.get();

            Double length = demPoint.getLength();
            Double speed = equip.getCrossSpeed() * coefficient.getCoefficient();
            return length / speed;
        };

        DijkstraShortestPathFinder pf = new DijkstraShortestPathFinder(graph, startNode, weighter);

        pf.calculate();
        Path path = pf.getPath(endNode);
        if (Objects.isNull(path)) {
            log.error("未找到线路");
            return null;
        }
        return path.toString();
    }

    @Override
    public List<PointDTO> AStarShortestPath(PointDTO start, PointDTO end) {
        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        BasicGraphBuilder basicGraphBuilder = new BasicGraphBuilder();

        List<DEMPoint> points = demPointService.list();
        points.forEach(point -> point.setIsWater(0));

        //创建node节点 构造边
        Map<String, Node> map = getStarNodeMap(points, basicGraphGenerator, basicGraphBuilder);

        //寻找离终点和起点最近的点
        int sNode = PointUtil.closePointIndex(start, map);
        int eNode = PointUtil.closePointIndex(end, map);
        //计算线路
        String nodeIds = AStarCalcPath(basicGraphBuilder.getGraph(), map.get(Integer.toString(sNode)), map.get(Integer.toString(eNode)));
        if (nodeIds == null) {
            return null;
        }

        List<String> list = JSONUtil.toList(nodeIds, String.class);
        return list.stream().map(x -> (PointDTO) map.get(x).getObject()).collect(Collectors.toList());
    }


    /**
     * 创建节点
     */
    private Node createNode(BasicGraphGenerator basicGraphGenerator, PointDTO point, int i) {
        Node node = basicGraphGenerator.getGraphBuilder().buildNode();
        basicGraphGenerator.getGraphBuilder().addNode(node);
        node.setObject(point);
        node.setID(i);
        node.setCount(i);
        node.setVisited(true);
        return node;
    }


    /**
     * 计算最佳路径 避开涉水
     */
    private String AStarCalcPath(Graph graph, Node startNode, Node endNode) {

        AStarIterator.AStarFunctions aStarFunction = new AStarIterator.AStarFunctions(endNode) {
            @Override
            public double cost(AStarIterator.AStarNode aStarNode, AStarIterator.AStarNode aStarNode1) {
                //依据权重来计算代价
                Edge edge = aStarNode.getNode().getEdge(aStarNode1.getNode());
                DEMPoint demPoint = (DEMPoint) edge.getObject();
                if (demPoint.getIsWater().equals(1)) {
                    return Double.MAX_VALUE;
                } else {
                    return demPoint.getLength();
                }
            }

            @Override
            public double h(Node node) {
                PointDTO pws = (PointDTO) endNode.getObject();
                PointDTO pwd = (PointDTO) node.getObject();
                return PointUtil.calcDistanceMeter(pws, pwd);

            }
        };
        AStarShortestPathFinder aStarPf = new AStarShortestPathFinder(graph, startNode, endNode, aStarFunction);
        try {
            aStarPf.calculate();
            //算法途径点
            return aStarPf.getPath().toString();
        } catch (Exception e) {
            log.error("路线不存在");
        }
        return null;
    }

    @Override
    public List<PointDTO> DijkstraShortestPath(PointDTO start, PointDTO end) {
        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        GraphBuilder graphBuilder = basicGraphGenerator.getGraphBuilder();

        List<DEMPoint> points = demPointService.list();
        points.forEach(point -> point.setIsWater(0));


        //创建node节点 构造边
        Map<String, Node> map = getStarNodeMap(points, basicGraphGenerator, graphBuilder);

        //寻找离终点和起点最近的点
        int sNode = PointUtil.closePointIndex(start, map);
        int eNode = PointUtil.closePointIndex(end, map);
        //计算线路

        return getDijkstrapath(graphBuilder, map, sNode, eNode);
    }

    //根据dijkstra算法计算线路
    private List<PointDTO> getDijkstrapath(GraphBuilder graphBuilder, Map<String, Node> map, int sNode, int eNode) {
        DijkstraIterator.EdgeWeighter weighter = edge -> {
            DEMPoint demPoint = (DEMPoint) edge.getObject();
            return demPoint.getLength();
        };

        Node source = map.get(Integer.toString(sNode));
        Node destNode = map.get(Integer.toString(eNode));
        Graph graph = graphBuilder.getGraph();

        DijkstraShortestPathFinder pf = new DijkstraShortestPathFinder(graph, source, weighter);

        pf.calculate();
        Path path = pf.getPath(destNode);
        if (Objects.isNull(path)) {
            log.error("未找到线路");
            return null;
        }
        String nodeIds = path.toString();

        List<String> list = JSONUtil.toList(nodeIds, String.class);
        return list.stream().map(x -> (PointDTO) map.get(x).getObject()).collect(Collectors.toList());
    }


    private static void getEdge(GraphBuilder graphBuilder, DEMPoint point, Node startNode, Node endNode) {
        Edge edge = new BasicEdge(startNode, endNode);
        edge.setObject(point);
        edge.setVisited(true);
        graphBuilder.addEdge(edge);
    }

    @Override
    public List<PointDTO> DijkstraNoWaterPath(PointDTO start, PointDTO end) {
        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        GraphBuilder graphBuilder = basicGraphGenerator.getGraphBuilder();

        List<DEMPoint> points = demPointService.list();

        //创建node节点 构造边
        Map<String, Node> map = getStarNodeMap(points, basicGraphGenerator, graphBuilder);

        //寻找离终点和起点最近的点
        int sNode = PointUtil.closePointIndex(start, map);
        int eNode = PointUtil.closePointIndex(end, map);
        //计算线路

        return getDijkstrapath(graphBuilder, map, sNode, eNode);
    }
}
