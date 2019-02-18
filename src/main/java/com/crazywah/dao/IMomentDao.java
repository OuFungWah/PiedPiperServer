package com.crazywah.dao;

import com.crazywah.entity.Moment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface IMomentDao {

    @Select("select * from moment where account_id in (select target_id from friendship where origin_id = (select account_id from user where token = #{token})) and relation != 4 order by post_time ")
    @Results({
            @Result(property = "momentId", column = "moment_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "postTime", column = "post_time"),
            @Result(property = "postContent", column = "post_content"),
            @Result(property = "visiableRange", column = "visiable_range"),
            @Result(property = "blackList", column = "black_list"),
            @Result(property = "whiteList", column = "white_list"),
            @Result(property = "photoList", column = "photo_list")
    })
    List<Moment> getAllMomentByToken(@Param("token") String token) throws SQLException;

    @Select("select * from moment where account = (select account_id from user where token = #{token})")
    @Results({
            @Result(property = "momentId", column = "moment_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "postTime", column = "post_time"),
            @Result(property = "postContent", column = "post_content"),
            @Result(property = "visiableRange", column = "visiable_range"),
            @Result(property = "blackList", column = "black_list"),
            @Result(property = "whiteList", column = "white_list"),
            @Result(property = "photoList", column = "photo_list")
    })
    List<Moment> getMomentByToken(@Param("token") String token) throws SQLException ;

    @Select("select * from moment where account = #{account_id}")
    @Results({
            @Result(property = "momentId", column = "moment_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "postTime", column = "post_time"),
            @Result(property = "postContent", column = "post_content"),
            @Result(property = "visiableRange", column = "visiable_range"),
            @Result(property = "blackList", column = "black_list"),
            @Result(property = "whiteList", column = "white_list"),
            @Result(property = "photoList", column = "photo_list")
    })
    List<Moment> getMomentByAccountId(@Param("account_id") String accountId) throws SQLException ;

    @Insert("insert into moment(account_id, post_time, post_content, visiable_range, black_list, white_list, photo_list) values(#{accountId}, #{postTime}, #{postContent}, #{visiableRange}, #{blackList}, #{whiteList}, #{photoList})")
    void postMoment(Moment moment) throws SQLException ;

    @Delete("delete * form moment where moment_id = #{moment_id} and account_id = #{account_id}")
    void deleteMoment(@Param("moment_id") int id,@Param("account_id") String accounId) throws SQLException ;

}
