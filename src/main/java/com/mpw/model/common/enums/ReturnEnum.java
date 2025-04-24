package com.mpw.model.common.enums;

/**
 * 所以业务异常的枚举
 */
public enum ReturnEnum {

    /**
     * 返回给前端的成功状态
     */
    SERVICE_SUCESS(200,"SUCCESS"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(-1 ,"Internal Server Error {0}"),

    REQ_PARA_IS_NULL(6001,"{0} 不允许为空，请检查"),

    PARA_CHECK_ERROR(6002,"{0}"),

    SERVICE_CHECK_ERROR(6003,"{0}"),

    SERVICE_RIN_ERROR(6004,"操作失败原因:{0}"),

    FEIGN_RUN_ERROR(6005,"feign调用失败"),

    FLOWLIB_RUN_ERROR(7001,""),

    FLOWLIB_RETURN_ERROR(7002, "声场计算返回错误:{0}");


    ReturnEnum(int code, String message) {
        this.friendlyCode = code;
        this.friendlyMsg = message;
    }

    private int friendlyCode;

    private String friendlyMsg;

    public int getCode() {
        return friendlyCode;
    }

    public void setCode(int friendlyCode) {
        this.friendlyCode = friendlyCode;
    }

    public String getMessage() {
        return friendlyMsg;
    }

    public void setFriendlyMsg(String friendlyMsg) {
        this.friendlyMsg = friendlyMsg;
    }
}
