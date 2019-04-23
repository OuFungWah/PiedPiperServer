package com.crazywah.dao;

import com.crazywah.bean.TokenBean;
import com.crazywah.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
@Mapper
public interface IUserDao {

    /**
     * 注册用户需要 accountId、nickname、password、registerTime、token
     *
     * @param user
     */
    @Insert("insert into user(account_id,nickname,password,register_time,token) values(#{accountId},#{nickname},#{password},#{registerTime},#{token})")
    void addUser(User user) throws SQLException;

    @Select("select * from user")
    @Results({
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "registerTime", column = "register_time")
    })
    List<User> selectAllUser() throws SQLException;

    @Select("select account_id, nickname, avatar, gender from user where account_id like #{id}")
    @Results({
            @Result(property = "accountId", column = "account_id")
    })
    List<User> getStrangers(@Param("id") String id) throws SQLException;

    @Select("select * from user where token = #{token}")
    @Results({
            @Result(property = "accountId", column = "account_id")
    })
    User getUserByToken(@Param("token") String token) throws SQLException;

    @Select("select a.account_id, a.nickname, a.avatar, a.gender, a.signature, a.address, a.email, a.mobile, a.birthday, b.alias, b.remark, b.friend_time, b.relation from user a left join friendship b on a.account_id = b.target_id where a.account_id in (select target_id from friendship where origin_id = (select account_id from user where token = #{token})) and b.relation = #{relation}")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "friendTime", column = "friend_time")
    })
    List<User> getUserByTokenRelation(@Param("token") String token, @Param("relation") int relation) throws SQLException;

    @Select("select a.account_id, a.nickname, a.gender, a.signature, a.address, a.email, a.mobile, a.birthday, a. avatar, b.relation, b.alias, b.remark, c.requestTime, c.requestMessage from (user a left join friendship b on a.account_id = b.origin_id) left join friendRequest c on b.origin_id = c.fromId AND b.target_id = c.toId where b.target_id = (select account_id from user where token = #{token}) AND b.relation = 1")
    @Results({
            @Result(property = "accountId", column = "account_id"),
    })
    List<User> getFriendsAllInfo(@Param("token") String token) throws SQLException;

    @Select("select * from user where account_id = #{accountId} and password = #{password}")
    @Results({
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "registerTime", column = "register_time")
    })
    User getUser(@Param("accountId") String accountId, @Param("password") String password) throws SQLException;

    @Select("select account_id, avatar, nickname, gender, signature from user where account_id = #{accountId}")
    @Results({
            @Result(property = "accountId", column = "account_id")
    })
    User getUserBaseInfoByAccount(String accountId) throws SQLException;

    @Select("select a.account_id, a.nickname, a.avatar, a.gender, a.signature, a.address, a.email, a.mobile, a.birthday, b.alias, b.relation, b.remark from user a left join friendship b on a.account_id = b.target_id where a.account_id = #{account_id} and b.origin_id = (select account_id from user where token = #{token})")
    @Results({
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "requestTime", column = "request_time"),
            @Result(property = "requestMessage", column = "request_message")
    })
    User getUserFullInfo(@Param("token") String token, @Param("account_id") String accountId) throws SQLException;

    @Select("select account_id, avatar, nickname, token from user where account_id = #{accountId} AND password = #{password}")
    @Results({
            @Result(property = "accountId", column = "account_id")
    })
    User getUserByAccountPassword(User user) throws SQLException;

    @Update("update user set password = #{password} where token = #{token}")
    void updatePasswordByToken(@Param("token") String token, @Param("password") String password) throws SQLException;

    @Update("update user set account_id = #{accountId}, nickname = #{nickname}, password = #{password} where token = #{token}")
    void updateUserBaseInfo(User user) throws SQLException;

    @Update("update user set nickname = #{nickname} where token = #{token}")
    void updateUserNickName(User user) throws SQLException;

    @Update("update user set password = #{password} where token = #{token}")
    void updateUserPassword(User user) throws SQLException;

    @Update("update user set gender = #{gender} where token = #{token}")
    void updateUserGender(User user) throws SQLException;

    @Update("update user set birthday = #{birthday} where token = #{token}")
    void updateUserBirthday(User user) throws SQLException;

    @Update("update user set mobile = #{mobile} where token = #{token}")
    void updateUserMobile(User user) throws SQLException;

    @Update("update user set address = #{address} where token = #{token}")
    void updateUserAddress(User user) throws SQLException;

    @Update("update user set email = #{email} where token = #{token}")
    void updateUserEmail(User user) throws SQLException;

    @Update("update user set gender = #{gender}, signature = #{signature}, address = #{address}, email = #{email}, mobile = #{mobile}, birthday = #{birthday} where token = #{token}")
    void updateUserAdditionInfo(User user) throws SQLException;

    @Update("update user set signature = #{signature} where token = #{token}")
    void updateSignature(User user) throws SQLException;

    @Update("update user set token = #{token} where account_id = #{accountId}")
    void updateUserToken(User user) throws SQLException;

    @Delete("delete from user where account_id = #{accountId}")
    void deleteUser(User user) throws SQLException;

    @Update("update user set avatar = #{avatar} where account_id = #{accountId}")
    void updateAvatar(User user) throws SQLException;

}
