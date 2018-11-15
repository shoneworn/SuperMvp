package com.shoneworn.supermvp.common.widget.toastcompat.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shoneworn.libcore.utils.StringUtils;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.common.widget.toastcompat.ToastCompat;
import com.shoneworn.supermvp.uitls.DensityUtil;


/**
 * Created by admin on 2018/3/24.
 */

public class _ToastUtils {
    private static _ToastUtils toastUtils;
    public static final boolean isShowPic = true;
    private ToastCompat toastWithPicAndTitle;
    private ToastCompat toastWithTitle;
    private ToastCompat toastWithTitleBottom;
    private ToastCompat toastWithPic;
    private ToastCompat toastWithPicAndTitleRectangle;
    private ToastCompat toastWithTitleRectangle;

    private _ToastUtils() {
    }

    public static _ToastUtils getInstance() {
        if (null == toastUtils) {
            synchronized (_ToastUtils.class) {
                if (null == toastUtils) {
                    toastUtils = new _ToastUtils();
                }
            }
        }
        return toastUtils;
    }

    public void showToastWithPicAndTitle(Context ctx, Integer resId,
                                         CharSequence charSequence) {
        if (ctx == null || resId == null || resId == View.NO_ID || StringUtils.isEmpty(charSequence))
            return;
        int layoutId = R.layout.toast_with_title_pic;
        if (toastWithPicAndTitle != null) {
            toastWithPicAndTitle.cancel();
            toastWithPicAndTitle = null;
        }
        View toastRoot = View.inflate(ctx, layoutId, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
        iv_prom.setVisibility(isShowPic ? View.VISIBLE : View.GONE);
        message.setText(StringUtils.trimToEmpty(charSequence.toString()));
        iv_prom.setBackgroundResource(resId);
        toastWithPicAndTitle = new ToastCompat(ctx);
        toastWithPicAndTitle.setTitleView(message);
        toastWithPicAndTitle.setTitleView(message);
        toastWithPicAndTitle.setGravity(Gravity.CENTER, 0,
                0);
        toastWithPicAndTitle.setDuration(Toast.LENGTH_SHORT);
        toastWithPicAndTitle.setView(toastRoot);

        toastWithPicAndTitle.show();
    }

    public void showToastWithPicAndTitleRectangle(Context ctx, Integer resId,
                                                  CharSequence charSequence) {
        if (ctx == null || resId == null || resId == View.NO_ID || StringUtils.isEmpty(charSequence))
            return;

        if (toastWithPicAndTitleRectangle != null) {
            toastWithPicAndTitleRectangle.cancel();
            toastWithPicAndTitleRectangle = null;
        }
        View toastRoot = View.inflate(ctx, R.layout.toast_rectangle, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
        iv_prom.setVisibility(isShowPic ? View.VISIBLE : View.GONE);
        message.setText(StringUtils.trimToEmpty(charSequence.toString()));
        iv_prom.setBackgroundResource(resId);
        toastWithPicAndTitle.setTitleView(message);
        toastWithPicAndTitleRectangle = new ToastCompat(ctx);
        toastWithPicAndTitleRectangle.setView(toastRoot);

        toastWithPicAndTitleRectangle.setGravity(Gravity.BOTTOM, 0, 150);
        toastWithPicAndTitleRectangle.setDuration(Toast.LENGTH_SHORT);
        toastWithPicAndTitleRectangle.show();
    }

    public void showToastWithTitle(Context ctx, CharSequence charSequence) {
        if (ctx == null || StringUtils.isEmpty(charSequence))
            return;
        boolean isStyleRectangle = false;

        if (toastWithTitle != null) {
            toastWithTitle.cancel();
            toastWithTitle = null;
        }
        int layoutId = R.layout.toast_title;
        View toastRoot = View.inflate(ctx, layoutId, null);
        if (isStyleRectangle) {
            ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
            iv_prom.setVisibility(View.GONE);
        }
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        message.setText(StringUtils.trimToEmpty(charSequence.toString()));
        toastWithTitle = new ToastCompat(ctx);
        toastWithTitle.setTitleView(message);
        toastWithTitle.setGravity(isStyleRectangle ? Gravity.BOTTOM : Gravity.CENTER, 0, isStyleRectangle ? 150 : 0);
        toastWithTitle.setDuration(Toast.LENGTH_SHORT);
        toastWithTitle.setView(toastRoot);

        toastWithTitle.show();
    }

    public void showToastWithTitleRectangle(Context ctx, CharSequence charSequence) {
        if (ctx == null || StringUtils.isEmpty(charSequence))
            return;
        boolean isStyleRectangle = true;

        if (toastWithTitleRectangle != null) {
            toastWithTitleRectangle.cancel();
            toastWithTitleRectangle = null;
        }
        int layoutId = R.layout.toast_rectangle;
        View toastRoot = View.inflate(ctx, layoutId, null);
        if (isStyleRectangle) {
            ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
            iv_prom.setVisibility(View.GONE);
        }
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        message.setText(StringUtils.trimToEmpty(charSequence.toString()));
        toastWithTitleRectangle = new ToastCompat(ctx);
        toastWithTitleRectangle.setTitleView(message);

        toastWithTitleRectangle.setView(toastRoot);
        toastWithTitleRectangle.setGravity(isStyleRectangle ? Gravity.BOTTOM : Gravity.CENTER, 0, isStyleRectangle ? 150 : 0);
        toastWithTitleRectangle.setDuration(Toast.LENGTH_SHORT);
        toastWithTitleRectangle.show();
    }

    public void showToastWithTitleBottom(Context ctx, CharSequence charSequence) {
        if (ctx == null || StringUtils.isEmpty(charSequence))
            return;
//        ctx.setTheme(DFTTSdkNews.getInstance().getThemeResId());
        if (toastWithTitleBottom != null) {
            toastWithTitleBottom.cancel();
            toastWithTitleBottom = null;
        }

        View toastRoot = View.inflate(ctx, R.layout.toast_title_bottom, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        message.setText(StringUtils.trimToEmpty(charSequence.toString()));
//        message.setTextColor(ThemeUtils.getColorResIdFromAttr(ctx, R.attr.dftt_toast_txt_color));
//        ThemeUtils.changeViewColor(ctx,message,R.attr.dftt_toast_txt_color);
        message.setTextColor(Color.WHITE);
        toastWithTitleBottom = new ToastCompat(ctx);
        toastWithTitleBottom.setTitleView(message);
        toastWithTitleBottom.setView(toastRoot);
        toastWithTitleBottom.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(150));
        toastWithTitleBottom.setDuration(Toast.LENGTH_SHORT);

        toastWithTitleBottom.show();
    }

    public void showToastWithPic(Context ctx, Integer resId) {
        if (ctx == null || resId == null || resId == View.NO_ID)
            return;
        if (null != toastWithPic) {
            toastWithPic.cancel();
            toastWithPic = null;
        }
        View toastRoot = View.inflate(ctx, R.layout.toast_pic, null);
        ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
        iv_prom.setBackgroundResource(resId);
        toastWithPic = new ToastCompat(ctx);
        toastWithPic.setGravity(Gravity.CENTER, 0, 0);
        toastWithPic.setDuration(Toast.LENGTH_SHORT);
        toastWithPic.setView(toastRoot);

        toastWithPic.show();
    }

}
