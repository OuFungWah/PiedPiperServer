package com.crazywah.controller;

import com.crazywah.bean.CreateUserBean;
import com.crazywah.request.NIMCreateAccount;
import com.crazywah.tools.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class AccountController {

    @RequestMapping(value = "/action.create", method = RequestMethod.POST)
    public void execute(@RequestBody String body) {
        System.out.println("requestBody = " + body);
        CreateUserBean bean = new Gson().fromJson(body, CreateUserBean.class);
        String result = null;
        if (bean != null && !TextUtils.isEmpty(bean.getAccountId()) && !TextUtils.isEmpty(bean.getNickname()) && !TextUtils.isEmpty(bean.getToken())) {
            result = new NIMCreateAccount().request(bean.getAccountId(), bean.getNickname(), bean.getToken());
            System.out.println("response = "+result);
        } else {
            if (bean == null) {
                result = "解析参数出错，参数为空";
            } else {
                result = "参数有误" + "\naccountId = " + bean.getAccountId() + "\nnickname = " + bean.getNickname() + "\ntoken = " + bean.getToken();
            }
            System.out.println(result);
        }
    }

}
