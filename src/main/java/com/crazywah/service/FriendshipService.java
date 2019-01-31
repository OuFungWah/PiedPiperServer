package com.crazywah.service;

import com.crazywah.dao.IFriendshipDao;
import com.crazywah.entity.FriendShip;

import java.sql.SQLException;

public class FriendshipService {

    IFriendshipDao friendshipDao;

    public FriendshipService(IFriendshipDao friendshipDao){
        this.friendshipDao = friendshipDao;
    }

    public void addFriendShip(){

    }

    public FriendShip getFriendShip(String token, String targetId) throws SQLException{
        return friendshipDao.getFriendShip(token,targetId);
    }

}
