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
     * @param user
     */
    @Insert("insert into user(account_id,nickname,password,register_time,token) values(#{accountId},#{nickname},#{password},#{registerTime},#{token})")
    void addUser(User user) throws SQLException;

    @Select("select * from user")
    @Results({
            @Result(property = "memberId",column = "member_id"),
            @Result(property = "accountId",column = "account_id"),
            @Result(property = "registerTime",column = "register_time")
    })
    List<User> selectAllUser() throws SQLException;

    @Select("select account_id from user where token = #{token}")
    @Results({
            @Result(property = "accountId", column = "account_id")
    })
    User getUserByToken(@Param("token")String token) throws SQLException;

    @Select("select a.account_id, a.nickname, a.gender, a.signature, a.address, a.email, a.mobile, a.birthday, b.alias, b.remark, b.friend_time, b.relation from user a left join friendship b on a.account_id = b.target_id where a.account_id in (select target_id from friendship where origin_id = (select account_id from user where token = #{token})) and b.relation = #{relation}")
    @Results({
            @Result(property = "accountId",column = "account_id"),
            @Result(property = "friendTime",column = "friend_time")
    })
    List<User> getUserByTokenRelation(@Param("token")String token, @Param("relation")int relation) throws SQLException;

    @Select("select token from user where account_id = #{accountId} and password = #{password}")
    TokenBean getToken(@Param("accountId")String accountId, @Param("password")String password) throws SQLException;

    @Select("select account_id,nickname,gender,signature from user where account_id = #{accountId}")
    User getUserBaseInfoByAccount(String accountId) throws SQLException;

    @Select("select a.account_id, a.nickname, a.gender, a.signature, a.address, a.email, a.mobile, a.birthday, b.alias, b.relation, b.remark, b.request_time,b.request_message from user a left join friendship b on a.account_id = b.target_id where a.account_id = #{account_id} and b.origin_id = (select account_id from user where token = #{token})")
    User getUserFullInfo(@Param("token") String token, @Param("account_id") String accountId) throws SQLException;

    @Select("select account_id,nickname,token from user where account_id = #{accountId} AND password = #{password}")
    User getUserByAccountPassword(User user) throws SQLException;

    @Update("update user set password = #{password} where token = #{token}")
    void updatePasswordByToken(@Param("token")String token, @Param("password")String password) throws SQLException;

    @Update("update user set account_id = #{accountId}, nickname = #{nickname}, password = #{password} where token = #{token}")
    void updateUserBaseInfo(User user) throws SQLException;

    @Update("update user set gender = #{gender}, signature = #{signature}, address = #{address}, email = #{email}, mobile = #{mobile}, birthday = #{birthday} where token = #{token}")
    void updateUserAdditionInfo(User user) throws SQLException;

    @Update("update user set signature = #{signature} where token = #{token}")
    void updateSignature(User user) throws SQLException;

    @Update("update user set token = #{token} where account_id = #{accountId}")
    void updateUserToken(User user) throws SQLException;

    @Delete("delete from user where account_id = #{accountId}")
    void deleteUser(User user) throws SQLException;

}
