package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.JdEquip;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 数据库装备Mapper
 */
@Mapper
public interface JdEquipMapper {
    /**
     * 查询所有的装备信息
     *
     * @return 查询出的所有的装备信息列表
     */
    @Select("SELECT * FROM ROUTE.JD_EQUIP")
    List<JdEquip> selectAll();
}
