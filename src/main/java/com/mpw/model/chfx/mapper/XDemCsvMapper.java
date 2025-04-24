package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.DemCsv;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 数据库地图网格Mapper
 */
@Mapper
public interface XDemCsvMapper {
    /**
     * 根据区域查询地图网格列表
     *
     * @param area 区域WKT
     * @return 根据区域查询出的地图网格列表
     */
    List<DemCsv> selectByArea(@Param("area") String area);

    List<DemCsv> select(@Param("area") String area);

    /**
     * 根据线查询地图网格列表
     *
     * @param ling 线WKT
     * @return 根据线查询出的地图网格列表
     */
    List<DemCsv> selectByLine(@Param("line") String ling);

    /**
     * 根据点查询地图网格列表
     *
     * @param point 点WKT
     * @return 根据点查询出的地图网格列表
     */
    DemCsv selectByPoint(@Param("point") String point);

    /**
     * 查询所有的地图网格
     *
     * @return 查询出的所有的地图网格的列表
     */
    @Select("select * from ROUTE.DEM_CSV")
    List<DemCsv> selectAll();
}
