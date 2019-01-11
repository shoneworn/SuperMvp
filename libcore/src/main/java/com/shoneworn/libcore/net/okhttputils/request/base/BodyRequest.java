package com.shoneworn.libcore.net.okhttputils.request.base;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.text.TextUtils;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams.FileWrapper;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Request.Builder;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BodyRequest<R extends BodyRequest> extends Request<R> implements HasBody<R> {
    private static final long serialVersionUID = -6459175248476927501L;
    protected transient MediaType mediaType;
    protected String content;
    protected byte[] bs;
    protected transient File file;
    protected boolean isMultipart = false;
    protected boolean isSpliceUrl = false;
    protected RequestBody requestBody;

    public BodyRequest(String url) {
        super(url);
    }

    public R isMultipart(boolean isMultipart) {
        this.isMultipart = isMultipart;
        return (R)this;
    }

    public R isSpliceUrl(boolean isSpliceUrl) {
        this.isSpliceUrl = isSpliceUrl;
        return (R)this;
    }

    public R params(String key, File file, boolean... isReplace) {
        this.params.put(key, file, isReplace);
        return (R)this;
    }

    public R params(String key, File file, String fileName, boolean... isReplace) {
        this.params.put(key, file, fileName, isReplace);
        return (R)this;
    }

    public R params(String key, File file, String fileName, MediaType contentType, boolean... isReplace) {
        this.params.put(key, file, fileName, contentType, isReplace);
        return (R)this;
    }

    public R paramsFileMap(Map<String, File> params, boolean... isReplace) {
        this.params.putFileMap(params, isReplace);
        return (R)this;
    }

    public R paramsFileList(String key, List<File> files, boolean... isReplace) {
        this.params.putFileList(key, files, isReplace);
        return (R)this;
    }

    public R paramsFileWrapperList(String key, List<FileWrapper> fileWrappers, boolean... isReplace) {
        this.params.putFileWrapperList(key, fileWrappers, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, String value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, int value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, float value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, double value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, long value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, char value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQuery(String key, boolean value, boolean... isReplace) {
        this.params.putQuery(key, value, isReplace);
        return (R)this;
    }

    public R paramsQueryStringMap(Map<String, String> params, boolean... isReplace) {
        this.params.putQueryStringMap(params, isReplace);
        return (R)this;
    }

    public R paramsQueryStringList(String key, List<String> values, boolean... isReplace) {
        this.params.putQueryStringList(key, values, isReplace);
        return (R)this;
    }

    public R upString(String string) {
        this.content = string;
        this.mediaType = HttpParams.MEDIA_TYPE_PLAIN;
        return (R)this;
    }

    public R upString(String string, MediaType mediaType) {
        this.content = string;
        this.mediaType = mediaType;
        return (R)this;
    }

    public R upJson(String json) {
        this.content = json;
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R)this;
    }

    public R upJson(JSONObject jsonObject) {
        this.content = jsonObject.toString();
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R)this;
    }

    public R upJson(JSONArray jsonArray) {
        this.content = jsonArray.toString();
        this.mediaType = HttpParams.MEDIA_TYPE_JSON;
        return (R)this;
    }

    public R upBytes(byte[] bs) {
        this.bs = bs;
        this.mediaType = HttpParams.MEDIA_TYPE_STREAM;
        return (R)this;
    }

    public R upBytes(byte[] bs, MediaType mediaType) {
        this.bs = bs;
        this.mediaType = mediaType;
        return (R)this;
    }

    public R upFile(File file) {
        this.file = file;
        this.mediaType = HttpUtils.guessMimeType(file.getName());
        return (R)this;
    }

    public R upFile(File file, MediaType mediaType) {
        this.file = file;
        this.mediaType = mediaType;
        return (R)this;
    }

    public R upRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return (R)this;
    }

    public RequestBody generateRequestBody() {
        this.url = HttpUtils.createUrlFromParams(this.baseUrl, this.isSpliceUrl, this.params);
        return this.requestBody != null?this.requestBody:(this.content != null && this.mediaType != null?RequestBody.create(this.mediaType, this.content):(this.bs != null && this.mediaType != null?RequestBody.create(this.mediaType, this.bs):(this.file != null && this.mediaType != null?RequestBody.create(this.mediaType, this.file):HttpUtils.generateMultipartRequestBody(this.params, this.isMultipart))));
    }

    protected Builder generateRequestBuilder(RequestBody requestBody) {
        try {
            this.headers("Content-Length", String.valueOf(requestBody.contentLength()));
        } catch (IOException var3) {
            OkLogger.printStackTrace(var3);
        }

        Builder requestBuilder = new Builder();
        return HttpUtils.appendHeaders(requestBuilder, this.headers);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.mediaType == null?"":this.mediaType.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String mediaTypeString = (String)in.readObject();
        if(!TextUtils.isEmpty(mediaTypeString)) {
            this.mediaType = MediaType.parse(mediaTypeString);
        }

    }
}
