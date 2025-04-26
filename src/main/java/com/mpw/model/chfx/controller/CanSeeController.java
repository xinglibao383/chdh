package com.mpw.model.chfx.controller;

import com.mpw.model.chfx.domain.dto.*;
import com.mpw.model.chfx.domain.entity.GreenArea;
import com.mpw.model.chfx.domain.vo.CanSeeLineVO;
import com.mpw.model.chfx.domain.vo.CanSeeVO;
import com.mpw.model.chfx.domain.vo.VisualDomainVO;
import com.mpw.model.chfx.service.ICanSeeService;
import com.mpw.model.chfx.service.impl.HideService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 判断通视的Controller类
 */
@RestController
@RequestMapping("canSee")
public class CanSeeController {

    /**
     * 判断通视的Service接口
     */
    @Autowired
    private ICanSeeService canSeeService;

    @Autowired
    private HideService hideService;

    /**
     * 根据区域获取指定区域的植被区域列表
     * @param query
     * @return
     */
    @PostMapping("getGreenAreasByArea")
    @ApiOperation("根据区域获取指定区域的植被区域列表")
    public List<GreenArea> getGreenAreas(@RequestBody GreenDTO query) {
        return hideService.getGreenAreas(query.getArea());
    }

    /**
     * 根据直线获取植被区域列表
     * @param query
     * @return
     */
    @PostMapping("getGreenAreasByLine")
    @ApiOperation("根据直线获取植被区域列表")
    public List<GreenArea> getGreenAreasByLine(@RequestBody GreenDTOV2 query) {
        return hideService.getGreenAreas(query);
    }

    /**
     * 判断地图上一个区域是否通视
     *
     * @param query 判断地图上一个区域是否通视的查询请求
     * @return 判断地图上一个区域是否通视的返回结果
     */
    @PostMapping("judge")
    @ApiOperation("判断是否通视")
    public CanSeeVO judge(@RequestBody CanSeeDTO query) {
        // return canSeeService.judge(query);
        return canSeeService.judgePall(query);
    }

    /**
     * 判断地图上一个区域是否可通视
     *
     * @param query 判断地图上一个区域是否通视的查询请求
     * @return 判断地图上一个区域是否通视的返回结果
     */
    @PostMapping("visualDomainanalysis")
    @ApiOperation("可视域分析")
    public VisualDomainVO visualDomainAnalysis(@RequestBody VisualDomainDTO query) {
        return canSeeService.visualDomainAnalysis(query);
    }


    /**
     * 判断地图上一条线是否通视
     *
     * @param query 判断地图上一条线是否通视的查询请求
     * @return 判断地图上一条线是否通视的返回结果
     */
    @PostMapping("judge/line")
    @ApiOperation("判断直线上是否通视")
    public CanSeeLineVO judgeLine(@RequestBody CanSeeLineDTO query) {
        return canSeeService.judgeLine(query);
    }

    /**
     * 通视性分析
     * 判断地图上一条线是否通视
     *
     * @param query 判断地图上一条线是否通视的查询请求
     * @return 判断地图上一条线是否通视的返回结果
     */
    @PostMapping("linearsight")
    @ApiOperation("判断直线上是否通视")
    public CanSeeLineVO linearSight(@RequestBody LinearSightDTO query) {
        return canSeeService.linearSight(query);
    }
}
