package com.mpw.model.common.util;

import com.mpw.model.jdfx.dto.PointDTO;
import com.mpw.model.jdfx.entity.DEMPoint;
import com.mpw.model.jdfx.entity.RouteLine;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.geotools.graph.structure.Node;

import java.util.List;
import java.util.Map;

/**
 * 点拆分工具类
 */
public class PointUtil {

    //根据经纬度计算距离(米)
    public static double calcDistanceMeter(Double latFrom, Double lngFrom, Double latTo, Double lngTo) {
        //起点坐标
        GlobalCoordinates gpsFrom = new GlobalCoordinates(latFrom, lngFrom);
        //目标坐标
        GlobalCoordinates gpsTo = new GlobalCoordinates(latTo, lngTo);
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, gpsFrom, gpsTo);
        return geoCurve.getEllipsoidalDistance();
    }

    //根据经纬度计算距离(米)
    public static double calcDistanceMeter(PointDTO start, DEMPoint end) {
        return calcDistanceMeter(start.getLat(), start.getLng(), end.getSy(), end.getSx());
    }

    //根据经纬度计算距离(米)
    public static double calcDistanceMeter(PointDTO start, PointDTO end) {
        return calcDistanceMeter(start.getLat(), start.getLng(), end.getLat(), end.getLng());
    }

    //查询离点最近的节点
    public static int closePointIndex(PointDTO start, Map<String , Node> list) {
        int idx = 0;
        double dis = 999999d;

        for (Map.Entry<String, Node> entry : list.entrySet()) {
            String key = entry.getKey();
            Node node = entry.getValue();
            PointDTO point = (PointDTO) node.getObject();
            if (point.getIsWater().equals(1)) continue;
            double distance = PointUtil.calcDistanceMeter(start, point);
            if (distance < dis) {
                dis = distance;
                idx = Integer.parseInt(key);
            }
        }
        return idx;
    }

    //获取离点 最近的线路
    public static int closeLineIndex(PointDTO start, List<RouteLine> lines, String es) {
        RouteLine target = null;
        double dis = 999999d;

        for (RouteLine line : lines) {
            //计算点到线之间的距离
            double distance = GeoUtil.calcPointLineDistance(start, line.getGeometry());
            if (distance < dis) {
                dis = distance;
                target = line;
            }
        }

        if ("s".equals(es)) {
            return target.getsNode();
        }
        return target.geteNode();
    }



}
