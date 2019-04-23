package com.crazywah.service;


import com.crazywah.dao.IFriendRequestDao;
import com.crazywah.entity.FriendRequest;
import com.crazywah.entity.User;

import java.sql.SQLException;
import java.util.List;

public class FriendRequestService {

    IFriendRequestDao dao;

    public FriendRequestService(IFriendRequestDao dao) {
        this.dao = dao;
    }

    public void addRequest(FriendRequest request) throws SQLException {
        if (dao.getFriendRequestByUserId(request.getFromId(), request.getToId()) != null) {
            dao.updateRequest(request);
        } else {
            dao.addRequest(request);
        }
    }

    public void updateRequest(FriendRequest request) throws SQLException{
        dao.updateRequest(request);
    }

    public void updateRequestStatus(FriendRequest request) throws SQLException{
        dao.updateRequestStatus(request);
    }

    public List<User> getRequests(String token) throws SQLException {
        return dao.getRequestList(token);
    }

}
