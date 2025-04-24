package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.entity.JdEquip;

import java.util.List;

/**
 * 查询武器服务接口
 */
public interface IJdEquipService {
    /**
     * 查询所有的武器
     *
     * @return 武器列表
     */
    List<JdEquip> selectAll();
}
