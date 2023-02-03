package com.laowang.logindemo.ui.management;

import android.content.Context;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * 【注意】之所以把基类从 FragmentPagerAdapter 换成 FragmentStatePagerAdapter，
 * 是为了避免因【缓存】而产生的不必要的问题，比如首次显示，后续再次进入页面后空白的问题
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * UI tab页签的文字 资源值数组
     */
    @StringRes
    private static int[] TAB_TITLES;
    /**
     * 适配器所在的上下文（Context/View/Activity/Fragment...）
     */
    private final Context mContext;
    /**
     * 用来获取当前登录用户
     */
    private LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    /**
     * @param context 上下文环境
     * @param fm      页面管理器（根据代码实际表示的是context的子视图管理器）
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * 代码走查 ALT+F7 并不能找到此方法的明确调用位置，实际上父类页面 ManagementFragment对象onCreateView()中
     * TabLayout对象通过绑定ViewPager对象，再绑定了SectionsPagerAdapter对象，从而绑定了ManagementFragment对象和ChildFragmentManage对象，
     * 一旦TabLayout选项卡片产生【翻页事件】，就会触发此方法来获取 TabItem对象，而选中UI页签的index就是入参的position，
     * 自此，PlaceholderFragment实例对象就进入了子页面管理器由 sectionsPagerAdapter管理
     *
     * @param position tab页签的位置索引
     * @return 子页面
     */
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    /**
     * TabLayout UI部件等到 SectionsPagerAdapter对象 初始化后就调用此方法渲染页签文字
     *
     * @param position tab页签的位置索引
     * @return 字符序列 tab页签上显示的文字
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // 按照权限修改 TAB_TITLES,准备2套！
        if (loginRepository.getUser().getLevel().contains("1")) {
            if (TAB_TITLES == null || TAB_TITLES.length < 2) {
                TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
            }
        } else {
            if (TAB_TITLES == null || TAB_TITLES.length == 2) {
                TAB_TITLES = new int[]{R.string.tab_text_3};
            }
        }
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * 先确定 tab页签 数量，再调用 getPageTitle()确定 页签文字
     *
     * @return Tab页签的数量
     */
    @Override
    public int getCount() {
        // 依据当前登录的用户权限展示不同的页签，管理员2个（创建用户，修改用户（密码）），普通用户1个（修改密码）
        if (loginRepository.getUser().getLevel().contains("1")) {
            return 2;
        }else {
            return 1;
        }
    }
}