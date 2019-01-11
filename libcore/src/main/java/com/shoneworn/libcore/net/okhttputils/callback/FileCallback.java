package com.shoneworn.libcore.net.okhttputils.callback;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.convert.FileConvert;
import java.io.File;
import okhttp3.Response;

public abstract class FileCallback extends AbsCallback<File> {
    private FileConvert convert;

    public FileCallback() {
        this((String)null);
    }

    public FileCallback(String destFileName) {
        this((String)null, destFileName);
    }

    public FileCallback(String destFileDir, String destFileName) {
        this.convert = new FileConvert(destFileDir, destFileName);
        this.convert.setCallback(this);
    }

    public File convertResponse(Response response) throws Throwable {
        File file = this.convert.convertResponse(response);
        response.close();
        return file;
    }
}
