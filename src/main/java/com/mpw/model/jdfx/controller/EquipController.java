package com.mpw.model.jdfx.controller;

import com.mpw.model.common.config.CmcResult;
import com.mpw.model.jdfx.entity.Equip;
import com.mpw.model.jdfx.service.IEquipService;
import com.mpw.model.jdfx.service.IEquipSpeedCoefficientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/equip")
@AllArgsConstructor
@Api(tags = "装备类型")
public class EquipController {
    @Autowired
    private IEquipService equipService;

    @GetMapping("/list")
    @ApiOperation("获取所有装备类型")
    public CmcResult<List<Equip>> list(){
        return CmcResult.success(equipService.list());
    }

    @Autowired
    private IEquipSpeedCoefficientService coefficientService;

    /*@ApiOperation("新增装备类型速度系数")
    @GetMapping("/createCoefficient")
    public String createCoefficient(){
        //轻型高机动
        EquipSpeedCoefficient forest1 = new EquipSpeedCoefficient(4l, DemTypeEnum.SCARIFY_SOIL.getNum(), 0.9);
        EquipSpeedCoefficient forest2 = new EquipSpeedCoefficient(4l, DemTypeEnum.ARTIFICIAL_STRUCTURE.getNum(), 0.6);
        EquipSpeedCoefficient forest3 = new EquipSpeedCoefficient(4l, DemTypeEnum.FOREST.getNum(), 0.5);
        EquipSpeedCoefficient forest4 = new EquipSpeedCoefficient(4l, DemTypeEnum.PAN_SOIL.getNum(), 1.0);
        EquipSpeedCoefficient forest5 = new EquipSpeedCoefficient(4l, DemTypeEnum.SANDY_SOIL.getNum(), 0.9);
        coefficientService.saveBatch(Arrays.asList(forest1, forest2, forest3, forest4, forest5));
        return "success";
    }*/


}
