package com.crazywah.controller;

import com.crazywah.bean.ResponseBase;
import com.crazywah.bean.SearchMomentBean;
import com.crazywah.bean.StringListBean;
import com.crazywah.common.ResponseStateCode;
import com.crazywah.entity.Comment;
import com.crazywah.entity.Like;
import com.crazywah.entity.Moment;
import com.crazywah.entity.User;
import com.crazywah.service.MomentService;
import com.crazywah.service.UserService;
import com.crazywah.tools.CheckSumBuilder;
import com.crazywah.tools.Log;
import com.crazywah.tools.PictureUtils;
import com.crazywah.tools.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/moment")
public class MomentController {

    private static final String TAG = "MomentController";

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
                    if (!TextUtils.isEmpty(moment.getPhotoList())) {
                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        List<String> baseList = gson.fromJson(moment.getPhotoList(), type);
                        //保存图片文件
                        if (baseList != null && !baseList.isEmpty()) {
                            for (String base64 : baseList) {
                                String dirName = CheckSumBuilder.getMD5(user.getAccountId());
                                String fileName = CheckSumBuilder.getMD5(System.currentTimeMillis() + ".jpg");
                                picUrls.add(PictureUtils.savePic(base64, dirName, fileName));
                            }
                            moment.setPhotoList(gson.toJson(picUrls));
                        }
                    }
                    moment.setPostTime(new Date(System.currentTimeMillis()));
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
     * "limit":123,
     * "offset":123
     * }
     *
     * @param body
     * @param httpHeaders
     * @return
     */
    @PostMapping("/getAllMoments")
    public String getAllMoments(@RequestBody String body, @RequestHeader HttpHeaders httpHeaders) {
        SearchMomentBean params = gson.fromJson(body, SearchMomentBean.class);
        ResponseBase<List<Moment>> responseBase = new ResponseBase<>();
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    // 返回自己与好友的朋友圈
                    Log.d(TAG, "token = " + token + " limit = " + params.getLimit() + " offset = " + params.getOffset());
                    List<Moment> moments = momentService.getAllMoment(user.getToken(), params.getLimit(), params.getOffset());
                    responseBase.setStatus(ResponseStateCode.CODE_200);
                    responseBase.setResult(moments);
                    Log.d(TAG, "data = " + gson.toJson(responseBase));
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
            Moment params = gson.fromJson(body, Moment.class);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    momentService.deleteMoment(params.getMomentId(), user.getAccountId());
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
     * "toIdId":"ajskhdk",
     * "content":"asiyudhgkashd",
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
                    comment.setFromId(user.getAccountId());
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
                    momentService.deleteComment(comment.getCommentId());
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
     * "objId":123
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/likeMoment")
    public String likeMoment(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        ResponseBase responseBase = new ResponseBase<>();
        Like like = gson.fromJson(body, Like.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    momentService.likeMoment(user.getAccountId(), like.getObjId());
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
     * "objId":123
     * }
     *
     * @param httpHeaders
     * @param body
     * @return
     */
    @PostMapping("/dislikeMoment")
    public String dislikeMoment(@RequestHeader HttpHeaders httpHeaders, @RequestBody String body) {
        // TODO: 2019/3/31 取消点赞
        ResponseBase responseBase = new ResponseBase<>();
        Like like = gson.fromJson(body, Like.class);
        if (httpHeaders.containsKey("token")) {
            String token = httpHeaders.get("token").get(0);
            try {
                User user = userService.getUserByToken(token);
                if (user != null && user.getAccountId() != null) {
                    momentService.dislikeMoment(user.getAccountId(), like.getObjId());
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
