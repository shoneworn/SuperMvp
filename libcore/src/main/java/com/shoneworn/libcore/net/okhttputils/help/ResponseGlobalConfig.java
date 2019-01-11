package com.shoneworn.libcore.net.okhttputils.help;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.help.ResponseCommonCallback.ResponseConfig;

public class ResponseGlobalConfig {
    public ResponseConfig responseConfig;
    private static volatile ResponseGlobalConfig responseGlobalConfig = null;

    private ResponseGlobalConfig() {
    }

    public static ResponseGlobalConfig getInstance() {
        if(responseGlobalConfig == null) {
            Class var0 = ResponseGlobalConfig.class;
            synchronized(ResponseGlobalConfig.class) {
                if(responseGlobalConfig == null) {
                    responseGlobalConfig = new ResponseGlobalConfig();
                }
            }
        }

        return responseGlobalConfig;
    }

    public ResponseConfig getResponseConfig() {
        return this.responseConfig;
    }

    public void setResponseConfig(ResponseConfig responseConfig) {
        this.responseConfig = responseConfig;
    }
}
