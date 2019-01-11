package com.shoneworn.libcore.net.okhttputils.cookie;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.ContentValues;
import android.database.Cursor;
import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import okhttp3.Cookie;
import okhttp3.Cookie.Builder;

public class SerializableCookie implements Serializable {
    private static final long serialVersionUID = 6374381323722046732L;
    public static final String HOST = "host";
    public static final String NAME = "name";
    public static final String DOMAIN = "domain";
    public static final String COOKIE = "cookie";
    public String host;
    public String name;
    public String domain;
    private transient Cookie cookie;
    private transient Cookie clientCookie;

    public SerializableCookie(String host, Cookie cookie) {
        this.cookie = cookie;
        this.host = host;
        this.name = cookie.name();
        this.domain = cookie.domain();
    }

    public Cookie getCookie() {
        Cookie bestCookie = this.cookie;
        if(this.clientCookie != null) {
            bestCookie = this.clientCookie;
        }

        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.cookie.name());
        out.writeObject(this.cookie.value());
        out.writeLong(this.cookie.expiresAt());
        out.writeObject(this.cookie.domain());
        out.writeObject(this.cookie.path());
        out.writeBoolean(this.cookie.secure());
        out.writeBoolean(this.cookie.httpOnly());
        out.writeBoolean(this.cookie.hostOnly());
        out.writeBoolean(this.cookie.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String name = (String)in.readObject();
        String value = (String)in.readObject();
        long expiresAt = in.readLong();
        String domain = (String)in.readObject();
        String path = (String)in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Builder builder = new Builder();
        builder = builder.name(name);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = hostOnly?builder.hostOnlyDomain(domain):builder.domain(domain);
        builder = builder.path(path);
        builder = secure?builder.secure():builder;
        builder = httpOnly?builder.httpOnly():builder;
        this.clientCookie = builder.build();
    }

    public static SerializableCookie parseCursorToBean(Cursor cursor) {
        String host = cursor.getString(cursor.getColumnIndex("host"));
        byte[] cookieBytes = cursor.getBlob(cursor.getColumnIndex("cookie"));
        Cookie cookie = bytesToCookie(cookieBytes);
        return new SerializableCookie(host, cookie);
    }

    public static ContentValues getContentValues(SerializableCookie serializableCookie) {
        ContentValues values = new ContentValues();
        values.put("host", serializableCookie.host);
        values.put("name", serializableCookie.name);
        values.put("domain", serializableCookie.domain);
        values.put("cookie", cookieToBytes(serializableCookie.host, serializableCookie.getCookie()));
        return values;
    }

    public static String encodeCookie(String host, Cookie cookie) {
        if(cookie == null) {
            return null;
        } else {
            byte[] cookieBytes = cookieToBytes(host, cookie);
            return byteArrayToHexString(cookieBytes);
        }
    }

    public static byte[] cookieToBytes(String host, Cookie cookie) {
        SerializableCookie serializableCookie = new SerializableCookie(host, cookie);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(serializableCookie);
        } catch (IOException var5) {
            OkLogger.printStackTrace(var5);
            return null;
        }

        return os.toByteArray();
    }

    public static Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        return bytesToCookie(bytes);
    }

    public static Cookie bytesToCookie(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie)objectInputStream.readObject()).getCookie();
        } catch (Exception var4) {
            OkLogger.printStackTrace(var4);
        }

        return cookie;
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte element = var2[var4];
            int v = element & 255;
            if(v < 16) {
                sb.append('0');
            }

            sb.append(Integer.toHexString(v));
        }

        return sb.toString().toUpperCase(Locale.US);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];

        for(int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }

        return data;
    }

    public boolean equals(Object o) {
        if(this == o) {
            return true;
        } else if(o != null && this.getClass() == o.getClass()) {
            SerializableCookie that;
            label41: {
                that = (SerializableCookie)o;
                if(this.host != null) {
                    if(this.host.equals(that.host)) {
                        break label41;
                    }
                } else if(that.host == null) {
                    break label41;
                }

                return false;
            }

            if(this.name != null) {
                if(this.name.equals(that.name)) {
                    return this.domain != null?this.domain.equals(that.domain):that.domain == null;
                }
            } else if(that.name == null) {
                return this.domain != null?this.domain.equals(that.domain):that.domain == null;
            }

            return false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.host != null?this.host.hashCode():0;
        result = 31 * result + (this.name != null?this.name.hashCode():0);
        result = 31 * result + (this.domain != null?this.domain.hashCode():0);
        return result;
    }
}
