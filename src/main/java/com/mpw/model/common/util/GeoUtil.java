package com.mpw.model.common.util;

import com.mpw.model.jdfx.dto.PointDTO;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.operation.distance.DistanceOp;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeoUtil {

    private static WKTReader reader = new WKTReader();
    private static GeometryFactory geometryFactory = new GeometryFactory();

    public static List<PointDTO> wtkLineToPointDTO(String wktLineStr) {
        wktLineStr = wktLineStr.replaceAll("LINESTRING \\(", "").replaceAll("\\)", "");
        String[] split = wktLineStr.split(",");
        List<PointDTO> pointDTOs = new ArrayList<>();

        for (int i = 0; i < split.length; i++) {
            PointDTO point = new PointDTO();
            split[i] = split[i].trim();
            point.setLng(Double.parseDouble(split[i].split(" ")[0]));
            point.setLat(Double.parseDouble(split[i].split(" ")[1]));
            pointDTOs.add(point);
        }
        return pointDTOs;
    }

    /**
     * 拼接线段
     */
    public static List<PointDTO> joinLines(List<PointDTO> line1, List<PointDTO> line2) {
        PointDTO start = line1.get(0);
        PointDTO end = line1.get(line1.size() - 1);
        PointDTO start1 = line2.get(0);
        PointDTO end1 = line2.get(line2.size() - 1);
        double dis0 = PointUtil.calcDistanceMeter(start, start1);
        double dis4 = PointUtil.calcDistanceMeter(start, end1);
        double dis1 = PointUtil.calcDistanceMeter(end, start1);
        double dis2 = PointUtil.calcDistanceMeter(end, end1);
        //start是起始开头
        if ((dis0 + dis4) > (dis1 + dis2)) {
            if (dis1 <= dis2) {
                line1.addAll(line2);
            }else {
                Collections.reverse(line2);
                line1.addAll(line2);
            }
        }else {
            if (dis0 <= dis4) {
                Collections.reverse(line1);
                line1.addAll(line2);
            }else {
                line2.addAll(line1);
                line1 = line2;
            }
        }
        return line1;
    }

    /**
     * 判断点和线之间的距离
     */
    public static double calcPointLineDistance(PointDTO point, String wktLineStr) {
        Geometry line = getGeometry(wktLineStr);
        Coordinate pointCoord = new Coordinate(point.getLng(),point.getLat());
        Geometry pointGeometry = geometryFactory.createPoint(pointCoord);

        DistanceOp distanceOp = new DistanceOp(pointGeometry, line);
        return distanceOp.distance();
    }


    /**
     * 根据wkt构造geometry对象
     */
    public static Geometry getGeometry(String str){
        try {
            if(reader==null){
                reader = new WKTReader();
            }
            return reader.read(str);
        } catch (ParseException e) {
            throw new RuntimeException("buildGeometry Error",e);
        }
    }

    /**
     * 平面坐标系转换 84坐标转莫克托
     */
    public static double[] wgs84ToMercator(double lon, double lat){
        double[] longs = new double[2];
        double c_lon = (lon / 180.0) * 20037508.34;
        if (lat > 85.05112){
            lat = 85.05112;
        }
        if(lat == -85.05112){
            lat = -85.05112;
        }
        lat = (Math.PI / 180.0) * lat;
        double tmp = Math.PI / 4.0 + lat / 2.0;
        double c_lat = 20037508.34 * Math.log(Math.tan(tmp)) / Math.PI;
        longs[0] = c_lon;
        longs[1] = c_lat;
        return longs;
    }

    /**
     * 根据经纬度计算角度
     */
    public static double caLcAngleGeo(Double lat, Double lng, Double latTo, Double lngTo) {
        //起点坐标
        GlobalCoordinates gpsFrom = new GlobalCoordinates(lat, lng);
        //目标坐标
        GlobalCoordinates gpsTo = new GlobalCoordinates(latTo, lngTo);
        //创建GeodeticCalculator, 调用计算方法, 传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, gpsFrom, gpsTo);
        return geoCurve.getAzimuth();
    }

}
