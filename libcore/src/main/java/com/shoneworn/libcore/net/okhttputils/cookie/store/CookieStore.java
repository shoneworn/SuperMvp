package com.shoneworn.libcore.net.okhttputils.cookie.store;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import java.util.List;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public interface CookieStore {
    void saveCookie(HttpUrl var1, List<Cookie> var2);

    void saveCookie(HttpUrl var1, Cookie var2);

    List<Cookie> loadCookie(HttpUrl var1);

    List<Cookie> getAllCookie();

    List<Cookie> getCookie(HttpUrl var1);

    boolean removeCookie(HttpUrl var1, Cookie var2);

    boolean removeCookie(HttpUrl var1);

    boolean removeAllCookie();
}
