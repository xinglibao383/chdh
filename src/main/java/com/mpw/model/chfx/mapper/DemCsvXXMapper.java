package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.DemCsvXX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据库地图网格的Mapper
 */
@Mapper
public interface DemCsvXXMapper {
    /**
     * 根据区域查询该区域所有的网格
     *
     * @param area 区域WKT
     * @return 该区域所有的网格
     */
    List<DemCsvXX> selectByArea(@Param("area") String area);
}
