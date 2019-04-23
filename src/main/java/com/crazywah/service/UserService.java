package com.crazywah.service;

import com.crazywah.dao.IUserDao;
import com.crazywah.entity.FriendShip;
import com.crazywah.entity.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class UserService {

    private static final String TAG = "UserService";

    private IUserDao userDao;

    public UserService(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user) throws SQLException {
        userDao.addUser(user);
    }

    public User getUserByToken(String token) throws SQLException {
        return userDao.getUserByToken(token);
    }

    public List<User> getStrangers(String accountId) throws SQLException {
        return userDao.getStrangers(accountId + "%");
    }

    public List<User> getUsers() throws SQLException {
        return userDao.selectAllUser();
    }

    public List<User> getUsers(String token, int relation) throws SQLException {
        if (relation == FriendShip.FRIEND || relation == FriendShip.SP_FOLLOW || relation == FriendShip.STARED_FRIEND) {
            return userDao.getFriendsAllInfo(token);
        } else {
            return userDao.getUserByTokenRelation(token, relation);
        }
    }

    public User getUser(String accountId, String password) throws SQLException {
        return userDao.getUser(accountId, password);
    }

    public void updateToken(User user) throws SQLException {
        userDao.updateUserToken(user);
    }

    public User getUserByAccountPassword(User user) throws SQLException {
        return userDao.getUserByAccountPassword(user);
    }

    public User getUserBaseInfo(String accountId) throws SQLException {
        return userDao.getUserBaseInfoByAccount(accountId);
    }

    public User getUserFullInfo(String token, String targetId) throws SQLException {
        return userDao.getUserFullInfo(token, targetId);
    }

    public void updatePasswordByToken(String token, String password) throws SQLException {
        userDao.updatePasswordByToken(token, password);
    }

    public void updateUserNickName(User user) throws SQLException {
        userDao.updateUserNickName(user);
    }

    public void updateUserPassword(User user) throws SQLException {
        userDao.updateUserPassword(user);
    }

    public void updateUserGender(User user) throws SQLException {
        userDao.updateUserGender(user);
    }

    public void updateUserBirthday(User user) throws SQLException {
        userDao.updateUserBirthday(user);
    }

    public void updateUserMobile(User user) throws SQLException {
        userDao.updateUserMobile(user);
    }

    public void updateUserAddress(User user) throws SQLException {
        userDao.updateUserAddress(user);
    }

    public void updateUserEmail(User user) throws SQLException {
        userDao.updateUserEmail(user);
    }

    public void updateBaseInfo(User user) throws SQLException {
        userDao.updateUserBaseInfo(user);
    }

    public void updateAdditionInfo(User user) throws SQLException {
        userDao.updateUserAdditionInfo(user);
    }

    public void updateSignature(User user) throws SQLException {
        userDao.updateSignature(user);
    }

    public User getUserByAccount(String accountId) throws SQLException {
        return userDao.getUserBaseInfoByAccount(accountId);
    }

    public void deleteByAccountId(User user) throws SQLException {
        userDao.deleteUser(user);
    }

    public void updateAvatar(User user) throws SQLException {
        userDao.updateAvatar(user);
    }

}
