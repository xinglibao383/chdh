package com.mpw.model.jdfx.controller;

import com.mpw.model.jdfx.service.IDEMCsvService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "dem数据")
@RequestMapping("demCsv")
public class DEMCsvController {

    @Autowired
    private IDEMCsvService demCsvService;

    @GetMapping("trans")
    @ApiOperation("wkt转空间字段")
    public String trans(){
        demCsvService.tranas();
        return "success";
    }

    @GetMapping("")
    @ApiOperation("计算坡度")
    public String celcGradient(){
        demCsvService.calcGradient();
        return "success";
    }
}
