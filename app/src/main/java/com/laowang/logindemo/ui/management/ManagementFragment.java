package com.laowang.logindemo.ui.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.laowang.logindemo.databinding.FragmentManagementBinding;
import com.laowang.logindemo.util.ResourceProvider;

public class ManagementFragment extends Fragment {
//    private static AtomicInteger count = new AtomicInteger(0);
    private FragmentManagementBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        Log.e("管理页面","创建了"+count.incrementAndGet()+"次"); // 每次从其它页面切换到这个页面都会重新创建一次
        // 初始化 ResourceProvider 的上下文，注意，必须要在ManagementViewModel构造方法之前执行
        ResourceProvider.setmCtx(getContext());
        // 初始化 ManagementViewModel对象
        ManagementViewModel viewModel =
                new ViewModelProvider(this).get(ManagementViewModel.class);
        /* 充气 */
        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /* 页面部件的双向绑定 */
        final TextView textView = binding.textManagement;
        final TableLayout userList = binding.userList;
        /* 进入fragment更新用户列表 */
        viewModel.updateUserList(getContext());
        /* 视图模型 有数据的话 就会填充进 文本视图XML里！ */
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        viewModel.getTableRows().observe(getViewLifecycleOwner(), tableRows -> {
            // 添加表头
            TableRow head = viewModel.addTableHeadInRow(getContext());
            if (head.getParent() != null){
                ((TableLayout)(head.getParent())).removeAllViews();
            }
            userList.addView(head);
            for (TableRow tableRow : tableRows.values()) {
                /* 这是一个BUG，咱也不知道为啥tableRow会有一个父容器？！报错后看源码才找到的方法，
                * 既然tableRow有父容器，那么让父容器移除tableRow解除关系即可！！！真奇葩的BUG */
                if (tableRow.getParent()!=null){
                    ((TableLayout)(tableRow.getParent())).removeAllViews();
                }
                userList.addView(tableRow,userList.getChildCount());
            }
        });

        /* 选项页 */
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), requireActivity().getSupportFragmentManager());
        ViewPager viewPager = binding.viewpagerManagement;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabsManagement;
        tabs.setupWithViewPager(viewPager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}