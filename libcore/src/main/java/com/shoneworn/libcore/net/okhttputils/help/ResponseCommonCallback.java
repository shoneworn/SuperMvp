package com.shoneworn.libcore.net.okhttputils.help;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.content.Context;
import android.os.Looper;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.callback.AbsCallbackWrapper;
import com.shoneworn.libcore.net.okhttputils.cookie.CookieJarImpl;
import com.shoneworn.libcore.net.okhttputils.cookie.CookieParseUtil;
import com.shoneworn.libcore.net.okhttputils.cookie.store.CookieStore;
import com.shoneworn.libcore.net.okhttputils.exception.HttpException;
import com.shoneworn.libcore.utils.StringUtils;
import com.shoneworn.libcore.utils.Utils;
import com.shoneworn.libcore.utils.WeakHandler;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.Response.Builder;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ResponseCommonCallback<T> extends AbsCallbackWrapper<T> {
    public static final String DEFAULT_ERROR_RESPONSE_PROM = "客户端时间戳异常";
    public static final String DEFAULT_ERROR_RESPONSE_CODE = "0";
    public static final String DEFAULT_TAG_STATUS = "stat";
    public static final String DEFAULT_TAG_MSG = "msg";
    public static final String DEFAULT_TAG_DATA = "data";
    public static final Integer DEFAULT_SUCCESS_RESPONSE = Integer.valueOf(1);
    public static final Integer DEFAULT_ERROR_RESPONSE = Integer.valueOf(0);
    public static final Integer DEFAULT_ERROR_TOKEN = Integer.valueOf(-1000000001);
    public static final Integer DEFAULT_ERROR_THIRD_PLATFORM_NO_BIND = Integer.valueOf(-1000000002);
    public static final Integer DEFAULT_ERROR_PWD = Integer.valueOf(-1000000003);
    private Context ctx;
    private ResponseCommonCallback.ResponseConfig responseConfig;
    private static String ERROR_RESPONSE_PROM = "客户端时间戳异常";
    private static String ERROR_RESPONSE_CODE = "0";
    public static int ERROR_RESPONSE_NULL = -2147483647;
    public static int SUCCESS_RESPONSE;
    public static int ERROR_RESPONSE;
    public static int ERROR_TOKEN;
    public static int ERROR_THIRD_PLATFORM_NO_BIND;
    public static int ERROR_PWD;
    public static String STATUS;
    public static String DATA;
    public static String MSG;
    private String msg = "";
    private int code;
    private Class<T> clazz;
    private Type type;
    protected boolean isForceTrans;
    protected boolean isNeedFrameworkParseResponseData;
    protected boolean isReplaceUserId;
    protected boolean isNeedFrameWorkSetUserId;
    protected boolean isNeedFrameWorkSetTs;
    protected boolean isNeedFrameWorkSetCode;

    public ResponseCommonCallback(Context ctx) {
        this.code = SUCCESS_RESPONSE;
        this.isForceTrans = false;
        this.isNeedFrameworkParseResponseData = true;
        this.isReplaceUserId = true;
        this.isNeedFrameWorkSetUserId = false;
        this.isNeedFrameWorkSetTs = false;
        this.isNeedFrameWorkSetCode = false;
        this.ctx = ctx;
        this.setResponseConfig(ResponseGlobalConfig.getInstance().getResponseConfig());
    }

    public T convertResponse(final Response response) throws Throwable {
        this.type = this.getResponseType();
        this.clazz = this.getResponseClass();
        this.msg = "";
        final String responseData = new String(response.body().bytes());
        WeakHandler handler = OkHttpUtils.getInstance().getDelivery();
        if(Utils.isEmpty(handler)) {
            handler = new WeakHandler(Looper.getMainLooper());
        }

        handler.post(new Runnable() {
            public void run() {
                ResponseCommonCallback.this.onCallBackJsonInner(response, responseData);
                ResponseCommonCallback.this.onCallBackJsonData(response, responseData);
            }
        });

        HttpUrl url;
        try {
            CookieJarImpl cookieJar = OkHttpUtils.getInstance().getCookieJar();
            if(!Utils.isEmpty(cookieJar)) {
                url = response.request().url();
                Headers headers = response.headers();
                List<Cookie> cookieList = CookieParseUtil.parseAll(url, headers);
                if(!Utils.isEmpty(cookieList)) {
                    CookieStore cookieStore = cookieJar.getCookieStore();
                    if(!Utils.isEmpty(cookieStore)) {
                        cookieStore.saveCookie(response.request().url(), cookieList);
                    }
                }
            }
        } catch (Exception var10) {
            ;
        }

        response.close();
        if(StringUtils.contains(responseData, ERROR_RESPONSE_PROM) && StringUtils.contains(responseData, ERROR_RESPONSE_CODE) && !StringUtils.isEmpty(StringUtils.trim(ERROR_RESPONSE_PROM))) {
//            DfttParameter dfttParameterInstance = DfttParameter.getSingleton();
//            if(dfttParameterInstance.isHasInited()) {
//                dfttParameterInstance.setDefaultParameter();
//            }

            if(this.isNeedFrameworkParseResponseData) {
                throw new HttpException(this.code, this.msg);
            }
        }

        if(!this.isNeedFrameworkParseResponseData) {
            return null;
        } else {
            String temp;
            if(StringUtils.isEmpty(StringUtils.trim(responseData))) {
                temp = "\tresponse.body() is null !!!\t";
                temp = temp + this.getResponseMsg(response);
                throw new HttpException(ERROR_RESPONSE_NULL, this.msg + temp);
            } else {
                temp = null;
                if(!this.isForceTrans()) {
                    url = null;

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        if(jsonObject.has(MSG)) {
                            this.msg = jsonObject.getString(MSG);
                        }

                        if(jsonObject.has(STATUS)) {
                            this.code = jsonObject.optInt(STATUS, SUCCESS_RESPONSE);
                        }

                        if(jsonObject.has(DATA)) {
                            temp = jsonObject.optString(DATA, "");
                        }
                    } catch (JSONException var9) {
                        ;
                    }
                }

                String data = !StringUtils.isEmpty(temp) && !StringUtils.equalsIgnoreCase(temp, "null")?temp:responseData;
//                LogUtils.d("data>>" + data);
//                LogUtils.d("code>>" + this.code);
                if(SUCCESS_RESPONSE == this.code || ERROR_THIRD_PLATFORM_NO_BIND == this.code) {
                    if(this.clazz == String.class) {
                        return (T)data;
                    }

                    if(this.type != null) {
                        return this.fromJson(data, this.type);
                    }

                    if(this.clazz != null) {
                        return this.fromJson(data, this.clazz);
                    }

                    if(String.class == this.type) {
                        return (T)data;
                    }
                }

                if(ERROR_TOKEN != this.code && ERROR_PWD != this.code) {
                    throw new HttpException(this.code, this.msg);
                } else {
                    throw new HttpException(this.code, this.msg);
                }
            }
        }
    }

    protected String getResponseMsg(Response response) {
        String msg = "";
        if(Utils.isEmpty(response)) {
            return msg;
        } else {
            Builder builder = response.newBuilder();
            if(Utils.isEmpty(builder)) {
                return msg;
            } else {
                Response clone = builder.build();
                if(Utils.isEmpty(clone)) {
                    return msg;
                } else {
                    msg = msg + clone.code() + "\t" + clone.message() + "\t" + clone.request().url() + "\t";
                    return msg;
                }
            }
        }
    }

    protected void onCallBackJsonInner(Response response, String responseData) {
    }

    public boolean isForceTrans() {
        return this.isForceTrans;
    }

    public ResponseCommonCallback setForceTrans(boolean forceTrans) {
        this.isForceTrans = forceTrans;
        return this;
    }

    public ResponseCommonCallback setReplaceUserId(boolean isReplaceUserId) {
        this.isReplaceUserId = isReplaceUserId;
        return this;
    }

    public ResponseCommonCallback setNeedFrameWorkSetUserId(boolean needFrameWorkSetUserId) {
        this.isNeedFrameWorkSetUserId = needFrameWorkSetUserId;
        return this;
    }

    public ResponseCommonCallback setNeedFrameWorkSetTs(boolean needFrameWorkSetTs) {
        this.isNeedFrameWorkSetTs = needFrameWorkSetTs;
        return this;
    }

    public ResponseCommonCallback setNeedFrameWorkSetCode(boolean needFrameWorkSetCode) {
        this.isNeedFrameWorkSetCode = needFrameWorkSetCode;
        return this;
    }

    public ResponseCommonCallback setNeedFrameworkParseResponseData(boolean needFrameworkParseResponseData) {
        this.isNeedFrameworkParseResponseData = needFrameworkParseResponseData;
        return this;
    }

    public ResponseCommonCallback.ResponseConfig getResponseConfig() {
        return this.responseConfig;
    }

    private ResponseCommonCallback setResponseConfig(ResponseCommonCallback.ResponseConfig responseConfig) {
        if(null == responseConfig) {
            responseConfig = new ResponseCommonCallback.ResponseConfig();
        }

        this.responseConfig = responseConfig;
        responseConfig.build();
        return this;
    }

    public abstract Class<T> getResponseClass();

    public abstract Type getResponseType();

    public abstract T fromJson(String var1, Class<T> var2);

    public abstract T fromJson(String var1, Type var2);

    static {
        SUCCESS_RESPONSE = DEFAULT_SUCCESS_RESPONSE.intValue();
        ERROR_RESPONSE = DEFAULT_ERROR_RESPONSE.intValue();
        ERROR_TOKEN = DEFAULT_ERROR_TOKEN.intValue();
        ERROR_THIRD_PLATFORM_NO_BIND = DEFAULT_ERROR_THIRD_PLATFORM_NO_BIND.intValue();
        ERROR_PWD = DEFAULT_ERROR_PWD.intValue();
        STATUS = "stat";
        DATA = "data";
        MSG = "msg";
    }

    public static class ResponseConfig {
        private String errorResponseProm = "客户端时间戳异常";
        private String errorResponseCode = "0";
        private String statusTag = "stat";
        private String msgTag = "msg";
        private String dataTag = "data";
        private Integer successResponse;
        private Integer errorResponse;
        private Integer errorToken;
        private Integer errorThirdPlatformNoBind;
        private Integer errorPwd;

        public ResponseConfig() {
            this.successResponse = ResponseCommonCallback.DEFAULT_SUCCESS_RESPONSE;
            this.errorResponse = ResponseCommonCallback.DEFAULT_ERROR_RESPONSE;
            this.errorToken = ResponseCommonCallback.DEFAULT_ERROR_TOKEN;
            this.errorThirdPlatformNoBind = ResponseCommonCallback.DEFAULT_ERROR_THIRD_PLATFORM_NO_BIND;
            this.errorPwd = ResponseCommonCallback.DEFAULT_ERROR_PWD;
        }

        public String getStatusTag() {
            return this.statusTag;
        }

        public String getMsgTag() {
            return this.msgTag;
        }

        public String getDataTag() {
            return this.dataTag;
        }

        public int getSuccessResponse() {
            return this.successResponse.intValue();
        }

        public int getErrorResponse() {
            return this.errorResponse.intValue();
        }

        public int getErrorToken() {
            return this.errorToken.intValue();
        }

        public int getErrorThirdPlatformNoBind() {
            return this.errorThirdPlatformNoBind.intValue();
        }

        public int getErrorPwd() {
            return this.errorPwd.intValue();
        }

        public String getErrorResponseProm() {
            return this.errorResponseProm;
        }

        public String getErrorResponseCode() {
            return this.errorResponseCode;
        }

        public ResponseCommonCallback.ResponseConfig setStatusTag(String statusTag) {
            this.statusTag = statusTag;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setMsgTag(String msgTag) {
            this.msgTag = msgTag;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setDataTag(String dataTag) {
            this.dataTag = dataTag;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setSuccessResponse(Integer successResponse) {
            this.successResponse = successResponse;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setErrorResponse(Integer errorResponse) {
            this.errorResponse = errorResponse;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setErrorToken(Integer errorToken) {
            this.errorToken = errorToken;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setErrorThirdPlatformNoBind(Integer errorThirdPlatformNoBind) {
            this.errorThirdPlatformNoBind = errorThirdPlatformNoBind;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setErrorPwd(Integer errorPwd) {
            this.errorPwd = errorPwd;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setErrorResponseProm(String errorResponseProm) {
            this.errorResponseProm = errorResponseProm;
            return this;
        }

        public ResponseCommonCallback.ResponseConfig setErrorResponseCode(String errorResponseCode) {
            this.errorResponseCode = errorResponseCode;
            return this;
        }

        private void build() {
            this.build(this);
        }

        private void build(ResponseCommonCallback.ResponseConfig responseConfig) {
            if(Utils.isNotEmpty(responseConfig.successResponse)) {
                ResponseCommonCallback.SUCCESS_RESPONSE = responseConfig.successResponse.intValue();
            }

            if(Utils.isNotEmpty(responseConfig.errorResponse)) {
                ResponseCommonCallback.ERROR_RESPONSE = responseConfig.errorResponse.intValue();
            }

            if(Utils.isNotEmpty(responseConfig.errorToken)) {
                ResponseCommonCallback.ERROR_TOKEN = responseConfig.errorToken.intValue();
            }

            if(Utils.isNotEmpty(responseConfig.errorThirdPlatformNoBind)) {
                ResponseCommonCallback.ERROR_THIRD_PLATFORM_NO_BIND = responseConfig.errorThirdPlatformNoBind.intValue();
            }

            if(Utils.isNotEmpty(responseConfig.errorPwd)) {
                ResponseCommonCallback.ERROR_PWD = responseConfig.errorPwd.intValue();
            }

            if(StringUtils.isNotEmpty(responseConfig.statusTag)) {
                ResponseCommonCallback.STATUS = responseConfig.statusTag;
            }

            if(StringUtils.isNotEmpty(responseConfig.dataTag)) {
                ResponseCommonCallback.DATA = responseConfig.dataTag;
            }

            if(StringUtils.isNotEmpty(responseConfig.msgTag)) {
                ResponseCommonCallback.MSG = responseConfig.msgTag;
            }

            if(StringUtils.isNotEmpty(responseConfig.errorResponseProm)) {
                ResponseCommonCallback.ERROR_RESPONSE_PROM = responseConfig.errorResponseProm;
            }

            if(StringUtils.isNotEmpty(responseConfig.errorResponseCode)) {
                ResponseCommonCallback.ERROR_RESPONSE_CODE = responseConfig.errorResponseCode;
            }

        }

        public String toString() {
            return "ResponseConfig{errorResponseProm='" + this.errorResponseProm + '\'' + ", errorResponseCode='" + this.errorResponseCode + '\'' + ", statusTag='" + this.statusTag + '\'' + ", msgTag='" + this.msgTag + '\'' + ", dataTag='" + this.dataTag + '\'' + ", successResponse=" + this.successResponse + ", errorResponse=" + this.errorResponse + ", errorToken=" + this.errorToken + ", errorThirdPlatformNoBind=" + this.errorThirdPlatformNoBind + ", errorPwd=" + this.errorPwd + '}';
        }
    }
}
