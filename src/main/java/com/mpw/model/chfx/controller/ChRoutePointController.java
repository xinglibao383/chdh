package com.mpw.model.chfx.controller;

import com.mpw.model.chfx.service.IChRoutePointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api(tags = "测绘用的路径点")
@RequestMapping("chRoutePoint")
public class ChRoutePointController {

    @Autowired
    private IChRoutePointService chRoutePointService;

    @GetMapping("/trans")
    @ApiOperation("wkt转空间字段")
    public String trans(){
        chRoutePointService.tranas();
        return "success";
    }
}
