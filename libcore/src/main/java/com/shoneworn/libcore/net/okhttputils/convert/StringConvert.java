package com.shoneworn.libcore.net.okhttputils.convert;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StringConvert implements Converter<String> {
    public StringConvert() {
    }

    public String convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        return body == null?null:body.string();
    }
}
