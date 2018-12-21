package com.shoneworn.supermvp.business.home.presenter;

import com.shoneworn.libcore.infrastruction._activity_fragment.PresenterWrapper;
import com.shoneworn.supermvp.business.home.ui.Htab1Fragment;
import com.shoneworn.supermvp.common.Bean.MySection;
import com.shoneworn.supermvp.common.Bean.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxiangxiang on 2018/12/20.
 */

public class Htab1Presenter extends PresenterWrapper<Htab1Fragment> {
    public List<MySection> createData() {
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "本月"));
        list.add(new MySection(new Type("佣金明细", "2017.01.10")));
        list.add(new MySection(new Type("佣金明细", "2017.03.15")));
        list.add(new MySection(true, "上月"));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(true, "今年"));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        list.add(new MySection(new Type("佣金累计", "2018.03.15")));
        return list;
    }
}
