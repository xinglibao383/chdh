package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 平均速度VO列表
 */
public class SpeedVO implements Serializable {
    /**
     * 平均速度VO列表
     */
    private List<AvgSpeedVO> avgSpeedVOList;

    public SpeedVO() {
    }

    public SpeedVO(List<AvgSpeedVO> avgSpeedVOList) {
        this.avgSpeedVOList = avgSpeedVOList;
    }

    public List<AvgSpeedVO> getAvgSpeedVOList() {
        return avgSpeedVOList;
    }

    public void setAvgSpeedVOList(List<AvgSpeedVO> avgSpeedVOList) {
        this.avgSpeedVOList = avgSpeedVOList;
    }
}
