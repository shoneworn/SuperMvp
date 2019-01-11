package com.shoneworn.libcore.net.okhttputils.cookie.store;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.shoneworn.libcore.net.okhttputils.cookie.SerializableCookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class SPCookieStore implements CookieStore {
    private static final String COOKIE_PREFS = "okgo_cookie";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final SharedPreferences cookiePrefs;

    public SPCookieStore(Context context) {
        this.cookiePrefs = context.getSharedPreferences("okgo_cookie", 0);
        this.cookies = new HashMap();
        Map<String, ?> prefsMap = this.cookiePrefs.getAll();
        Iterator var3 = prefsMap.entrySet().iterator();

        while (true) {
            Entry entry;
            do {
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    entry = (Entry) var3.next();
                } while (entry.getValue() == null);
            } while (((String) entry.getKey()).startsWith("cookie_"));

            String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
            String[] var6 = cookieNames;
            int var7 = cookieNames.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String name = var6[var8];
                String encodedCookie = this.cookiePrefs.getString("cookie_" + name, (String) null);
                if (encodedCookie != null) {
                    Cookie decodedCookie = SerializableCookie.decodeCookie(encodedCookie);
                    if (decodedCookie != null) {
                        if (!this.cookies.containsKey(entry.getKey())) {
                            this.cookies.put((String) entry.getKey(), new ConcurrentHashMap());
                        }

                        ((ConcurrentHashMap) this.cookies.get(entry.getKey())).put(name, decodedCookie);
                    }
                }
            }
        }
    }

    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    public synchronized void saveCookie(HttpUrl url, List<Cookie> urlCookies) {
        Iterator var3 = urlCookies.iterator();

        while (var3.hasNext()) {
            Cookie cookie = (Cookie) var3.next();
            this.saveCookie(url, cookie);
        }

    }

    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        if (!this.cookies.containsKey(url.host())) {
            this.cookies.put(url.host(), new ConcurrentHashMap());
        }

        if (isCookieExpired(cookie)) {
            this.removeCookie(url, cookie);
        } else {
            this.saveCookie(url, cookie, this.getCookieToken(cookie));
        }

    }

    private void saveCookie(HttpUrl url, Cookie cookie, String cookieToken) {
        ((ConcurrentHashMap) this.cookies.get(url.host())).put(cookieToken, cookie);
        Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.putString(url.host(), TextUtils.join(",", ((ConcurrentHashMap) this.cookies.get(url.host())).keySet()));
        prefsWriter.putString("cookie_" + cookieToken, SerializableCookie.encodeCookie(url.host(), cookie));
        prefsWriter.apply();
    }

    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList();
        if (!this.cookies.containsKey(url.host())) {
            return ret;
        } else {
            Collection<Cookie> urlCookies = ((ConcurrentHashMap) this.cookies.get(url.host())).values();
            Iterator var4 = urlCookies.iterator();

            while (var4.hasNext()) {
                Cookie cookie = (Cookie) var4.next();
                if (isCookieExpired(cookie)) {
                    this.removeCookie(url, cookie);
                } else {
                    ret.add(cookie);
                }
            }

            return ret;
        }
    }

    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        if (!this.cookies.containsKey(url.host())) {
            return false;
        } else {
            String cookieToken = this.getCookieToken(cookie);
            if (!((ConcurrentHashMap) this.cookies.get(url.host())).containsKey(cookieToken)) {
                return false;
            } else {
                ((ConcurrentHashMap) this.cookies.get(url.host())).remove(cookieToken);
                Editor prefsWriter = this.cookiePrefs.edit();
                if (this.cookiePrefs.contains("cookie_" + cookieToken)) {
                    prefsWriter.remove("cookie_" + cookieToken);
                }

                prefsWriter.putString(url.host(), TextUtils.join(",", ((ConcurrentHashMap) this.cookies.get(url.host())).keySet()));
                prefsWriter.apply();
                return true;
            }
        }
    }

    public synchronized boolean removeCookie(HttpUrl url) {
        if (!this.cookies.containsKey(url.host())) {
            return false;
        } else {
            ConcurrentHashMap<String, Cookie> urlCookie = (ConcurrentHashMap) this.cookies.remove(url.host());
            Set<String> cookieTokens = urlCookie.keySet();
            Editor prefsWriter = this.cookiePrefs.edit();
            Iterator var5 = cookieTokens.iterator();

            while (var5.hasNext()) {
                String cookieToken = (String) var5.next();
                if (this.cookiePrefs.contains("cookie_" + cookieToken)) {
                    prefsWriter.remove("cookie_" + cookieToken);
                }
            }

            prefsWriter.remove(url.host());
            prefsWriter.apply();
            return true;
        }
    }

    public synchronized boolean removeAllCookie() {
        this.cookies.clear();
        Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        return true;
    }

    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> ret = new ArrayList();
        Iterator var2 = this.cookies.keySet().iterator();

        while (var2.hasNext()) {
            String key = (String) var2.next();
            ret.addAll(((ConcurrentHashMap) this.cookies.get(key)).values());
        }

        return ret;
    }

    public synchronized List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList();
        Map<String, Cookie> mapCookie = (Map) this.cookies.get(url.host());
        if (mapCookie != null) {
            ret.addAll(mapCookie.values());
        }

        return ret;
    }
}
