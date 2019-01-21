package com.crazywah.request;

import com.crazywah.common.RequestUrl;
import org.springframework.lang.NonNull;

public class NIMCreateAccount extends RequestBase{

    /**
     * 返回状态吗 200、403、414、416、431、500
     * @param accountId
     * @param nickName
     * @param token
     * @return
     */
    public String request(@NonNull String accountId,@NonNull String nickName,@NonNull String token){
        params.add("accid",accountId);
        params.add("name",nickName);
        params.add("token",token);
        return requestToNIM();
    }

    @Override
    protected String getUrl() {
        return RequestUrl.URL_USER_CREATE_ACTION;
    }
}
