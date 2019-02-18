package com.crazywah.controller;

import com.crazywah.bean.ResponseBase;
import com.crazywah.bean.StringListBean;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.entity.Comment;
import com.crazywah.entity.Moment;
import com.crazywah.entity.User;
import com.crazywah.service.MomentService;
import com.crazywah.service.UserService;
import com.crazywah.tools.CheckSumBuilder;
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
@RequestMapping(value = "/moment")
public class MomentController {

    private static final String PIC_SERVER = "www.crazywah.com:2019/";
    private MomentService momentService;
    private UserService userService;
    private Gson gson = new Gson();

    public MomentController(MomentService momentService, UserService userService) {
        this.momentService = momentService;
        this.userService = userService;
    }

    /**
     * {
     * "postTime":1234-12-12 12:21:23,
     * "postContent":"This is content",
     * "visiableRange":0,
     * "blackList":"["",""]",
     * "whiteList":"["",""]",
     * "photoList":"["",""]"
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @RequestMapping(value = "/postMoment", method = RequestMethod.POST)
    public String postMoment(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<Moment> responseBase = new ResponseBase<>();
        Moment moment = gson.fromJson(body, Moment.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                List<String> picUrls = new ArrayList<>();
                if (user != null && user.getAccountId() != null) {
                    moment.setAccountId(user.getAccountId());
                    StringListBean picListBean = gson.fromJson(moment.getPhotoList(), StringListBean.class);
                    //保存图片文件
                    for (String base64 : picListBean.getStringList()) {
                        String dirName = CheckSumBuilder.getMD5(user.getAccountId());
                        String fileName = CheckSumBuilder.getMD5(System.currentTimeMillis() + ".jpg");
                        PictureUtils.savePic(base64, dirName, fileName);
                        picUrls.add(PIC_SERVER + dirName + "/" + fileName);
                    }
                    moment.setPhotoList(gson.toJson(picUrls));
                    momentService.addMoment(moment);
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                    responseBase.setResult(moment);
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

    @PostMapping("/getMomentsByToken")
    public String getMoments(@RequestHeader HttpHeaders httpHeaders) {
        ResponseBase<List<Moment>> responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    List<Moment> moments = momentService.getMomentsByToken(token);
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                    responseBase.setResult(moments);
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
     * "momentId":123
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/deleteMoment")
    public String deleteMomentById(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<List<Moment>> responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            Moment moment = gson.fromJson(body, Moment.class);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    momentService.deleteMoment(moment.getMomentId(), user.getAccountId());
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
     * {
     * "momentId":123,
     * "targetId":"ajskhdk",
     * "commentContent":"asiyudhgkashd",
     * "commentTime":"1234-23-23 23:23:23"
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/addComment")
    public String addComment(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<Comment> responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            Comment comment = gson.fromJson(body, Comment.class);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    comment.setCommenterId(user.getAccountId());
                    momentService.addComment(comment);
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
     * "momentId":123
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/getCommentByMomentId")
    public String getComments(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase<List<Comment>> responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            Moment moment = gson.fromJson(body, Moment.class);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    List<Comment> comments = momentService.getCommentByMomentId(moment.getMomentId(), user.getToken());
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                    responseBase.setResult(comments);
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
     * "commentId":123
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/deleteComment")
    public String deleteComment(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            Comment comment = gson.fromJson(body, Comment.class);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    momentService.deleteMoment(comment.getCommentId(), user.getAccountId());
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

}