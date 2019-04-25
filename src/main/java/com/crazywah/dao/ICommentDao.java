package com.crazywah.dao;

import com.crazywah.entity.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface ICommentDao {

    @Insert("insert into t_comment (\n" +
            "  momentId, fromId, toId, content, commentTime\n" +
            ") VALUES (#{momentId}, #{fromId}, #{toId}, #{content}, #{commentTime})")
    void insertComment(Comment comment) throws SQLException;

    @Select("select b.nickname as fromName, b.avatar, a.* from t_comment a left join user b on a.fromId = b.account_id where momentId = #{momentId}")
    List<Comment> getAllCommentByMomentId(@Param("momentId") int momentId) throws SQLException;

    @Select("select * from t_comment where momentId = #{momentId}")
    List<Comment> getCommentByMomentId(@Param("momentId") int momentId, @Param("token") String token) throws SQLException;

    @Delete("delete * form t_comment where commentId = #{id}")
    void deleteComment(@Param("id") int id) throws SQLException;

}
