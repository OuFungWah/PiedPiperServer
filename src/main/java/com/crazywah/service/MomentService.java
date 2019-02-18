package com.crazywah.service;

import com.crazywah.dao.ICommentDao;
import com.crazywah.dao.IMomentDao;
import com.crazywah.entity.Comment;
import com.crazywah.entity.Moment;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class MomentService {

    private IMomentDao momentDao;
    private ICommentDao commentDao;

    public MomentService(IMomentDao momentDao, ICommentDao commentDao) {
        this.momentDao = momentDao;
        this.commentDao = commentDao;
    }

    public void addMoment(Moment moment) throws SQLException {
        momentDao.postMoment(moment);
    }

    public void addComment(Comment comment) throws SQLException {
        commentDao.insertComment(comment);
    }

    public List<Moment> getMomentsByToken(String token) throws SQLException {
        return momentDao.getMomentByToken(token);
    }

    public List<Comment> getCommentByMomentId(int momentId, String token) throws SQLException{
        return commentDao.getCommentByMomentId(momentId,token);
    }

    public void deleteMoment(int id, String accountId) throws SQLException {
        momentDao.deleteMoment(id,accountId);
    }

    public void deleteComment(int id) throws SQLException {
        commentDao.deleteComment(id);
    }

}
