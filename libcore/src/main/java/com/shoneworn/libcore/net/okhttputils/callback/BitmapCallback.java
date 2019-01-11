package com.shoneworn.libcore.net.okhttputils.callback;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView.ScaleType;
import com.shoneworn.libcore.net.okhttputils.convert.BitmapConvert;
import okhttp3.Response;

public abstract class BitmapCallback extends AbsCallback<Bitmap> {
    private BitmapConvert convert;

    public BitmapCallback() {
        this.convert = new BitmapConvert();
    }

    public BitmapCallback(int maxWidth, int maxHeight) {
        this.convert = new BitmapConvert(maxWidth, maxHeight);
    }

    public BitmapCallback(int maxWidth, int maxHeight, Config decodeConfig, ScaleType scaleType) {
        this.convert = new BitmapConvert(maxWidth, maxHeight, decodeConfig, scaleType);
    }

    public Bitmap convertResponse(Response response) throws Throwable {
        Bitmap bitmap = this.convert.convertResponse(response);
        response.close();
        return bitmap;
    }
}
