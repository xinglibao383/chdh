package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.ChRoutePoint;
import com.mpw.model.jdfx.entity.DEMCsv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ChRoutePoint的数据库Mapper
 */
@Mapper
public interface ChRoutePointMapper {
    /**
     * 根据面积查询ChRoutePoint列表
     *
     * @param area 区域WKT
     * @return 根据面积查询ChRoutePoint的列表
     */
    List<ChRoutePoint> selectByArea(@Param("area") String area);


    /**
     * WKT字符串转换空间字段
     * @param id
     * @param geometry
     * @return
     */
    int transGeom(Long id, String geometry);

    /**
     * 查询全部
      * @return
     */
    List<ChRoutePoint> selectList();

}
