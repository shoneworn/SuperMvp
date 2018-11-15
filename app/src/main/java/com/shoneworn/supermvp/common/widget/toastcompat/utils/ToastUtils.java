package com.shoneworn.supermvp.common.widget.toastcompat.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shoneworn.libcore.utils.StringUtils;
import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.common.widget.toastcompat.ToastCompat;


class ToastUtils {
    public static final boolean isShowPic = true;

    public static void showToastWithPicAndTitle(Context ctx, Integer resId,
                                                CharSequence charSequence) {
        showToastWithPicAndTitle(ctx, false, resId, charSequence);
    }

    public static void showToastWithPicAndTitle(Context ctx, boolean isStyleRectangle, Integer resId,
                                                CharSequence charSequence) {
        if (ctx == null || resId == null || StringUtils.isEmpty(charSequence))
            return;
        int layoutId = isStyleRectangle ? R.layout.toast_rectangle : R.layout.toast_with_title_pic;
        View toastRoot = View.inflate(ctx, layoutId, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
        iv_prom.setVisibility(isShowPic ? View.VISIBLE : View.GONE);
        message.setText(charSequence.toString().trim());
        iv_prom.setBackgroundResource(resId);
        ToastCompat toastStart = new ToastCompat(ctx);
        toastStart.setGravity(isStyleRectangle ? Gravity.BOTTOM : Gravity.CENTER, 0,
                isStyleRectangle ? 150 : 0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }


    public static void showToastWithTitle(Context ctx, CharSequence charSequence) {
        showToastWithTitle(ctx, false, charSequence);
    }

    public static void showToastWithTitle(Context ctx, boolean isStyleRectangle, CharSequence charSequence) {
        if (ctx == null || StringUtils.isEmpty(charSequence))
            return;
        int layoutId = isStyleRectangle ? R.layout.toast_rectangle : R.layout.toast_title;
        View toastRoot = View.inflate(ctx, layoutId, null);
        if (isStyleRectangle) {
            ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
            iv_prom.setVisibility(View.GONE);
        }
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        message.setText(charSequence.toString().trim());
        ToastCompat toastStart = new ToastCompat(ctx);
        toastStart.setGravity(isStyleRectangle ? Gravity.BOTTOM : Gravity.CENTER, 0,
                isStyleRectangle ? 150 : 0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();

    }

    public static void showToastWithTitleBottom(Context ctx, CharSequence charSequence) {
        if (ctx == null || StringUtils.isEmpty(charSequence))
            return;
        View toastRoot = View.inflate(ctx, R.layout.toast_title_bottom, null);
        TextView message = (TextView) toastRoot.findViewById(R.id.message);
        message.setText(charSequence.toString().trim());
        ToastCompat toastStart = new ToastCompat(ctx);
        toastStart.setGravity(Gravity.BOTTOM, 0, 0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();

    }

    public static void showToastWithPic(Context ctx, Integer resId) {
        if (ctx == null || resId == null)
            return;
        //if(isStyleRectangle){
        //   return;
        //}
        View toastRoot = View.inflate(ctx, R.layout.toast_pic, null);
        ImageView iv_prom = (ImageView) toastRoot.findViewById(R.id.iv_prom);
        iv_prom.setBackgroundResource(resId);
        ToastCompat toastStart = new ToastCompat(ctx);
        toastStart.setGravity(Gravity.CENTER, 0, 0);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }


}
