package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.ChTypeWeight;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据库载重系数的Mapper
 */
@Mapper
public interface ChTypeWeightMapper {
    /**
     * 查询所有的载重系数
     *
     * @return 所有的载重系数
     */
    @Select("SELECT * FROM ROUTE.CH_TYPE_WEIGHT")
    List<ChTypeWeight> selectAll();
}
