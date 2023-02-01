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
                // 保留表头
                if (userList.getChildCount()>1){ //不确定是否直接子view就是TableRow...
                    userList.removeViews(1,userList.getChildCount() - 1);//不确定是否从0开始
                }
                for (TableRow tableRow : tableRows.values()) {
                    userList.addView(tableRow);
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