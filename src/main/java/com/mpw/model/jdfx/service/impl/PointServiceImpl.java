package com.mpw.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.jdfx.entity.DEMPoint;
import com.mpw.model.jdfx.mapper.DEMPointMapper;
import com.mpw.model.jdfx.service.IDEMPointService;
import org.springframework.stereotype.Service;

@Service
public class PointServiceImpl extends ServiceImpl<DEMPointMapper, DEMPoint> implements IDEMPointService {

}
