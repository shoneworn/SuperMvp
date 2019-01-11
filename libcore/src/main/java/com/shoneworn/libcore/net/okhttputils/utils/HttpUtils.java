package com.shoneworn.libcore.net.okhttputils.utils;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.model.HttpHeaders;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams.FileWrapper;
import com.shoneworn.libcore.utils.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.HttpUrl.Builder;

public class HttpUtils {
    public HttpUtils() {
    }

    public static String createUrlFromParams(String url, boolean isSpliceUrl, HttpParams httpParams) {
        try {
            String tempUrl = url;

            Entry pathParams;
            for(Iterator var4 = httpParams.pathParamsMap.entrySet().iterator(); var4.hasNext(); tempUrl = tempUrl.replace("{" + (String)pathParams.getKey() + "}", String.valueOf(pathParams.getValue()))) {
                pathParams = (Entry)var4.next();
            }

            HttpUrl httpUrl = HttpUrl.parse(tempUrl);
            if(httpUrl == null) {
                return tempUrl;
            } else {
                Builder urlBuilder = httpUrl.newBuilder();
                Map<String, List<String>> params = new LinkedHashMap();
                if(isSpliceUrl) {
                    params.putAll(httpParams.stringParamsMap);
                }

                params.putAll(httpParams.queryParamsMap);
                Iterator var7 = params.entrySet().iterator();

                while(var7.hasNext()) {
                    Entry<String, List<String>> queryParams = (Entry)var7.next();
                    List<String> queryValues = (List)queryParams.getValue();
                    Iterator var10 = queryValues.iterator();

                    while(var10.hasNext()) {
                        String value = (String)var10.next();
                        urlBuilder.addQueryParameter((String)queryParams.getKey(), value);
                    }
                }

                return urlBuilder.build().toString();
            }
        } catch (Exception var12) {
            OkLogger.printStackTrace(var12);
            return url;
        }
    }

    public static okhttp3.Request.Builder appendHeaders(okhttp3.Request.Builder builder, HttpHeaders headers) {
        if(headers.headersMap.isEmpty()) {
            return builder;
        } else {
            okhttp3.Headers.Builder headerBuilder = new okhttp3.Headers.Builder();

            try {
                Iterator var3 = headers.headersMap.entrySet().iterator();

                while(var3.hasNext()) {
                    Entry<String, String> entry = (Entry)var3.next();
                    headerBuilder.add((String)entry.getKey(), (String)entry.getValue());
                }
            } catch (Exception var5) {
                OkLogger.printStackTrace(var5);
            }

            builder.headers(headerBuilder.build());
            return builder;
        }
    }

    public static RequestBody generateMultipartRequestBody(HttpParams params, boolean isMultipart) {
        Iterator var3;
        List fileValues;
        Iterator var6;
        String value;
        if(params.fileParamsMap.isEmpty() && !isMultipart) {
            okhttp3.FormBody.Builder bodyBuilder = new okhttp3.FormBody.Builder();
            var3 = params.stringParamsMap.keySet().iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                fileValues = (List)params.stringParamsMap.get(key);
                var6 = fileValues.iterator();

                while(var6.hasNext()) {
                    value = (String)var6.next();
                    bodyBuilder.add(key, value);
                }
            }

            return bodyBuilder.build();
        } else {
            okhttp3.MultipartBody.Builder multipartBodybuilder = (new okhttp3.MultipartBody.Builder()).setType(MultipartBody.FORM);
            Entry entry;
            if(!params.stringParamsMap.isEmpty()) {
                var3 = params.stringParamsMap.entrySet().iterator();

                while(var3.hasNext()) {
                    entry = (Entry)var3.next();
                    fileValues = (List)entry.getValue();
                    var6 = fileValues.iterator();

                    while(var6.hasNext()) {
                        value = (String)var6.next();
                        multipartBodybuilder.addFormDataPart((String)entry.getKey(), value);
                    }
                }
            }

            var3 = params.fileParamsMap.entrySet().iterator();

            while(var3.hasNext()) {
                entry = (Entry)var3.next();
                fileValues = (List)entry.getValue();
                var6 = fileValues.iterator();

                while(var6.hasNext()) {
                    FileWrapper fileWrapper = (FileWrapper)var6.next();
                    RequestBody fileBody = RequestBody.create(fileWrapper.contentType, fileWrapper.file);
                    multipartBodybuilder.addFormDataPart((String)entry.getKey(), fileWrapper.fileName, fileBody);
                }
            }

            return multipartBodybuilder.build();
        }
    }

    public static String getNetFileName(Response response, String url) {
        String fileName = getHeaderFileName(response);
        if(TextUtils.isEmpty(fileName)) {
            fileName = getUrlFileName(url);
        }

        if(TextUtils.isEmpty(fileName)) {
            fileName = "unknownfile_" + System.currentTimeMillis();
        }

        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException var4) {
            OkLogger.printStackTrace(var4);
        }

        return fileName;
    }

    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response.header("Content-Disposition");
        if(dispositionHeader != null) {
            dispositionHeader = dispositionHeader.replaceAll("\"", "");
            String split = "filename=";
            int indexOf = dispositionHeader.indexOf(split);
            if(indexOf != -1) {
                return dispositionHeader.substring(indexOf + split.length(), dispositionHeader.length());
            }

            split = "filename*=";
            indexOf = dispositionHeader.indexOf(split);
            if(indexOf != -1) {
                String fileName = dispositionHeader.substring(indexOf + split.length(), dispositionHeader.length());
                String encode = "UTF-8''";
                if(fileName.startsWith(encode)) {
                    fileName = fileName.substring(encode.length(), fileName.length());
                }

                return fileName;
            }
        }

        return null;
    }

    private static String getUrlFileName(String url) {
        String filename = null;
        String[] strings = url.split("/");
        String[] var3 = strings;
        int var4 = strings.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String string = var3[var5];
            if(string.contains("?")) {
                int endIndex = string.indexOf("?");
                if(endIndex != -1) {
                    filename = string.substring(0, endIndex);
                    return filename;
                }
            }
        }

        if(strings.length > 0) {
            filename = strings[strings.length - 1];
        }

        return filename;
    }

    public static String createAppendUrlFromParams(String url, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if(url.indexOf(38) <= 0 && url.indexOf(63) <= 0) {
                sb.append("?");
            } else {
                sb.append("&");
            }

            Iterator var3 = params.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, String> urlParams = (Entry)var3.next();
                String urlValue = URLEncoder.encode((String)urlParams.getValue(), "UTF-8");
                sb.append((String)urlParams.getKey()).append("=").append(urlValue).append("&");
            }

            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException var6) {
            var6.printStackTrace();
            return url;
        }
    }

    public static String createAppendUrlFromParamsWithoutEncode(String url, Map<String, String> params) {
        return createAppendUrlFromParamsWithoutEncode(url, params, true);
    }

    public static String createAppendUrlFromParamsWithoutEncode(String url, Map<String, String> params, boolean isEmptyValueFormatEnable) {
        if(!isEmpty(params) && !trimToEmpty(url)) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(url);
                if(url.indexOf(38) <= 0 && url.indexOf(63) <= 0) {
                    sb.append("?");
                } else {
                    sb.append("&");
                }

                Iterator var4 = params.entrySet().iterator();

                while(true) {
                    while(var4.hasNext()) {
                        Entry<String, String> urlParams = (Entry)var4.next();
                        String urlValue = (String)urlParams.getValue();
                        String urlKey = (String)urlParams.getKey();
                        if(StringUtils.isEmpty(urlValue) && !isEmptyValueFormatEnable) {
                            sb.append(urlKey).append("&");
                        } else {
                            sb.append(urlKey).append("=").append(urlValue).append("&");
                        }
                    }

                    sb.deleteCharAt(sb.length() - 1);
                    return sb.toString();
                }
            } catch (Exception var8) {
                var8.printStackTrace();
                return url;
            }
        } else {
            return url;
        }
    }

    public static boolean deleteFile(String path) {
        if(TextUtils.isEmpty(path)) {
            return true;
        } else {
            File file = new File(path);
            if(!file.exists()) {
                return true;
            } else if(file.isFile()) {
                boolean delete = file.delete();
                OkLogger.e("deleteFile:" + delete + " path:" + path);
                return delete;
            } else {
                return false;
            }
        }
    }

    public static MediaType guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = fileName.replace("#", "");
        String contentType = fileNameMap.getContentTypeFor(fileName);
        return contentType == null?HttpParams.MEDIA_TYPE_STREAM:MediaType.parse(contentType);
    }

    public static <T> T checkNotNull(T object, String message) {
        if(object == null) {
            throw new NullPointerException(message);
        } else {
            return object;
        }
    }

    public static void runOnUiThread(Runnable runnable) {
        OkHttpUtils.getInstance().getDelivery().post(runnable);
    }

    public static boolean isContains(List<String> list, String key) {
        if(isEmpty((Collection)list)) {
            return false;
        } else {
            Iterator var2 = list.iterator();

            String temp;
            do {
                if(!var2.hasNext()) {
                    return false;
                }

                temp = (String)var2.next();
            } while(!StringUtils.equals(temp, key));

            return true;
        }
    }

    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean isEmpty(Object obj) {
        return null == obj;
    }

    public static boolean isEmpty(Map map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(int[] objs) {
        return null == objs || objs.length <= 0;
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() <= 0;
    }

    public static String _trimToEmpty(String str) {
        return str == null?"":str.trim();
    }

    public static boolean trimToEmpty(String str) {
        return isEmpty((CharSequence)_trimToEmpty(str));
    }

    public static boolean hasPermission(Context ctx, String permission) {
        if(!isEmpty((Object)ctx) && !TextUtils.isEmpty(permission)) {
            int targetSdkVersion = ctx.getApplicationInfo().targetSdkVersion;
            return targetSdkVersion >= 23?ContextCompat.checkSelfPermission(ctx, permission) == 0:PermissionChecker.checkSelfPermission(ctx, permission) == 0;
        } else {
            return false;
        }
    }
}
