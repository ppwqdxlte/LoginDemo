package com.laowang.logindemo.ui.management;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.databinding.FragmentManagementViewpagerBinding;
import com.laowang.logindemo.util.ResourceProvider;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentManagementViewpagerBinding binding;
    /**
     * LoginRepository对象贯穿真个app生命周期，故而可以在别的类中添加这种成员变量，如果换成
     */
    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
    /**
     * 静态时有缓存的效果，缓存当前显示的选项卡名称
     */
    private static String tabName;

    /**
     * 获得选项卡片的实例并确定 页面索引index
     *
     * @param index 选项卡片的index索引，从1开始
     * @return 选项卡片实例
     */
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * newInstance()构造方法之后，View视图创建之前。初始化或获取已有的 PageViewModel对象，并给该视图模型注入 index索引
     *
     * @param savedInstanceState 一捆fragment实例状态
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 初始化 pageViewModel视图模型对象 */
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        /* 视图模型设置index */
        pageViewModel.setIndex(index);
    }

    /**
     * 创建UI视图，用ViewModel对象【监视】属性变化来【影响】UI显示
     *
     * @param inflater           UI视图充气机
     * @param container          UI视图容器（包括父视图，视图管理器等）
     * @param savedInstanceState 一捆fragment实例状态
     * @return UI视图
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentManagementViewpagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /* 渲染tab卡片 */
        renderTabs();
        /* 单击CANCEL清空输入 */
        binding.sectionBtnCancel.setOnClickListener(v -> cleanInputs());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        /* 根据网友关于【数据倒灌】的源码分析产生的灵感，就是在销毁 fragment 时候防止乱七八糟的设置而导致无法清空ViewModel对象，还没写交互代码，但愿可以解决之前的BUG
        * 【上句话简直是一厢情愿】，还没写tab逻辑呢，从home或者token页面跳回management页面时候，依然有数据倒灌！那到底应该怎么彻底清空呢？
        * 【上句话也是不明白】，input的用户密码啥的会保存，也许这种现象不算是 ViewModel 的【数据倒灌】！而PageViewModel中的fromState对象才是需要清空的！
        * 【根据业务需要】 ManagementViewModel对象（因为保存用户列表，create不能重名就得查它）不必清空，而tab页面的viewModel需要清空，故而只在这里调用即可 */
        getViewModelStore().clear();
    }

    /**
     * 保存实例状态时候的操作
     *
     * @param outState 当前页面的外部页面（父页面）的一捆状态
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        默认保存父类View的状态，如果不注释掉，那么 this.getActivity()获取的父容器已经销毁了就会报空指针异常
//        super.onSaveInstanceState(outState);
    }

    /**
     * 渲染选项卡片容器，不同的卡片有不同的组件组合
     */
    private void renderTabs() {
        if (loginRepository.getUser().getLevel().contains("1")) {
            int sectionNumber = this.getArguments() != null ? this.getArguments().getInt(ARG_SECTION_NUMBER) : 0;
            if (sectionNumber == 1) {
                if (tabName == null || !tabName.equals(ResourceProvider.getString(R.string.tab_text_1))) {
                    tabName = ResourceProvider.getString(R.string.tab_text_1);
                    renderCreateUserTab();
                }
            } else if (sectionNumber == 2) {
                if (tabName == null || !tabName.equals(ResourceProvider.getString(R.string.tab_text_2))) {
                    tabName = ResourceProvider.getString(R.string.tab_text_2);
                    renderModifyUserTab();
                }
            } else {
                if (tabName == null || !tabName.equals(ResourceProvider.getString(R.string.tab_text_3))) {
                    tabName = ResourceProvider.getString(R.string.tab_text_3);
                    renderDeleteUserTab();
                }
            }
        } else {
            if (tabName == null) {
                tabName = ResourceProvider.getString(R.string.tab_text_4);
                renderChangePasswordTab();
            }
        }
    }

    private void cleanInputs() {
        binding.sectionUsernameSelected.setText(null);
        binding.sectionUsernameNew.setText(null);
        binding.sectionPasswordOld.setText(null);
        binding.sectionPasswordNew.setText(null);
        binding.sectionPasswordRepeat.setText(null);
        binding.sectionAdministrator.setChecked(false);
        binding.sectionRegularUser.setChecked(false);
    }

    private void renderCreateUserTab() {
        binding.sectionUsernameSelected.setVisibility(View.GONE);
        binding.sectionPasswordOld.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
        /* 【问题来了】到底在这里绑定控件事件呢还是在外面绑定呢？【答】这里，因为可以减少不必要编译
        *  【如何避免state状态混乱的 BUG ？】【答】还记得【数据倒灌】吗？就是你其它地方输入状态在切换tab或者重新
        * 跳入管理页时候都会显示不该显示的提示，诸如此类都属于该现象，so有必要 getViewModelStore().clear()　*/
    }

    private void renderModifyUserTab() {
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }

    private void renderDeleteUserTab() {
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPasswordOld.setVisibility(View.GONE);
        binding.sectionPasswordNew.setVisibility(View.GONE);
        binding.sectionPasswordRepeat.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnConfirm.setVisibility(View.GONE);
    }

    private void renderChangePasswordTab() {
        binding.sectionUsernameSelected.setVisibility(View.GONE);
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }
}