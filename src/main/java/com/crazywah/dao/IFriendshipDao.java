package com.crazywah.dao;

import com.crazywah.entity.FriendShip;
import com.crazywah.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Repository
@Mapper
public interface IFriendshipDao {

    @Insert("insert into friendship (origin_id, target_id, request_time, relation, friend_time) values (#{originId}, #{targetId}, #{requestTime}, #{relation}, #{friendTime})")
    void addFriendShipRequest(FriendShip friendShip) throws SQLException;

    @Update("update friendship set remark = #{remark} where origin_id = #{originId} and target_id = #{targetId}")
    void updateRemark(FriendShip friendShip) throws SQLException;

    @Update("update friendship set alias = #{alias} where origin_id = #{originId} and target_id = #{targetId}")
    void updateAlias(FriendShip friendShip) throws SQLException;

    @Update("update friendship set relation = #{relation}, friend_time = #{friendTime} where origin_id = #{originId} and target_id = #{targetId}")
    void updateRelation(FriendShip friendShip) throws SQLException;

    @Update("update friendship set relation = #{relation}, request_time = #{requestTime}, request_message = #{requestMessage} where origin_id = #{originId} and target_id = #{targetId}")
    void updateRequest(FriendShip friendShip) throws SQLException;

    @Select("select * from friendship where origin_id = #{fromId} and target_id = #{toId}")
    FriendShip getFriendShipById(@Param("fromId") String fromId, @Param("toId") String toId) throws SQLException;

    @Select("select a.account_id, a.nickname, a.avatar, a.gender, a.signature, b.alias, b.relation, b.remark, b.request_time,b.request_message from user a left join friendship b on a.account_id = b.target_id where b.request_message is not null and b.origin_id = (select account_id from user where token = #{token})")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "requestTime", column = "request_time"),
            @Result(property = "requestMessage", column = "request_message")
    })
    List<User> getRequestingUser(@Param("token") String token) throws SQLException;

    @Select("select * from friendship where origin_id = (select account_id from user where token = #{token}) AND target_id = #{targetId}")
    @Results({
            @Result(property = "targetId", column = "target_id"),
            @Result(property = "friendTime", column = "friend_time"),
            @Result(property = "requestTime", column = "request_time"),
            @Result(property = "requestMessage", column = "request_message")
    })
    FriendShip getFriendShip(@Param("token") String token, @Param("targetId") String targetId) throws SQLException;

}
