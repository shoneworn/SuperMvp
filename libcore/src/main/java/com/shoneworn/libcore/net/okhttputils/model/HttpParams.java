package com.shoneworn.libcore.net.okhttputils.model;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import com.shoneworn.libcore.net.okhttputils.utils.HttpUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import okhttp3.MediaType;

public class HttpParams implements Serializable {
    private static final long serialVersionUID = 7369819159227055048L;
    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    public static final boolean IS_REPLACE = true;
    public LinkedHashMap<String, String> pathParamsMap;
    public LinkedHashMap<String, List<String>> stringParamsMap;
    public LinkedHashMap<String, List<String>> queryParamsMap;
    public LinkedHashMap<String, List<HttpParams.FileWrapper>> fileParamsMap;
    public HashMap<String, String> keyMap;

    public HttpParams() {
        this.init();
    }

    public HttpParams(String key, String value) {
        this.init();
        this.put(key, value, new boolean[]{true});
    }

    public HttpParams(String key, File file) {
        this.init();
        this.put(key, file, new boolean[]{true});
    }

    private void init() {
        this.keyMap = new HashMap();
        this.pathParamsMap = new LinkedHashMap();
        this.stringParamsMap = new LinkedHashMap();
        this.queryParamsMap = new LinkedHashMap();
        this.fileParamsMap = new LinkedHashMap();
    }

    public void put(HttpParams params) {
        if(params != null) {
            HashMap<String, String> paramsKeyMap = params.getKeyMap();
            if(!HttpUtils.isEmpty(paramsKeyMap)) {
                this.keyMap.putAll(paramsKeyMap);
            }

            if(params.stringParamsMap != null && !params.stringParamsMap.isEmpty()) {
                this.stringParamsMap.putAll(params.stringParamsMap);
            }

            if(params.fileParamsMap != null && !params.fileParamsMap.isEmpty()) {
                this.fileParamsMap.putAll(params.fileParamsMap);
            }

        }
    }

    public void put(String key, int value, boolean... isReplace) {
        this.put(key, String.valueOf(value), isReplace);
    }

    public void put(String key, long value, boolean... isReplace) {
        this.put(key, String.valueOf(value), isReplace);
    }

    public void put(String key, float value, boolean... isReplace) {
        this.put(key, String.valueOf(value), isReplace);
    }

    public void put(String key, double value, boolean... isReplace) {
        this.put(key, String.valueOf(value), isReplace);
    }

    public void put(String key, char value, boolean... isReplace) {
        this.put(key, String.valueOf(value), isReplace);
    }

    public void put(String key, boolean value, boolean... isReplace) {
        this.put(key, String.valueOf(value), isReplace);
    }

    public void put(String key, String value, boolean... isReplace) {
        if(key != null && value != null) {
            this.keyMap.put(key, "");
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            List<String> stringParams = (List)this.stringParamsMap.get(key);
            if(stringParams == null) {
                stringParams = new ArrayList();
                this.stringParamsMap.put(key, stringParams);
            }

            if(replace) {
                ((List)stringParams).clear();
            }

            ((List)stringParams).add(value);
        }
    }

    public void putStringMap(Map<String, String> params, boolean... isReplace) {
        if(params != null && !params.isEmpty()) {
            Iterator var3 = params.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, String> entry = (Entry)var3.next();
                this.put((String)entry.getKey(), (String)entry.getValue(), isReplace);
            }

        }
    }

    public void putStringList(String key, List<String> values, boolean... isReplace) {
        if(key != null && values != null && !values.isEmpty()) {
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            if(replace) {
                List<String> params = (List)this.stringParamsMap.get(key);
                if(params != null) {
                    params.clear();
                }
            }

            Iterator var7 = values.iterator();

            while(var7.hasNext()) {
                String value = (String)var7.next();
                this.put(key, value, new boolean[]{false});
            }

        }
    }

    public void put(String key, File file, boolean... isReplace) {
        if(file != null) {
            this.put(key, file, file.getName(), isReplace);
        }
    }

    public void put(String key, File file, String fileName, boolean... isReplace) {
        this.put(key, file, fileName, HttpUtils.guessMimeType(fileName), isReplace);
    }

    public void put(String key, HttpParams.FileWrapper fileWrapper, boolean... isReplace) {
        if(fileWrapper != null) {
            this.put(key, fileWrapper.file, fileWrapper.fileName, fileWrapper.contentType, isReplace);
        }
    }

    public void put(String key, File file, String fileName, MediaType contentType, boolean... isReplace) {
        if(key != null && file != null) {
            this.keyMap.put(key, "");
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            List<HttpParams.FileWrapper> fileParams = (List)this.fileParamsMap.get(key);
            if(fileParams == null) {
                fileParams = new ArrayList();
                this.fileParamsMap.put(key, fileParams);
            }

            if(replace) {
                ((List)fileParams).clear();
            }

            ((List)fileParams).add(new HttpParams.FileWrapper(file, fileName, contentType));
        }
    }

    public void putFileMap(Map<String, File> params, boolean... isReplace) {
        if(params != null && !params.isEmpty()) {
            Iterator var3 = params.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, File> entry = (Entry)var3.next();
                this.put((String)entry.getKey(), (File)entry.getValue(), isReplace);
            }

        }
    }

    public void putFileList(String key, List<File> files, boolean... isReplace) {
        if(key != null && files != null && !files.isEmpty()) {
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            if(replace) {
                List<HttpParams.FileWrapper> params = (List)this.fileParamsMap.get(key);
                if(params != null) {
                    params.clear();
                }
            }

            Iterator var7 = files.iterator();

            while(var7.hasNext()) {
                File file = (File)var7.next();
                this.put(key, file, new boolean[]{false});
            }

        }
    }

    public void putFileWrapperList(String key, List<HttpParams.FileWrapper> fileWrappers, boolean... isReplace) {
        if(key != null && fileWrappers != null && !fileWrappers.isEmpty()) {
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            if(replace) {
                List<HttpParams.FileWrapper> params = (List)this.fileParamsMap.get(key);
                if(params != null) {
                    params.clear();
                }
            }

            Iterator var7 = fileWrappers.iterator();

            while(var7.hasNext()) {
                HttpParams.FileWrapper fileWrapper = (HttpParams.FileWrapper)var7.next();
                this.put(key, fileWrapper, new boolean[]{false});
            }

        }
    }

    public void putPath(String key, String value) {
        if(key != null && value != null) {
            this.keyMap.put(key, "");
            this.pathParamsMap.put(key, value);
        }
    }

    public void putQuery(String key, int value, boolean... isReplace) {
        this.putQuery(key, String.valueOf(value), isReplace);
    }

    public void putQuery(String key, long value, boolean... isReplace) {
        this.putQuery(key, String.valueOf(value), isReplace);
    }

    public void putQuery(String key, float value, boolean... isReplace) {
        this.putQuery(key, String.valueOf(value), isReplace);
    }

    public void putQuery(String key, double value, boolean... isReplace) {
        this.putQuery(key, String.valueOf(value), isReplace);
    }

    public void putQuery(String key, char value, boolean... isReplace) {
        this.putQuery(key, String.valueOf(value), isReplace);
    }

    public void putQuery(String key, boolean value, boolean... isReplace) {
        this.putQuery(key, String.valueOf(value), isReplace);
    }

    public void putQuery(String key, String value, boolean... isReplace) {
        if(key != null && value != null) {
            this.keyMap.put(key, "");
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            List<String> stringParams = (List)this.queryParamsMap.get(key);
            if(stringParams == null) {
                stringParams = new ArrayList();
                this.queryParamsMap.put(key, stringParams);
            }

            if(replace) {
                ((List)stringParams).clear();
            }

            ((List)stringParams).add(value);
        }
    }

    public void putQueryStringMap(Map<String, String> params, boolean... isReplace) {
        if(params != null && !params.isEmpty()) {
            Iterator var3 = params.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, String> entry = (Entry)var3.next();
                this.putQuery((String)entry.getKey(), (String)entry.getValue(), isReplace);
            }

        }
    }

    public void putQueryStringList(String key, List<String> values, boolean... isReplace) {
        if(key != null && values != null && !values.isEmpty()) {
            boolean replace = true;
            if(isReplace != null && isReplace.length > 0) {
                replace = isReplace[0];
            }

            if(replace) {
                List<String> params = (List)this.stringParamsMap.get(key);
                if(params != null) {
                    params.clear();
                }
            }

            Iterator var7 = values.iterator();

            while(var7.hasNext()) {
                String value = (String)var7.next();
                this.putQuery(key, value, new boolean[]{false});
            }

        }
    }

    public void removeUrl(String key) {
        this.stringParamsMap.remove(key);
    }

    public void removeFile(String key) {
        this.fileParamsMap.remove(key);
    }

    public void remove(String key) {
        this.removeUrl(key);
        this.removeFile(key);
    }

    public void clear() {
        this.stringParamsMap.clear();
        this.fileParamsMap.clear();
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

    public String[] getKeyArray() {
        List<String> keyList = this.getKeyList();
        if(HttpUtils.isEmpty(keyList)) {
            return null;
        } else {
            String[] excludeCommonParamKeys = new String[keyList.size()];

            for(int i = 0; i < keyList.size(); ++i) {
                excludeCommonParamKeys[i] = (String)keyList.get(i);
            }

            return excludeCommonParamKeys;
        }
    }

    public HashMap<String, String> getKeyMap() {
        return this.keyMap;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        Iterator var2;
        Entry entry;
        for(var2 = this.stringParamsMap.entrySet().iterator(); var2.hasNext(); result.append((String)entry.getKey()).append("=").append(entry.getValue())) {
            entry = (Entry)var2.next();
            if(result.length() > 0) {
                result.append("&");
            }
        }

        for(var2 = this.fileParamsMap.entrySet().iterator(); var2.hasNext(); result.append((String)entry.getKey()).append("=").append(entry.getValue())) {
            entry = (Entry)var2.next();
            if(result.length() > 0) {
                result.append("&");
            }
        }

        return result.toString();
    }

    public HttpParams deepClone() {
        HttpParams httpParams = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            httpParams = (HttpParams)ois.readObject();
        } catch (IOException var6) {
            var6.printStackTrace();
        } catch (ClassNotFoundException var7) {
            var7.printStackTrace();
        }

        return httpParams;
    }

    public static class FileWrapper implements Serializable {
        private static final long serialVersionUID = -2356139899636767776L;
        public File file;
        public String fileName;
        public transient MediaType contentType;
        public long fileSize;

        public FileWrapper(File file, String fileName, MediaType contentType) {
            this.file = file;
            this.fileName = fileName;
            this.contentType = contentType;
            this.fileSize = file.length();
        }

        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            out.writeObject(this.contentType.toString());
        }

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            this.contentType = MediaType.parse((String)in.readObject());
        }

        public String toString() {
            return "FileWrapper{file=" + this.file + ", fileName=" + this.fileName + ", contentType=" + this.contentType + ", fileSize=" + this.fileSize + "}";
        }
    }
}
