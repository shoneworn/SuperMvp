package com.shoneworn.libcore.infrastruction.model;

import android.os.Looper;

import com.shoneworn.libcore.utils.WeakHandler;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chenxiangxiang on 2018/11/19.
 */

class BackThread extends Thread {
    private WeakHandler handler;
    private Queue<Runnable> queue = new LinkedBlockingQueue();

    BackThread() {
    }

    public void run() {
        Looper.prepare();
        this.handler = new WeakHandler();
        Iterator var1 = this.queue.iterator();

        while(var1.hasNext()) {
            Runnable runnable = (Runnable)var1.next();
            this.handler.post(runnable);
        }

        Looper.loop();
    }

    void execute(Runnable runnable) {
        if(this.handler == null) {
            this.queue.offer(runnable);
        } else {
            this.handler.post(runnable);
        }

    }

    void quit() {
        this.handler.post(new Runnable() {
            public void run() {
                Looper.myLooper().quit();
            }
        });
    }
}
