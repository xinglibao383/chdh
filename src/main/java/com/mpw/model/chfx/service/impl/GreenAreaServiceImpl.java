package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.domain.entity.GreenArea;
import com.mpw.model.chfx.service.IGreenAreaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GreenAreaServiceImpl implements IGreenAreaService {
    @Override
    public List<GreenArea> selectByBigArea(String bigArea) {
        List<GreenArea> result = new ArrayList<>();
        return result;
    }
}
