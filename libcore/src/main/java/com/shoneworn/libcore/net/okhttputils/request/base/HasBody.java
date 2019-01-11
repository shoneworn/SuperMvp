package com.shoneworn.libcore.net.okhttputils.request.base;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.model.HttpParams;

import java.io.File;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;

public interface HasBody<R> {
    R isMultipart(boolean var1);

    R isSpliceUrl(boolean var1);

    R params(String var1, File var2, boolean... var3);

    R params(String var1, File var2, String var3, boolean... var4);

    R params(String var1, File var2, String var3, MediaType var4, boolean... var5);

    R paramsFileMap(Map<String, File> var1, boolean... var2);

    R paramsFileList(String var1, List<File> var2, boolean... var3);

    R paramsFileWrapperList(String var1, List<HttpParams.FileWrapper> var2, boolean... var3);

    R paramsQuery(String var1, String var2, boolean... var3);

    R paramsQuery(String var1, int var2, boolean... var3);

    R paramsQuery(String var1, float var2, boolean... var3);

    R paramsQuery(String var1, double var2, boolean... var4);

    R paramsQuery(String var1, long var2, boolean... var4);

    R paramsQuery(String var1, char var2, boolean... var3);

    R paramsQuery(String var1, boolean var2, boolean... var3);

    R paramsQueryStringMap(Map<String, String> var1, boolean... var2);

    R paramsQueryStringList(String var1, List<String> var2, boolean... var3);

    R upString(String var1);

    R upString(String var1, MediaType var2);

    R upJson(String var1);

    R upJson(JSONObject var1);

    R upJson(JSONArray var1);

    R upBytes(byte[] var1);

    R upBytes(byte[] var1, MediaType var2);

    R upFile(File var1);

    R upFile(File var1, MediaType var2);

    R upRequestBody(RequestBody var1);
}
