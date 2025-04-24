package com.mpw.model.jdfx.service;



import com.mpw.model.jdfx.dto.NavQueryDTO;
import com.mpw.model.jdfx.dto.PointDTO;

import java.util.List;

/**
 * dem数据线路规划
 */
public interface IDEMRoutingService {

    /**
     * a星 避开涉水
     */
    List<PointDTO> AStarShorNoWaterPath(PointDTO start, PointDTO end);

    /**
     * a星 距离优先
     */
    List<PointDTO> AStarShortestPath(PointDTO start, PointDTO end);

    /**
     * a星 时间优先
     */
    List<PointDTO> getPathTimeFirst(NavQueryDTO query);

    /**
     * Dijikstra 距离优先
     */
    List<PointDTO> DijkstraShortestPath(PointDTO start, PointDTO end);

    /**
     * Dijikstra 避开涉水
     */
    List<PointDTO> DijkstraNoWaterPath(PointDTO start, PointDTO end);

}