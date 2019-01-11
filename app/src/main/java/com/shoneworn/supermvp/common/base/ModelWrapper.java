package com.shoneworn.supermvp.common.base;


import com.shoneworn.libcore.common.base.model.AbsModelWrapper;
import com.shoneworn.libcore.net.okhttputils.model.HttpHeaders;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by admin on 2018/4/28.
 */

public abstract class ModelWrapper extends AbsModelWrapper {
    public static final String ERR = -Integer.MAX_VALUE + "";
    protected HttpParams params = new HttpParams();
    protected HttpHeaders commonHeaders = new HttpHeaders();

    protected String getNewKey() {
        return null;
    }

    protected String getEndKey() {
        return null;
    }

    protected String[] getNeedData(String jsonData) {
        String[] keys = new String[]{"", ""};
        JSONObject jsonObject = null;
        String endKey = getEndKey();
        String newKey = getNewKey();
        try {
            jsonObject = new JSONObject(jsonData);
            if (jsonObject.has(endKey) && !StringUtils.isEmpty(endKey)) {
                keys[0] = jsonObject.optString(endKey);
            }
            if (jsonObject.has(newKey) && !StringUtils.isEmpty(newKey)) {
                keys[1] = jsonObject.optString(newKey);
            }
            return keys;

        } catch (JSONException e) {
        }
        return keys;
    }

    protected String[] getNeedData(String jsonData, String newKey, String endKey) {
        String[] keys = new String[]{"", ""};
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            if (jsonObject.has(endKey) && !StringUtils.isEmpty(endKey)) {
                keys[0] = jsonObject.optString(endKey);
            }
            if (jsonObject.has(newKey) && !StringUtils.isEmpty(newKey)) {
                keys[1] = jsonObject.optString(newKey);
            }
            return keys;

        } catch (JSONException e) {
        }
        return keys;
    }

    protected String getNeedData(String jsonData, String newKey) {
        String key = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            if (jsonObject.has(newKey) && !StringUtils.isEmpty(newKey)) {
                key = jsonObject.optString(newKey);
            }
            return key;

        } catch (JSONException e) {
        }
        return key;
    }
}
