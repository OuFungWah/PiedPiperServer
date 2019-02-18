package com.crazywah.entity;

import java.util.Date;

/**
 * create table moment(
 *   moment_id int auto_increment unique ,
 *   account_id varchar(64),
 *   post_time datetime,
 *   post_content text,
 *   visiable_range int, #0:All, 1:whitelist, 2:blacklist, 3:private
 *   black_list text,
 *   white_list text,
 *   primary key(account_id, post_time)
 * )
 */
public class Moment {

    private int momentId;
    private String accountId;
    private Date postTime;
    private String postContent;
    private int visiableRange;
    private String blackList;
    private String whiteList;
    private String photoList;

    public int getMomentId() {
        return momentId;
    }

    public void setMomentId(int momentId) {
        this.momentId = momentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public int getVisiableRange() {
        return visiableRange;
    }

    public void setVisiableRange(int visiableRange) {
        this.visiableRange = visiableRange;
    }

    public String getBlackList() {
        return blackList;
    }

    public void setBlackList(String blackList) {
        this.blackList = blackList;
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    public String getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String photoList) {
        this.photoList = photoList;
    }
}
