package com.crazywah.entity;

import java.util.Date;

/**
 * create table t_comment (
 * commentId   int auto_increment unique,
 * momentId    int,
 * fromId      varchar(64),
 * toId        varchar(64),
 * content     text,
 * commentTime datetime,
 * primary key (fromId, commentTime)
 * );
 */
public class Comment {

    private String avatar;
    private String fromName;
    private int commentId;
    private int momentId;
    private String fromId;
    private String toId;
    private String content;
    private Date commentTime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getMomentId() {
        return momentId;
    }

    public void setMomentId(int momentId) {
        this.momentId = momentId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}
