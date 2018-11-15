package com.shoneworn.supermvp.common.widget.toastcompat.utils;

import android.content.Context;
import android.view.View;

import com.shoneworn.libcore.utils.Utils;


/**
 * 提示信息的管理
 */

public class PromptUtils {

    public static void showToast(Context ctx, String msg) {
        if (Utils.isEmpty(ctx)) {
            return;
        }
        showToast(ctx, false, msg);
    }

    public static void showToastBottom(Context ctx, String msg) {
        if (Utils.isEmpty(ctx)) {
            return;
        }
        _ToastUtils.getInstance().showToastWithTitleBottom(ctx.getApplicationContext(), msg);
    }

    public static void showToastBottom(Context ctx, int resId) {
        if (Utils.isEmpty(ctx) || resId == View.NO_ID) {
            return;
        }
        CharSequence msg = ctx.getString(resId);
        _ToastUtils.getInstance().showToastWithTitleBottom(ctx.getApplicationContext(), msg);
    }

    public static void showToast(Context ctx, boolean isStyleRectangle, String msg) {
        if (Utils.isEmpty(ctx)) {
            return;
        }
        if (isStyleRectangle) {
            _ToastUtils.getInstance().showToastWithTitleRectangle(ctx.getApplicationContext(), msg);
        } else {
            _ToastUtils.getInstance().showToastWithTitle(ctx.getApplicationContext(), msg);
        }
    }

    public static void showToast(Context ctx, Integer resId, CharSequence charSequence) {
        if (Utils.isEmpty(ctx) || resId == -1) {
            return;
        }
        showToast(ctx.getApplicationContext(), false, resId, charSequence);
    }

    public static void showToast(Context ctx, boolean isStyleRectangle, Integer resId, CharSequence charSequence) {
        if (Utils.isEmpty(ctx) || resId == View.NO_ID) {
            return;
        }
        if (isStyleRectangle) {
            _ToastUtils.getInstance().showToastWithPicAndTitleRectangle(ctx.getApplicationContext(), resId, charSequence);
        } else {
            _ToastUtils.getInstance().showToastWithPicAndTitle(ctx, resId, charSequence);
        }
    }

    public static void showToast(Context ctx, int resId) {
        if (Utils.isEmpty(ctx) || resId == View.NO_ID) {
            return;
        }
        showToast(ctx.getApplicationContext(), false, resId);
    }

    public static void showToast(Context ctx, boolean isStyleRectangle, int resId) {
        if (Utils.isEmpty(ctx) || resId == View.NO_ID) {
            return;
        }
        _ToastUtils.getInstance().showToastWithPic(ctx.getApplicationContext(), resId);
    }


}
