package com.mpw.model.jdfx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.jdfx.entity.RouteLine;
import com.mpw.model.jdfx.mapper.RoutePointMapper;
import com.mpw.model.jdfx.service.IRouteLineService;
import org.springframework.stereotype.Service;

@Service
public class RouteLineServiceImpl extends ServiceImpl<RoutePointMapper, RouteLine> implements IRouteLineService {


}
