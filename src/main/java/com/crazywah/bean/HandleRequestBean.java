package com.crazywah.bean;

public class HandleRequestBean {

    public static final int REFUSE = 0;
    public static final int ACCEPT = 1;

    /**
     * 0:拒绝
     * 1:接收
     */
    private int result;
    private String to;
    private String attach;

    public String getTo() {
        return to;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
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
