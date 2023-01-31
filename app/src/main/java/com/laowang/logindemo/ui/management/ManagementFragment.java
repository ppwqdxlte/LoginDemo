package com.laowang.logindemo.ui.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.databinding.FragmentGalleryBinding;
import com.laowang.logindemo.util.ResourceProvider;

public class ManagementFragment extends Fragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // 初始化 ResourceProvider 的上下文，注意，必须要在ManagementViewModel构造方法之前执行
        ResourceProvider.setmCtx(getContext());
        // 初始化 ManagementViewModel对象
        ManagementViewModel viewModel =
                new ViewModelProvider(this).get(ManagementViewModel.class);
        /* 充气 */
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /* 选项页的文字视图，TODO 把文字替换成布局，1.按权限级别显示用户列表（序号，名称，级别，创建时间），1-全部，0-自己；
                     TODO 2.按权限显示页面底部操作tab页签 点什么就打开对应的fragment */
        final TextView textView = binding.textGallery;
        // 视图模型能拿出来文字就会填充进 文本视图XML里！
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}