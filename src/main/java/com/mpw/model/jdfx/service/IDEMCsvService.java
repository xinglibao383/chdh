package com.mpw.model.jdfx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mpw.model.jdfx.entity.DEMCsv;

public interface IDEMCsvService extends IService<DEMCsv> {
    /**
     * WKT转空间字段
     */
    boolean tranas();

    /**
     * 计算相邻地块的坡度
     */
    boolean calcGradient();
}
