package com.example.demo.model;

public class CommonResult {

    private  Integer statusCode;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    private  String errorMesg;


    private  Object result;


}
