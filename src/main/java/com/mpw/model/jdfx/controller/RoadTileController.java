package com.mpw.model.jdfx.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
@RequestMapping("/tile")
@AllArgsConstructor
public class RoadTileController {

    @GetMapping("/road")
    public void drawRoadTile(@RequestParam Integer width,
                             @RequestParam Integer height,
//                             @RequestParam String typeList,//道路类型列表
                             @RequestParam String BBOX,
                             HttpServletResponse response
                             ) {

        double[] bounds = Arrays.stream(BBOX.split(",")).mapToDouble(Double::valueOf).toArray();
        //TODO
        //通过空间函数，查询出bounds范围内的道路空间数据
        double y = bounds[0];
        double x = bounds[1];

        //将Bounds和 道路 转换为 墨卡托投影坐标
        //计算x和y方向上的分辨率
        //遍历道路，将道路坐标转化为平面坐标
        //将futures渲染成256*256的图片
    }

}
