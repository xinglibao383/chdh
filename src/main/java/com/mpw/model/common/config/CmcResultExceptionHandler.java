package com.mpw.model.common.config;

import dm.jdbc.driver.DMException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@Slf4j
public class CmcResultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CmcResult<String> exceptionHandler(Exception e, HttpServletResponse response){
        if (e instanceof DMException || e instanceof DataIntegrityViolationException) {
            e.printStackTrace();
            return CmcResult.error("请重新绘制作战区域，计算目前只支持多边形（凸）。");
        }
        e.printStackTrace();
        return CmcResult.error(e.getMessage());
    }
}
