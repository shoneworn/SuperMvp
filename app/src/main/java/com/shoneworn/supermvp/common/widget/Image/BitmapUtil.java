package com.shoneworn.supermvp.common.widget.Image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;


import com.shoneworn.supermvp.uitls.NetworkUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BitmapUtil {
    private static final String TAG = "BtimapUtil";

    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/duobao/";


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp == null) {
            return null;
        }
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }


    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    /**
     * 注释：下面的是工具方法。
     * by AnWen on 2016/10/28 11:08;
     */
    public static Bitmap getSmallBitmapInUse(Context context, Uri originalUri, int width, int height) throws FileNotFoundException {
        File file = null;
        file = getFileFromMediaUri(context, originalUri);
        Bitmap photoBmp = getSmallBitmap(context, Uri.fromFile(file), width, height);
        int degree = getBitmapDegree(file.getAbsolutePath());
        /**
         * 把图片旋转为正的方向
         */
        Bitmap newbitmap = rotateBitmapByDegree(photoBmp, degree);
        return newbitmap;
    }

    public static Bitmap getSmallBitmap(Context context, Uri uri, int width, int height) throws FileNotFoundException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.decodeStream(input, null, options);
//		/external/images/media/2053 不能直接decodeFile.

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, width, height);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(uri.getPath(), options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) maxHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) maxWidth);
            }

            float totalPixels = (float) (width * height);

            for (float maxTotalPixels = (float) (maxWidth * maxHeight * 2); totalPixels / (float) (inSampleSize * inSampleSize) > maxTotalPixels; ++inSampleSize) {
                ;
            }
        }

        return inSampleSize;
    }

    /**
     * 通过Uri获取文件
     *
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if (uri.getScheme().toString().compareTo("content") == 0) {
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0) {
            return new File(uri.toString().replace("file://", ""));
        }
        return null;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static Bitmap getNetBitmap(String strUrl, Context context) {
        Bitmap bitmap = null;
            if (NetworkUtils.isNetworkAvailable(context)) {
            try {
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url
                        .openConnection();
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
        return bitmap;
    }

    /**
     * 由原Bitmap生成指定大小的Bitmap
     *
     * @param bitmap
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        float scaleX = targetWidth * 1.0f / bitmap.getWidth();
        float scaleY = targetHeight * 1.0f / bitmap.getHeight();
        return resizeBitmap(bitmap, scaleX, scaleY);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, float scale) {
        return resizeBitmap(bitmap, scale, scale);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth) {
        float scaleX = targetWidth * 1.0f / bitmap.getWidth();
        return resizeBitmap(bitmap, scaleX, scaleX);
    }

    /**
     * 由原Bitmap生成指定大小的Bitmap(保持缩放比裁剪)
     *
     * @param bitmap
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap recropBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        Matrix matrix = new Matrix();
        Bitmap result;
        float scaleX = targetWidth * 1.0f / bitmap.getWidth();
        float scaleY = targetHeight * 1.0f / bitmap.getHeight();
        float scale = scaleX > scaleY ? scaleX : scaleY;
        matrix.setScale(scale, scale);
        Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        if (scale == scaleX) {
            result = Bitmap.createBitmap(temp, 0, (temp.getHeight() - targetHeight) / 2, targetWidth, targetHeight, null, false);
        } else {
            result = Bitmap.createBitmap(temp, (temp.getWidth() - targetWidth) / 2, 0, targetWidth, targetHeight, null, false);
        }
        if (!temp.isRecycled()) {
            temp.recycle();
        }
        return result;
    }

    //
    public static Bitmap resizeBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static String bitMaptoFile(Bitmap bitmap, String dir, String fileName) {
        try {
            File file = new File(dir + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
