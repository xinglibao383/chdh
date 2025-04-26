package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.dto.GreenDTOV2;
import com.mpw.model.chfx.domain.entity.GreenArea;

import java.util.List;

public interface IGreenAreaService {
    List<GreenArea> selectByBigArea(String bigArea);

    List<GreenArea> selectByLine(GreenDTOV2 query);
}
