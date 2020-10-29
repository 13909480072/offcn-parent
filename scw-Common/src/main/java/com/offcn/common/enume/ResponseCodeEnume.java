package com.offcn.common.enume;
//设定键值关系，根据键获取值
public enum ResponseCodeEnume {
    SUCCESS(0,"操作成功"),
    FAIL(1,"服务器异常"),
    NOT_FOUND(404,"资源为找到"),
    NOT_AUIHED(403,"无权限，访问拒绝"),
    PARAM_INVAILD(400,"提交参数非法");

    private Integer code;
    private String msg;

    ResponseCodeEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
