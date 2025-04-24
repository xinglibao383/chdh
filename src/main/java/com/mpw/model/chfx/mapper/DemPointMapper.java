package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.DemPoint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库DemPoint的Mapper
 */
@Mapper
public interface DemPointMapper {
    /**
     * 根据区域查询该地图上的DemPoint
     *
     * @param area 区域WKT
     * @return 根据区域查询该地图上的DemPoint列表
     */
    List<DemPoint> selectByArea(@Param("area") String area);
}
