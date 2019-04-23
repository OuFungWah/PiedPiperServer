package com.crazywah.service;

import com.crazywah.dao.IFriendshipDao;
import com.crazywah.entity.FriendShip;
import com.crazywah.entity.User;

import java.sql.SQLException;
import java.util.List;

public class FriendshipService {

    IFriendshipDao friendshipDao;

    public FriendshipService(IFriendshipDao friendshipDao) {
        this.friendshipDao = friendshipDao;
    }

    public void addFriendShip(FriendShip friendShip) throws SQLException {
        if (friendshipDao.getFriendShipById(friendShip.getOriginId(), friendShip.getTargetId()) == null) {
            friendShip.setRelation(FriendShip.STRANGER);
            friendshipDao.addFriendShipRequest(friendShip);
        }
        if (friendshipDao.getFriendShipById(friendShip.getTargetId(), friendShip.getOriginId()) == null) {
            FriendShip temp = new FriendShip();
            temp.setOriginId(friendShip.getTargetId());
            temp.setTargetId(friendShip.getOriginId());
            temp.setRelation(FriendShip.STRANGER);
            friendshipDao.addFriendShipRequest(temp);
        }
    }

    public void updateRelation(FriendShip friendShip) throws SQLException {
        friendshipDao.updateRelation(friendShip);
    }

    public void updateRequest(FriendShip friendShip) throws SQLException {
        friendshipDao.updateRequest(friendShip);
    }

    public void updateRemark(FriendShip friendShip) throws SQLException {
        friendshipDao.updateRemark(friendShip);
    }

    public void updateAlias(FriendShip friendShip) throws SQLException {
        friendshipDao.updateAlias(friendShip);
    }

    public List<User> getRequestingList(String token) throws SQLException {
        return friendshipDao.getRequestingUser(token);
    }

    public FriendShip getFriendShip(String token, String targetId) throws SQLException {
        return friendshipDao.getFriendShip(token, targetId);
    }

}
