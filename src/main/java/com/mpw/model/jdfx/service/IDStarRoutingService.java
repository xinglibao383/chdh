package com.mpw.model.jdfx.service;

import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.dto.RoadVO;

import java.util.List;

/**
 * D* 路径规划
 */
public interface IDStarRoutingService {

    /**
     * dem 路径规划
     */
    List<PointDTO> demShortestPath(PointDTO start, PointDTO end, boolean noWater);

    /**
     * route 路径规划
     */
    RoadVO routeShortestPath(PointDTO start, PointDTO end, boolean noBridgeAndTunnel);

}
