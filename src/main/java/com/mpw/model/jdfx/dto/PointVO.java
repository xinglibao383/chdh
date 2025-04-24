package com.mpw.model.jdfx.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PointVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //路线途径点
    private List<PointDTO> points;

    //线路总长度
    private Double length;

    private PointDTO start;
    private PointDTO end;

    //计算耗时 单位:ms
    private Double calcTime;
    //路径耗时
    private Double pathTime;

    //算法类型
    private String calcType;

    //是否成功查找到路径 当不存在路径时返回false
    private Boolean isSuccess;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getCalcType() {
        return calcType;
    }

    public void setCalcType(String calcType) {
        this.calcType = calcType;
    }

    public Double getCalcTime() {
        return calcTime;
    }

    public void setCalcTime(Double calcTime) {
        this.calcTime = calcTime;
    }

    public Double getPathTime() {
        return pathTime;
    }

    public void setPathTime(Double pathTime) {
        this.pathTime = pathTime;
    }

    //经过的地形和各地形 长度
    private List<DemTypeLengthVO> demTypeLengthVOS;


    public List<PointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<PointDTO> points) {
        this.points = points;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public PointDTO getStart() {
        return start;
    }

    public void setStart(PointDTO start) {
        this.start = start;
    }

    public PointDTO getEnd() {
        return end;
    }

    public void setEnd(PointDTO end) {
        this.end = end;
    }

    public PointVO(List<PointDTO> points, PointDTO start, PointDTO end, Double calcTime, double speed, String calcType, boolean isSuccess) {
        this.points = points;
        this.start = start;
        this.end = end;
        this.calcTime = calcTime;
        this.calcType = calcType;
        this.isSuccess = isSuccess;

        List<DemTypeLengthVO> dtlVos = new ArrayList<>();
        Map<String, List<PointDTO>> demTypeMap = points.stream().collect(Collectors.groupingBy(PointDTO::getDemType));
        //路线总耗时
        double pathTime = 0;
        double length = 0;
        for (Map.Entry<String, List<PointDTO>> entry : demTypeMap.entrySet()) {
            //地形
            String demType = entry.getKey();
            List<PointDTO> dtoList = entry.getValue();
            //统计地形长度
            DoubleSummaryStatistics summary = dtoList.stream().mapToDouble(PointDTO::getLength).summaryStatistics();
            double totalLength = summary.getSum();
            length += totalLength;
            pathTime += totalLength / speed;
            dtlVos.add(new DemTypeLengthVO(demType, totalLength));
        }
        this.length = length;
        this.pathTime = pathTime;
        this.demTypeLengthVOS = dtlVos;
    }

    public List<DemTypeLengthVO> getDemTypeLengthVOS() {
        return demTypeLengthVOS;
    }

    public void setDemTypeLengthVOS(List<DemTypeLengthVO> demTypeLengthVOS) {
        this.demTypeLengthVOS = demTypeLengthVOS;
    }

    public PointVO() {
    }

}
