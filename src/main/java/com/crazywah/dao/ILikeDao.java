package com.crazywah.dao;

import com.crazywah.entity.Like;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface ILikeDao {

    @Select("select * from t_liken where fromId = #{fromId} AND objId = #{objId} AND objType = #{objType}")
    Like getLikeByIdAndType(Like like) throws SQLException;

    @Select("select b.avatar, a.* from t_like a left join user b on a.fromId = b.account_id where objId = #{momentId} order by a.likeTime")
    List<Like> getLikesByMomentId(@Param("momentId") int momentId) throws SQLException;

    @Insert("insert into t_like (fromId, objId, objType, likeTime) values (#{fromId}, #{objId}, #{objType} ,#{likeTime})")
    void addLike(Like like) throws SQLException;

    @Insert("update t_like set likeTime = #{likeTime} where likeId = #{likeId}")
    void updateLike(Like like) throws SQLException;

    @Delete("delete from t_like where fromId = #{fromId} and objId = #{objId} and objType = #{objType}")
    void deleteLike(Like like) throws SQLException;

}
