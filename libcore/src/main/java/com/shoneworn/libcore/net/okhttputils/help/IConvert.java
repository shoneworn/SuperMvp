package com.shoneworn.libcore.net.okhttputils.help;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import java.lang.reflect.Type;

public interface IConvert {
    <T> T fromJson(String var1, Class<T> var2);

    <T> T fromJson(String var1, Type var2);
}
