package com.mpw.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.jdfx.entity.RailRoad;
import com.mpw.model.jdfx.mapper.RailRoadMapper;
import com.mpw.model.jdfx.service.IRailRoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RailRoadServiceImpl extends ServiceImpl<RailRoadMapper, RailRoad> implements IRailRoadService {

    private final Logger log = LoggerFactory.getLogger(RailRoadServiceImpl.class);
}
