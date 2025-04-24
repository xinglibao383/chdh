package com.mpw.model.jdfx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpw.model.jdfx.entity.DEMCsv;

public interface DEMCsvMapper extends BaseMapper<DEMCsv> {

    int transGeom(Long id, String geometryStr);
}
