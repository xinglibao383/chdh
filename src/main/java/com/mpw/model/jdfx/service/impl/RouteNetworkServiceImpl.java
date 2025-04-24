package com.mpw.model.jdfx.service.impl;

import cn.hutool.json.JSONUtil;
import com.mpw.model.common.constant.Constant;
import com.mpw.model.common.util.GeoUtil;
import com.mpw.model.common.util.PointUtil;
import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.dto.RoadVO;
import com.mpw.model.jdfx.entity.RouteLine;
import com.mpw.model.jdfx.service.IRoadNetworkService;
import com.mpw.model.jdfx.service.IRouteLineService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@AllArgsConstructor
@Slf4j
public class RouteNetworkServiceImpl implements IRoadNetworkService {

    private final Logger log = LoggerFactory.getLogger(RouteNetworkServiceImpl.class);

    @Autowired
    private IRouteLineService routePointService;

    @Override
    public RoadVO AStarShortestPath(PointDTO start, PointDTO end, boolean noBridgeAndTunnel) {
        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        BasicGraphBuilder basicGraphBuilder = new BasicGraphBuilder();
        List<RouteLine> lines = routePointService.list();

        Map<String, Node> map = createNodeAndEdge(noBridgeAndTunnel, basicGraphGenerator, basicGraphBuilder, lines);

        //寻找离终点和起点最近的点
        int sNode = PointUtil.closeLineIndex(start, lines, "s");
        int eNode = PointUtil.closeLineIndex(end, lines, "e");
        //计算线路
        String nodeIds = AStarNoWaterPath(basicGraphBuilder.getGraph(), map.get(Integer.toString(sNode)), map.get(Integer.toString(eNode)));
        if (nodeIds == null) {
            return null;
        }

        return getRoadVO(lines, nodeIds);
    }

    @Override
    public RoadVO DijkstraShortNoWaterPath(PointDTO start, PointDTO end, boolean noBridgeAndTunnel) {
        BasicGraphGenerator basicGraphGenerator = new BasicGraphGenerator();
        GraphBuilder graphBuilder = basicGraphGenerator.getGraphBuilder();
        List<RouteLine> lines = routePointService.list();

        Map<String, Node> map = createNodeAndEdge(noBridgeAndTunnel, basicGraphGenerator, graphBuilder, lines);

        //寻找离终点和起点最近的点
        int sNode = PointUtil.closeLineIndex(start, lines, "s");
        int eNode = PointUtil.closeLineIndex(end, lines, "e");
        //计算线路
        DijkstraIterator.EdgeWeighter weighter = edge -> {
            RouteLine line = (RouteLine) edge.getObject();
            return line.getLength();
        };

        Graph graph = graphBuilder.getGraph();
        Node source = map.get(Integer.toString(sNode));
        Node destNode = map.get(Integer.toString(eNode));
        DijkstraShortestPathFinder pf = new DijkstraShortestPathFinder(graph, source, weighter);
        pf.calculate();
        Path path = pf.getPath(destNode);
        if (Objects.isNull(path)) {
            log.error("未找到线路");
            return null;
        }
        String nodeIds = path.toString();
        return getRoadVO(lines, nodeIds);
    }

    private Map<String, Node> createNodeAndEdge(boolean noBridgeAndTunnel, BasicGraphGenerator basicGraphGenerator, GraphBuilder graphBuilder, List<RouteLine> lines) {
        Map<String, Node> map = new HashMap<>();
        for (RouteLine line : lines) {
            List<PointDTO> points = GeoUtil.wtkLineToPointDTO(line.getGeometry());
            //判断是否 不经过桥梁和隧道
            if (noBridgeAndTunnel) {
                if (Constant.T.equals(line.getBridge()) || Constant.T.equals(line.getTunnel())) {
                    continue;
                }
            }

            Integer sNode = line.getsNode();
            Integer eNode = line.geteNode();
            Node startNode;
            Node endNode;
            //构造节点
            if (!map.containsKey(sNode.toString())) {
                startNode = createNode(basicGraphGenerator, points.get(0), sNode);
                map.put(sNode.toString(), startNode);
                basicGraphGenerator.getGraphBuilder().addNode(startNode);
            } else {
                startNode = map.get(sNode.toString());
            }

            if (!map.containsKey(eNode.toString())) {
                endNode = createNode(basicGraphGenerator, points.get(points.size() - 1), eNode);
                map.put(eNode.toString(), endNode);
                basicGraphGenerator.getGraphBuilder().addNode(endNode);
            } else {
                endNode = map.get(eNode.toString());
            }

            //构造边
            createEdge(graphBuilder, line, startNode, endNode);
            createEdge(graphBuilder, line, endNode, startNode);
        }
        return map;
    }

    //构造返回对象
    private RoadVO getRoadVO(List<RouteLine> lines, String nodeIds) {
        List<Integer> list = JSONUtil.toList(nodeIds, Integer.class);
        List<RouteLine> resLines = new ArrayList<>();
        //筛选出node路径
        for (int i = 0; i < list.size() - 1; i++) {
            Integer node = list.get(i);
            Integer nextNode = list.get(i + 1);
            //线段 包含 起始和终止节点
            Optional<RouteLine> lineOptional = lines.stream().filter(x ->
                    (x.getsNode().equals(node) && x.geteNode().equals(nextNode)) || (x.getsNode().equals(nextNode) && x.geteNode().equals(node)))
                    .findFirst();
            if (lineOptional.isPresent()) {
                RouteLine line = lineOptional.get();
                resLines.add(line);
            }
        }

        //合并所有的线
        List<PointDTO> pointDTOS = GeoUtil.wtkLineToPointDTO(resLines.get(0).getGeometry());
        for (int i = 1; i < resLines.size(); i++) {
            RouteLine line = resLines.get(i);
            pointDTOS = GeoUtil.joinLines(pointDTOS, GeoUtil.wtkLineToPointDTO(line.getGeometry()));
        }
        RoadVO vo = new RoadVO();
        vo.setLines(resLines);
        vo.setPoints(pointDTOS);
        return vo;
    }

    private void createEdge(GraphBuilder graphBuilder, RouteLine line, Node startNode, Node endNode) {
        Edge edge = new BasicEdge(startNode, endNode);
        edge.setObject(line);
        edge.setVisited(true);
        graphBuilder.addEdge(edge);
    }

    /**
     * 计算最佳路径 避开涉水
     */
    private String AStarNoWaterPath(Graph graph, Node startNode, Node endNode) {

        AStarIterator.AStarFunctions aStarFunction = new AStarIterator.AStarFunctions(endNode) {
            @Override
            public double cost(AStarIterator.AStarNode aStarNode, AStarIterator.AStarNode aStarNode1) {
                //依据权重来计算代价
                Edge edge = aStarNode.getNode().getEdge(aStarNode1.getNode());
                RouteLine line = (RouteLine) edge.getObject();
                return line.getLength();
            }

            @Override
            public double h(Node node) {
                PointDTO pws = (PointDTO) startNode.getObject();
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
            log.error("线路不存在");
        }
        return null;
    }

    private Node createNode(BasicGraphGenerator basicGraphGenerator, PointDTO point, int i) {
        Node node = basicGraphGenerator.getGraphBuilder().buildNode();
        basicGraphGenerator.getGraphBuilder().addNode(node);
        node.setObject(point);
        node.setID(i);
        node.setCount(i);
        node.setVisited(true);
        return node;
    }
}
