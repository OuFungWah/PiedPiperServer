package com.crazywah.request;

import com.crazywah.common.RequestUrl;
import org.springframework.lang.NonNull;

public class NIMCreateAccount extends RequestBase{

    /**
     * 返回状态吗 200、403、414、416、431、500
     *
     * {"desc":"already register","code":414}
     *
     * {"code":200,"info":{"token":"123456","accid":"admin1","name":"admin1"}}
     *
     * @param accountId
     * @param nickName
     * @return
     */
    public String request(@NonNull String accountId,@NonNull String nickName){
        params.add("accid",accountId);
        params.add("name",nickName);
        return requestToNIM();
    }

    @Override
    protected String getUrl() {
        return RequestUrl.URL_USER_CREATE_ACTION;
    }
}
