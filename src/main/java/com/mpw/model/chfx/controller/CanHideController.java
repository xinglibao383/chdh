package com.mpw.model.chfx.controller;

import com.mpw.model.chfx.domain.dto.ConcealmentDTO;
import com.mpw.model.chfx.domain.dto.DasqueradeDTO;
import com.mpw.model.chfx.domain.dto.HideDTO;
import com.mpw.model.chfx.domain.dto.HidePossibilityPreDTO;
import com.mpw.model.chfx.domain.vo.CanHideVO;
import com.mpw.model.chfx.domain.vo.ConcealmentVO;
import com.mpw.model.chfx.domain.vo.HidePossibilityVO;
import com.mpw.model.chfx.domain.vo.MaskVO;
import com.mpw.model.chfx.service.ICanHideService;
import com.mpw.model.chfx.service.impl.HideService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 隐蔽Controller类
 */
@RestController
@RequestMapping("canHide")
public class CanHideController {

    /**
     * 隐蔽Service接口
     */
    @Autowired
    private ICanHideService canHideService;

    @Autowired
    private HideService hideService;

    /**
     * 判断地图上一个区域是否可以隐蔽
     *
     * @param query 判断地图上一个区域是否可以隐蔽的查询请求体
     * @return 判断地图上一个区域是否可以隐蔽的返回结果
     */
    @PostMapping("judge")
    @ApiOperation("判断是否隐蔽")
    public CanHideVO judge(@RequestBody HideDTO query) {
        return canHideService.judge(query);
    }

    /**
     * 隐蔽概率分析
     * 判断地图上一个区域是否可以隐蔽
     *
     * @param query 判断地图上一个区域是否可以隐蔽的查询请求体
     * @return 判断地图上一个区域是否可以隐蔽的返回结果
     */
    @PostMapping("hiddenProbability")
    @ApiOperation("隐蔽概率分析")
    public ConcealmentVO hiddenProbabilityAnalysis(@RequestBody ConcealmentDTO query) {
        return canHideService.hiddenProbabilityAnalysis(query);
    }

    @PostMapping("hiddenProbability/V2")
    @ApiOperation("隐蔽概率分析V2")
    public HidePossibilityVO hiddenProbabilityAnalysisV2(@RequestBody ConcealmentDTO query) {
        return hideService.hiddenProbabilityAnalysis(query);
    }

    /**
     * 隐蔽概率分析的前置方法，主要查询出植被区域，然后让用户设置高度
     * @param query
     * @return
     */
    public HidePossibilityPreDTO hidePossibilityPre(@RequestBody ConcealmentDTO query) {
        return canHideService.hidePossibilityPre(query);
    }

    /**
     * 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级
     *
     * @param query 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的查询请求体
     * @return 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的返回结果
     */
    @PostMapping("judge/mask")
    @ApiOperation("伪装")
    public MaskVO judgeMask(@RequestBody HideDTO query) {
        return canHideService.judgeMask(query);
    }


    /**
     * 伪装程度分析
     * 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级
     *
     * @param query 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的查询请求体
     * @return 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的返回结果
     */
    @PostMapping("camouflageDegree")
    @ApiOperation("伪装程度分析")
    public MaskVO camouflageAnalysis(@RequestBody DasqueradeDTO query) {
        return canHideService.camouflageAnalysis(query);
    }
}
