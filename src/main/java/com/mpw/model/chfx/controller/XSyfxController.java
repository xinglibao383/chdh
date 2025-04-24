package com.mpw.model.chfx.controller;

import com.mpw.model.chfx.service.ICanSeeService;
import com.mpw.model.chfx.domain.dto.EffDTO;
import com.mpw.model.chfx.domain.dto.SHideDTO;
import com.mpw.model.chfx.domain.dto.XDeadAreaDTO;
import com.mpw.model.chfx.domain.dto.XViewDTO;
import com.mpw.model.chfx.domain.entity.XWeapon;
import com.mpw.model.chfx.mapper.XWeaponMapper;
import com.mpw.model.chfx.service.IXSyfxService;
import com.mpw.model.chfx.domain.vo.DeadVO;
import com.mpw.model.chfx.domain.vo.EffVO;
import com.mpw.model.chfx.domain.vo.SHideVO;
import com.mpw.model.chfx.domain.vo.ViewVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 射击影响分析控制器
 */
@RestController
@RequestMapping("xsyfx")
public class XSyfxController {

    @Autowired
    private IXSyfxService ixSyfxService;

    @Autowired
    private ICanSeeService iCanSeeService;

    @Autowired
    private XWeaponMapper xWeaponMapper;

    /**
     * 射击效能分析
     *
     * @param effDTO
     * @return
     */
    @ApiOperation("射击效能分析")
    @PostMapping("eff")
    public EffVO eff(@RequestBody EffDTO effDTO) {
        return ixSyfxService.eff(effDTO);
    }

    /**
     * 隐蔽能力分析
     *
     * @param hideDTO
     * @return
     */
    @ApiOperation("隐蔽能力分析")
    @PostMapping("hide")
    public SHideVO hide(@RequestBody SHideDTO hideDTO) {
        return ixSyfxService.hide(hideDTO);
    }

    /**
     * 获取武器列表
     *
     * @return
     */
    @ApiOperation("获取武器列表")
    @GetMapping("/getWeapons")
    public List<XWeapon> getWeapons() {
        return xWeaponMapper.selectAll();
    }

    /**
     * 战场视野分析
     *
     * @param xViewDTO
     * @return
     */
    @ApiOperation("战场视野分析")
    @PostMapping("view")
    public ViewVO view(@RequestBody XViewDTO xViewDTO) {
        return iCanSeeService.view(xViewDTO);
    }

    /**
     * 射击死区分析
     *
     * @param xDeadAreaDTO
     * @return
     */
    @ApiOperation("射击死区分析")
    @PostMapping("dead")
    public DeadVO dead(@RequestBody XDeadAreaDTO xDeadAreaDTO) {
        return ixSyfxService.dead(xDeadAreaDTO);
    }
}
