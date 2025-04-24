package com.mpw.model.jdfx.service;

import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.dto.RoadVO;

/**
 * 路网线路规划
 */
public interface IRoadNetworkService {
    /**
     * a星 算法查找线路
     */
    RoadVO AStarShortestPath(PointDTO start, PointDTO end, boolean noBridgeAndTunnel);

    /**
     * Dijikstra 算法查找线路
     */
    RoadVO DijkstraShortNoWaterPath(PointDTO start, PointDTO end, boolean noBridgeAndTunnel);
}
