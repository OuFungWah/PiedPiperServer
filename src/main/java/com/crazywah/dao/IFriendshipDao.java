package com.crazywah.dao;

import com.crazywah.entity.FriendShip;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Date;


@Repository
@Mapper
public interface IFriendshipDao {

    @Insert("insert into friendship (origin_id, target_id, request_time, friend_time) values (#{origin_id}, #{target_id}, #{request_time}, #{start_time})")
    void addFriendShipRequest(@Param("origin_id") String originId, @Param("target_id") String targetId, @Param("request_time") Date requestTime, @Param("start_time") Date startTime) throws SQLException;

    @Update("update friendship set relation = #{relation}, alias = #{alias}, remark = #{remark}, friend_time = #{friend_Time}, request_time = #{requestTime}, request_message = #{requestMessage} where origin_id = #{originId} and target_id = #{targetId}\n")
    void updateRelation(FriendShip friendShip) throws SQLException;

    @Select("select * from friendship where origin_id = (select account_id from user where token = #{token}) AND target_id = #{target_id}")
    FriendShip getFriendShip(@Param("token")String token,@Param("target_id")String targetId) throws SQLException;

}
