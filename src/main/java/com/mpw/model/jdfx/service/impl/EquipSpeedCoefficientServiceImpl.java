package com.mpw.model.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.jdfx.entity.EquipSpeedCoefficient;
import com.mpw.model.jdfx.mapper.EquipSpeedCoefficientMapper;
import com.mpw.model.jdfx.service.IEquipSpeedCoefficientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EquipSpeedCoefficientServiceImpl extends ServiceImpl<EquipSpeedCoefficientMapper, EquipSpeedCoefficient> implements IEquipSpeedCoefficientService {

    private final Logger log = LoggerFactory.getLogger(EquipSpeedCoefficientServiceImpl.class);
}
