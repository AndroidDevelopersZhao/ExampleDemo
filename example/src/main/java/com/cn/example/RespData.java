package com.cn.example;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/7.
 */
public class RespData implements Serializable {
    private int errorCode = -1;
    private String resultMsg = null;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
