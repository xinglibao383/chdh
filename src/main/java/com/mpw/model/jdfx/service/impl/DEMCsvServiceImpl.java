package com.mpw.model.jdfx.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mpw.model.common.util.GeoUtil;
import com.mpw.model.common.util.PointUtil;
import com.mpw.model.jdfx.entity.DEMCsv;
import com.mpw.model.jdfx.mapper.DEMCsvMapper;
import com.mpw.model.jdfx.service.IDEMCsvService;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class DEMCsvServiceImpl extends ServiceImpl<DEMCsvMapper, DEMCsv> implements IDEMCsvService {

    @Override
    public boolean tranas() {
        List<DEMCsv> demCsvs = baseMapper.selectList(null);
        for (DEMCsv demCsv : demCsvs) {
            Long id = demCsv.getId();
            String geometryStr = demCsv.getGeometryStr();
            baseMapper.transGeom(id, geometryStr);
        }
        return false;
    }

    @Override
    public boolean calcGradient() {
        List<DEMCsv> demCsvs = baseMapper.selectList(null);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (int i = 0; i < demCsvs.size(); i++) {
            DEMCsv demCsv = demCsvs.get(i);
            Geometry geometry = GeoUtil.getGeometry(demCsv.getGeometryStr());
            DEMCsv shortestDemCsv = null;
            double diffHeight = -1;

            for (int j = 0; j < demCsvs.size(); j++) {
                if (i == j) continue;
                DEMCsv csv = demCsvs.get(j);
                Geometry polygon = GeoUtil.getGeometry(csv.getGeometryStr());
                //如果两个多边形相交 计算距离，计算与距离最近的坡度
                if (geometry.touches(polygon)) {
//                    Point centroid = geometry.getCentroid();
//                    Point centroid1 = polygon.getCentroid();
//                    double distance = PointUtil.calcDistanceMeter(centroid.getY(), centroid.getX(), centroid1.getY(), centroid1.getX());
//                    double distance = centroid.distance(centroid1);
                    double height = Math.abs(demCsv.getValue() - csv.getValue());

                    if (diffHeight < height) {
                        diffHeight = height;
                        shortestDemCsv = csv;
                    }
                }
            }
            //计算坡度
//            double diffHeight = Math.abs(demCsv.getValue() - shortestDemCsv.getValue());
            Point centroid = geometry.getCentroid();
            Point centroid1 = GeoUtil.getGeometry(shortestDemCsv.getGeometryStr()).getCentroid();
            double distance = PointUtil.calcDistanceMeter(centroid.getY(), centroid.getX(), centroid1.getY(), centroid1.getX());
            double tan = Math.atan(diffHeight / distance);
            double gradient = Math.toDegrees(tan);
            String format = decimalFormat.format(gradient);
            demCsv.setGradient(gradient);

            baseMapper.update(new UpdateWrapper<DEMCsv>().eq("ID", demCsv.getId()).set("GRADIENT", Double.valueOf(format)));

        }
        return false;
    }
}
