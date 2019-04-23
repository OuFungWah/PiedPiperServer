package com.crazywah.dao;

import com.crazywah.entity.FriendRequest;
import com.crazywah.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Mapper
@Repository
public interface IFriendRequestDao {

    @Insert("insert into friendRequest (fromId ,toId ,requestMessage ,requestTime, requestStatus) values(#{fromId} ,#{toId} ,#{requestMessage} ,#{requestTime}, #{requestStatus})")
    void addRequest(FriendRequest request) throws SQLException;

    @Update("update friendRequest set requestStatus = #{requestStatus}, requestTime = #{requestTime}, requestMessage = #{requestMessage} where fromId = #{fromId} and toId = #{toId}")
    void updateRequest(FriendRequest request) throws SQLException;

    @Update("update friendRequest set requestStatus = #{requestStatus} where fromId = #{fromId} and toId = #{toId}")
    void updateRequestStatus(FriendRequest request) throws SQLException;

    @Select("select * from friendRequest where fromId = #{fromId} and toId = #{toId}")
    FriendRequest getFriendRequestByUserId(@Param("fromId") String from,@Param("toId") String to) throws SQLException;

    @Select("select a.account_id, a.nickname, a.avatar, a.gender, a.signature, b.requestStatus, b.requestTime, b.requestMessage from user a left join friendRequest b on a.account_id = b.fromId where b.toId = (select account_id from user where token = #{token})")
    @Results({
            @Result(property = "accountId", column = "account_id")
    })
    List<User> getRequestList(@Param("token") String token) throws SQLException;

}
