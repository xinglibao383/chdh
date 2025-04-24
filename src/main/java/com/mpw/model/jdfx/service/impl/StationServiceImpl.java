package com.mpw.model.jdfx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.jdfx.entity.Station;
import com.mpw.model.jdfx.mapper.StationMapper;
import com.mpw.model.jdfx.service.IStationService;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationService {
}
