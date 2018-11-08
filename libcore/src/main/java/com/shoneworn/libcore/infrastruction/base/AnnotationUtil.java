package com.shoneworn.libcore.infrastruction.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by chenxiangxiang on 2018/11/5.
 */

public class AnnotationUtil {

    public static BasePresenter getAnnotationValue(Class clazz) {
        Class clz = clazz;
        boolean clzHasAnno = clz.isAnnotationPresent(PresenterTyper.class);
        if (clzHasAnno) {
            PresenterTyper annotation = (PresenterTyper) clz.getAnnotation(PresenterTyper.class);
            Class<BasePresenter> clasz = annotation.value();
            if (clasz != null) {
                try {
                    Constructor<BasePresenter> constructor = clasz.getConstructor();
                    return constructor.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return null;
    }
}
