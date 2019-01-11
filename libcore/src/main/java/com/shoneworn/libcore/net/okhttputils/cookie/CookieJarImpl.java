package com.shoneworn.libcore.net.okhttputils.cookie;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import com.shoneworn.libcore.net.okhttputils.cookie.store.CookieStore;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieJarImpl implements CookieJar {
    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if(cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null!");
        } else {
            this.cookieStore = cookieStore;
        }
    }

    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookieStore.saveCookie(url, cookies);
    }

    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        return this.cookieStore.loadCookie(url);
    }

    public CookieStore getCookieStore() {
        return this.cookieStore;
    }
}
