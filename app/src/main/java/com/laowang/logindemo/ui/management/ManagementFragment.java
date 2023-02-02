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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.databinding.FragmentManagementBinding;
import com.laowang.logindemo.util.ResourceProvider;

import java.util.Map;

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
        viewModel.getTableRows().observe(getViewLifecycleOwner(), new Observer<Map<String,TableRow>>() {
            @Override
            public void onChanged(Map<String,TableRow> tableRows) {
                Log.e("how many tablelayout's child?",userList.getChildCount()+"");
                View childAt = userList.getChildAt(0);
                Log.e("child is",childAt instanceof TableRow ? "table row":childAt !=null?childAt.getClass().getName():"userList目前还么有子元素");
                Log.e("How may table rows?",tableRows.keySet().stream().count()+"");
//                userList.removeAllViews();
//                userList.removeAllViewsInLayout();
                // 添加表头
                TableRow head = viewModel.addTableHeadInRow(getContext());
                if (head.getParent() != null){
                    ((TableLayout)(head.getParent())).removeAllViews();
                }
                userList.addView(head);
                int c = 0;
                for (TableRow row : tableRows.values()) {
                    Log.d("数据库查出来的第"+(++c)+"个table row",((TextView)(row.getChildAt(1))).getText().toString());
                }
                for (TableRow tableRow : tableRows.values()) {
//                    Log.e("tableRow",tableRow == null?"null":((TextView)tableRow.getChildAt(0)).getText().toString());
                    Log.e("tableLayout-userList到底几个儿子？",userList.getChildCount()+"");
                    Log.e("tableLayout-userList 第一个儿子长啥样啊？",
                            userList.getChildCount()>0?(userList.getChildAt(0) instanceof TableRow ? "TableRow":userList.getChildAt(0).getRootView().toString()):"userList没有子元素，真奇怪啊");
//                    Log.e("tableRow",tableRow == null?"null":((TextView)tableRow.getChildAt(0)).getText().toString());
                    Log.e("userList has ",userList.getChildCount() + "个儿子。");
                    /* 这是一个BUG，咱也不知道为啥tableRow会有一个父容器？！报错后看源码才找到的方法，
                    * 既然tableRow有父容器，那么让父容器移除tableRow解除关系即可！！！真奇葩的BUG */
                    if (tableRow.getParent()!=null){
                        Log.e("tableRow的parent是啥呢？",tableRow.getParent().getClass().getName());
                        Log.e("u的parent是啥呢？",tableRow.getParent().getClass().getName()); // TaybleLayout
                        ((TableLayout)(tableRow.getParent())).removeAllViews();
                    }
                    userList.addView(tableRow,userList.getChildCount());
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void addUser(LoggedInUser newUser){
        // TODO 添加用户
    }

    private void modifyPwd(LoggedInUser user){
        // TODO 修改密码
    }

    private void deleteUser(LoggedInUser user){
        // TODO 删除用户
    }
}