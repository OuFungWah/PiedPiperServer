package com.crazywah.entity;

import java.util.Date;

/**
 * create table discussion(
 *   comment_id int auto_increment unique,
 *   moment_id int,
 *   commenter_id varchar(64),
 *   target_id varchar(64),
 *   comment_content text,
 *   comment_time datetime,
 *   primary key (commenter_id, comment_time)
 * )
 */
public class Comment {

    private int commentId;
    private int momentId;
    private String commenterId;
    private String targetId;
    private String commentContent;
    private Date commentTime;

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

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}
