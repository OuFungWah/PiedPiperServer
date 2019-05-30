package com.crazywah.service;

import com.crazywah.dao.ICommentDao;
import com.crazywah.dao.ILikeDao;
import com.crazywah.dao.IMomentDao;
import com.crazywah.entity.Comment;
import com.crazywah.entity.Like;
import com.crazywah.entity.Moment;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class MomentService {

    private IMomentDao momentDao;
    private ICommentDao commentDao;
    private ILikeDao likeDao;

    public MomentService(IMomentDao momentDao, ICommentDao commentDao, ILikeDao likeDao) {
        this.likeDao = likeDao;
        this.momentDao = momentDao;
        this.commentDao = commentDao;
    }

    public void addMoment(Moment moment) throws SQLException {
        momentDao.postMoment(moment);
    }

    public Comment addComment(Comment comment) throws SQLException {
        comment.setCommentTime(new Date(System.currentTimeMillis()));
        commentDao.insertComment(comment);
        return commentDao.getCommentByUserTime(comment);
    }

    public List<Moment> getAllMoment(String token, int limit, int offset) throws SQLException {
        return momentDao.getAllMomentByToken(token, limit, offset);
    }

    public List<Moment> getMomentsByToken(String token) throws SQLException {
        return momentDao.getMomentByToken(token);
    }

    public List<Comment> getCommentByMomentId(int momentId, String token) throws SQLException {
        return commentDao.getCommentByMomentId(momentId, token);
    }

    public void deleteMoment(int id, String accountId) throws SQLException {
        momentDao.deleteMoment(id, accountId);
    }

    public void deleteComment(int id) throws SQLException {
        commentDao.deleteComment(id);
    }

    public void likeMoment(String accountId, int momentId) throws SQLException {
        Like like = new Like();
        like.setFromId(accountId);
        like.setObjId(momentId);
        like.setObjType(Like.TYPE_MOMENT);
        like.setLikeTime(new Date(System.currentTimeMillis()));
        likeDao.addLike(like);
    }

    public void dislikeMoment(String accountId, int momentId) throws SQLException {
        Like like = new Like();
        like.setFromId(accountId);
        like.setObjId(momentId);
        like.setObjType(Like.TYPE_MOMENT);
        likeDao.deleteLike(like);
    }

    public Moment getMomentById(String token, Moment moment) throws SQLException {
        return momentDao.getMomentById(token, moment.getMomentId());
    }

    public List<Like> getLikeListByMomentId(int momentId) throws SQLException {
        return likeDao.getLikesByMomentId(momentId);
    }

    public List<Comment> getCommentListByMomentId(int momentId) throws SQLException {
        return commentDao.getAllCommentByMomentId(momentId);
    }

}
