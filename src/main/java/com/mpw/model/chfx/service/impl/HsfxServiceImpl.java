package com.mpw.model.chfx.service.impl;

import com.mpw.model.chfx.constants.TrajectoryType;
import com.mpw.model.chfx.constants.WeaponStatus;
import com.mpw.model.chfx.constants.WeaponType;
import com.mpw.model.chfx.domain.dto.*;
import com.mpw.model.chfx.domain.entity.XWeapon;
import com.mpw.model.chfx.domain.model.Equipment;
import com.mpw.model.chfx.domain.model.ShootingPoint;
import com.mpw.model.chfx.mapper.XWeaponMapper;
import com.mpw.model.chfx.service.HsfxService;
import com.mpw.model.common.util.ReportGeneratorV2;
import com.mpw.model.common.util.UtilsV2;
import dm.jdbc.filter.stat.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 毁伤分析
 */
@Service
public class HsfxServiceImpl implements HsfxService {

    @Autowired
    private XWeaponMapper xWeaponMapper;

    /**
     * 毁伤能力分析
     * @param target
     * @return
     */
    @Override
    public MutilationDTO damageability(ShootingPoint target) {
        MutilationDTO mutilationDTO=new MutilationDTO();
        Equipment enemyWeapon = getExplode(target.getId());
        List<PersonnelDamageDTO> personnelDamageDTOList =  new ArrayList<>();
        ShootingPoint friendlyTarget = new ShootingPoint.Builder(target.getLatitude(), target.getLongitude(), true).height(target.getHeight()).build();
        double rangNum = enemyWeapon.getRangNum();//毁伤半径
        double step = rangNum / 10;
        double tempDistance = 1;
        for (int i = 1; i < 10; i++) {
            //获取时间
            double time = UtilsV2.calculateTime(tempDistance);
            JSONObject damageReport = ReportGeneratorV2.generateComprehensibeDestructionReport(tempDistance, friendlyTarget, enemyWeapon, null, enemyWeapon.getRangNum(), 50);
            JSONObject json = (JSONObject) damageReport.get("毁伤区域分析");
            //取出JSON
            JSONObject jsonZoneReport = (JSONObject) json.get("zoneReport");
            PersonnelDamageDTO personnelDamageDTO = new PersonnelDamageDTO();
            personnelDamageDTO.setRadiuStep((tempDistance-1) +" - "+ (step + tempDistance-1));
            personnelDamageDTO.setTime(time);
            personnelDamageDTO.setPeakPressire(jsonZoneReport.get("峰值压力").toString());
            //计算 overpressure * 1000 按照 kPa输出
            double  overpressureDouble = Double.parseDouble(jsonZoneReport.get("overpressure").toString()) * 1000;
            personnelDamageDTO.setOverpressure(String.valueOf(overpressureDouble));
            double  dynamicPressursDouble = Double.parseDouble(jsonZoneReport.get("dynamicPressurs").toString()) * 1000;
            personnelDamageDTO.setDynamicPressurs(String.valueOf(dynamicPressursDouble));
            personnelDamageDTO.setImpulse(jsonZoneReport.get("impulse").toString());
            JSONObject groundDamage3 = (JSONObject) jsonZoneReport.get("地面毁伤评估");
            JSONObject groundDamage1 = (JSONObject) jsonZoneReport.get("建筑毁伤评估");
            personnelDamageDTO.setBuildingDamageWkt(groundDamage1.get("BEM 数据").toString());
            personnelDamageDTO.setGroundDamageWkt(groundDamage3.get("BEM 数据").toString());



            JSONObject groundDamage2 = (JSONObject) jsonZoneReport.get("人员毁伤评估");
            personnelDamageDTO.setRadiuStep((tempDistance-1) +" - "+ (step + tempDistance-1)); //检测半径
            /**
             * 毁伤等级判断当前 默认为无伤状态
             */
            personnelDamageDTO.setCyLevel("0");
            if(groundDamage2.get("毁伤等级").toString().equals("致命")){
                personnelDamageDTO.setCyLevel("4");
            }
            if(groundDamage2.get("毁伤等级").toString().equals("重伤（骨折或者内出血）")){
                personnelDamageDTO.setCyLevel("3");

            }
            if(groundDamage2.get("毁伤等级").toString().equals("中伤（内伤或者耳膜破裂）")){
                personnelDamageDTO.setCyLevel("2");

            }
            if(groundDamage2.get("毁伤等级").toString().equals("轻伤（耳鸣）")){
                personnelDamageDTO.setCyLevel("1");
            }
            personnelDamageDTO.setShLevel(groundDamage2.get("毁伤等级").toString());
            personnelDamageDTO.setWtk(groundDamage2.get("BEM 数据").toString());
            personnelDamageDTOList.add(personnelDamageDTO);
            tempDistance += step;
        }
        mutilationDTO.setPersonnelDamageDTOList(personnelDamageDTOList);
        YanDTO yanDTO=terrainAnalysis(target);
        mutilationDTO.setYanVO(yanDTO);
        return mutilationDTO;
    }

    /**
     * 爆炸冲击结果分析
     * @param target
     * @return shockDTO
     */
    @Override
    public List<shockDTO> explosionShock(ShootingPoint target) {
        Equipment enemyWeapon = getWeapon(target.getId());
        List<shockDTO> staffDTOList =  new ArrayList<>();
        ShootingPoint friendlyTarget = new ShootingPoint.Builder(target.getLatitude(), target.getLongitude(), true).height(target.getHeight()).build();
            double rangNum = enemyWeapon.getRangNum();
            double step = rangNum / 10;
            double tempDistance = 1;
            for (int i = 1; i < 10; i++) {
                //获取时间
                double time = UtilsV2.calculateTime(tempDistance);
                JSONObject damageReport = ReportGeneratorV2.generateComprehensibeDestructionReport(tempDistance, friendlyTarget, enemyWeapon, null, enemyWeapon.getRangNum(), 50);
                JSONObject json = (JSONObject) damageReport.get("毁伤区域分析");
                //取出JSON
                JSONObject jsonZoneReport = (JSONObject) json.get("zoneReport");
                shockDTO shockDTO = new shockDTO();
                shockDTO.setRadiuStep((tempDistance-1) +" - "+ (step + tempDistance-1));
                shockDTO.setTime(time);
                shockDTO.setPeakPressire(jsonZoneReport.get("峰值压力").toString());
                //计算 overpressure * 1000 按照 kPa输出
                double  overpressureDouble = Double.parseDouble(jsonZoneReport.get("overpressure").toString()) * 1000;
                shockDTO.setOverpressure(String.valueOf(overpressureDouble));
                double  dynamicPressursDouble = Double.parseDouble(jsonZoneReport.get("dynamicPressurs").toString()) * 1000;
                shockDTO.setDynamicPressurs(String.valueOf(dynamicPressursDouble));
                shockDTO.setImpulse(jsonZoneReport.get("impulse").toString());
                JSONObject groundDamage3 = (JSONObject) jsonZoneReport.get("地面毁伤评估");
                JSONObject groundDamage1 = (JSONObject) jsonZoneReport.get("建筑毁伤评估");
                shockDTO.setBuildingDamageWkt(groundDamage1.get("BEM 数据").toString());
                shockDTO.setGroundDamageWkt(groundDamage3.get("BEM 数据").toString());
                staffDTOList.add(shockDTO);
                tempDistance += step;
            }
        return staffDTOList;
    }

    /**
     * 结构与地形破坏分析
     * @param target
     * @return
     */
    @Override
    public YanDTO terrainAnalysis(ShootingPoint target) {
        Equipment enemyWeapon = getWeapon(target.getId());
        //初始价当前经度纬度节点
        ShootingPoint friendlyTarget = new ShootingPoint.Builder(target.getLatitude(), target.getLongitude(), true).height(target.getHeight()).build();
        double rangNum = enemyWeapon.getRangNum();
        JSONObject damageReport = ReportGeneratorV2.generateComprehensibeDestructionReport(rangNum,friendlyTarget,enemyWeapon,null, enemyWeapon.getRangNum(),50);
        JSONObject json = (JSONObject) damageReport.get("毁伤区域分析");
        JSONObject jsonZoneReport = (JSONObject) json.get("zoneReport");
        YanDTO yanDTO = new YanDTO();
        yanDTO.setPenntrationDepth(jsonZoneReport.get("侵彻深度").toString());
        JSONObject groundDamage3 = (JSONObject) jsonZoneReport.get("地面毁伤评估");
        JSONObject groundDamage1 = (JSONObject) jsonZoneReport.get("建筑毁伤评估");
        yanDTO.setMaxDamageRadius(groundDamage3.get("毁伤范围半径").toString());
        yanDTO.setFragametRange(jsonZoneReport.get("破片破坏范围").toString());
        yanDTO.setGroundDamage(groundDamage3.get("毁伤等级").toString());
        yanDTO.setBuildingDamage(groundDamage1.get("毁伤等级").toString());
        yanDTO.setBuildingDamageWkt(groundDamage1.get("BEM 数据").toString());
        yanDTO.setGroundDamageWkt(groundDamage3.get("BEM 数据").toString());
        return yanDTO;
    }

    /**
     * 人员伤害分析
     * @param target
     * @return List<staffDTO>
     */
    @Override
    public List<staffDTO> harmPeople(ShootingPoint target) {
        List<staffDTO> staffDTOList =  new ArrayList<>();
        Equipment enemyWeapon = getWeapon(target.getId());
        ShootingPoint friendlyTarget = new ShootingPoint.Builder(target.getLatitude(), target.getLongitude(), true).height(target.getHeight()).build();
        double rangNum = enemyWeapon.getRangNum();
        double step = rangNum / 10;
        double tempDistance = 1;
        for (int i = 1; i < 10; i++) {
                JSONObject damageReport = ReportGeneratorV2.generateComprehensibeDestructionReport(tempDistance,friendlyTarget,enemyWeapon,null, enemyWeapon.getRangNum(),50);
                JSONObject json = (JSONObject) damageReport.get("毁伤区域分析");
                JSONObject jsonZoneReport = (JSONObject) json.get("zoneReport");
                staffDTO yanDTO = new staffDTO();
                JSONObject groundDamage3 = (JSONObject) jsonZoneReport.get("人员毁伤评估");
                yanDTO.setRadiuStep((tempDistance-1) +" - "+ (step + tempDistance-1)); //检测半径
                /**
                 * 毁伤等级判断当前 默认为无伤状态
                 */
                yanDTO.setCyLevel("0");
                if(groundDamage3.get("毁伤等级").toString().equals("致命")){
                    yanDTO.setCyLevel("4");
                }
                if(groundDamage3.get("毁伤等级").toString().equals("重伤（骨折或者内出血）")){
                    yanDTO.setCyLevel("3");

                }
                if(groundDamage3.get("毁伤等级").toString().equals("中伤（内伤或者耳膜破裂）")){
                    yanDTO.setCyLevel("2");

                }
                if(groundDamage3.get("毁伤等级").toString().equals("轻伤（耳鸣）")){
                    yanDTO.setCyLevel("1");
                }
                yanDTO.setShLevel(groundDamage3.get("毁伤等级").toString());
                yanDTO.setWtk(groundDamage3.get("BEM 数据").toString());
                staffDTOList.add(yanDTO);
                tempDistance += step;

        }
        return staffDTOList;

    }

    /**
     * 返回武器列表
     * @return XWeapon
     */
    @Override
    public List<XWeapon> getWeaponList() {
        return xWeaponMapper.selectAll();
    }

    /**
     * 返回爆炸物列表
     * @return XWeapon
     */
    @Override
    public List<XWeapon> getExplodeList() {
        return xWeaponMapper.selectExplodeAll();
    }

    /**
     * 根据id查询 当前使用武器
     * @param id
     * @return Equipment
     */
    private Equipment getWeapon(Long id) {
        List<XWeapon> weaponList = xWeaponMapper.selectAll();
        for (XWeapon weapon : weaponList) {
            if (weapon.getId() == id) {
                /**
                 * 构造对象
                 */
                Equipment equipment = new Equipment(
                        WeaponType.fromDescription(
                                weapon.getWeaponType()),
                        weapon.getObserveLineHigh(),
                        weapon.getMinRange(),
                        weapon.getMaxRange(),
                        weapon.getDeadZoneRange(),
                        weapon.getWeaponPrecision(),
                        WeaponStatus.fromDescription(weapon.getWeaponStatus()),
                        fromDescription(weapon.getBallisticType()),
                        weapon.getExplosiveYield());
                equipment.setRangNum(weapon.getRangNum());
                return equipment;
            }
        }
        return null;
    }

    /**
     * 根据id查询 当前使用爆炸物参数
     * @param id
     * @return Equipment
     */
    private Equipment getExplode(Long id) {
        XWeapon weaponOne = xWeaponMapper.selectExplodeById(id);

                /**
                 * 构造对象
                 */

        return new Equipment(weaponOne.getRangNum(),weaponOne.getExplosivePower());

    }

    /**
     * 判断弹道类型
     * @param value
     * @return
     */
    public static TrajectoryType fromDescription(String value) {
        for (TrajectoryType type : TrajectoryType.values()) {
            if (type.getDescription().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}
