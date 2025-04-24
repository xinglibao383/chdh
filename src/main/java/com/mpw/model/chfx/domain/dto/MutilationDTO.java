package com.mpw.model.chfx.domain.dto;

import java.util.List;

public class MutilationDTO {

    /**
     * 结构与地形破坏
     */
    private YanDTO yanVO;

    /**
     * 爆炸冲击与人员伤害
     */
    private List<PersonnelDamageDTO> personnelDamageDTOList;


    public YanDTO getYanVO() {
        return yanVO;
    }

    public void setYanVO(YanDTO yanVO) {
        this.yanVO = yanVO;
    }
    public List<PersonnelDamageDTO> getPersonnelDamageDTOList() {
        return personnelDamageDTOList;
    }

    public void setPersonnelDamageDTOList(List<PersonnelDamageDTO> personnelDamageDTOList) {
        this.personnelDamageDTOList = personnelDamageDTOList;
    }
}
