package com.mpw.model.jdfx.dto;

import com.mpw.model.jdfx.entity.RouteLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoadVO extends PointVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //所有线路
    private List<RouteLine> lines;

    public List<RouteLine> getLines() {
        return lines;
    }

    public void setLines(List<RouteLine> lines) {
        this.lines = lines;
    }
}
