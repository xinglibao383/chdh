package com.mpw.model.chfx.mapper;

import com.mpw.model.chfx.domain.entity.XWeapon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface XWeaponMapper {

    @Select("select * from ROUTE.CH_WEAPON")
    public List<XWeapon> selectAll();

    @Select("select * from ROUTE.CH_WEAPON where ID=#{id}")
    XWeapon selectById(Long id);

    @Select("select * from ROUTE.CH_EXPLODE")
    public List<XWeapon> selectExplodeAll();

    @Select("select * from ROUTE.CH_EXPLODE where ID=#{id}")
    XWeapon selectExplodeById(Long id);
}
