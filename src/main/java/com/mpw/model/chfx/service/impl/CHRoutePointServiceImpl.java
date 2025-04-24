package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.entity.ChRoutePoint;
import com.mpw.model.chfx.mapper.ChRoutePointMapper;
import com.mpw.model.chfx.service.IChRoutePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CHRoutePointServiceImpl implements IChRoutePointService {

    @Autowired
    private ChRoutePointMapper chRoutePointMapper;

    @Override
    public boolean tranas() {
        List<ChRoutePoint> routePointList = chRoutePointMapper.selectList();
        for (ChRoutePoint routePoint : routePointList) {
            Long id = routePoint.getId();
            String geometry = routePoint.getGeometry();
            chRoutePointMapper.transGeom(id, geometry);
        }
        return false;
    }
}
