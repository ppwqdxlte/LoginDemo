package com.laowang.logindemo.ui.management;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.R;
import com.laowang.logindemo.databinding.FragmentManagementViewpagerBinding;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static AtomicInteger newInstanceTimes = new AtomicInteger(0);
    private static AtomicInteger placeFragOnCreateTimes = new AtomicInteger(0);
    private static AtomicInteger placeFragOncreateViewTimes = new AtomicInteger(0);

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentManagementViewpagerBinding binding;

    public static PlaceholderFragment newInstance(int index) {
        // 记录实例化次数
        Log.e("newInstanceTimes",newInstanceTimes.incrementAndGet()+"");

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("placeFragOnCreateTimes",placeFragOnCreateTimes.incrementAndGet()+"");

        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.e("placeFragOncreateViewTimes",placeFragOncreateViewTimes.incrementAndGet()+"");

        /* 先试一试 */
        /*if (binding!=null && binding.getRoot() != null) {
            Log.e("关键你进来了吗？","============================================");
            ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
            if (parent != null) {
                parent.removeView(binding.getRoot());
            }
            return binding.getRoot();
        }*/

        binding = FragmentManagementViewpagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sectionLabel;
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}