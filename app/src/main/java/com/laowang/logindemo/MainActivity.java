package com.laowang.logindemo;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.laowang.logindemo.databinding.ActivityMainBinding;
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
    /** 创建时
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
                // TODO:可以把导出功能写在这个按钮上
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        /* 配置AppBar(吸顶条) */
        DrawerLayout drawer = binding.drawerLayout; // 根布局，本质就是一个部件，（每个页面可能不一样，此页面是抽屉布局）
        mAppBarConfiguration = new AppBarConfiguration.Builder( //？？？将每个菜单ID作为一组ID传递，因为每个菜单都应被视为顶级目的地
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        /* 导航UI设置actionBar(动作交互条) 导航控制器(content_main，host-fragment包含了3个子片段 */
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        /* 导航UI配置 控制器，（左侧导航视图+控制器） */
        NavigationView navigationView = binding.navView;
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    /** 创建OptionsMenu 吸顶选项菜单时
     * @param menu 菜单
     * @return ？？
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /* main指的是右上角的下拉菜单(OptionsMenu)。。。。有log out的那个 */
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}