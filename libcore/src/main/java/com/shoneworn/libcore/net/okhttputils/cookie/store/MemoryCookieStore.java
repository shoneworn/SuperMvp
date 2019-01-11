package com.shoneworn.libcore.net.okhttputils.cookie.store;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class MemoryCookieStore implements CookieStore {
    private final Map<String, List<Cookie>> memoryCookies = new HashMap();

    public MemoryCookieStore() {
    }

    public synchronized void saveCookie(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> oldCookies = (List)this.memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList();
        Iterator var5 = cookies.iterator();

        while(var5.hasNext()) {
            Cookie newCookie = (Cookie)var5.next();
            Iterator var7 = oldCookies.iterator();

            while(var7.hasNext()) {
                Cookie oldCookie = (Cookie)var7.next();
                if(newCookie.name().equals(oldCookie.name())) {
                    needRemove.add(oldCookie);
                }
            }
        }

        oldCookies.removeAll(needRemove);
        oldCookies.addAll(cookies);
    }

    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = (List)this.memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList();
        Iterator var5 = cookies.iterator();

        while(var5.hasNext()) {
            Cookie item = (Cookie)var5.next();
            if(cookie.name().equals(item.name())) {
                needRemove.add(item);
            }
        }

        cookies.removeAll(needRemove);
        cookies.add(cookie);
    }

    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> cookies = (List)this.memoryCookies.get(url.host());
        if(cookies == null) {
            cookies = new ArrayList();
            this.memoryCookies.put(url.host(), cookies);
        }

        return (List)cookies;
    }

    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> cookies = new ArrayList();
        Set<String> httpUrls = this.memoryCookies.keySet();
        Iterator var3 = httpUrls.iterator();

        while(var3.hasNext()) {
            String url = (String)var3.next();
            cookies.addAll((Collection)this.memoryCookies.get(url));
        }

        return cookies;
    }

    public List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> cookies = new ArrayList();
        List<Cookie> urlCookies = (List)this.memoryCookies.get(url.host());
        if(urlCookies != null) {
            cookies.addAll(urlCookies);
        }

        return cookies;
    }

    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = (List)this.memoryCookies.get(url.host());
        return cookie != null && cookies.remove(cookie);
    }

    public synchronized boolean removeCookie(HttpUrl url) {
        return this.memoryCookies.remove(url.host()) != null;
    }

    public synchronized boolean removeAllCookie() {
        this.memoryCookies.clear();
        return true;
    }
}
