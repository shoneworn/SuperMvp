package com.shoneworn.supermvp.common.serverbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

public class ServerPrettyGirl implements Serializable {
    public String _id;
    public String createdAt;
    public String desc;
    public String type;
    public String url;
    public String who;

    public boolean error;
    public List<ServerPrettyGirl> results;
}
