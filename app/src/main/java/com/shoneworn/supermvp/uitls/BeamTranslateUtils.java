package com.shoneworn.supermvp.uitls;

import com.shoneworn.libcore.utils.Utils;
import com.shoneworn.supermvp.common.bean.PrettyGirl;
import com.shoneworn.supermvp.common.serverbean.ServerPrettyGirl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiangxiang on 2019/1/11.
 */

public class BeamTranslateUtils {

    public static List<PrettyGirl> toPrettyGirls(List<ServerPrettyGirl> list){
        List<PrettyGirl> prettyGirlList = new ArrayList<>();
        if(Utils.isEmpty(list)) return prettyGirlList;
        for(ServerPrettyGirl serverPrettyGirl:list){
            PrettyGirl prettyGirl = new PrettyGirl();
            prettyGirl.set_id(serverPrettyGirl._id);
            prettyGirl.setCreatedAt(serverPrettyGirl.createdAt);
            prettyGirl.setDesc(serverPrettyGirl.desc);
            prettyGirl.setType(serverPrettyGirl.type);
            prettyGirl.setUrl(serverPrettyGirl.url);
            prettyGirl.setWho(serverPrettyGirl.who);
            prettyGirlList.add(prettyGirl);
        }
        return prettyGirlList;
    }

}
