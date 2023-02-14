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

public class Main0Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMain0Binding binding0;

    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() != null && item.getTitle().toString().equals(ResourceProvider.getString(R.string.log_out))) {
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