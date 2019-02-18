package com.crazywah.controller;

import com.crazywah.bean.HandleRequestBean;
import com.crazywah.bean.MessageBean;
import com.crazywah.bean.NIMBean;
import com.crazywah.bean.ResponseBase;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.dao.IFriendshipDao;
import com.crazywah.dao.IUserDao;
import com.crazywah.entity.FriendShip;
import com.crazywah.entity.Moment;
import com.crazywah.entity.User;
import com.crazywah.request.NIMSendMessage;
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
    private Gson gson = new Gson();

    public FriendController(IUserDao userDao, IFriendshipDao friendshipDao) {
        userService = new UserService(userDao);
        friendshipService = new FriendshipService(friendshipDao);
    }

    /**
     * originId
     * targetId
     * requestMessage
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/addFriend")
    public String addFriend(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body){
        ResponseBase<NIMBean> responseBase = new ResponseBase<>();
        FriendShip friendShip = gson.fromJson(body,FriendShip.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    String response = new NIMSendMessage().request(friendShip.getOriginId(),0,friendShip.getTargetId(),"");
                    // 添加数据库添加 双方 friendship
                    friendShip.setRequestTime(new Date(System.currentTimeMillis()));
                    //如果第一次请求则添加双方关系进入关系表，否则就只修改
                    if(friendshipService.getFriendShip(token,friendShip.getTargetId())==null){
                        friendshipService.addFriendShip(friendShip);
                    }else{
                        friendshipService.updateRequest(friendShip);
                    }
                    NIMBean bean = gson.fromJson(response,NIMBean.class);
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
     *     "result":0,
     *     "to":""
     * }
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/handleRequest")
    public String handleRequest(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body){
        ResponseBase<NIMBean> responseBase = new ResponseBase<>();
        HandleRequestBean handleResult = gson.fromJson(body,HandleRequestBean.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    // 因为是处理好友请求，默认不需要处理关系
                    if(handleResult.getResult() == 0){
                        String response = new NIMSendMessage().request(user.getAccountId(),0,handleResult.getTo(),"");
                        // 云信发送消息
                        NIMBean bean = gson.fromJson(response,NIMBean.class);
                        responseBase.setStatus(bean.getCode());
                        if(bean.getCode()==ResponseStateCode.CODE_200){
                            responseBase.setMessage("你已成功拒绝对方");
                        }
                    }else{
                        // 修改双方关系
                        FriendShip friendShip = new FriendShip();
                        friendShip.setOriginId(user.getAccountId());
                        friendShip.setTargetId(handleResult.getTo());
                        friendShip.setRelation(FriendShip.FRIEND);
                        friendshipService.updateRelation(friendShip);
                        friendShip.setOriginId(handleResult.getTo());
                        friendShip.setTargetId(user.getAccountId());
                        friendshipService.updateRelation(friendShip);
                        String response = new NIMSendMessage().request(user.getAccountId(),0,handleResult.getTo(),"");
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

}
