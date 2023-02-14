package com.laowang.logindemo.ui.management;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static int[] TAB_TITLES;

    private final Context mContext;

    private LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (loginRepository.getUser().getLevel().contains("1")) {
            if (TAB_TITLES == null || TAB_TITLES.length < 3) {
                TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
            }
        } else {
            if (TAB_TITLES == null || TAB_TITLES.length == 3) {
                TAB_TITLES = new int[]{R.string.tab_text_4};
            }
        }
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        if (loginRepository.getUser().getLevel().contains("1")) {
            return 3;
        }else {
            return 1;
        }
    }
}