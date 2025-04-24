package com.mpw.model.chfx.domain.dto;

public class XWeaponDTO {
    private Long id;

    //爆炸当量
    private Double explosiveYield;

    //武器类型
    private String weaponType;

    //观察线高度
    private Double observeLineHigh;

    //最大射程
    private Double maxRange;

    //最小射程
    private Double minRange;

    //死界距离
    private Double deadZoneRange;

    //武器精度
    private Double weaponPrecision;

    //武器状态
    private String weaponStatus;

    //弹道类型
    private String ballisticType;

    public XWeaponDTO() {
    }

    public XWeaponDTO(Long id, Double explosiveYield, String weaponType, Double observeLineHigh, Double maxRange, Double minRange, Double deadZoneRange, Double weaponPrecision, String weaponStatus, String ballisticType) {
        this.id = id;
        this.explosiveYield = explosiveYield;
        this.weaponType = weaponType;
        this.observeLineHigh = observeLineHigh;
        this.maxRange = maxRange;
        this.minRange = minRange;
        this.deadZoneRange = deadZoneRange;
        this.weaponPrecision = weaponPrecision;
        this.weaponStatus = weaponStatus;
        this.ballisticType = ballisticType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getExplosiveYield() {
        return explosiveYield;
    }

    public void setExplosiveYield(Double explosiveYield) {
        this.explosiveYield = explosiveYield;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    public Double getObserveLineHigh() {
        return observeLineHigh;
    }

    public void setObserveLineHigh(Double observeLineHigh) {
        this.observeLineHigh = observeLineHigh;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(Double maxRange) {
        this.maxRange = maxRange;
    }

    public Double getMinRange() {
        return minRange;
    }

    public void setMinRange(Double minRange) {
        this.minRange = minRange;
    }

    public Double getDeadZoneRange() {
        return deadZoneRange;
    }

    public void setDeadZoneRange(Double deadZoneRange) {
        this.deadZoneRange = deadZoneRange;
    }

    public Double getWeaponPrecision() {
        return weaponPrecision;
    }

    public void setWeaponPrecision(Double weaponPrecision) {
        this.weaponPrecision = weaponPrecision;
    }

    public String getWeaponStatus() {
        return weaponStatus;
    }

    public void setWeaponStatus(String weaponStatus) {
        this.weaponStatus = weaponStatus;
    }

    public String getBallisticType() {
        return ballisticType;
    }

    public void setBallisticType(String ballisticType) {
        this.ballisticType = ballisticType;
    }
}
