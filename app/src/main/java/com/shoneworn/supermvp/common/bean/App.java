package com.shoneworn.supermvp.common.bean;

import java.io.Serializable;

/**
 * Created by chenxiangxiang on 2019/1/3.
 */

public class App implements Serializable {
    private String id;
    private String name;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
