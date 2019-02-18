package com.crazywah.service;

import com.crazywah.dao.IFriendshipDao;
import com.crazywah.entity.FriendShip;

import java.sql.SQLException;

public class FriendshipService {

    IFriendshipDao friendshipDao;

    public FriendshipService(IFriendshipDao friendshipDao){
        this.friendshipDao = friendshipDao;
    }

    public void addFriendShip(FriendShip friendShip) throws SQLException{
        friendshipDao.addFriendShipRequest(friendShip);
        FriendShip temp = new FriendShip();
        temp.setOriginId(friendShip.getTargetId());
        temp.setTargetId(friendShip.getOriginId());
        temp.setRelation(friendShip.getRelation());
        temp.setRequestTime(friendShip.getRequestTime());
        friendshipDao.addFriendShipRequest(temp);
    }

    public void updateRelation(FriendShip friendShip) throws SQLException{
        friendshipDao.updateRelation(friendShip);
    }

    public void updateRequest(FriendShip friendShip) throws SQLException{
        friendshipDao.updateRequest(friendShip);
    }

    public void updateRemark(FriendShip friendShip) throws SQLException{
        friendshipDao.updateRemark(friendShip);
    }

    public void updateAlias(FriendShip friendShip) throws SQLException{
        friendshipDao.updateAlias(friendShip);
    }

    public FriendShip getFriendShip(String token, String targetId) throws SQLException{
        return friendshipDao.getFriendShip(token,targetId);
    }

}
