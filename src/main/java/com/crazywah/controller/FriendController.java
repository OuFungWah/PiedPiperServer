package com.crazywah.controller;

import com.crazywah.bean.HandleRequestBean;
import com.crazywah.bean.MessageBean;
import com.crazywah.bean.NIMBean;
import com.crazywah.bean.ResponseBase;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.dao.IFriendRequestDao;
import com.crazywah.dao.IFriendshipDao;
import com.crazywah.dao.IUserDao;
import com.crazywah.entity.FriendRequest;
import com.crazywah.entity.FriendShip;
import com.crazywah.entity.Moment;
import com.crazywah.entity.User;
import com.crazywah.request.NIMSendMessage;
import com.crazywah.service.FriendRequestService;
import com.crazywah.service.FriendshipService;
import com.crazywah.service.UserService;
import com.crazywah.tools.TextUtils;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private Gson gson = new Gson();

    public FriendController(IUserDao userDao, IFriendshipDao friendshipDao, IFriendRequestDao friendRequestDao) {
        userService = new UserService(userDao);
        friendshipService = new FriendshipService(friendshipDao);
        friendRequestService = new FriendRequestService(friendRequestDao);
    }

    /**
     * fromId
     * toId
     * requestMessage
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/addFriend")
    public String addFriend(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<NIMBean> responseBase = new ResponseBase<>();
        FriendRequest friendRequest = gson.fromJson(body, FriendRequest.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    String response = new NIMSendMessage().request(friendRequest.getFromId(), 0, friendRequest.getToId(), friendRequest.getRequestMessage());
                    // 添加数据库添加 双方 friendship
                    FriendShip friendShip = new FriendShip();
                    friendShip.setOriginId(friendRequest.getFromId());
                    friendShip.setTargetId(friendRequest.getToId());
                    friendShip.setRequestMessage(friendRequest.getRequestMessage());
                    friendshipService.addFriendShip(friendShip);

                    friendRequest.setRequestTime(new Date(System.currentTimeMillis()));
                    friendRequest.setRequestStatus(FriendRequest.REQUESTING);
                    friendRequestService.addRequest(friendRequest);
                    NIMBean bean = gson.fromJson(response, NIMBean.class);
                    responseBase.setStatus(bean.getCode());
                    responseBase.setResult(bean);
                } else {
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("用户token错误");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("数据库操作错误：" + e.getSQLState());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

    /**
     * {
     * "result":0,
     * "to":"",
     * "attach":""
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/handleRequest")
    public String handleRequest(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<NIMBean> responseBase = new ResponseBase<>();
        HandleRequestBean handleResult = gson.fromJson(body, HandleRequestBean.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    FriendRequest friendRequest = new FriendRequest();
                    friendRequest.setToId(user.getAccountId());
                    friendRequest.setFromId(handleResult.getTo());
                    // 因为是处理好友请求，默认不需要处理关系； 但修改请求状态
                    if (handleResult.getResult() == HandleRequestBean.REFUSE) {
                        String response = new NIMSendMessage().request(user.getAccountId(), 0, handleResult.getTo(), handleResult.getAttach());
                        // 云信发送消息
                        NIMBean bean = gson.fromJson(response, NIMBean.class);
                        friendRequest.setRequestStatus(FriendRequest.REFUSED);
                        friendRequestService.updateRequestStatus(friendRequest);
                        responseBase.setStatus(bean.getCode());
                        if (bean.getCode() == ResponseStateCode.CODE_200) {
                            responseBase.setMessage("你已成功拒绝对方");
                        }
                    } else {
                        // 修改双方关系
                        FriendShip friendShip = new FriendShip();
                        friendShip.setOriginId(user.getAccountId());
                        friendShip.setTargetId(handleResult.getTo());
                        friendShip.setFriendTime(new Date(System.currentTimeMillis()));
                        friendShip.setRelation(FriendShip.FRIEND);
                        friendshipService.updateRelation(friendShip);
                        friendShip.setOriginId(handleResult.getTo());
                        friendShip.setTargetId(user.getAccountId());
                        friendshipService.updateRelation(friendShip);
                        // 修改请求状态
                        friendRequest.setRequestStatus(FriendRequest.ACCEPTED);
                        friendRequestService.updateRequestStatus(friendRequest);
                        // 云信发送消息
                        String response = new NIMSendMessage().request(user.getAccountId(), 0, handleResult.getTo(),  handleResult.getAttach());
                        NIMBean bean = gson.fromJson(response, NIMBean.class);
                        responseBase.setStatus(bean.getCode());
                        if (bean.getCode() == ResponseStateCode.CODE_200) {
                            responseBase.setMessage("你已成功添加对方为好友");
                        }
                    }
                } else {
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("用户token错误");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("数据库操作错误：" + e.getSQLState());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

    /**
     * @param httpHeaders
     * @return
     */
    @PostMapping("/getAllRequest")
    public String getAllRequest(@RequestHeader HttpHeaders httpHeaders) {
        ResponseBase<List<User>> responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    responseBase.setResult(friendRequestService.getRequests(user.getToken()));
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                } else {
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("用户token错误");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("数据库操作错误：" + e.getSQLState());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

    /**
     * from	        String	是	发送者accid，用户帐号，最大32字符，APP内唯一
     * msgtype	    int	    是	0：点对点自定义通知，1：群消息自定义通知，其他返回414
     * to	        String	是	msgtype==0是表示accid即用户id，msgtype==1表示tid即群id
     * attach	    String	是	自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字符
     */
    @PostMapping("/send")
    public String send(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<NIMBean> responseBase = new ResponseBase<>();
        MessageBean messageBean = gson.fromJson(body, MessageBean.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    String response = new NIMSendMessage().request(user.getAccountId(), 0, messageBean.getTo(), messageBean.getAttach());
                    NIMBean bean = gson.fromJson(response, NIMBean.class);
                    responseBase.setResult(bean);
                } else {
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("用户token错误");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("数据库操作错误：" + e.getSQLState());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

}
