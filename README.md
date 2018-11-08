# SuperMVP

#### 项目介绍
作为架构，目前只处理了V和P层，并没有处理M层。架构的目的就是方便V和P之间的通讯。
SuperMvp带来了什么好处？ 意义是很明显的。当你需要使用Presenter里的方法时，一个getPresenter()就可以直接调用了,当你需要在Presenter里使用Activity的方法时，getview().method 就可直接使用。不用指定特定接口让activity去回调。没有繁琐步骤，想用就用。

完整的框架需要有一下几个组成部分：

1.MVP架构

2.网络层(也叫接口层，包含数据请求及回调，数据处理等各种api)

3.数据层(包含数据持久化等各项操作)

4.异常处理及日志上报

如果有了这些，app该怎么做？ 直接填数据和业务！对业务层继续封装。

#### Activity的使用

    @PresenterTyper(MainPresenter.class)
    public class MainActivity extends BaseActivity<MainPresenter> {
        LinearLayout container;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            container = findViewById(R.id.container);
            getPresenter().showToastFromActivity();
        }

        public void showToastFromPresenter() {
            Toast.makeText(this, "MainActivity", Toast.LENGTH_SHORT).show();
        }
    }

使用的时候，一定要在Activity上面加上注解@PresenterTyper 值为当前Activity对应的Presenter的class 。继承的BaseActivity对应的泛型也是必填项，值为对应的Presenter .

#### Prenseter使用
    public class MainPresenter extends PresenterWrapper<MainActivity> {

        @Override
        public void onResume() {
            super.onResume();
            getView().showToastFromPresenter();

        }

        public void showToastFromActivity(){
            Toast.makeText(getActivity(), "在presenter里", Toast.LENGTH_SHORT).show();
        }
    }
Prenster里一定要注意的是，需要集成对应的PresenterWrapper， Activity和Fragment对应的是_activity_fragment 目录下的PresenterWrapper ，自定义ViewGroup则对应的是_View目录下的PresenterWrapper . 切记，不要用混。
另外，一定要注意的是： PresenterWrapper<> 这里的泛型值，必须为何Presenter对应的Activity 。这里为MainActivity  ，一定要加上。千万不能用错。


#### View中使用Mvp
和在activity中一样。也是配置两个地方。 view 和presenter
先看在View中

    @PresenterTyper(DefineLinearPresenter.class)
    public class DefineLinearLayout extends BaseLinearLayout<DefineLinearPresenter> {
    TextView tv;

    public DefineLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        layoutInflater.inflate(R.layout.liear_define,this);
        tv = $(R.id.tv);
        getPresenter().showText();
    }

    public void setText(){
        tv.setText("我时陈祥祥");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setViewListener() {

    }
    }

和在Activity中使用方式一样。


接下来看看Presenter

    public class DefineLinearPresenter extends PresenterWrapper<DefineLinearLayout> {

    public void showText(){
        getView().setText();
        Toast.makeText(ctx, "ctx", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "getActivity", Toast.LENGTH_SHORT).show();
    }
    }


OK , 讲解完毕。后续持续更新添加新的功能。



