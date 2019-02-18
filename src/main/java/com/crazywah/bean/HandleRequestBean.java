package com.crazywah.bean;

public class HandleRequestBean {

    /**
     * 0:拒绝
     * 1:接收
     */
    private int result;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
