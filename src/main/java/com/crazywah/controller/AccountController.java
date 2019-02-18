package com.crazywah.controller;

import com.crazywah.bean.NIMBean;
import com.crazywah.bean.ResponseBase;
import com.crazywah.bean.TokenBean;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.dao.IUserDao;
import com.crazywah.entity.User;
import com.crazywah.request.NIMCreateAccount;
import com.crazywah.request.NIMRefreshToken;
import com.crazywah.service.UserService;
import com.crazywah.tools.Log;
import com.crazywah.tools.TextUtils;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    private static final String TAG = "AccountController";

    private Gson gson = new Gson();
    private UserService userService;

    public AccountController(IUserDao userDao) {
        userService = new UserService(userDao);
    }

    /**
     * 登录接口
     * <p>
     * {
     * "accid":"xxxx",
     * "password":"xxxx"
     * }
     *
     * @param body
     * @return 返回 token
     */
    @RequestMapping("/login")
    public String login(@RequestBody String body) {
        ResponseBase<TokenBean> responseBase = new ResponseBase<>();
        NIMBean.Info params = gson.fromJson(body, NIMBean.Info.class);
        if (!TextUtils.isEmpty(params.getAccid()) && !TextUtils.isEmpty(params.getPassword())) {
            try {
                TokenBean token = userService.getToken(params.getAccid(), params.getPassword());
                if (token == null || TextUtils.isEmpty(token.getToken())) {
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("用户不存在或密码不正确");
                }else{
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                    responseBase.setResult(token);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_14001);
                responseBase.setMessage(e.getSQLState());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage(responseBase.getMessage() + ",请检查是否参数有误。");
        }
        return gson.toJson(responseBase);
    }

    /**
     * 请求示例
     * {
     * "accid":"oufenghua",
     * "name":"区枫华",
     * "password":"123456"
     * }
     * NIM 返回
     * {
     * "code":200,
     * "info":{"token":"xx","accid":"xx","name":"xx"}
     * }
     *
     * @param body
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String createAccount(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) {
        Log.d(TAG, httpHeaders.get("Content-Type").toString());
        Log.d(TAG, "requestBody = " + body);
        ResponseBase<User> responseBase = new ResponseBase();
        NIMBean.Info info = gson.fromJson(body, NIMBean.Info.class);
        if (info != null && !TextUtils.isEmpty(info.getAccid()) && !TextUtils.isEmpty(info.getName()) && !TextUtils.isEmpty(info.getPassword())) {
            User user = new User();
            user.setAccountId(info.getAccid());
            user.setNickname(info.getName());
            user.setPassword(info.getPassword());
            try {
                NIMBean bean = gson.fromJson(new NIMCreateAccount().request(info.getAccid(), info.getName()), NIMBean.class);
                if (bean.getCode() == ResponseStateCode.CODE_200) {
                    //云信注册成功
                    user.setToken(bean.getInfo().getToken());
                    user.setRegisterTime(new Date(System.currentTimeMillis()));
                    userService.addUser(user);
                    responseBase.setResult(user);
                } else {
                    //注册失败
                    responseBase.setStatus(bean.getCode());
                }
            } catch (SQLException e) {
                responseBase.setStatus(ResponseStateCode.CODE_14001);
                responseBase.setMessage(e.getSQLState());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage(responseBase.getMessage() + ",请检查是否参数有误。");
        }
        return gson.toJson(responseBase);
    }

    /**
     * 请求示例
     * {
     * "accid":"oufenghua",
     * "password":"123456"
     * }
     *
     * @param body
     */
    @ResponseBody
    @RequestMapping(value = "/refreshToken", method = RequestMethod.POST)
    public String updateToken(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) {
        ResponseBase<User> responseBase = new ResponseBase<>();
        NIMBean.Info info = gson.fromJson(body, NIMBean.Info.class);
        User user = new User();
        user.setAccountId(info.getAccid());
        user.setPassword(info.getPassword());
        try {
            user = userService.getUserByAccountPassword(user);
            if (user == null || TextUtils.isEmpty(user.getAccountId())) {
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("账户或密码错误");
            } else {
                NIMBean bean = gson.fromJson(new NIMRefreshToken().request(user.getAccountId()), NIMBean.class);
                if (bean.getCode() == ResponseStateCode.CODE_200) {
                    //刷新 Token 成功
                    user.setToken(bean.getInfo().getToken());
                    userService.updateToken(user);
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                    responseBase.setResult(user);
                } else {
                    responseBase.setStatus(bean.getCode());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responseBase.setStatus(ResponseStateCode.CODE_14001);
        }
        return gson.toJson(responseBase);
    }

    /**
     * 更新密码
     * <p>
     * 请求示例
     * {
     * "password":"123456"
     * }
     *
     * @param body
     */
    @ResponseBody
    @RequestMapping(value = "/refreshPassword", method = RequestMethod.POST)
    public String updatePassword(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) {
        ResponseBase<User> responseBase = new ResponseBase<>();
        NIMBean.Info params = gson.fromJson(body, NIMBean.Info.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            String password = params.getPassword();
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(password)) {
                try {
                    if (userService.getUserByToken(token)!=null){
                        userService.updatePasswordByToken(token, password);
                        responseBase.setStatus(ResponseStateCode.CODE_200);
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
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

    /**
     * {
     * "address":"oufenghua",
     * "email":12314564321654,
     * "mobile":"",
     * "birthday":"",
     * "gender":0
     * }
     *
     * @param body
     * @param httpHeaders
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refreshAdditionInfo", method = RequestMethod.POST)
    public String updateAdditionInfo(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) {
        ResponseBase responseBase = new ResponseBase<>();
        NIMBean.Info params = gson.fromJson(body, NIMBean.Info.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                if (userService.getUserByToken(token)!=null){
                    User user = new User();
                    user.setBirthday(new Date(params.getBirthday()));
                    user.setToken(token);
                    user.setAddress(params.getAddress());
                    user.setMobile(params.getMobile());
                    user.setEmail(params.getEmail());
                    user.setGender(params.getGender());
                    userService.updateAdditionInfo(user);
                    responseBase.setStatus(ResponseStateCode.CODE_200);
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
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

    /**
     * 修改个性签名
     *
     * {
     *     "signature":"xxxxx"
     * }
     *
     * @param body
     * @param httpHeaders
     * @return
     */
    @RequestMapping("/refreshSignature")
    public String updateSignature(@RequestBody String body,@RequestHeader HttpHeaders httpHeaders){
        NIMBean.Info params = gson.fromJson(body,NIMBean.Info.class);
        ResponseBase responseBase = new ResponseBase();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User requester  = userService.getUserByToken(token);
                Log.d(TAG,gson.toJson(requester));
                if (requester!=null){
                    User user = new User();
                    user.setSignature(params.getSignature());
                    user.setToken(token);
                    userService.updateSignature(user);
                    responseBase.setStatus(ResponseStateCode.CODE_200);
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
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

}
