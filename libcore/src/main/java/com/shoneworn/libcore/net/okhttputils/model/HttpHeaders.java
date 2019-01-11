package com.shoneworn.libcore.net.okhttputils.model;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpHeaders implements Serializable {
    private static final long serialVersionUID = 8458647755751403873L;
    public static final String FORMAT_HTTP_DATA = "EEE, dd MMM y HH:mm:ss 'GMT'";
    public static final TimeZone GMT_TIME_ZONE = TimeZone.getTimeZone("GMT");
    public static final String HEAD_KEY_RESPONSE_CODE = "ResponseCode";
    public static final String HEAD_KEY_RESPONSE_MESSAGE = "ResponseMessage";
    public static final String HEAD_KEY_ACCEPT = "Accept";
    public static final String HEAD_KEY_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEAD_VALUE_ACCEPT_ENCODING = "gzip, deflate";
    public static final String HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEAD_KEY_CONTENT_TYPE = "Content-Type";
    public static final String HEAD_KEY_CONTENT_LENGTH = "Content-Length";
    public static final String HEAD_KEY_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEAD_KEY_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEAD_KEY_CONTENT_RANGE = "Content-Range";
    public static final String HEAD_KEY_RANGE = "Range";
    public static final String HEAD_KEY_CACHE_CONTROL = "Cache-Control";
    public static final String HEAD_KEY_CONNECTION = "Connection";
    public static final String HEAD_VALUE_CONNECTION_KEEP_ALIVE = "keep-alive";
    public static final String HEAD_VALUE_CONNECTION_CLOSE = "close";
    public static final String HEAD_KEY_DATE = "Date";
    public static final String HEAD_KEY_EXPIRES = "Expires";
    public static final String HEAD_KEY_E_TAG = "ETag";
    public static final String HEAD_KEY_PRAGMA = "Pragma";
    public static final String HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String HEAD_KEY_IF_NONE_MATCH = "If-None-Match";
    public static final String HEAD_KEY_LAST_MODIFIED = "Last-Modified";
    public static final String HEAD_KEY_LOCATION = "Location";
    public static final String HEAD_KEY_USER_AGENT = "User-Agent";
    public static final String HEAD_KEY_COOKIE = "Cookie";
    public static final String HEAD_KEY_COOKIE2 = "Cookie2";
    public static final String HEAD_KEY_SET_COOKIE = "Set-Cookie";
    public static final String HEAD_KEY_SET_COOKIE2 = "Set-Cookie2";
    public LinkedHashMap<String, String> headersMap;
    private static String acceptLanguage;
    private static String userAgent;
    public HashMap<String, String> keyMap;

    private void init() {
        this.keyMap = new HashMap();
        this.headersMap = new LinkedHashMap();
    }

    public HttpHeaders() {
        this.init();
    }

    public HttpHeaders(String key, String value) {
        this.init();
        this.put(key, value);
    }

    public void put(String key, String value) {
        if(key != null && value != null) {
            this.keyMap.put(key, "");
            this.headersMap.put(key, value);
        }

    }

    public void put(HttpHeaders headers) {
        if(headers != null && headers.headersMap != null && !headers.headersMap.isEmpty()) {
            HashMap<String, String> headersKeyMap = headers.getKeyMap();
            if(!HttpUtils.isEmpty(headersKeyMap)) {
                this.keyMap.putAll(headersKeyMap);
            }

            this.headersMap.putAll(headers.headersMap);
        }

    }

    public String get(String key) {
        return (String)this.headersMap.get(key);
    }

    public String remove(String key) {
        return (String)this.headersMap.remove(key);
    }

    public void clear() {
        this.headersMap.clear();
    }

    public Set<String> getNames() {
        return this.headersMap.keySet();
    }

    public final String toJSONString() {
        JSONObject jsonObject = new JSONObject();

        try {
            Iterator var2 = this.headersMap.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<String, String> entry = (Entry)var2.next();
                jsonObject.put((String)entry.getKey(), entry.getValue());
            }
        } catch (JSONException var4) {
            OkLogger.printStackTrace(var4);
        }

        return jsonObject.toString();
    }

    public static long getDate(String gmtTime) {
        try {
            return parseGMTToMillis(gmtTime);
        } catch (ParseException var2) {
            return 0L;
        }
    }

    public static String getDate(long milliseconds) {
        return formatMillisToGMT(milliseconds);
    }

    public static long getExpiration(String expiresTime) {
        try {
            return parseGMTToMillis(expiresTime);
        } catch (ParseException var2) {
            return -1L;
        }
    }

    public static long getLastModified(String lastModified) {
        try {
            return parseGMTToMillis(lastModified);
        } catch (ParseException var2) {
            return 0L;
        }
    }

    public static String getCacheControl(String cacheControl, String pragma) {
        return cacheControl != null?cacheControl:(pragma != null?pragma:null);
    }

    public static void setAcceptLanguage(String language) {
        acceptLanguage = language;
    }

    public static String getAcceptLanguage() {
        if(TextUtils.isEmpty(acceptLanguage)) {
            Locale locale = Locale.getDefault();
            String language = locale.getLanguage();
            String country = locale.getCountry();
            StringBuilder acceptLanguageBuilder = new StringBuilder(language);
            if(!TextUtils.isEmpty(country)) {
                acceptLanguageBuilder.append('-').append(country).append(',').append(language).append(";q=0.8");
            }

            acceptLanguage = acceptLanguageBuilder.toString();
            return acceptLanguage;
        } else {
            return acceptLanguage;
        }
    }

    public static void setUserAgent(String agent) {
        userAgent = agent;
    }

    public static String getUserAgent() {
        if(TextUtils.isEmpty(userAgent)) {
            String webUserAgent = null;

            try {
                Class<?> sysResCls = Class.forName("com.android.internal.R$string");
                Field webUserAgentField = sysResCls.getDeclaredField("web_user_agent");
                Integer resId = (Integer)webUserAgentField.get((Object)null);
                webUserAgent = OkHttpUtils.getInstance().getContext().getString(resId.intValue());
            } catch (Exception var6) {
                ;
            }

            if(TextUtils.isEmpty(webUserAgent)) {
                webUserAgent = "okhttp-okgo/jeasonlzy";
            }

            Locale locale = Locale.getDefault();
            StringBuffer buffer = new StringBuffer();
            String version = VERSION.RELEASE;
            if(version.length() > 0) {
                buffer.append(version);
            } else {
                buffer.append("1.0");
            }

            buffer.append("; ");
            String language = locale.getLanguage();
            String id;
            if(language != null) {
                buffer.append(language.toLowerCase(locale));
                id = locale.getCountry();
                if(!TextUtils.isEmpty(id)) {
                    buffer.append("-");
                    buffer.append(id.toLowerCase(locale));
                }
            } else {
                buffer.append("en");
            }

            if("REL".equals(VERSION.CODENAME)) {
                id = Build.MODEL;
                if(id.length() > 0) {
                    buffer.append("; ");
                    buffer.append(id);
                }
            }

            id = Build.ID;
            if(id.length() > 0) {
                buffer.append(" Build/");
                buffer.append(id);
            }

            userAgent = String.format(webUserAgent, new Object[]{buffer, "Mobile "});
            return userAgent;
        } else {
            return userAgent;
        }
    }

    public static long parseGMTToMillis(String gmtTime) throws ParseException {
        if(TextUtils.isEmpty(gmtTime)) {
            return 0L;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM y HH:mm:ss 'GMT'", Locale.US);
            formatter.setTimeZone(GMT_TIME_ZONE);
            Date date = formatter.parse(gmtTime);
            return date.getTime();
        }
    }

    public static String formatMillisToGMT(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM y HH:mm:ss 'GMT'", Locale.US);
        simpleDateFormat.setTimeZone(GMT_TIME_ZONE);
        return simpleDateFormat.format(date);
    }

    public List<String> getKeyList() {
        List<String> keyList = new ArrayList();
        if(HttpUtils.isEmpty(this.keyMap)) {
            return keyList;
        } else {
            Set<String> keySet = this.keyMap.keySet();
            if(!HttpUtils.isEmpty(keySet)) {
                keyList.addAll(keySet);
            }

            return keyList;
        }
    }

    public HashMap<String, String> getKeyMap() {
        return this.keyMap;
    }

    public String toString() {
        return "HttpHeaders{headersMap=" + this.headersMap + '}';
    }

    public HttpHeaders deepClone() {
        HttpHeaders httpHeaders = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            httpHeaders = (HttpHeaders)ois.readObject();
        } catch (IOException var6) {
            var6.printStackTrace();
        } catch (ClassNotFoundException var7) {
            var7.printStackTrace();
        }

        return httpHeaders;
    }
}
