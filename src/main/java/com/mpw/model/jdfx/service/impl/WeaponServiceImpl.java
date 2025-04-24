package com.mpw.model.jdfx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.jdfx.entity.Weapon;
import com.mpw.model.jdfx.mapper.WeaponMapper;
import com.mpw.model.jdfx.service.IWeaponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WeaponServiceImpl extends ServiceImpl<WeaponMapper, Weapon> implements IWeaponService {

    private final Logger log = LoggerFactory.getLogger(WeaponServiceImpl.class);
}
