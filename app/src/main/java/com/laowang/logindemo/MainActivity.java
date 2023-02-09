package com.laowang.logindemo;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.XmlRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.databinding.ActivityMainBinding;
import com.laowang.logindemo.ui.login.LoginActivity;
import com.laowang.logindemo.util.ResourceProvider;

/**
 * 左侧导航的主页
 */
public class MainActivity extends AppCompatActivity {
    /**
     * AppBar配置 ？？？
     */
    private AppBarConfiguration mAppBarConfiguration;
    /**
     * 页面捆绑
     */
    private ActivityMainBinding binding;

    /**
     * 创建时
     *
     * @param savedInstanceState 保存的页面实例状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 充气（不管哪种Activity，必须充气） */
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /* 设置顶部条（可选） */
        setSupportActionBar(binding.appBarMain.toolbar);
        /* 漂浮动作按钮，添加单击监听器 */
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        /* 配置AppBar(吸顶条) */
        DrawerLayout drawer = binding.drawerLayout; // 根布局，本质就是一个部件，（每个页面可能不一样，此页面是抽屉布局）
        mAppBarConfiguration = new AppBarConfiguration.Builder( //？？？将每个菜单ID作为一组ID传递，因为每个菜单都应被视为顶级目的地
                R.id.nav_home, R.id.nav_management, R.id.nav_token_browser,R.id.nav_token_import)
                .setOpenableLayout(drawer)
                .build();
        /* 导航UI设置actionBar(动作交互条) 导航控制器(content_main，host-fragment包含了3个子片段 */
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        /* 导航UI配置 控制器，（左侧导航视图+控制器） */
        NavigationView navigationView = binding.navView;
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * 创建OptionsMenu 吸顶选项菜单时，框架运行肯定会执行这个方法，返回值才确定是否显示出来【重写此方法的意义在于：在Activity中注册Menu】
     *
     * @param menu 菜单
     * @return true应该表示确定显示，false表示不显示，那么return之前的代码应该是框架帮着做好了，就等着显示出来而已
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d("总会执行的方法体", "返回值确定是否显示出来，但准备工作是已经做完了的");
        // Inflate the menu; this adds items to the action bar if it is present.
        /* main指的是右上角的下拉菜单(OptionsMenu)。。。。有log out的那个
         * 我确定 这个 下拉菜单没有在 其它XML标签中被显示引用，但是在下方源码才引入了R.memu.main，如果说 menu是框架默认自带的，
         * 那么 菜单项写入到 /res/menu/main.xml中，再从代码里嵌入到 默认的menu里。【注意】res页面或者部件，要在源码使用先注册一下*/
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 监听菜单项的选中事件，而菜单项可不仅仅是 右上角的呦！还包括左上角的抽屉菜单！！！所以这个监听器能做很多事情！无比区分不同的菜单项！！
     * 1.单击Log out菜单项即注销user 退出 main进入login页面,LoginRepository该类的 logout（）方法已经做完这三件事了，只需要调用即可
     * @param item 选中菜单项
     * @return 是否显示变化
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Log.i("selected item id", item.getItemId() + ""); // 2131231224
//        Log.i("item order", item.getOrder() + "");        //100
//        Log.i("item title", item.getTitle().toString());  // Log out
        if (item.getTitle() != null && item.getTitle().toString().equals(ResourceProvider.getString(R.string.log_out))) { // 先判断选中项是否为Log out
            LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
            loginRepository.logout(MainActivity.this, LoginActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}