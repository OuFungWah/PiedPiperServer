package com.crazywah.dao;

import com.crazywah.entity.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface ICommentDao {

    @Insert("insert into discussion(moment_id, commenter_id, target_id, comment_content, comment_time) values(#{commentId}, #{momentId}, #{commenterId}, #{targetId}, #{commentContent}, #{commentTime})")
    void insertComment(Comment comment) throws SQLException;

    @Select("select * from discussion where moment_id = #{momentId} and commenter_id in (select account_id from friendship where target_id = (select account_id from user where token = #{token})) and target_id in (select account_id from friendship where target_id = (select account_id from user where token = #{token})) order by comment_time")
    @Results({
            @Result(property = "momentId",column = "moment_id"),
            @Result(property = "commenterId",column = "commenter_id"),
            @Result(property = "targetId",column = "target_id"),
            @Result(property = "commentContent",column = "comment_content"),
            @Result(property = "commentTime",column = "comment_time")
    })
    List<Comment> getCommentByMomentId(@Param("momentId") int momentId, @Param("token") String token) throws SQLException ;

    @Delete("delete * form discussion where comment_id = #{id}")
    void deleteComment(@Param("id") int id) throws SQLException ;

}
