package com.laowang.logindemo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
import com.laowang.logindemo.databinding.ActivityMain0Binding;
import com.laowang.logindemo.databinding.ActivityMainBinding;
import com.laowang.logindemo.ui.login.LoginActivity;
import com.laowang.logindemo.util.ResourceProvider;

/**
 * 【普通用户】左侧导航的主页
 */
public class Main0Activity extends AppCompatActivity {
    /**
     * AppBar配置 ？？？
     */
    private AppBarConfiguration mAppBarConfiguration;
    /**
     * 页面捆绑
     */
    private ActivityMain0Binding binding0;

    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
    /**
     * 创建时
     *
     * @param savedInstanceState 保存的页面实例状态
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding0 = ActivityMain0Binding.inflate(getLayoutInflater());
        setContentView(binding0.getRoot());
        setSupportActionBar(binding0.appBarMain0.toolbar);
        binding0.appBarMain0.fab0.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer0 = binding0.drawerLayout0;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_management,R.id.nav_token_browser)
                .setOpenableLayout(drawer0)
                .build();
        NavController navController0 = Navigation.findNavController(this,R.id.nav_host_fragment_content_main0);
        NavigationUI.setupActionBarWithNavController(this,navController0,mAppBarConfiguration);
        NavigationView navigationView0 = binding0.navView0;
        NavigationUI.setupWithNavController(navigationView0,navController0);
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
            loginRepository.logout(Main0Activity.this, LoginActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController0 = Navigation.findNavController(this, R.id.nav_host_fragment_content_main0);
        return NavigationUI.navigateUp(navController0, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}