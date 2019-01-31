package com.crazywah.entity;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /*---------以下属性来自用户表-----------*/

    /**
     * 成员ID
     */
    private long memberId;
    /**
     * 应用内ID
     */
    private String accountId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    private String password;
    /**
     * 网易云IM token
     */
    private String token;
    /**
     * 性别 0:male 1:female 2:unknown
     */
    private int gender;
    /**
     * 地址
     */
    private String address;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 生日
     */
    private Date birthday;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 个性签名
     */
    private String signature;
    /**
     * 邮箱
     */
    private String email;

    /*---------以下属性来自好友关系表-----------*/

    /**
     * 关系标识 0-陌生人；1-好友；2-星标朋友；3-特别关注；4-黑名单；
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

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}