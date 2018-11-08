package com.shoneworn.myframework;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shoneworn.libcore.infrastruction.base.PresenterTyper;
import com.shoneworn.libcore.infrastruction._activity_fragment.BaseActivity;

@PresenterTyper(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> {
    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPresenter().showToast();getPresenter().showToast();
                initView();
            }
        },3000);

    }

    private void initView() {
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        transaction.add(R.id.container, homeFragment);
        transaction.commit();
        container.addView(new DefineLinearLayout(this));
    }

    public void showToast(){
        Toast.makeText(this, "MainActivity", Toast.LENGTH_SHORT).show();
    }
}
