package com.crazywah.dao;

import com.crazywah.entity.FriendShip;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;


@Repository
@Mapper
public interface IFriendshipDao {

    @Insert("insert into friendship (origin_id, target_id, request_time, relation, friend_time) values (#{originId}, #{targetId}, #{requestTime}, #{relation}, #{friendTime})")
    void addFriendShipRequest(FriendShip friendShip) throws SQLException;

    @Update("update friendship set remark = #{remark} where origin_id = #{originId} and target_id = #{targetId}")
    void updateRemark(FriendShip friendShip) throws SQLException;

    @Update("update friendship set alias = #{alias} where origin_id = #{originId} and target_id = #{targetId}")
    void updateAlias(FriendShip friendShip) throws SQLException;

    @Update("update friendship set relation = #{relation}, friend_time = #{friend_Time} where origin_id = #{originId} and target_id = #{targetId}")
    void updateRelation(FriendShip friendShip) throws SQLException;

    @Update("update friendship set relation = #{relation}, request_time = #{requestTime}, request_message = #{requestMessage} where origin_id = #{originId} and target_id = #{targetId}")
    void updateRequest(FriendShip friendShip) throws SQLException;

    @Select("select * from friendship where origin_id = (select account_id from user where token = #{token}) AND target_id = #{targetId}")
    @Results({
            @Result(property = "targetId", column = "target_id"),
            @Result(property = "friendTime", column = "friend_time"),
            @Result(property = "requestTime", column = "request_time"),
            @Result(property = "requestMessage", column = "request_message")
    })
    FriendShip getFriendShip(@Param("token")String token,@Param("targetId")String targetId) throws SQLException;

}
