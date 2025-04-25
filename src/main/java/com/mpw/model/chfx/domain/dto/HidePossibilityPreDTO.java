package com.mpw.model.chfx.domain.dto;

import com.mpw.model.chfx.domain.entity.GreenArea;

import java.util.ArrayList;
import java.util.List;

public class HidePossibilityPreDTO {
    private List<GreenArea> redGreenAreas;
    private List<GreenArea> blueGreenAreas;

    public HidePossibilityPreDTO() {
        redGreenAreas = new ArrayList<>();
        blueGreenAreas = new ArrayList<>();
    }

    public HidePossibilityPreDTO(List<GreenArea> redGreenAreas, List<GreenArea> blueGreenAreas) {
        this.redGreenAreas = redGreenAreas == null ? new ArrayList<>() : redGreenAreas;
        this.blueGreenAreas = blueGreenAreas == null ? new ArrayList<>() : blueGreenAreas;
    }

    public List<GreenArea> getRedGreenAreas() {
        return redGreenAreas;
    }

    public void setRedGreenAreas(List<GreenArea> redGreenAreas) {
        this.redGreenAreas = redGreenAreas;
    }

    public List<GreenArea> getBlueGreenAreas() {
        return blueGreenAreas;
    }

    public void setBlueGreenAreas(List<GreenArea> blueGreenAreas) {
        this.blueGreenAreas = blueGreenAreas;
    }
}
