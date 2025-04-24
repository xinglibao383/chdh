package com.mpw.model.jdfx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("ROUTE.JD_RAILROAD")
public class RailRoad {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("NAME")
    private String name;

    @TableField("LENGTH")
    private Double length;

    @TableField("GEOMETRY")
    private String geometry;


}
