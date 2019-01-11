package com.shoneworn.libcore.net.okhttputils.cookie.store;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.Context;
import com.shoneworn.libcore.net.okhttputils.cookie.SerializableCookie;
import com.shoneworn.libcore.net.okhttputils.db.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class DBCookieStore implements CookieStore {
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;

    public DBCookieStore(Context context) {
        CookieManager.init(context);
        this.cookies = new HashMap();
        List<SerializableCookie> cookieList = CookieManager.getInstance().queryAll();
        Iterator var3 = cookieList.iterator();

        while(var3.hasNext()) {
            SerializableCookie serializableCookie = (SerializableCookie)var3.next();
            if(!this.cookies.containsKey(serializableCookie.host)) {
                this.cookies.put(serializableCookie.host, new ConcurrentHashMap());
            }

            Cookie cookie = serializableCookie.getCookie();
            ((ConcurrentHashMap)this.cookies.get(serializableCookie.host)).put(this.getCookieToken(cookie), cookie);
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

        while(var3.hasNext()) {
            Cookie cookie = (Cookie)var3.next();
            this.saveCookie(url, cookie);
        }

    }

    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        if(!this.cookies.containsKey(url.host())) {
            this.cookies.put(url.host(), new ConcurrentHashMap());
        }

        if(isCookieExpired(cookie)) {
            this.removeCookie(url, cookie);
        } else {
            ((ConcurrentHashMap)this.cookies.get(url.host())).put(this.getCookieToken(cookie), cookie);
            SerializableCookie serializableCookie = new SerializableCookie(url.host(), cookie);
            CookieManager.getInstance().replace(serializableCookie);
        }

    }

    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList();
        if(!this.cookies.containsKey(url.host())) {
            return ret;
        } else {
            List<SerializableCookie> query = CookieManager.getInstance().query("host=?", new String[]{url.host()});
            Iterator var4 = query.iterator();

            while(var4.hasNext()) {
                SerializableCookie serializableCookie = (SerializableCookie)var4.next();
                Cookie cookie = serializableCookie.getCookie();
                if(isCookieExpired(cookie)) {
                    this.removeCookie(url, cookie);
                } else {
                    ret.add(cookie);
                }
            }

            return ret;
        }
    }

    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        if(!this.cookies.containsKey(url.host())) {
            return false;
        } else {
            String cookieToken = this.getCookieToken(cookie);
            if(!((ConcurrentHashMap)this.cookies.get(url.host())).containsKey(cookieToken)) {
                return false;
            } else {
                ((ConcurrentHashMap)this.cookies.get(url.host())).remove(cookieToken);
                String whereClause = "host=? and name=? and domain=?";
                String[] whereArgs = new String[]{url.host(), cookie.name(), cookie.domain()};
                CookieManager.getInstance().delete(whereClause, whereArgs);
                return true;
            }
        }
    }

    public synchronized boolean removeCookie(HttpUrl url) {
        if(!this.cookies.containsKey(url.host())) {
            return false;
        } else {
            this.cookies.remove(url.host());
            String whereClause = "host=?";
            String[] whereArgs = new String[]{url.host()};
            CookieManager.getInstance().delete(whereClause, whereArgs);
            return true;
        }
    }

    public synchronized boolean removeAllCookie() {
        this.cookies.clear();
        CookieManager.getInstance().deleteAll();
        return true;
    }

    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> ret = new ArrayList();
        Iterator var2 = this.cookies.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            ret.addAll(((ConcurrentHashMap)this.cookies.get(key)).values());
        }

        return ret;
    }

    public synchronized List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList();
        Map<String, Cookie> mapCookie = (Map)this.cookies.get(url.host());
        if(mapCookie != null) {
            ret.addAll(mapCookie.values());
        }

        return ret;
    }
}
