package com.mpw.model.chfx.domain.vo;

import java.io.Serializable;
import java.util.List;

public class DeadVO implements Serializable {
    Double totalArea;
    Double friendlyTotalArea;
    Double enemyTotalArea;

    Double friendlyDeadZoneArea;
    List<double[]> friendlyDeadZone;
    Double friendlyDeadZoneAreaRatio;

    Double friendlyDeadZoneSectorArea;
    List<double[]> friendlyDeadZoneSector;

    Double enemyDeadZoneArea;
    List<double[]> enemyDeadZone;
    Double enemyDeadZoneAreaRatio;

    Double enemyDeadZoneSectorArea;
    List<double[]> enemyDeadZoneSector;

    public DeadVO() {
    }

    public DeadVO(Double totalArea, Double friendlyTotalArea, Double enemyTotalArea, Double friendlyDeadZoneArea, List<double[]> friendlyDeadZone, Double friendlyDeadZoneAreaRatio, Double friendlyDeadZoneSectorArea, List<double[]> friendlyDeadZoneSector, Double enemyDeadZoneArea, List<double[]> enemyDeadZone, Double enemyDeadZoneAreaRatio, Double enemyDeadZoneSectorArea, List<double[]> enemyDeadZoneSector) {
        this.totalArea = totalArea;
        this.friendlyTotalArea = friendlyTotalArea;
        this.enemyTotalArea = enemyTotalArea;
        this.friendlyDeadZoneArea = friendlyDeadZoneArea;
        this.friendlyDeadZone = friendlyDeadZone;
        this.friendlyDeadZoneAreaRatio = friendlyDeadZoneAreaRatio;
        this.friendlyDeadZoneSectorArea = friendlyDeadZoneSectorArea;
        this.friendlyDeadZoneSector = friendlyDeadZoneSector;
        this.enemyDeadZoneArea = enemyDeadZoneArea;
        this.enemyDeadZone = enemyDeadZone;
        this.enemyDeadZoneAreaRatio = enemyDeadZoneAreaRatio;
        this.enemyDeadZoneSectorArea = enemyDeadZoneSectorArea;
        this.enemyDeadZoneSector = enemyDeadZoneSector;
    }

    public Double getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Double totalArea) {
        this.totalArea = totalArea;
    }

    public Double getFriendlyTotalArea() {
        return friendlyTotalArea;
    }

    public void setFriendlyTotalArea(Double friendlyTotalArea) {
        this.friendlyTotalArea = friendlyTotalArea;
    }

    public Double getEnemyTotalArea() {
        return enemyTotalArea;
    }

    public void setEnemyTotalArea(Double enemyTotalArea) {
        this.enemyTotalArea = enemyTotalArea;
    }

    public Double getFriendlyDeadZoneArea() {
        return friendlyDeadZoneArea;
    }

    public void setFriendlyDeadZoneArea(Double friendlyDeadZoneArea) {
        this.friendlyDeadZoneArea = friendlyDeadZoneArea;
    }

    public List<double[]> getFriendlyDeadZone() {
        return friendlyDeadZone;
    }

    public void setFriendlyDeadZone(List<double[]> friendlyDeadZone) {
        temp(friendlyDeadZone);
        this.friendlyDeadZone = friendlyDeadZone;
    }

    public Double getFriendlyDeadZoneAreaRatio() {
        return friendlyDeadZoneAreaRatio;
    }

    public void setFriendlyDeadZoneAreaRatio(Double friendlyDeadZoneAreaRatio) {
        this.friendlyDeadZoneAreaRatio = friendlyDeadZoneAreaRatio;
    }

    public void temp(List<double[]> list) {
        for (double[] doubles : list) {
            double num = doubles[0];
            doubles[0] = doubles[1];
            doubles[1] = num;
        }
    }

    public Double getFriendlyDeadZoneSectorArea() {
        return friendlyDeadZoneSectorArea;
    }

    public void setFriendlyDeadZoneSectorArea(Double friendlyDeadZoneSectorArea) {
        this.friendlyDeadZoneSectorArea = friendlyDeadZoneSectorArea;
    }

    public List<double[]> getFriendlyDeadZoneSector() {
        return friendlyDeadZoneSector;
    }

    public void setFriendlyDeadZoneSector(List<double[]> friendlyDeadZoneSector) {
        temp(friendlyDeadZoneSector);
        this.friendlyDeadZoneSector = friendlyDeadZoneSector;
    }

    public Double getEnemyDeadZoneArea() {
        return enemyDeadZoneArea;
    }

    public void setEnemyDeadZoneArea(Double enemyDeadZoneArea) {
        this.enemyDeadZoneArea = enemyDeadZoneArea;
    }

    public List<double[]> getEnemyDeadZone() {
        return enemyDeadZone;
    }

    public void setEnemyDeadZone(List<double[]> enemyDeadZone) {
        temp(enemyDeadZone);
        this.enemyDeadZone = enemyDeadZone;
    }

    public Double getEnemyDeadZoneAreaRatio() {
        return enemyDeadZoneAreaRatio;
    }

    public void setEnemyDeadZoneAreaRatio(Double enemyDeadZoneAreaRatio) {
        this.enemyDeadZoneAreaRatio = enemyDeadZoneAreaRatio;
    }

    public Double getEnemyDeadZoneSectorArea() {
        return enemyDeadZoneSectorArea;
    }

    public void setEnemyDeadZoneSectorArea(Double enemyDeadZoneSectorArea) {
        this.enemyDeadZoneSectorArea = enemyDeadZoneSectorArea;
    }

    public List<double[]> getEnemyDeadZoneSector() {
        return enemyDeadZoneSector;
    }

    public void setEnemyDeadZoneSector(List<double[]> enemyDeadZoneSector) {
        temp(enemyDeadZoneSector);
        this.enemyDeadZoneSector = enemyDeadZoneSector;
    }
}
