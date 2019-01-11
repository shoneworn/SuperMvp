package com.shoneworn.libcore.net.okhttputils.request.base;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;

public abstract class NoBodyRequest<R extends NoBodyRequest> extends Request<R> {
    private static final long serialVersionUID = 1200621102761691196L;

    public NoBodyRequest(String url) {
        super(url);
    }

    public RequestBody generateRequestBody() {
        return null;
    }

    protected Builder generateRequestBuilder(RequestBody requestBody) {
        this.url = HttpUtils.createUrlFromParams(this.baseUrl, true, this.params);
        Builder requestBuilder = new Builder();
        return HttpUtils.appendHeaders(requestBuilder, this.headers);
    }
}
