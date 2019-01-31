package com.crazywah.request;

import com.crazywah.common.RequestUrl;

public class NIMRefreshToken extends RequestBase {

    public String request(String accountId){
        params.add("accid",accountId);
        return requestToNIM();
    }

    @Override
    protected String getUrl() {
        return RequestUrl.URL_USER_REFRESH_TOKEN_ACTION;
    }

}
