package com.mpw.model.jdfx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpw.model.common.config.CmcResult;
import com.mpw.model.jdfx.entity.Station;
import com.mpw.model.jdfx.service.IStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/station")
@Api(tags = "站点")
public class StationController {
    @Autowired
    private IStationService stationService;

    @GetMapping("/list")
    @ApiOperation("列表")
    public CmcResult<List<Station>> list() {
        List<Station> list = stationService.list(new QueryWrapper<Station>().orderByDesc("ID"));
        return CmcResult.success(list);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或更新")
    public CmcResult<String> update(@RequestBody Station station) {
        boolean b;
        if (Objects.isNull(station.getId())) {
            b = stationService.save(station);
        }else {
            b = stationService.updateById(station);
        }
        if (b) return CmcResult.success();
        return CmcResult.error("");
    }

    @PostMapping("/remove")
    @ApiOperation("删除")
    public CmcResult<String> remove(@RequestBody Long[] ids) {
        for (Long id : ids) {
            boolean b = stationService.removeById(id);
            if (!b) return CmcResult.error("");
        }
        return CmcResult.success();
    }
}
