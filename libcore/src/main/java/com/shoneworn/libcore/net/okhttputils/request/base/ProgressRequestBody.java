package com.shoneworn.libcore.net.okhttputils.request.base;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Progress;
import com.shoneworn.libcore.net.okhttputils.model.Progress.Action;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody<T> extends RequestBody {
    private RequestBody requestBody;
    private Callback<T> callback;
    private ProgressRequestBody.UploadInterceptor interceptor;

    ProgressRequestBody(RequestBody requestBody, Callback<T> callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }

    public RequestBody getRawRequestBody() {
        return this.requestBody;
    }

    public MediaType contentType() {
        return this.requestBody.contentType();
    }

    public long contentLength() {
        try {
            return this.requestBody.contentLength();
        } catch (IOException var2) {
            OkLogger.printStackTrace(var2);
            return -1L;
        }
    }

    public void writeTo(BufferedSink sink) throws IOException {
        ProgressRequestBody<T>.CountingSink countingSink = new ProgressRequestBody.CountingSink(sink);
        BufferedSink bufferedSink = Okio.buffer(countingSink);
        this.requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private void onProgress(final Progress progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            public void run() {
                if(ProgressRequestBody.this.callback != null) {
                    ProgressRequestBody.this.callback.uploadProgress(progress);
                }

            }
        });
    }

    public void setInterceptor(ProgressRequestBody.UploadInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public interface UploadInterceptor {
        void uploadProgress(Progress var1);
    }

    private final class CountingSink extends ForwardingSink {
        private Progress progress = new Progress();

        CountingSink(Sink delegate) {
            super(delegate);
            this.progress.totalSize = ProgressRequestBody.this.contentLength();
        }

        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            Progress.changeProgress(this.progress, byteCount, new Action() {
                public void call(Progress progress) {
                    if(ProgressRequestBody.this.interceptor != null) {
                        ProgressRequestBody.this.interceptor.uploadProgress(progress);
                    } else {
                        ProgressRequestBody.this.onProgress(progress);
                    }

                }
            });
        }
    }
}
