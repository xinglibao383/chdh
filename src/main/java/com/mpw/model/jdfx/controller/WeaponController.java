package com.mpw.model.jdfx.controller;

import com.mpw.model.common.config.CmcResult;
import com.mpw.model.jdfx.entity.Weapon;
import com.mpw.model.jdfx.service.IWeaponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weapon")
@Api(tags = "武器")
public class WeaponController {

    @Autowired
    private IWeaponService weaponService;

    @GetMapping("/list")
    @ApiOperation("武器列表")
    public CmcResult<List<Weapon>> list(){
        List<Weapon> list = weaponService.list();
        return CmcResult.success(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增武器")
    public CmcResult<String> save(@RequestBody Weapon weapon){
        boolean save = weaponService.save(weapon);
        return CmcResult.success();
    }

}
