package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.dto.MutilationDTO;
import com.mpw.model.chfx.domain.dto.YanDTO;
import com.mpw.model.chfx.domain.dto.shockDTO;
import com.mpw.model.chfx.domain.dto.staffDTO;
import com.mpw.model.chfx.domain.entity.XWeapon;
import com.mpw.model.chfx.domain.model.ShootingPoint;

import java.util.List;

/**
 * 毁伤分析
 */
public interface HsfxService {

    /**
     *
     * @param target
     * @return mutilationDTO
     */
    MutilationDTO damageability(ShootingPoint target);

    /**
     * 爆炸冲击结果分析
     * @param target
     * @return shockDTO
     */
    List<shockDTO> explosionShock(ShootingPoint target);

    /**
     *  结构与地形破坏分析
     * @param target
     * @return YanDTO
     */
    YanDTO terrainAnalysis(ShootingPoint target);

    /**
     * 人员伤害分析
     * @param target
     * @return staffDTO
     */
    List<staffDTO> harmPeople(ShootingPoint target);

    /**
     * 武器类型
     * @return
     */
    List<XWeapon> getWeaponList();

    /**
     * 获取爆炸物列表
     * @return
     */
    List<XWeapon> getExplodeList();
}
