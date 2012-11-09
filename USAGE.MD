# 介绍
* 在项目pom.xml文件中引用
	> `<dependency>`
	>
	> `<groupId>com.d.pulltorefresh</groupId>`
	>
	> `<artifactId>pulltorefresh</artifactId>`
	>
	> `<version>1.4.2</version>`
	>
	> `<type>apklib</type>`
	>
	> `</dependency>`
	>
	
* 在layout文件中使用pulltorefreshlistview
	>`<com.handmark.pulltorefresh.library.PullToRefreshListView`
	>
	> ```xmlns:ptr="http://schemas.android.com/apk/res-auto"```
	>
	>```android:id="@+id/pull_refresh_list"```
	>
	>```android:layout_width="fill_parent"```
    >
    >```android:layout_height="fill_parent"```
    >
    >```android:cacheColorHint="#00000000"```
    >
    >```android:divider="#19000000"```
    >
    >```ptr:ptrAnimationStyle="flip" />```
* 使用pulltorefreshlistview的时候如果使用xml来配置一定要引入命名空间```xmlns:ptr="http://schemas.android.com/apk/res-auto"```

# 命名空间介绍
* 以ptr开头
* ptr:ptrAdapterViewBackground (默认系统背景色)
	* 设置显示内容list的背景颜色
* ptr:ptrHeaderBackground
	* 上拉/下拉刷新区域的背景颜色 (默认系统背景色)
* ptr:ptrHeaderTextColor
	* 上拉/下拉刷新区域的标题文字颜色 (默认系统文字颜色)
* ptr:ptrHeaderSubTextColor
	* 上拉/下拉刷新区域的副标题文字颜色 (默认系统文字颜色)
* ptr:ptrMode 分四种模式
	* disabled 关闭上下拉刷新
	* pullDownFromTop 仅打开下拉刷新 (默认状态)
	* pullUpFromBottom 仅打开上拉刷新
	* both 上拉 下拉都打开
* ptr:ptrShowIndicator
	* true or false 是否打开小块箭头 (默认false)
* ptr:ptrDrawable
	* drawable 标题前区域素材(不区分上下拉刷新)
* ptr:ptrDrawableTop
	* drawable 下拉刷新素材
* ptr:ptrDrawableBottom
	* drawable 上拉刷新素材
* ptr:ptrOverScroll
	* true or false 越界效果(默认关闭)
* ptr:ptrHeaderTextAppearance
	* 字体大小 (栗子: ```?android:attr/textAppearanceMedium```)
* ptr:ptrSubHeaderTextAppearance
	* 副标题文字大小
* ptr:ptrAnimationStyle 动画样式 (只能使用xml来指定 不能通过代码来配置)
	* rotate 旋转图片
	* flip 上下箭头样式(默认状态)
* ptr:ptrPullDownLabel
	* string 下拉刷新标题文字	
* ptr:ptrPullUpLabel
	* string 上拉刷新标题文字	
* ptr:ptrPullUpRlsLabel
	* string 上拉刷新释放标题文字
* ptr:ptrPullDownRlsLabel
	* string 下拉刷新释放标题文字
* ptr:ptrRefreshingLabel
	* string 刷新标题文字

# 使用
* setOnRefreshListener 在上拉或下拉刷新时触发
* setOnAutoLoadMoreListener 在ptr:ptrMode为pullDownFromTop是才能起效果 在最后的时候自动加载更多的时候出发 重写相应方法完成加载
	* 刷新完成时候调用 onLoadMoreComplete()方法 
	* 如果无数据可以调用 onNoLoadMore(String label) 方法 设置需要显示的文本
* setOnRefreshListener2 分别重写上拉下拉刷新方法
	* 完成后调用 onRefreshComplete() 结束刷新界面展示
* SoundPullEventListener 拉动事件声音监听器,重写相应方法可以在拉动的不同状态发出传入的声音文件的声音 将对象 setOnPullEventListener(new SoundPullEventListener)
	* 举个栗子
	 > SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(this);
	 > soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
	 > soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
	 > soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
	 > mPullRefreshListView.setOnPullEventListener(soundListener);

* setDisableScrollingWhileRefreshing 设置是否可以在刷新进行的时候进行Listview的滚动
* emptyView使用,可以设置下拉刷新的view的emptyview
	* 举个栗子:
	> TextView tv = new TextView(this);
	> tv.setGravity(Gravity.CENTER);
	> tv.setText("Empty View, Pull Down/Up to Add Items");
	> mPullRefreshGridView.setEmptyView(tv);

