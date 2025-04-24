package com.mpw.model.jdfx.controller;

import com.mpw.model.common.config.CmcResult;
import com.mpw.model.common.util.GeoUtil;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wkt")
@Api(tags = "wkt形状判断")
public class WktController {


    @ApiOperation("判断两个geom是否相交")
    @GetMapping("/intersects")
    public CmcResult<Boolean> intersects(@RequestParam String wktStr1,
                                    @RequestParam String wktStr2){
        Geometry geom1 = GeoUtil.getGeometry(wktStr1);
        Geometry geom2 = GeoUtil.getGeometry(wktStr2);
        boolean intersects = geom1.intersects(geom2);
        return CmcResult.success(intersects);
    }

}
