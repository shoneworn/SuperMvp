package com.shoneworn.libcore.net.okhttputils.convert;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.widget.ImageView.ScaleType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BitmapConvert implements Converter<Bitmap> {
    private int maxWidth;
    private int maxHeight;
    private Config decodeConfig;
    private ScaleType scaleType;

    public BitmapConvert() {
        this(1000, 1000, Config.ARGB_8888, ScaleType.CENTER_INSIDE);
    }

    public BitmapConvert(int maxWidth, int maxHeight) {
        this(maxWidth, maxHeight, Config.ARGB_8888, ScaleType.CENTER_INSIDE);
    }

    public BitmapConvert(int maxWidth, int maxHeight, Config decodeConfig, ScaleType scaleType) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.decodeConfig = decodeConfig;
        this.scaleType = scaleType;
    }

    public Bitmap convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        return body == null?null:this.parse(body.bytes());
    }

    private Bitmap parse(byte[] byteArray) throws OutOfMemoryError {
        Options decodeOptions = new Options();
        Bitmap bitmap;
        if(this.maxWidth == 0 && this.maxHeight == 0) {
            decodeOptions.inPreferredConfig = this.decodeConfig;
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
        } else {
            decodeOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
            int actualWidth = decodeOptions.outWidth;
            int actualHeight = decodeOptions.outHeight;
            int desiredWidth = getResizedDimension(this.maxWidth, this.maxHeight, actualWidth, actualHeight, this.scaleType);
            int desiredHeight = getResizedDimension(this.maxHeight, this.maxWidth, actualHeight, actualWidth, this.scaleType);
            decodeOptions.inJustDecodeBounds = false;
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, decodeOptions);
            if(tempBitmap == null || tempBitmap.getWidth() <= desiredWidth && tempBitmap.getHeight() <= desiredHeight) {
                bitmap = tempBitmap;
            } else {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true);
                tempBitmap.recycle();
            }
        }

        return bitmap;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary, ScaleType scaleType) {
        if(maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        } else if(scaleType == ScaleType.FIT_XY) {
            return maxPrimary == 0?actualPrimary:maxPrimary;
        } else {
            double ratio;
            if(maxPrimary == 0) {
                ratio = (double)maxSecondary / (double)actualSecondary;
                return (int)((double)actualPrimary * ratio);
            } else if(maxSecondary == 0) {
                return maxPrimary;
            } else {
                ratio = (double)actualSecondary / (double)actualPrimary;
                int resized = maxPrimary;
                if(scaleType == ScaleType.CENTER_CROP) {
                    if((double)maxPrimary * ratio < (double)maxSecondary) {
                        resized = (int)((double)maxSecondary / ratio);
                    }

                    return resized;
                } else {
                    if((double)maxPrimary * ratio > (double)maxSecondary) {
                        resized = (int)((double)maxSecondary / ratio);
                    }

                    return resized;
                }
            }
        }
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double)actualWidth / (double)desiredWidth;
        double hr = (double)actualHeight / (double)desiredHeight;
        double ratio = Math.min(wr, hr);

        float n;
        for(n = 1.0F; (double)(n * 2.0F) <= ratio; n *= 2.0F) {
            ;
        }

        return (int)n;
    }
}
