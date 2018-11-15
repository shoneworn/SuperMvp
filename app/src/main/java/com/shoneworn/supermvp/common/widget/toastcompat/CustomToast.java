package com.shoneworn.supermvp.common.widget.toastcompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.shoneworn.supermvp.R;
import com.shoneworn.supermvp.uitls.WeakHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ttt on 2016/7/5.
 */
public class CustomToast implements IToast {

    private static WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());

    /**
     * 维护toast的队列
     */
    private static BlockingQueue<CustomToast> mQueue = new LinkedBlockingQueue<CustomToast>();

    /**
     * 原子操作：判断当前是否在读取{@linkplain #mQueue 队列}来显示toast
     */
    protected static AtomicInteger mAtomicInteger = new AtomicInteger(0);

    long mDurationMillis;
    private Context mContext;

    private boolean inTheQueue = false;

    private Toast toast;
    private Object mTN;
    private Method show;
    private Method hide;
    private WindowManager.LayoutParams params;
    private View mView;
    private Drawable background;


    /**
     * 返回系统的background
     *
     * @return
     */
    public Drawable getBackground() {
        return background;
    }


    public void setInTheQueue(boolean inTheQueue) {
        this.inTheQueue = inTheQueue;
    }

    public static CustomToast makeText(Context context, String text, long duration) {

        return new CustomToast(context).setText(text).setDuration(duration)
                .setGravity(Gravity.CENTER, 0, 0);
    }


    public static CustomToast makeView(Context context, View view, long duration) {
        return new CustomToast(context).setView(view).setDuration(duration)
                .setGravity(Gravity.CENTER, 0, 0);

    }


    public CustomToast(Context context) {
        mContext = context;

        //获取系统的background
        if (background == null) {
            Toast toastSource = Toast.makeText(context, "", Toast.LENGTH_SHORT);
            View view = toastSource.getView();
            if (view != null) {
                background = view.getBackground();
            }
        }

        if (toast == null) {
            toast = new Toast(mContext);
        }


    }

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    @Override
    public CustomToast setGravity(int gravity, int xOffset, int yOffset) {
        toast.setGravity(gravity, xOffset, yOffset);
        return this;
    }


    /**
     * set the duration  dismiss
     *
     * @param durationMillis
     * @return
     */
    @Override
    public CustomToast setDuration(long durationMillis) {

        if (durationMillis < 0) {
            mDurationMillis = 0;
        }
        if (durationMillis == Toast.LENGTH_SHORT) {
            mDurationMillis = 2000;
        } else if (durationMillis == Toast.LENGTH_LONG) {
            mDurationMillis = 3500;
        } else {
            mDurationMillis = durationMillis;
        }

        return this;
    }

    /**
     * 不能和{@link #setText(String)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param view 传入view
     * @return 自身对象
     */
    @Override
    public CustomToast setView(View view) {
        if (toast != null) {
            toast.setView(view);
        }
        return this;
    }

    @Override
    public IToast setMargin(float horizontalMargin, float verticalMargin) {
        if (toast != null) {
            toast.setMargin(horizontalMargin, verticalMargin);
        }
        return this;
    }

    /**
     * 不能和{@link #setView(View)}一起使用，要么{@link #setView(View)} 要么{@link #setView(View)}
     *
     * @param text 字符串
     * @return 自身对象
     */
    @Override
    public CustomToast setText(String text) {
        if (mContext == null || toast == null) {
            return this;
        }
        if (mView == null) {
            LayoutInflater inflate = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflate.inflate(R.layout.layout_toast_title_bottom, null);
        }
        TextView desc = (TextView) mView.findViewById(R.id.gx_framework_message);
        desc.setTextColor(Color.WHITE);
        desc.setText(text);
        toast.setView(mView);
        return this;
    }


    public View getView() {
        return toast.getView();
    }

    @Override
    public void show() {
        // 1. 将本次需要显示的toast加入到队列中
        mQueue.offer(this);
        this.setInTheQueue(true);
        initTN();
        // 2. 如果队列还没有激活，就激活队列，依次展示队列中的toast
        if (0 >= mAtomicInteger.get()) {
            mAtomicInteger.incrementAndGet();
            mHandler.post(mActivite);
        }
    }

    @Override
    public void cancel() {
        // 1. 如果队列已经处于非激活状态或者队列没有toast了，就表示队列没有toast正在展示了，直接return
        if (0 == mAtomicInteger.get() && mQueue.isEmpty()) {
            return;
        }
        // 2. 当前显示的toast是否为本次要取消的toast，如果是的话
        // 2.1 先移除之前的队列逻辑
        // 2.2 立即暂停当前显示的toast
        // 2.3 重新激活队列
        if (this.equals(mQueue.peek())) {
            mHandler.removeCallbacks(mActivite);
            mHandler.post(mHide);
            mHandler.post(mActivite);

        }
    }


    private void handleShow() {
        try {
            if (show == null) {
                initTN();
            }
            if (Build.VERSION.SDK_INT >= 25) {
                //25的时候需要穿一个IBinder对象
                show.invoke(mTN, new Object[]{null});
            } else {
                show.invoke(mTN);
            }

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    private void handleHide() {
        try {
            if (hide == null) {
                initTN();
            }

            hide.invoke(mTN);
            CustomToast poll = mQueue.poll();

            if (poll != null) {
                poll.setInTheQueue(false);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    private static void activeQueue() {
        CustomToast compatToast = mQueue.peek();
        if (compatToast == null) {
            // 如果不能从队列中获取到toast的话，那么就表示已经暂时完所有的toast了
            // 这个时候需要标记队列状态为：非激活读取中
            if (mAtomicInteger.get() > 0) {
                mAtomicInteger.decrementAndGet();
            }
        } else {

            // 如果还能从队列中获取到toast的话，那么就表示还有toast没有展示
            // 1. 展示队首的toast
            // 2. 设置一定时间后主动采取toast消失措施
            // 3. 设置展示完毕之后再次执行本逻辑，以展示下一个toast
            mHandler.post(compatToast.mShow);
            mHandler.postDelayed(compatToast.mHide, compatToast.mDurationMillis);
            mHandler.postDelayed(mActivite, compatToast.mDurationMillis);
        }
    }

    private final Runnable mShow = new Runnable() {
        @Override
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        @Override
        public void run() {
            handleHide();
        }
    };

    private final static Runnable mActivite = new Runnable() {
        @Override
        public void run() {
            activeQueue();
        }
    };


    public boolean inTheQueue() {
        return inTheQueue;

    }


    /**
     * 取消掉当前的View,然后移除掉所有的handler 的msg
     */
    public static void cancle(Context context) {
        mHandler.removeCallbacksAndMessages(null);
        CustomToast peek = mQueue.peek();
        if (peek != null) {
            peek.cancel();
        }

        mQueue.clear();


        if (mAtomicInteger.get() > 0) {
            mAtomicInteger.decrementAndGet();
        }

    }


    private void initTN() {
        try {
            if (toast == null) {
                toast = new Toast(mContext);
            }
            Field tnField = toast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(toast);

            //在7.1.多了一个IBinder的参数.
            if (Build.VERSION.SDK_INT >= 25) {
                show = mTN.getClass().getMethod("handleShow", IBinder.class);
            } else {
                show = mTN.getClass().getMethod("handleShow");
            }

            hide = mTN.getClass().getMethod("handleHide");
            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;


            /**设置动画*/
            params.windowAnimations = android.R.style.Animation_Toast;

            /**调用tn.show()之前一定要先设置mNextView*/
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //setGravity(Gravity.BOTTOM, 0, 0);
    }
}
