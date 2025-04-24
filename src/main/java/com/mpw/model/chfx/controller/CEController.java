package com.mpw.model.chfx.controller;

import com.mpw.model.chfx.domain.dto.AreaDTO;
import com.mpw.model.chfx.domain.dto.AreaEquipDTO;
import com.mpw.model.chfx.service.ICEService;
import com.mpw.model.chfx.domain.vo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 机动影响分析的Controller类
 */
@RestController
@RequestMapping("/ce")
public class CEController {

    /**
     * 机动影响分析的Service接口
     */
    @Autowired
    private ICEService ceService;
    /**
     * 机动能力分析
     *
     * @param query 机动能力分析的请求体
     * @return 机动能力分析的返回结果实体
     */
    @ApiOperation("机动能力分析")
    @PostMapping("motorabilityanalysis")
    public MotorAbilityVO motorAbilityAnalysis(@RequestBody AreaDTO query) {
        return ceService.motorAbilityAnalysis(query.getAreaRange());
    }

    /**
     * 道路機動分析
     *
     * @param query 道路機動分析的请求体
     * @return 道路機動分析的返回结果实体
     */
    @ApiOperation("道路機動分析")
    @PostMapping("move")
    public MoveVO move(@RequestBody AreaDTO query) {
        return ceService.move(query.getAreaRange());
    }

    /**
     * 行車速度計算
     *
     * @param query 行車速度計算的请求体
     * @return 行車速度計算的返回结果实体
     */
    @ApiOperation("行車速度計算")
    @PostMapping("speed")
    public SpeedVO speed(@RequestBody AreaDTO query) {
        return ceService.speed(query.getAreaRange());
    }

    /**
     * 水路機動分析
     *
     * @param query 水路機動分析的请求体
     * @return 水路機動分析的返回结果实体
     */
    @ApiOperation("水路機動分析")
    @PostMapping("water")
    public WaterVO water(@RequestBody AreaDTO query) {
        return ceService.water(query.getAreaRange());
    }

    /**
     * 可通行性分析
     *
     * @param query 可通行性分析的请求体
     * @return 可通行性分析的返回结果实体
     */
    @ApiOperation("可通行性分析")
    @PostMapping("pass")
    public PassVO pass(@RequestBody AreaEquipDTO query) {
        return ceService.pass(query);
    }

    /**
     * 越野機動分析
     *
     * @param query 越野機動分析的请求体
     * @return 越野機動分析的返回结果实体
     */
    @ApiOperation("越野機動分析")
    @PostMapping("wild")
    public WildVO wild(@RequestBody AreaDTO query) {
        return ceService.wild(query.getAreaRange());
    }

    /**
     * 越野機動分析
     *
     * @param query 越野機動分析的请求体
     * @return 通行能力分析的返回结果实体
     */
    @ApiOperation("通行能力分析")
    @PostMapping("passAbility")
    public PassAbilityVO passAbility(@RequestBody AreaDTO query) {
        return ceService.passAbility(query.getAreaRange());
    }
}
