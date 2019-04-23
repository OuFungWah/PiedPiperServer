package com.crazywah.entity;

import java.util.Date;

public class FriendShip {

    public static final int STRANGER = 0;
    public static final int FRIEND = 1;
    public static final int STARED_FRIEND = 2;
    public static final int SP_FOLLOW = 3;
    public static final int BLACK_LIST = 4;
    public static final int REQUESTING = 5;

    /**
     * 主动者 ID
     */
    private String originId;
    /**
     * 目标者 ID
     */
    private String targetId;
    /**
     * 关系标识 0-陌生人；1-好友；2-星标朋友；3-特别关注；4-黑名单；5-请求中；
     */
    private int relation;
    /**
     * 备注名
     */
    private String alias;
    /**
     * 附注信息
     */
    private String remark;
    /**
     * 请求好友时间
     */
    private Date requestTime;
    /**
     * 添加为好友的时间
     */
    private Date friendTime;
    /**
     * 请求信息
     */
    private String requestMessage;

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getFriendTime() {
        return friendTime;
    }

    public void setFriendTime(Date friendTime) {
        this.friendTime = friendTime;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}
