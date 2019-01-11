package com.shoneworn.libcore.net.okhttputils.callback;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
import com.shoneworn.libcore.net.okhttputils.convert.StringConvert;
import okhttp3.Response;

public abstract class StringCallback extends AbsCallback<String> {
    private StringConvert convert = new StringConvert();

    public StringCallback() {
    }

    public String convertResponse(Response response) throws Throwable {
        String s = this.convert.convertResponse(response);
        response.close();
        return s;
    }
}