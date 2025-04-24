package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.entity.JdEquip;
import com.mpw.model.chfx.mapper.JdEquipMapper;
import com.mpw.model.chfx.service.IJdEquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询武器服务接口
 */
@Service
public class JdEquipServiceImpl implements IJdEquipService {

    /**
     * 数据库装备Mapper
     */
    @Autowired
    private JdEquipMapper jdEquipMapper;

    /**
     * 查询所有的武器
     *
     * @return 武器列表
     */
    @Override
    public List<JdEquip> selectAll() {
        return jdEquipMapper.selectAll();
    }
}
