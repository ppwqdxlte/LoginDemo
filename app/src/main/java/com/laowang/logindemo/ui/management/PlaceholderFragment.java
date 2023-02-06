package com.laowang.logindemo.ui.management;

import android.os.Bundle;
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
        /* UI选项卡片的文字区域 */
        final TextView textView = binding.sectionLabel;
        /* 【后端】注入数据到【UI前端】 从onCreate()调用viewModel.index_setter()后viewModel.text_getter()的结果就产生变化了...new Observer<String>() {...} */
        /* 此处逻辑非常有【意思】：一旦后端ViewModel的getter()获取的mText有变化，UI界面就发生变化 */
        pageViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        /* 选项卡片的显示内容在这里修改，因为2个卡片UI都不一样，最好动态代码修改，而不是写第三层的fragment，
            因为我估计childFragmentManager只能管理到直接子类页面，更深的后代页面就管不到了。 */
        renderTabContainer();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 渲染选项卡片容器，不同的卡片有不同的组件组合
     */
    private void renderTabContainer() {
        if (loginRepository.getUser().getLevel().contains("1")) {
            int sectionNumber = this.getArguments() != null ? this.getArguments().getInt(ARG_SECTION_NUMBER) : 0;
            if (sectionNumber == 1) {
                if (tabName == null || !tabName.equals(ResourceProvider.getString(R.string.tab_text_1))) {
                    tabName = ResourceProvider.getString(R.string.tab_text_1);
                    renderCreateUserTabcard();
                }
            } else if (sectionNumber == 2) {
                if (tabName == null || !tabName.equals(ResourceProvider.getString(R.string.tab_text_2))) {
                    tabName = ResourceProvider.getString(R.string.tab_text_2);
                    renderModifyUserTabcard();
                }
            } else {
                if (tabName == null || !tabName.equals(ResourceProvider.getString(R.string.tab_text_3))) {
                    tabName = ResourceProvider.getString(R.string.tab_text_3);
                    renderDeleteUserTabcard();
                }
            }
        } else {
            if (tabName == null) {
                tabName = ResourceProvider.getString(R.string.tab_text_4);
                renderChangePasswordTabcard();
            }
        }
    }

    private void renderCreateUserTabcard() {
        // TODO 增减渲染卡片
    }

    private void renderModifyUserTabcard() {
        // TODO 增减渲染卡片
    }

    private void renderDeleteUserTabcard() {
        // TODO 增减渲染卡片
    }

    private void renderChangePasswordTabcard() {
        // TODO 增减渲染卡片
    }
}