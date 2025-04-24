package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.Speed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据库装备速度系数Mapper
 */
@Mapper
public interface XJdEquipSpeedMapper {

    /**
     * 查询所有的装备速度系数
     *
     * @return 查询出的所有的装备速度系数列表
     */
    @Select("SELECT * FROM ROUTE.JD_EQUIP_SPEED")
    List<Speed> selectAll();
}
