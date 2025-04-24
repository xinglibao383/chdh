package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.dto.ConcealmentDTO;
import com.mpw.model.chfx.domain.dto.DasqueradeDTO;
import com.mpw.model.chfx.domain.dto.HideDTO;
import com.mpw.model.chfx.domain.dto.HidePossibilityPreDTO;
import com.mpw.model.chfx.domain.vo.CanHideVO;
import com.mpw.model.chfx.domain.vo.ConcealmentVO;
import com.mpw.model.chfx.domain.vo.MaskVO;

/**
 * 判断是否隐蔽的服务接口
 */
public interface ICanHideService {
    /**
     * 判断是否隐蔽
     *
     * @param query 判断是否隐蔽的查询实体类
     * @return 判断是否隐蔽的结果VO
     */
    CanHideVO judge(HideDTO query);

    /**
     * 隐蔽概率分析
     * 判断地图上一个区域是否可以隐蔽
     *
     * @param query 判断地图上一个区域是否可以隐蔽的查询请求体
     * @return 判断地图上一个区域是否可以隐蔽的返回结果
     */
    ConcealmentVO hiddenProbabilityAnalysis(ConcealmentDTO query);

    /**
     * 判断伪装程度
     *
     * @param query 判断伪装程度的查询实体类
     * @return 判断伪装程度的结果VO
     */
    MaskVO judgeMask(HideDTO query);


    /**
     * 伪装程度分析
     * 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级
     *
     * @param query 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的查询请求体
     * @return 查询地图上一个区域哪些位置是绝对隐蔽，以及其他区域的伪装等级的返回结果
     */
    MaskVO camouflageAnalysis(DasqueradeDTO query);

    HidePossibilityPreDTO hidePossibilityPre(ConcealmentDTO query);
}
