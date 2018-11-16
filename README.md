# SuperMVP
#### 初衷
做开发也有些年了。项目也做过不少，都是不停地码代码，有自己从0开始的，有从其他人手里接锅的，经验多了，就发现很多东西值得备份起来，以备不时之需。但是每个项目都有自己的收获，下一个项目做完后，就想推翻上一个使用的策略和方法，随着经验丰富起来，及时机成熟，开始想要封装一套框架，用来满足以后项目的开发需求。在框架写了一部分后，恰好读到一篇好文章，作者也和我一样，不过，他已经搭好自己的框架，而我，还在启动阶段。这更坚定了我造轮子的决心。文章地址：https://www.jianshu.com/p/77dd326f21dc  他的开源项目：https://github.com/getActivity/AndroidProject

#### 项目介绍
作为架构，目前只处理了V和P层，并没有处理M层。架构的目的就是方便V和P之间的通讯。
SuperMvp带来了什么好处？ 意义是很明显的。当你需要使用Presenter里的方法时，一个getPresenter()就可以直接调用了,当你需要在Presenter里使用Activity的方法时，getview().method 就可直接使用。不用指定特定接口让activity去回调。没有繁琐步骤，想用就用。

SuperMvp 目前规划为：

1.MVP架构  
2.网络层(也叫接口层，包含数据请求及回调，数据处理等各种api)  
3.数据层(包含数据持久化等各项操作)  
4.异常处理及日志上报  
5.通用控件的封装  
如果有了这些，app该怎么做？ 直接填数据和业务！对业务层继续封装。

SuperMvp 支持哪些功能？  
1.沉浸式状态栏  (https://github.com/gyf-dev/ImmersionBar)
2.懒加载fragment  
3.软键盘顶起view的处理方法。(两种方式，最终实现基本一直。一种是布局时采用固定位置。第二种就是，采用相对布局，等界面绘制完成后，根据所在位置，设置成固定位置)  
4.动态权限申请  (https://github.com/getActivity/XXPermissions)
5.侧滑删除activity (https://github.com/bingoogolapple/BGASwipeBackLayout-Android)
6.无限轮播viewpager (https://github.com/H07000223/FlycoBanner_Master)
7.下拉刷新，上拉加载  (https://github.com/scwang90/SmartRefreshLayout)
8.behavior的使用
9.水平滑动的顶部导航栏 (https://github.com/ogaclejapan/SmartTabLayout)

。。。。。。。持续扩展中。。。。。。。。。。
#### MVP架构
里面有不少值得分享的地方，比如泛型，注解等解决了view与presenter之间直接的调用。当然，如果仔细阅读代码，你会发现，我怎么没有初始化presenter 就直接用起来了。view里的getActivity()从哪里来的？activity里没有传给他呀。MVP只能用在Activity里吗？ 阅读代码，你会找到答案。
##### Activity的使用

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

##### Prenseter使用
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

##### View中使用Mvp
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
        tv.setText("我是陈祥祥");
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



