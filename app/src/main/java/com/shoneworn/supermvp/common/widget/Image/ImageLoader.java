package com.shoneworn.supermvp.common.widget.Image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.shoneworn.libcore.utils.MD5Coder;
import com.shoneworn.libcore.utils.Utils;
import com.shoneworn.supermvp.uitls.LogUtils;
import com.shoneworn.supermvp.uitls.WeakHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by chenxiangxiang on 2018/11/16.
 */

public class ImageLoader {


    final static WeakHandler handler = new WeakHandler(Looper.getMainLooper());
    public static final String GIF = ".gif";

    /**
     * Created by AnWen on 2017/5/24;
     * 注释：获得对应的策略。
     */
    public static DiskCacheStrategy getDiskCacheStrategy(String url) {
        DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.ALL;
        if (url != null && url.endsWith(GIF)) {
            diskCacheStrategy = DiskCacheStrategy.SOURCE;
        }
        return diskCacheStrategy;
    }

    public static void with(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withNoCache(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public static void with(Context context, ImageView imageView, Object url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void withGif(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .asGif()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void withGifListener(Context context, ImageView imageView, String url, RequestListener requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .asGif()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(requestListener)
                .into(imageView);
    }

    public static void withResource(Context context, ImageView imageView, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void withGif(Context context, ImageView imageView, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(resourceId)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void withGIFLocal(Context context, ImageView imageView, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(resourceId)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public static void with(Context context, ImageView imageView, int resourceId, BitmapTransformation bitmapTransformation) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(resourceId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(bitmapTransformation)
                .into(imageView);

    }

    public static void with(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(diskCacheStrategy)
                .dontAnimate()
                .into(imageView);
    }

    public static void with2Tag(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        if (url.equals(imageView.getTag())) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .dontAnimate()
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withCenterCropForSource(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withCenterCrop(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withCenterCropForSource(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withCenterCrop(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withCenterCrop(Context context, ImageView imageView, String url, Drawable drawable) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .placeholder(drawable)
                .error(drawable)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }


    public static void withFitCenterForSource(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withFitCenter(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withFitCenterForSource(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    /**
     * 图片加载成功失败的监听回调
     *
     * @param requestListener
     */
    public static void withFitCenterForSource(Context context, ImageView imageView, String url, RequestListener requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .diskCacheStrategy(diskCacheStrategy)
                .listener(requestListener)
                .into(imageView);

    }

    /**
     * 图片加载成功失败的监听回调 ,跟着activity的生命周期走
     *
     * @param requestListener
     */
    public static void withListener(Context context, ImageView imageView, String url, RequestListener requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(diskCacheStrategy)
                .listener(requestListener)
                .into(imageView);

    }

    public static void withListenerNoCache(Context context, ImageView imageView, String url, RequestListener<String, GlideDrawable> requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        LogUtils.w("share:" + url);
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .listener(requestListener)
                .into(imageView);

    }

    public static void withAsBitmapListener(Context context, String url, boolean readCache, BitmapImageViewTarget requestListener) {
        if (!isGlideEnable(context)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        BitmapTypeRequest<String> stringBitmapTypeRequest = Glide.with(context.getApplicationContext())
                .load(url)
                .asBitmap();
        if (readCache) {
            stringBitmapTypeRequest
                    .diskCacheStrategy(diskCacheStrategy);
        } else {
            stringBitmapTypeRequest
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//禁用磁盘缓存
                    .skipMemoryCache(true);//跳过内存缓存
        }
        stringBitmapTypeRequest.into(requestListener);
    }

    /**
     * 图片加载成功失败的监听回调
     */
    public static void withCenterCrop(Context context, ImageView imageView, String url, RequestListener requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCacheStrategy)
                .listener(requestListener)
                .into(imageView);

    }

    public static void withFitCenter(Context context, ImageView imageView, String url, int resourceId) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .placeholder(resourceId)
                .error(resourceId)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    public static void withFitCenter(Context context, ImageView imageView, String url, Drawable drawable) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .fitCenter()
                .placeholder(drawable)
                .error(drawable)
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }


    public static void aysnWithFitCenter(final Context context, final ImageView imageView, final String url, final int resourceId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (!isGlideEnable(context, imageView)) {
                    return;
                }
                withFitCenter(context.getApplicationContext(), imageView, url, resourceId);
            }
        };
        handler.post(r);
    }

    public static void aysnWithFitCenter(final Context context, final ImageView imageView, final String url, final Drawable drawable) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (!isGlideEnable(context, imageView)) {
                    return;
                }
                withFitCenter(context.getApplicationContext(), imageView, url, drawable);
            }
        };
        handler.post(r);
    }


    public static void aysnWithCenterCrop(final Context context, final ImageView imageView, final String url, final int resourceId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (!isGlideEnable(context, imageView)) {
                    return;
                }
                withCenterCrop(context.getApplicationContext(), imageView, url, resourceId);
            }
        };
        handler.post(r);
    }

    public static void aysnWithCenterCrop(final Context context, final ImageView imageView, final String url, final Drawable drawable) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (!isGlideEnable(context, imageView)) {
                    return;
                }
                withCenterCrop(context.getApplicationContext(), imageView, url, drawable);
            }
        };
        handler.post(r);
    }


    /**
     * 图片高斯模糊
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius
     */
    public static void withBlurImage(Context context, ImageView imageView, String url, int radius) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .bitmapTransform(new ImageBlurTransformation(context, radius))
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }


    /**
     * 通过url获取bitmap
     */
    public static void withBitmap(Context context, String url, SimpleTarget<Bitmap> target) {
        if (!isGlideEnable(context)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext()).load(url).asBitmap().diskCacheStrategy(diskCacheStrategy).into(target);
    }


    /**
     * 开屏页广告图片加载成功失败的监听回调
     *
     * @param context
     * @param imageView
     * @param url
     * @param requestListener
     */
    public static void withCenterCropForSource(Context context, ImageView imageView, String url, RequestListener requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCacheStrategy)
                .listener(requestListener)
                .into(imageView);
    }

    /**
     * 登录页面邀请好友大奖提示图片加载成功失败的监听回调
     * 不缓存内存和硬盘
     *
     * @param context
     * @param imageView
     * @param url
     * @param requestListener
     */
    public static void withInviteHintPic(Context context, ImageView imageView, String url, RequestListener requestListener) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(requestListener)
                .into(imageView);
    }

    /**
     * 下载图片并监听
     *
     * @param context
     * @param url
     * @param requestListener
     */
    public static void withListener(Context context, String url, RequestListener requestListener) {
        if (!isGlideEnable(context)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(diskCacheStrategy)
                .listener(requestListener)
                .into(new SimpleTarget() {
                    @Override
                    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                    }
                });

    }

    public static void withSmallGift(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(diskCacheStrategy)
                .into(imageView);
    }

    /**
     * 仅仅缓存图片
     */
    public static void justCacheImage(Context context, String url) {
        if (!isGlideEnable(context)) {
            return;
        }
        DiskCacheStrategy diskCacheStrategy = getDiskCacheStrategy(url);
        Glide.with(context.getApplicationContext())
                .load(url)
                .diskCacheStrategy(diskCacheStrategy)
                .into(new SampleTarget());
    }

    public static void downloadOnlyImage(Context context, String url, SimpleTarget<File> target) {
        if (!isGlideEnable(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .downloadOnly(target);
    }

    //只能在子线程调用
    public static File downloadOnlyImage1(Context context, String url) {
        if (!isGlideEnable(context)) {
            return null;
        }
        FutureTarget<File> target = Glide.with(context)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        try {
            return target.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void moveLocalImage(Context context, final String localImagePath, final String savePath, final String saveFileName, final ImageSaveListener listener) {
        if (!isGlideEnable(context)) {
            return;
        }
        InputStream fromStream = null;
        OutputStream toStream = null;
        try {
            File localFile = new File(localImagePath);
            if (!localFile.exists() || !localFile.isFile()) {
                LogUtils.e("moveLocalImage-!localFile.exists() || !localFile.isFile()");
                listener.onSaveFail();
                return;
            }
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = saveFileName;
            if (!saveFileName.contains("."))
                fileName = saveFileName + localFile.getAbsolutePath().subSequence(localFile.getAbsolutePath().lastIndexOf("."), localFile.getAbsolutePath().length()).toString();
            File file = new File(dir, fileName);
            fromStream = new FileInputStream(localFile);
            toStream = new FileOutputStream(file);
            byte length[] = new byte[1024];
            int count;
            while ((count = fromStream.read(length)) > 0) {
                toStream.write(length, 0, count);
            }
            //用广播通知相册进行更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            listener.onSaveSuccess(file);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("moveLocalImage-downloadOnlyImage>>" + e.getMessage());
            listener.onSaveFail();
        } finally {
            try {
                if (fromStream != null) {
                    fromStream.close();
                    fromStream = null;
                }
                if (toStream != null) {
                    toStream.close();
                    toStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isLocalImage(String url) {
        return !url.startsWith("http://") && !url.startsWith("https://");
    }

    public static void saveImage(final Context context, final String url, final String savePath, final String saveFileName, final boolean fileNameMd5, final ImageSaveListener listener) {
        if (!isGlideEnable(context)) {
            return;
        }
        if (!CommonUtils.isSDCardExsit() || TextUtils.isEmpty(url)) {
            LogUtils.e("saveImage-!CommonUtils.isSDCardExsit() || TextUtils.isEmpty(url)");
            listener.onSaveFail();
            return;
        }

        if (isLocalImage(url)) {
            //本地图片
            moveLocalImage(context, url, savePath, saveFileName, listener);
            return;
        }

        downloadOnlyImage(context, url, new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File cacheFile, GlideAnimation<? super File> glideAnimation) {
                InputStream fromStream = null;
                OutputStream toStream = null;
                try {
                    if (cacheFile == null || !cacheFile.exists()) {
                        LogUtils.e("saveImage-cacheFile == null || !cacheFile.exists()");
                        listener.onSaveFail();
                        return;
                    }
                    File dir = new File(savePath);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File file = new File(dir, fileNameMd5 ? MD5Coder.encode(saveFileName) : saveFileName);
                    fromStream = new FileInputStream(cacheFile);
                    toStream = new FileOutputStream(file);
                    byte length[] = new byte[1024];
                    int count;
                    while ((count = fromStream.read(length)) > 0) {
                        toStream.write(length, 0, count);
                    }
                    //用广播通知相册进行更新相册
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    context.sendBroadcast(intent);
                    listener.onSaveSuccess(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e("saveImage-downloadOnlyImage>>" + e.getMessage());
                    listener.onSaveFail();
                } finally {
                    try {
                        if (fromStream != null) {
                            fromStream.close();
                            fromStream = null;
                        }
                        if (toStream != null) {
                            toStream.close();
                            toStream = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                LogUtils.e("saveImage-onLoadFailed>>" + e.getMessage());
                listener.onSaveFail();
            }
        });
    }

    public static void clearImageDiskCache(final Context context) {
        if (!isGlideEnable(context)) {
            return;
        }
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void clearImageMemoryCache(Context context) {
        if (!isGlideEnable(context)) {
            return;
        }
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    public static void lowMemory(Context context) {
        if (!isGlideEnable(context)) {
            return;
        }
        Glide.with(context).onLowMemory();
    }

    /**
     * 圆角裁剪
     *
     * @param context
     * @param url
     * @param view
     * @param resourceId
     */
    public static void loadRoundedCorners(Context context, ImageView view, String url, int resourceId, int radius, RoundedCornersTransformation.CornerType cornerType) {
        if (!isGlideEnable(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, radius, 0, cornerType))
                //.thumbnail(0.1f)//请求给定系数的缩略图
                .diskCacheStrategy(DiskCacheStrategy.ALL)//设置缓存策略。DiskCacheStrategy.SOURCE：缓存原始数据，DiskCacheStrategy.RESULT：缓存变换(如缩放、裁剪等)后的资源数据，DiskCacheStrategy.NONE：什么都不缓存，DiskCacheStrategy.ALL：缓存SOURC和RESULT。默认采用DiskCacheStrategy.RESULT策略，对于download only操作要使用DiskCacheStrategy.SOURCE。
                .placeholder(resourceId)//设置资源加载过程中的占位Drawable。
                .fallback(resourceId)//设置model为空时要显示的Drawable。如果没设置fallback，model为空时将显示error的Drawable，如果error的Drawable也没设置，就显示placeholder的Drawable。
                .error(resourceId)//设置load失败时显示的Drawable。
                .dontAnimate()
                .into(view);
    }

    public static void loadRoundedCornersNoCache(Context context, ImageView view, String url, int resourceId, int radius, RoundedCornersTransformation.CornerType cornerType) {
        if (!isGlideEnable(context)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, radius, 0, cornerType))
                //.thumbnail(0.1f)//请求给定系数的缩略图
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//设置缓存策略。DiskCacheStrategy.SOURCE：缓存原始数据，DiskCacheStrategy.RESULT：缓存变换(如缩放、裁剪等)后的资源数据，DiskCacheStrategy.NONE：什么都不缓存，DiskCacheStrategy.ALL：缓存SOURC和RESULT。默认采用DiskCacheStrategy.RESULT策略，对于download only操作要使用DiskCacheStrategy.SOURCE。
                .placeholder(resourceId)//设置资源加载过程中的占位Drawable。
                .fallback(resourceId)//设置model为空时要显示的Drawable。如果没设置fallback，model为空时将显示error的Drawable，如果error的Drawable也没设置，就显示placeholder的Drawable。
                .error(resourceId)//设置load失败时显示的Drawable。
                .dontAnimate()
                .into(view);
    }

    private static boolean isGlideEnable(Context context, ImageView imageView) {
        if (Utils.isEmpty(context) || Utils.isEmpty(imageView)) {
            return false;
        }
        return true;
    }

    private static boolean isGlideEnable(Context context) {
        return !Utils.isEmpty(context);
    }

    private static String signatureInOrder = System.currentTimeMillis() + "";

    public static void initGlideSignature(String obj) {
        signatureInOrder = obj;
    }

    /**
     * 通过添加签名的方式更新图片缓存
     * 注意：Glide 版本若更新，StringSignature将被废弃，替换成signature(new ObjectKey(object))
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void loadImageWithSignature(Context context, ImageView imageView, String url) {
        if (!isGlideEnable(context, imageView)) {
            return;
        }
        Glide.with(context.getApplicationContext())
                .load(url)
                .signature(new StringSignature(signatureInOrder))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

}
