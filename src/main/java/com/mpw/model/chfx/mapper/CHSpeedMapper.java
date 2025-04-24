package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.CHSpeed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据库速度系数的Mapper
 */
@Mapper
public interface CHSpeedMapper {
    /**
     * 查询所有的速度系数
     *
     * @return 所有的速度系数
     */
    @Select("SELECT * FROM ROUTE.CH_EQUIP_SPEED")
    List<CHSpeed> selectAll();
}
