package com.shoneworn.libcore.net.okhttputils.request;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */
import com.shoneworn.libcore.net.okhttputils.model.HttpMethod;
import com.shoneworn.libcore.net.okhttputils.request.base.BodyRequest;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;

public class PostRequest extends BodyRequest<PostRequest> {
    public PostRequest(String url) {
        super(url);
    }

    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    public Request generateRequest(RequestBody requestBody) {
        Builder requestBuilder = this.generateRequestBuilder(requestBody);
        return requestBuilder.post(requestBody).url(this.url).tag(this.tag).build();
    }
}
