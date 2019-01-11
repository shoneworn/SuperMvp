package com.shoneworn.libcore.net.okhttputils.convert;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.os.Environment;
import android.text.TextUtils;

import com.shoneworn.libcore.net.okhttputils.callback.Callback;
import com.shoneworn.libcore.net.okhttputils.model.Progress;
import com.shoneworn.libcore.net.okhttputils.model.Progress.Action;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import com.shoneworn.libcore.net.okhttputils.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class FileConvert implements Converter<File> {
    public static final String DM_TARGET_FOLDER;
    private String folder;
    private String fileName;
    private Callback<File> callback;

    public FileConvert() {
        this((String) null);
    }

    public FileConvert(String fileName) {
        this(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER, fileName);
    }

    public FileConvert(String folder, String fileName) {
        this.folder = folder;
        this.fileName = fileName;
    }

    public void setCallback(Callback<File> callback) {
        this.callback = callback;
    }

    public File convertResponse(Response response) throws Throwable {
        String url = response.request().url().toString();
        if (TextUtils.isEmpty(this.folder)) {
            this.folder = Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER;
        }

        if (TextUtils.isEmpty(this.fileName)) {
            this.fileName = HttpUtils.getNetFileName(response, url);
        }

        File dir = new File(this.folder);
        IOUtils.createFolder(dir);
        File file = new File(dir, this.fileName);
        IOUtils.delFileOrFolder(file);
        InputStream bodyStream = null;
        byte[] buffer = new byte[8192];
        FileOutputStream fileOutputStream = null;

        try {
            ResponseBody body = response.body();
            Progress progress;
            if (body == null) {
                progress = null;
                return null;
            } else {
                bodyStream = body.byteStream();
                progress = new Progress();
                progress.totalSize = body.contentLength();
                progress.fileName = this.fileName;
                progress.filePath = file.getAbsolutePath();
                progress.status = 2;
                progress.url = url;
                progress.tag = url;
                fileOutputStream = new FileOutputStream(file);

                int len;
                while ((len = bodyStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                    if (this.callback != null) {
                        Progress.changeProgress(progress, (long) len, new Action() {
                            public void call(Progress progress) {
                                FileConvert.this.onProgress(progress);
                            }
                        });
                    }
                }

                fileOutputStream.flush();
                File var11 = file;
                return var11;
            }
        } finally {
            IOUtils.closeQuietly(bodyStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    private void onProgress(final Progress progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            public void run() {
                FileConvert.this.callback.downloadProgress(progress);
            }
        });
    }

    static {
        DM_TARGET_FOLDER = File.separator + "download" + File.separator;
    }
}
