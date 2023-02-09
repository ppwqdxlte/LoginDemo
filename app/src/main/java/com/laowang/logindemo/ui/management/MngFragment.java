package com.laowang.logindemo.ui.management;

import android.os.Bundle;
import android.util.Log;
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

public class MngFragment extends Fragment {
//    private static AtomicInteger count = new AtomicInteger(0);
    private FragmentManagementBinding binding;

    private MngViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        Log.e("管理页面","创建了"+count.incrementAndGet()+"次"); // 每次从其它页面切换到这个页面都会重新创建一次
        // 初始化 ResourceProvider 的上下文，注意，必须要在ManagementViewModel构造方法之前执行
        ResourceProvider.setmCtx(getContext());
        // 初始化 ManagementViewModel对象
        viewModel = new ViewModelProvider(this).get(MngViewModel.class);
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
        /* 选项页 有一个BUG，首次访问此页面选项卡片都正常，但是以后再从别的页面点回来时，选项卡片就不显示了， 应该是生命周期的问题
        *       可以确定Fragment每次点进都会创建一次，那么就是PageViewModel问题了
        * 【更正】也不是PageViewModel的问题，期间我尝试了很多，没想到最终解决问题的方法居然是 this.getChildFragmentManager()！！
        * 把父fragment的 SectionsPagerAdapter 区域传呼适配器 构造传参的 父FragmentManager 改成 ChildFragmentManager!!!!! */
        /* 【后端】绑【后端】【区段寻呼机适配器】顾名思义，就是把不同段区的设备连接起来，是指可以互相转换通信，tabLayout中的适配器就是把 father View和 son Views之间建立一个信道 */
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), this.getChildFragmentManager());
        /*　UI（XML）视图适配器 */
        ViewPager viewPager = binding.viewpagerManagement;
        /*　【后端】绑【前端】后端适配器绑定到UI适配器　*/
        viewPager.setAdapter(sectionsPagerAdapter);
        /*　UI选项卡布局　*/
        TabLayout tabs = binding.tabsManagement;
        /*　【前端】绑【前端】UI视图适配器从逻辑层面绑定到UI选项卡布局。【细节】XML中TabLayout和ViewPager是分开的，结构上并不包含　*/
        tabs.setupWithViewPager(viewPager);
        /* TableLayout每一项绑定单击事件，单击 table-row（除了表头），用户名显示在selectedName文本区内 */
        userList.setClickable(true);
        listenTablerowClicked();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        getViewModelStore().clear(); 感觉没必要清空，因为页面还要用到该对象
    }

    /**
     * 监听用户列表的单击事件
     */
    private void listenTablerowClicked() {
        for (TableRow row : viewModel.getTableRows().getValue().values()) {
            if (!((TextView) row.getChildAt(0)).getText().toString().toLowerCase().contains("sn")) {
                // 先清空
                row.setOnClickListener(null);
                row.setClickable(true);
                // 再创建
                row.setOnClickListener(v -> {
                    // 我直接在这里改状态行不？不调用控件了行不？让控件跟随 状态变化行不？？？Multable<String> selectedName...
                    String selectedName = ((TextView) row.getChildAt(1)).getText().toString();
                    viewModel.setmSelectedName(selectedName);
                });
            }
        }
    }
}