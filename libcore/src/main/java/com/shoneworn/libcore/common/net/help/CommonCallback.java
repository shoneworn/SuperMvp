package com.shoneworn.libcore.common.net.help;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.content.Context;

import com.shoneworn.libcore.net.okhttputils.help.ResponseCommonCallback;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.net.okhttputils.request.base.Request;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map.Entry;

public abstract class CommonCallback<T> extends ResponseCommonCallback<T> {
    private Context ctx;
    private String entryCode;
    private String entryCodeAccordTs;
    private static final Random RANDOM = new Random();
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public CommonCallback(Context ctx) {
        super(ctx);
        this.ctx = ctx;
    }


    protected void appendRequestParamsIfNeed(Request<? extends Request> request, HttpParams params) {
    }

    private String getRndStr(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            char ch = "0123456789abcdefghijklmnopqrstuvwxyz".charAt(RANDOM.nextInt("0123456789abcdefghijklmnopqrstuvwxyz".length()));
            sb.append(ch);
        }

        return sb.toString();
    }

    private Map<String, List<String>> getSortedMapByKey(LinkedHashMap<String, List<String>> map) {
        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        TreeMap<String, List<String>> treeMap = new TreeMap(comparator);
        Iterator var4 = map.entrySet().iterator();

        while (var4.hasNext()) {
            Entry<String, List<String>> entry = (Entry) var4.next();
            treeMap.put(entry.getKey(), entry.getValue());
        }

        return treeMap;
    }

    public CommonCallback setEntryCode(String entryCode) {
        this.entryCode = entryCode;
        return this;
    }

    public CommonCallback setEntryCodeAccordTs(String entryCodeAccordTs) {
        this.entryCodeAccordTs = entryCodeAccordTs;
        return this;
    }
}
