package com.crazywah.service;

import com.crazywah.bean.TokenBean;
import com.crazywah.dao.IUserDao;
import com.crazywah.entity.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class UserService {

    private static final String TAG = "UserService";

    private IUserDao userDao;

    public UserService(IUserDao userDao){
        this.userDao = userDao;
    }

    public void addUser(User user) throws SQLException {
        userDao.addUser(user);
    }

    public User getUserByToken(String token) throws SQLException{
        return userDao.getUserByToken(token);
    }

    public List<User> getUsers() throws SQLException{
        return userDao.selectAllUser();
    }

    public List<User> getUsers(String token,int relation) throws SQLException{
        return userDao.getUserByTokenRelation(token,relation);
    }

    public TokenBean getToken(String accountId, String password) throws SQLException{
        return userDao.getToken(accountId, password);
    }

    public void updateToken(User user) throws SQLException{
        userDao.updateUserToken(user);
    }

    public User getUserByAccountPassword(User user) throws SQLException{
        return userDao.getUserByAccountPassword(user);
    }

    public User getUserBaseInfo(String accountId) throws SQLException{
        return userDao.getUserBaseInfoByAccount(accountId);
    }

    public User getUserFullInfo(String token, String targetId) throws SQLException{
        return userDao.getUserFullInfo(token,targetId);
    }

    public void updatePasswordByToken(String token,String password) throws SQLException{
        userDao.updatePasswordByToken(token,password);
    }

    public void updateBaseInfo(User user) throws SQLException{
        userDao.updateUserBaseInfo(user);
    }

    public void updateAdditionInfo(User user) throws SQLException{
        userDao.updateUserAdditionInfo(user);
    }

    public void updateSignature(User user) throws SQLException{
        userDao.updateSignature(user);
    }

    public User getUserByAccount(String accountId) throws SQLException{
        return userDao.getUserBaseInfoByAccount(accountId);
    }

    public void deleteByAccountId(User user) throws SQLException{
        userDao.deleteUser(user);
    }

}
