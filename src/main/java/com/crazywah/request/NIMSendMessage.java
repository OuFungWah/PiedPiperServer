package com.crazywah.request;

import com.crazywah.common.RequestUrl;

public class NIMSendMessage extends RequestBase{

    /**
     * from	        String	是	发送者accid，用户帐号，最大32字符，APP内唯一
     * msgtype	    int	    是	0：点对点自定义通知，1：群消息自定义通知，其他返回414
     * to	        String	是	msgtype==0是表示accid即用户id，msgtype==1表示tid即群id
     * attach	    String	是	自定义通知内容，第三方组装的字符串，建议是JSON串，最大长度4096字符
     * pushcontent	String	否	iOS推送内容，第三方自己组装的推送内容,不超过150字符
     * payload	    String	否	iOS推送对应的payload,必须是JSON,不能超过2k字符
     * sound	    String	否	如果有指定推送，此属性指定为客户端本地的声音文件名，长度不要超过30个字符，如果不指定，会使用默认声音
     * save	        int	    否	1表示只发在线，2表示会存离线，其他会报414错误。默认会存离线
     * option	    String	否	发消息时特殊指定的行为选项,Json格式，可用于指定消息计数等特殊行为;option中字段不填时表示默认值。
     * option示例：
     * {"badge":false,"needPushNick":false,"route":false}
     * 字段说明：
     * 1. badge:该消息是否需要计入到未读计数中，默认true;
     * 2. needPushNick: 推送文案是否需要带上昵称，不设置该参数时默认false(ps:注意与sendMsg.action接口有别);
     * 3. route: 该消息是否需要抄送第三方；默认true (需要app开通消息抄送功能)
     * @return
     */
    public String request(String from, int msgtype, String to, String attach){
        params.add("from",from);
        params.add("msgtype",msgtype+"");
        params.add("to",to);
        params.add("attach",attach);
        return requestToNIM();
    }

    @Override
    protected String getUrl() {
        return RequestUrl.URL_SEND_CUSTOM_MESSAGE;
    }
}
