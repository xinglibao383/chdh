package com.mpw.model.chfx.domain.model;


import com.mpw.model.common.util.UtilsV2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 作战区域类
 * 
 * 表示一个矩形的作战区域，包含左上角和右下角的经纬度坐标，以及区域的总面积。 支持区分敌我方区域，并可计算发射点（中心点或左上角）。
 */
public class Area {
	private double topLeftLat; // 左上角纬度
	private double topLeftLon; // 左上角经度
	private double bottomRightLat; // 右下角纬度
	private double bottomRightLon; // 右下角经度
	private double totalArea; // 区域总面积（单位：平方公里）
	private boolean isEnemyArea; // 区域类型：true = 敌方区域，false = 我方区域
	private static final double EARTH_RADIUS_KM = 6371.0; // 地球半径（公里）
	private static final double EPSILON = 1e-8; // 精度容差
	 
 

	
	/** 区域边界（多边形，按顺时针或逆时针排列的坐标点） */
    private List<double[]> boundaryPolygon;
    public AreaType getAreaType() {
		return areaType;
	}

	public void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}

	public void setBoundaryPolygon(List<double[]> boundaryPolygon) {
		this.boundaryPolygon = boundaryPolygon;
	}

	/** 区域类型（BATTLEFIELD = 总体区域，FRIENDLY = 我方，ENEMY = 敌方） */
    private AreaType areaType;

    /**
     * 区域类型（用于区分总体、敌方和我方区域）
     */
    public enum AreaType {
        BATTLEFIELD,  // 总体作战区域
        FRIENDLY,     // 我方作战区域
        ENEMY         // 敌方作战区域
    }
    /**
     * 构造多边形作战区域
     *
     * @param boundaryPolygon 区域边界多边形（顶点坐标）
     * @param areaType        区域类型（BATTLEFIELD, FRIENDLY, ENEMY）
     */
    public Area(List<double[]> boundaryPolygon, AreaType areaType) {
    	this.boundaryPolygon = UtilsV2.ensurePolygonClosed(boundaryPolygon);  // 确保多边形闭合
        this.totalArea = calculateArea(boundaryPolygon);
        this.areaType = areaType;
    }
    
    /**
     * 计算多边形面积（Shoelace公式）
     */
    private double calculateArea(List<double[]> polygon) {
        double area = 0.0;
        int n = polygon.size();
        for (int i = 0; i < n; i++) {
            double[] p1 = polygon.get(i);
            double[] p2 = polygon.get((i + 1) % n);
            area += (p1[0] * p2[1]) - (p2[0] * p1[1]);
        }
        return Math.abs(area) / 2.0;
    }
    
	/**
	 * 构造作战区域（默认类型：我方区域）
	 */
	public Area(double topLeftLat, double topLeftLon, double bottomRightLat, double bottomRightLon) {
		this(topLeftLat, topLeftLon, bottomRightLat, bottomRightLon, false);
	}

	/**
	 * 构造作战区域（支持敌我标识）
	 */
	public Area(double topLeftLat, double topLeftLon, double bottomRightLat, double bottomRightLon,
			boolean isEnemyArea) {
		if (!isValidLatitude(topLeftLat) || !isValidLatitude(bottomRightLat)) {
			throw new IllegalArgumentException("纬度必须在 -90 到 90 之间");
		}
		if (!isValidLongitude(topLeftLon) || !isValidLongitude(bottomRightLon)) {
			throw new IllegalArgumentException("经度必须在 -180 到 180 之间");
		}

		// 自动调整坐标顺序，保证正确的矩形表示
		this.topLeftLat = Math.max(topLeftLat, bottomRightLat);
		this.bottomRightLat = Math.min(topLeftLat, bottomRightLat);
		this.topLeftLon = Math.min(topLeftLon, bottomRightLon);
		this.bottomRightLon = Math.max(topLeftLon, bottomRightLon);

		this.isEnemyArea = isEnemyArea;
	}

	/**
	 * 动态计算区域面积（单位：平方公里）
	 */
	public double getTotalArea() {
		// 经纬度转换为弧度
		double lat1Rad = Math.toRadians(topLeftLat);
		double lat2Rad = Math.toRadians(bottomRightLat);
		double lon1Rad = Math.toRadians(topLeftLon);
		double lon2Rad = Math.toRadians(bottomRightLon);

		// 面积公式（适用于小区域）
		double area = EARTH_RADIUS_KM * EARTH_RADIUS_KM * Math.abs(Math.sin(lat1Rad) - Math.sin(lat2Rad))
				* Math.abs(lon1Rad - lon2Rad);

		return area;
	}

	/**
	 * 获取发射点
	 *
	 * @param useCenter 是否使用中心点（true：中心点，false：左上角）
	 * @return TargetPoint 发射点（使用 Builder 创建）
	 */
	public ShootingPoint getFiringPoint(boolean useCenter) {
		if (useCenter) {
			return getCenterPoint();
		} else {
			return new ShootingPoint.Builder(topLeftLat, topLeftLon, true).reason("默认左上角发射点") // 设置原因说明（可选）
					.build();
		}
	}

	/**
	 * 计算多边形的中心点（质心）
	 */
	public ShootingPoint getCenterPoint() {
	    List<double[]> polygon = this.getBoundaryPolygon();
	    //System.out.println("[DEBUG] 多边形质心=============: (" + polygon.toString() + ")");
	    double area = 0.0;
	    double centroidLat = 0.0;
	    double centroidLon = 0.0;

	    int n = polygon.size();

	    for (int i = 0; i < n - 1; i++) {
	        double lat1 = polygon.get(i)[0];
	        double lon1 = polygon.get(i)[1];
	        double lat2 = polygon.get(i + 1)[0];
	        double lon2 = polygon.get(i + 1)[1];

	        double factor = (lat1 * lon2 - lat2 * lon1);
	        area += factor;
	        centroidLat += (lat1 + lat2) * factor;
	        centroidLon += (lon1 + lon2) * factor;
	    }

	    area *= 0.5;

	    if (Math.abs(area) < 1e-7) {
	        throw new IllegalStateException("多边形面积接近于 0，无法计算中心点");
	    }

	    centroidLat /= (6 * area);
	    centroidLon /= (6 * area);
	    //System.out.println("[DEBUG] 多边形质心: (" + centroidLat + ", " + centroidLon + ")");
	    return new ShootingPoint.Builder(centroidLat, centroidLon, true).reason("多边形质心").build();
	}


	/**
	 * 判断是否为敌方区域
	 *
	 * @return true：敌方区域，false：我方区域
	 */
	public boolean isEnemyArea() {
		return isEnemyArea;
	}

	
	/**
	 * 获取闭合的多边形边界
	 */
	public List<double[]> getBoundaryPolygon() {
	    List<double[]> polygon = new ArrayList<>(this.boundaryPolygon);

	    // 如果首尾不一致，闭合多边形
	    if (!Arrays.equals(polygon.get(0), polygon.get(polygon.size() - 1))) {
	        polygon.add(polygon.get(0));
	    }

	    return polygon;
	}



	/**
	 * 检查纬度是否有效
	 *
	 * @param latitude 纬度值
	 * @return true 表示有效，false 表示无效
	 */
	private boolean isValidLatitude(double latitude) {
		return latitude >= -90 && latitude <= 90;
	}

	/**
	 * 检查经度是否有效
	 *
	 * @param longitude 经度值
	 * @return true 表示有效，false 表示无效
	 */
	private boolean isValidLongitude(double longitude) {
		return longitude >= -180 && longitude <= 180;
	}

	/**
	 * 设置区域总面积
	 *
	 * @param totalArea 区域总面积（单位：平方公里）
	 */
	public void setTotalArea(double totalArea) {
		if (totalArea <= 0) {
			throw new IllegalArgumentException("区域总面积必须为正数");
		}
		this.totalArea = totalArea;
	}

	/**
	 * 判断指定的点是否在多边形区域内（使用射线法）
	 *
	 * @param latitude  点的纬度
	 * @param longitude 点的经度
	 * @return true：点在区域内；false：点在区域外
	 */
	public boolean isInside(double latitude, double longitude) {
	    int intersectCount = 0;
	    List<double[]> polygon = this.getBoundaryPolygon();

	    for (int i = 0; i < polygon.size() - 1; i++) {
	        double[] point1 = polygon.get(i);
	        double[] point2 = polygon.get(i + 1);

	        // 判断射线是否与边相交
	        if (isIntersecting(point1, point2, latitude, longitude)) {
	            intersectCount++;
	        }
	    }
	    // 奇数次相交 -> 点在多边形内；偶数次 -> 点在多边形外
	    return (intersectCount % 2) == 1;
	}
	/**
	 * 判断点与多边形边界是否相交（辅助方法）
	 */
	private boolean isIntersecting(double[] p1, double[] p2, double lat, double lon) {
	    double lat1 = p1[0], lon1 = p1[1];
	    double lat2 = p2[0], lon2 = p2[1];

	    if (lat1 == lat2) return false;  // 水平边界跳过

	    if (lat < Math.min(lat1, lat2) || lat >= Math.max(lat1, lat2)) return false;

	    double intersectLon = lon1 + (lat - lat1) * (lon2 - lon1) / (lat2 - lat1);
	    return intersectLon > lon;
	}
	/**
	 * 判断当前区域是否完全包含另一个区域
	 */
	public boolean contains(Area otherArea) {
		// 纬度（北-南）
		boolean latCheck = (this.topLeftLat + EPSILON >= otherArea.topLeftLat)
				&& (this.bottomRightLat - EPSILON <= otherArea.bottomRightLat);

		// 经度（西-东）
		boolean lonCheck = (this.topLeftLon - EPSILON <= otherArea.topLeftLon)
				&& (this.bottomRightLon + EPSILON >= otherArea.bottomRightLon);

		return latCheck && lonCheck;
	}

	public double getTopLeftLat() {
		return topLeftLat;
	}

	public void setTopLeftLat(double topLeftLat) {
		this.topLeftLat = topLeftLat;
	}

	public double getTopLeftLon() {
		return topLeftLon;
	}

	public void setTopLeftLon(double topLeftLon) {
		this.topLeftLon = topLeftLon;
	}

	public double getBottomRightLat() {
		return bottomRightLat;
	}

	public void setBottomRightLat(double bottomRightLat) {
		this.bottomRightLat = bottomRightLat;
	}

	public double getBottomRightLon() {
		return bottomRightLon;
	}

	public void setBottomRightLon(double bottomRightLon) {
		this.bottomRightLon = bottomRightLon;
	}

	public void setEnemyArea(boolean isEnemyArea) {
		this.isEnemyArea = isEnemyArea;
	}

}
