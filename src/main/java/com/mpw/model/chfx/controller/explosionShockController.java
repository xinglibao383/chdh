package com.mpw.model.chfx.controller;


import com.mpw.model.chfx.domain.dto.MutilationDTO;
import com.mpw.model.chfx.domain.dto.YanDTO;
import com.mpw.model.chfx.domain.dto.shockDTO;
import com.mpw.model.chfx.domain.dto.staffDTO;
import com.mpw.model.chfx.domain.entity.XWeapon;
import com.mpw.model.chfx.domain.model.ShootingPoint;
import com.mpw.model.chfx.service.HsfxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "毁伤分析")
@RequestMapping("harmFx")
public class explosionShockController {

    @Autowired
    private HsfxService hsfxService;

    /**
     * 毁伤能力结果分析
     * @return
     */
    @PostMapping("damageability")
    @ApiOperation("毁伤能力结果分析")
    public MutilationDTO damageability(@RequestBody ShootingPoint target){
        return hsfxService.damageability(target);
    }

    /**
     * 爆炸冲击结果分析
     * @return
     */
    @PostMapping("explosionShock")
    @ApiOperation("爆炸冲击结果分析")
    public List<shockDTO> explosionShock(@RequestBody ShootingPoint target){
        return hsfxService.explosionShock(target);
    }

    /**
     * 结构与地形破坏分析
     * @return
     */
    @PostMapping("terrainAnalysis")
    @ApiOperation("结构与地形破坏分析")
    public YanDTO terrainAnalysis(@RequestBody ShootingPoint target){
        return hsfxService.terrainAnalysis(target);
    }

    /**
     * 人员伤害分析
     * @return
     */
    @PostMapping("harmPeople")
    @ApiOperation("人员伤害分析")
    public List<staffDTO> harmPeople(@RequestBody ShootingPoint target){
        return hsfxService.harmPeople(target);
    }

    /**
     * 武器类型
     * @return
     */
    @GetMapping("getWeaponList")
    @ApiOperation("武器类型")
    public List<XWeapon> getWeaponList(){
        return hsfxService.getWeaponList();
    }

    /**
     * 获取爆炸物列表
     * @return
     */
    @ApiOperation("获取爆炸物列表")
    @GetMapping("/getExplodeList")
    public List<XWeapon> getExplodeList() {
        return hsfxService.getExplodeList();
    }


}
