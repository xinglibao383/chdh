package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.dto.AreaEquipDTO;
import com.mpw.model.chfx.domain.vo.*;

/**
 * 机动影响分析的接口
 */
public interface ICEService {

    /**
     *机动能力分析
     * @param areaRange 区域WKT
     * @return 机动能力分析的返回结果实体
     */
    MotorAbilityVO motorAbilityAnalysis(String areaRange);

    /**
     * 行車速度計算
     *
     * @param areaRange 区域WKT
     * @return 行車速度計算的返回结果实体
     */
    SpeedVO speed(String areaRange);

    /**
     * 水路機動分析
     *
     * @param areaRange 区域WKT
     * @return 水路機動分析的返回结果实体
     */
    WaterVO water(String areaRange);

    /**
     * 可通行性分析
     *
     * @param areaEquipDTO 可通行性分析的请求体
     * @return 可通行性分析的返回结果实体
     */
    PassVO pass(AreaEquipDTO areaEquipDTO);

    /**
     * 越野機動分析
     *
     * @param areaRange 区域WKT
     * @return 越野機動分析的返回结果实体
     */
    WildVO wild(String areaRange);

    /**
     * 越野機動分析
     *
     * @param areaRange 区域WKT
     * @return 通行能力分析的返回结果实体
     */
    PassAbilityVO passAbility(String areaRange);

    /**
     * 道路機動分析
     *
     * @param areaRange 区域WKT
     * @return 道路機動分析的返回结果实体
     */
    MoveVO move(String areaRange);
}
