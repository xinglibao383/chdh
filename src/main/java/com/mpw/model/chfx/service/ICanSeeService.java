package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.dto.*;
import com.mpw.model.chfx.domain.vo.CanSeeLineVO;
import com.mpw.model.chfx.domain.vo.CanSeeVO;
import com.mpw.model.chfx.domain.vo.ViewVO;
import com.mpw.model.chfx.domain.vo.VisualDomainVO;

/**
 * 判断是否可以通视的服务接口
 */
public interface ICanSeeService {
    /**
     * 判断一个区域的通视情况
     *
     * @param query 判断一个区域的通视情况的请求体
     * @return 判断一个区域的通视结果VO
     */
    CanSeeVO judge(CanSeeDTO query);

    /**
     * 通过多线程优化计算
     *
     * @param query 判断一个区域的通视情况的请求体
     * @return 判断一个区域的通视结果VO
     */
    CanSeeVO judgePall(CanSeeDTO query);

    /**
     * 判断地图上一个区域是否可通视
     * @param query 判断地图上一个区域是否通视的查询请求
     * @return 判断地图上一个区域是否通视的返回结果
     */
    VisualDomainVO visualDomainAnalysis(VisualDomainDTO query);

    /**
     * 判断一条线是否通视
     *
     * @param query 判断一条线是否通视的请求体
     * @return 判断一条线是否通视的返回结果VO
     */
    CanSeeLineVO judgeLine(CanSeeLineDTO query);


    /**
     * 通视性分析
     * 判断地图上一条线是否通视
     *
     * @param query 判断地图上一条线是否通视的查询请求
     * @return 判断地图上一条线是否通视的返回结果
     */
    CanSeeLineVO linearSight(LinearSightDTO query);

    /**
     * 战场视野分析
     *
     * @param xViewDTO
     * @return
     */
    ViewVO view(XViewDTO xViewDTO);

}
