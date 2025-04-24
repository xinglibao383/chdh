package com.mpw.model.chfx.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 测绘-武器表
 */
@TableName("ROUTE.CH_WEAPON")
public class XWeapon {
    @TableId(type = IdType.AUTO)
    private Long id;

    //武器名称
    @TableField("NAME")
    private String name;

    //口径
    @TableField("CALIBER")
    private Double caliber;

    //有效射程
    @TableField("EFFECT_RANGE")
    private Double effectRange;

    //爆炸当量
    @TableField("EXPLOSIVE_YIELD")
    private Double explosiveYield;

    //武器类型
    @TableField("WEAPON_TYPE")
    private String weaponType;

    //观察线高度
    @TableField("OBSERVE_LINE_HIGH")
    private Double observeLineHigh;

    //最大射程
    @TableField("MAX_RANGE")
    private Double maxRange;

    //最大射程
    @TableField("MIN_RANGE")
    private Double minRange;

    //死界距离
    @TableField("DEAD_ZONE_RANGE")
    private Double deadZoneRange;

    //武器精度
    @TableField("WEAPON_PRECISION")
    private Double weaponPrecision;

    //武器状态
    @TableField("WEAPON_STATUS")
    private String weaponStatus;

    //弹道类型
    @TableField("BALLISTIC_TYPE")
    private String ballisticType;

    @TableField("EXPLOSIVE_POWER")
    private double explosivePower;
    @TableField("RANG_NUM")
    private double rangNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCaliber() {
        return caliber;
    }

    public void setCaliber(Double caliber) {
        this.caliber = caliber;
    }

    public Double getEffectRange() {
        return effectRange;
    }

    public void setEffectRange(Double effectRange) {
        this.effectRange = effectRange;
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

    public Double getMinRange() {
        return minRange;
    }

    public void setMinRange(Double minRange) {
        this.minRange = minRange;
    }

    public XWeapon(Long id, String name, Double caliber, Double effectRange, Double explosiveYield, String weaponType, Double observeLineHigh, Double maxRange, Double minRange, Double deadZoneRange, Double weaponPrecision, String weaponStatus, String ballisticType) {
        this.id = id;
        this.name = name;
        this.caliber = caliber;
        this.effectRange = effectRange;
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

    public XWeapon() {
    }

    public double getExplosivePower() {
        return explosivePower;
    }

    public void setExplosivePower(double explosivePower) {
        this.explosivePower = explosivePower;
    }

    public double getRangNum() {
        return rangNum;
    }

    public void setRangNum(double rangNum) {
        this.rangNum = rangNum;
    }
}
