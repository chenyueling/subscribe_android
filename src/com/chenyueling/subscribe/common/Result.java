package com.chenyueling.subscribe.common;


import com.chenyueling.subscribe.utils.JsonUtil;

/**
 * Created by chenyueling on 2015/4/29.
 */
public class Result {
    private String data;
    private String status;
    private String code;

    public static final String SUCCESS_STATUS = "success";
    public static final String ERROR_STATUS = "error";
    public Result() {
    }

    public static Result Success(){
        Result result = new Result();
        result.setData("operate success!");
        result.setStatus(SUCCESS_STATUS);
        return result;
    }

    public Result( String status,String data) {
        this.data = data;
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }




    public static void main(String[] args) {
        Result r = Result.Success();
        System.out.println(JsonUtil.toJson(r));
    }

    public static Result SystemError() {
        Result r = new Result(Result.ERROR_STATUS,"system error,please contact system admin!");
        return r;
    }
}
