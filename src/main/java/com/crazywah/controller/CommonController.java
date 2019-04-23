package com.crazywah.controller;

import com.crazywah.bean.PicBean;
import com.crazywah.bean.ResponseBase;
import com.crazywah.bean.StringListBean;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.entity.Moment;
import com.crazywah.entity.User;
import com.crazywah.service.MomentService;
import com.crazywah.service.UserService;
import com.crazywah.tools.CheckSumBuilder;
import com.crazywah.tools.Log;
import com.crazywah.tools.PictureUtils;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/common")
public class CommonController {

    private static final String TAG = "CommonController";

    private static final String PIC_SERVER = "~/";
    private MomentService momentService;
    private UserService userService;
    private Gson gson = new Gson();

    public CommonController(MomentService momentService, UserService userService) {
        this.momentService = momentService;
        this.userService = userService;
    }

    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public String uploadPic(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase responseBase = new ResponseBase<>();
        PicBean picBean = gson.fromJson(body, PicBean.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    String dirName = CheckSumBuilder.getMD5(user.getAccountId());
                    String fileName = CheckSumBuilder.getMD5(System.currentTimeMillis() + ".png");
                    PictureUtils.savePic(picBean.getBase64(), dirName, fileName);
                } else {
                    responseBase.setStatus(ResponseStateCode.CODE_202);
                    responseBase.setMessage("用户token错误");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("数据库操作错误：" + e.getSQLState());
            } catch (FileAlreadyExistsException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("保存文件失误：" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                responseBase.setStatus(ResponseStateCode.CODE_202);
                responseBase.setMessage("保存文件失误：" + e.getMessage());
            }
        } else {
            responseBase.setStatus(ResponseStateCode.CODE_202);
            responseBase.setMessage("缺少token");
        }
        return gson.toJson(responseBase);
    }

}
