package com.mpw.model.jdfx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpw.model.common.config.CmcResult;
import com.mpw.model.jdfx.entity.RouteLine;
import com.mpw.model.jdfx.service.IRouteLineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("routePoint")
@AllArgsConstructor
@Api(tags = "道路拓扑数据")
public class RoutePointController {

    @Autowired
    private IRouteLineService lineService;

    /**
     * motorway 汽车高速公路
     * trunk  管道
     * secondary  二级公路
     * unclassified  无级别公路
     * residential  住宅的，与居住相关的
     * tertiary  第三的
     * service 服务
     * footway 小路、人行道
     * cycleway 自行车道
     * pedestrian 步行道
     * path 小路
     * step 步行道
     * track 轨迹
     */
    @ApiOperation("查询所有的道路类型")
    @GetMapping("/getClassType")
    public CmcResult<List<String>> getClassType() {
        List<RouteLine> list = lineService.list(new QueryWrapper<RouteLine>()
                .select("fClass").groupBy("fClass"));
        List<String> fClassList = list.stream().map(RouteLine::getfClass).collect(Collectors.toList());
        return CmcResult.success(fClassList);
    }

    @ApiOperation("按道路类型查询")
    @GetMapping("/getByClass")
    public CmcResult<List<RouteLine>> getByClass(@RequestParam(required = false) String fClass) {
        List<RouteLine> list;
        if (Objects.isNull(fClass)) {
            list = lineService.list();
        } else {
            list = lineService.list(new QueryWrapper<RouteLine>().lambda().eq(RouteLine::getfClass, fClass));
        }
        return CmcResult.success(list);
    }

    @ApiOperation("修改线")
    @PostMapping("update")
    public CmcResult<String> update(@RequestBody RouteLine line){
        if (Objects.isNull(line.getId())) {
            throw new RuntimeException("参数缺少线id");
        }
        boolean b = lineService.updateById(line);
        if (b) return CmcResult.success();
        return CmcResult.error("修改线失败");

    }

    @GetMapping("readExcel")
    @ApiOperation("读取excel")
    public String readExcel(@RequestParam String fileName) {
        List<RouteLine> list = new ArrayList<>();
        List<Map<Integer, String>> dataList = new ArrayList<>();
        // 构造监听器对象
        AnalysisEventListener<Map<Integer, String>> listener = new AnalysisEventListener<Map<Integer, String>>() {
            @Override
            public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                dataList.add(rowData); // 将每行数据添加到容器中
            }
            @Override
            public void extra(CellExtra extra, AnalysisContext context) {
                // 处理额外的单元格信息，比如合并单元格、批注等
                // ...
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                // 数据解析完成后的操作，可以在这里进行业务逻辑处理
                // ...
            }
        };
        // 读取 Excel 文件
        EasyExcel.read(fileName).registerReadListener(listener).sheet(0).doRead();
        // 遍历数据
        for (Map<Integer, String> rowData : dataList) {
            // 处理每行数据
            for (Map.Entry<Integer, String> entry : rowData.entrySet()) {
                Integer columnIndex = entry.getKey(); // 列索引
                String value = entry.getValue();// 单元格数据
                // 处理每个单元格的值
                System.out.print(value + "\t");
            }

            RouteLine point = new RouteLine();
            point.setfClass(rowData.get(3));
            point.setName(rowData.get(4));
            point.setMaxSpeed(Integer.parseInt(rowData.get(7)));
            point.setBridge(rowData.get(9));
            point.setTunnel(rowData.get(10));
            point.setsNode(Integer.parseInt(rowData.get(11)));
            point.seteNode(Integer.parseInt(rowData.get(12)));
            point.setLength(Double.parseDouble(rowData.get(13)));
            point.setGeometry(rowData.get(14));
            list.add(point);

            System.out.println(); // 换行
        }
        boolean b = lineService.saveBatch(list);
        System.out.println(b);
        return "success";
    }


}
