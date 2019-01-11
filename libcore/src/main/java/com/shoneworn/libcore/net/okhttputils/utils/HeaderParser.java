package com.shoneworn.libcore.net.okhttputils.utils;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.text.TextUtils;
import com.shoneworn.libcore.net.okhttputils.cache.CacheEntity;
import com.shoneworn.libcore.net.okhttputils.cache.CacheMode;
import com.shoneworn.libcore.net.okhttputils.model.HttpHeaders;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import okhttp3.Headers;

public class HeaderParser {
    public HeaderParser() {
    }

    public static <T> CacheEntity<T> createCacheEntity(Headers responseHeaders, T data, CacheMode cacheMode, String cacheKey) {
        long localExpire = 0L;
        if(cacheMode == CacheMode.DEFAULT) {
            long date = HttpHeaders.getDate(responseHeaders.get("Date"));
            long expires = HttpHeaders.getExpiration(responseHeaders.get("Expires"));
            String cacheControl = HttpHeaders.getCacheControl(responseHeaders.get("Cache-Control"), responseHeaders.get("Pragma"));
            if(TextUtils.isEmpty(cacheControl) && expires <= 0L) {
                return null;
            }

            long maxAge = 0L;
            if(!TextUtils.isEmpty(cacheControl)) {
                StringTokenizer tokens = new StringTokenizer(cacheControl, ",");

                while(tokens.hasMoreTokens()) {
                    String token = tokens.nextToken().trim().toLowerCase(Locale.getDefault());
                    if(token.equals("no-cache") || token.equals("no-store")) {
                        return null;
                    }

                    if(token.startsWith("max-age=")) {
                        try {
                            maxAge = Long.parseLong(token.substring(8));
                            if(maxAge <= 0L) {
                                return null;
                            }
                        } catch (Exception var16) {
                            OkLogger.printStackTrace(var16);
                        }
                    }
                }
            }

            long now = System.currentTimeMillis();
            if(date > 0L) {
                now = date;
            }

            if(maxAge > 0L) {
                localExpire = now + maxAge * 1000L;
            } else if(expires >= 0L) {
                localExpire = expires;
            }
        } else {
            localExpire = System.currentTimeMillis();
        }

        HttpHeaders headers = new HttpHeaders();
        Iterator var7 = responseHeaders.names().iterator();

        while(var7.hasNext()) {
            String headerName = (String)var7.next();
            headers.put(headerName, responseHeaders.get(headerName));
        }

        CacheEntity<T> cacheEntity = new CacheEntity();
        cacheEntity.setKey(cacheKey);
        cacheEntity.setData(data);
        cacheEntity.setLocalExpire(localExpire);
        cacheEntity.setResponseHeaders(headers);
        return cacheEntity;
    }

    public static <T> void addCacheHeaders(Request request, CacheEntity<T> cacheEntity, CacheMode cacheMode) {
        if(cacheEntity != null && cacheMode == CacheMode.DEFAULT) {
            HttpHeaders responseHeaders = cacheEntity.getResponseHeaders();
            if(responseHeaders != null) {
                String eTag = responseHeaders.get("ETag");
                if(eTag != null) {
                    request.headers("If-None-Match", eTag);
                }

                long lastModified = HttpHeaders.getLastModified(responseHeaders.get("Last-Modified"));
                if(lastModified > 0L) {
                    request.headers("If-Modified-Since", HttpHeaders.formatMillisToGMT(lastModified));
                }
            }
        }

    }
}
