package com.shoneworn.supermvp.business.home.model;

import com.shoneworn.libcore.common.net.listener.RequestDataListener;
import com.shoneworn.libcore.net.okhttputils.OkHttpUtils;
import com.shoneworn.libcore.net.okhttputils.model.HttpParams;
import com.shoneworn.libcore.utils.Utils;
import com.shoneworn.supermvp.common.base.ModelWrapper;
import com.shoneworn.supermvp.common.bean.PrettyGirl;
import com.shoneworn.supermvp.common.net.callback.JsonCallbackCtx;
import com.shoneworn.supermvp.common.serverbean.ServerPrettyGirl;
import com.shoneworn.supermvp.uitls.BeamTranslateUtils;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

public class HomeModel extends ModelWrapper {
    HttpParams params = new HttpParams();
    private static String URL = "https://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";

    public static HomeModel getInstance() {
        return getInstance(HomeModel.class);
    }

    public void httpTest(Object tag, final int page, final RequestDataListener<ServerPrettyGirl, List<PrettyGirl>> listener) {
        params.clear();
        OkHttpUtils.get(URL + page)
                .tag(tag)
                .execute(new JsonCallbackCtx<ServerPrettyGirl>() {
                    @Override
                    public void onSuccess(ServerPrettyGirl serverPrettyGirl, Call var2, okhttp3.Response var3) {
                        if (Utils.isEmpty(serverPrettyGirl) || serverPrettyGirl.error) {
                            listener.onError("", "", null, null);
                        }
                        List<PrettyGirl> list = BeamTranslateUtils.toPrettyGirls(serverPrettyGirl.results);
                        listener.onSuccess(list, serverPrettyGirl, var3);
                    }

                    @Override
                    public void onError(String code, String msg, Call call, Response response, Exception e) {
                        super.onError(code, msg, call, response, e);
                        listener.onError(code, msg, response, e);
                    }
                });

    }

}
