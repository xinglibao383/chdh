package com.mpw.model.chfx.service;

import com.mpw.model.chfx.domain.dto.EffDTO;
import com.mpw.model.chfx.domain.dto.SHideDTO;
import com.mpw.model.chfx.domain.dto.XDeadAreaDTO;
import com.mpw.model.chfx.domain.vo.DeadVO;
import com.mpw.model.chfx.domain.vo.EffVO;
import com.mpw.model.chfx.domain.vo.SHideVO;

public interface IXSyfxService {
    DeadVO dead(XDeadAreaDTO xDeadAreaDTO);

    EffVO eff(EffDTO effDTO);

    SHideVO hide(SHideDTO hideDTO);
}
