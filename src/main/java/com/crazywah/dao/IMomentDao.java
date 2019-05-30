package com.crazywah.dao;

import com.crazywah.entity.Moment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface IMomentDao {

    /**
     * 获取自己以及所有朋友的朋友圈
     *
     * @param token
     * @return
     * @throws SQLException
     */
    @Select("select " +
            "  b.nickname, " +
            "  b.avatar, " +
            "  a.momentId, " +
            "  a.accountId, " +
            "  a.postTime, " +
            "  a.postContent, " +
            "  a.visiableRange, " +
            "  a.blackList, " +
            "  a.whiteList, " +
            "  a.photoList, " +
            "  (select count(fromId)" +
            "   from t_like" +
            "   where fromId = (select account_id" +
            "                   from user" +
            "                   where token = #{token}) and objId = a.momentId) as isLiked,"+
            "  (select count(likeId) " +
            "   from t_like " +
            "   where objId = a.momentId AND objType = 1) as likeCount, " +
            "  (select count(commentId) " +
            "   from t_comment " +
            "   where momentId = a.momentId)              as commentCount " +
            "from moment a left join user b on a.accountId = b.account_id " +
            "where a.accountId in (select origin_id " +
            "                      from friendship " +
            "                      where target_id in (select account_id " +
            "                                         from user " +
            "                                         where token = #{token}) AND relation != 0 AND " +
            "                                               relation != 4) OR a.accountId = (select account_id " +
            "                                                                                from user " +
            "                                                                                where token = " +
            "                                                                                      #{token} ) order by a.postTime desc limit #{limit} offset #{offset}")
    List<Moment> getAllMomentByToken(@Param("token") String token, @Param("limit") int limit, @Param("offset") int offset) throws SQLException;

    @Select("select a.momentId,\n" +
            "  a.accountId,\n" +
            "  a.postTime,\n" +
            "  a.postContent,\n" +
            "  a.visiableRange,\n" +
            "  a.blackList,\n" +
            "  a.whiteList,\n" +
            "  a.photoList,\n" +
            "  (select count(likeId)\n" +
            "   from t_like\n" +
            "   where objId = a.momentId AND objType = 1) as likeCount,\n" +
            "  (select count(commentId)\n" +
            "   from t_comment\n" +
            "   where momentId = a.momentId)              as commentCount\n" +
            "from moment a where a.accountId = (select account_id from user where token = #{token})")
    List<Moment> getMomentByToken(@Param("token") String token) throws SQLException;

    @Select("select a.momentId,\n" +
            "  a.accountId,\n" +
            "  a.postTime,\n" +
            "  a.postContent,\n" +
            "  a.visiableRange,\n" +
            "  a.blackList,\n" +
            "  a.whiteList,\n" +
            "  a.photoList,\n" +
            "  (select count(likeId)\n" +
            "   from t_like\n" +
            "   where objId = a.momentId AND objType = 1) as likeCount,\n" +
            "  (select count(commentId)\n" +
            "   from t_comment\n" +
            "   where momentId = a.momentId)              as commentCount\n" +
            "from moment a where a.accountId = (select account_id from user where token = #{token})")
    List<Moment> getMomentByAccountId(@Param("account_id") String accountId) throws SQLException;

    @Insert("insert into moment(accountId, postTime, postContent, visiableRange, blackList, whiteList, photoList) values(#{accountId}, #{postTime}, #{postContent}, #{visiableRange}, #{blackList}, #{whiteList}, #{photoList})")
    void postMoment(Moment moment) throws SQLException;

    @Delete("delete from moment where momentId = #{momentId} and accountId = #{account_id}")
    void deleteMoment(@Param("momentId") int id, @Param("account_id") String accountId) throws SQLException;

    @Select("select " +
            "  b.nickname, " +
            "  b.avatar, " +
            "  a.momentId, " +
            "  a.accountId, " +
            "  a.postTime, " +
            "  a.postContent, " +
            "  a.visiableRange, " +
            "  a.blackList, " +
            "  a.whiteList, " +
            "  a.photoList, " +
            "  (select count(fromId)" +
            "   from t_like" +
            "   where fromId = (select account_id" +
            "                   from user" +
            "                   where token = #{token}) and objId = a.momentId) as isLiked,"+
            "  (select count(likeId) " +
            "   from t_like " +
            "   where objId = a.momentId AND objType = 1) as likeCount, " +
            "  (select count(commentId) " +
            "   from t_comment " +
            "   where momentId = a.momentId)              as commentCount " +
            "from moment a left join user b on a.accountId = b.account_id " +
            "where momentId = #{momentId}")
    Moment getMomentById(@Param("token")String token,@Param("momentId") int momentId) throws SQLException;

}
