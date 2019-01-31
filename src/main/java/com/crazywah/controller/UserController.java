package com.crazywah.controller;

import com.crazywah.bean.QueryParam;
import com.crazywah.bean.ResponseBase;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.dao.IFriendshipDao;
import com.crazywah.dao.IUserDao;
import com.crazywah.entity.FriendShip;
import com.crazywah.entity.User;
import com.crazywah.service.FriendshipService;
import com.crazywah.service.UserService;
import com.crazywah.tools.TextUtils;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final String TAG = "UserController";

    private Gson gson = new Gson();
    private UserService userService;
    private FriendshipService friendshipService;

    public UserController(IUserDao userDao, IFriendshipDao friendshipDao) {
        this.userService = new UserService(userDao);
        this.friendshipService = new FriendshipService(friendshipDao);
    }

    /**
     * 根据关系筛选出获取用户列表
     *
     * header 需要添加 token
     *
     * {
     *     "relation":0
     * }
     * @param body
     * @param httpHeaders
     * @return
     */
    @PostMapping("/getUsersByRelation")
    public String getFriends(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) {
        ResponseBase<List<User>> responseBase = new ResponseBase<>();
        if(httpHeaders.containsKey("token")){
            String token = httpHeaders.get("token").get(0);
            if (!TextUtils.isEmpty(token)) {
                QueryParam param = gson.fromJson(body, QueryParam.class);
                try {
                    if (userService.getUserByToken(token)!=null){
                        List<User> users = userService.getUsers(token, param.getRelation());
                        responseBase.setStatus(ResponseStateCode.CODE_200);
                        responseBase.setResult(users);
                    }else{
                        responseBase.setStatus(ResponseStateCode.CODE_202);
                        responseBase.setMessage("token验证错误");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    responseBase.setStatus(ResponseStateCode.CODE_14001);
                    responseBase.setMessage(e.getSQLState());
                }
            } else {
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("参数有误");
            }
        }else{
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

    /**
     * 根据关系获取用户信息
     *
     * header 需要添加 token
     * {
     *     "accid":"xxx"
     * }
     * @return
     */
    @RequestMapping("/getSingleUserById")
    public String getUser(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders){
        ResponseBase<User> responseBase = new ResponseBase<>();
        if(httpHeaders.containsKey("token")){
            String token = httpHeaders.get("token").get(0);
            if (!TextUtils.isEmpty(token)) {
                QueryParam param = gson.fromJson(body, QueryParam.class);
                if(!TextUtils.isEmpty(param.getAccid())){
                    try{
                        if (userService.getUserByToken(token)!=null) {
                            User user;
                            FriendShip friendShip = friendshipService.getFriendShip(token, param.getAccid());
                            if (friendShip == null || friendShip.getRelation() == FriendShip.STRANGER || friendShip.getRelation() == FriendShip.BLACK_LIST) {
                                //无关系或陌生人或黑名单
                                user = userService.getUserBaseInfo(param.getAccid());
                            } else {
                                //其他好友或更亲密关系
                                user = userService.getUserFullInfo(token, param.getAccid());
                            }
                            responseBase.setStatus(ResponseStateCode.CODE_200);
                            responseBase.setResult(user);
                        }else {
                            responseBase.setStatus(ResponseStateCode.CODE_202);
                            responseBase.setMessage("token验证错误");
                        }
                    }catch (SQLException e){
                        responseBase.setStatus(ResponseStateCode.CODE_14001);
                        responseBase.setMessage(e.getSQLState());
                    }
                }else{
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("缺少查询对象ID");
                }
            } else {
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("缺少token");
            }
        }else{
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

}
