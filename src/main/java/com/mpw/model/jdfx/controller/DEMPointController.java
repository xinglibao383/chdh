package com.mpw.model.jdfx.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.mpw.model.jdfx.entity.DEMPoint;
import com.mpw.model.jdfx.service.IDEMPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("demPoint")
@AllArgsConstructor
@Api(tags = "DEM 拓扑excel表读取")
public class DEMPointController {

    @Autowired
    private IDEMPointService pointService;

    @GetMapping("readExcel")
    @ApiOperation("读取excel")
    public String readExcel(@RequestParam String fileName) {
        List<DEMPoint> list = new ArrayList<>();

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

            DEMPoint point = new DEMPoint();
            point.setsNode(Integer.parseInt(rowData.get(1)));
            point.seteNode(Integer.parseInt(rowData.get(2)));
            point.setDegree(Double.parseDouble(rowData.get(3)));
            point.setIsWater(Integer.parseInt(rowData.get(4)));
            point.setType(rowData.get(5));
            point.setSx(Double.parseDouble(rowData.get(6)));
            point.setSy(Double.parseDouble(rowData.get(7)));
            point.setEx(Double.parseDouble(rowData.get(8)));
            point.setEy(Double.parseDouble(rowData.get(9)));
            point.setLength(Double.parseDouble(rowData.get(10)));
            list.add(point);

            System.out.println(); // 换行
        }
        boolean b = pointService.saveBatch(list);
        System.out.println(b);
        return "success";
    }
}
