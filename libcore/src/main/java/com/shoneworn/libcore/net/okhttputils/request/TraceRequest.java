package com.shoneworn.libcore.net.okhttputils.request;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
import com.shoneworn.libcore.net.okhttputils.model.HttpMethod;
import com.shoneworn.libcore.net.okhttputils.request.base.NoBodyRequest;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;

public class TraceRequest extends NoBodyRequest<TraceRequest> {
    public TraceRequest(String url) {
        super(url);
    }

    public HttpMethod getMethod() {
        return HttpMethod.TRACE;
    }

    public Request generateRequest(RequestBody requestBody) {
        Builder requestBuilder = this.generateRequestBuilder(requestBody);
        return requestBuilder.method("TRACE", requestBody).url(this.url).tag(this.tag).build();
    }
}
