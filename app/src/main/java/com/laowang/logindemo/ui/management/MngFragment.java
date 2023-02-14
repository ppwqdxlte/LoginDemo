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

    private FragmentManagementBinding binding;

    private MngViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ResourceProvider.setmCtx(getContext());
        viewModel = new ViewModelProvider(this).get(MngViewModel.class);
        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textManagement;
        final TableLayout userList = binding.userList;
        viewModel.updateUserList(getContext());
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        viewModel.getTableRows().observe(getViewLifecycleOwner(), tableRows -> {
            TableRow head = viewModel.addTableHeadInRow(getContext());
            if (head.getParent() != null){
                ((TableLayout)(head.getParent())).removeAllViews();
            }
            userList.addView(head);
            for (TableRow tableRow : tableRows.values()) {
                if (tableRow.getParent()!=null){
                    ((TableLayout)(tableRow.getParent())).removeAllViews();
                }
                userList.addView(tableRow,userList.getChildCount());
            }
        });
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getContext(), this.getChildFragmentManager());
        ViewPager viewPager = binding.viewpagerManagement;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabsManagement;
        tabs.setupWithViewPager(viewPager);
        userList.setClickable(true);
        listenTablerowClicked();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void listenTablerowClicked() {
        for (TableRow row : viewModel.getTableRows().getValue().values()) {
            if (!((TextView) row.getChildAt(0)).getText().toString().toLowerCase().contains("sn")) {
                row.setOnClickListener(null);
                row.setClickable(true);
                row.setOnClickListener(v -> {
                    String selectedName = ((TextView) row.getChildAt(1)).getText().toString();
                    viewModel.setmSelectedName(selectedName);
                });
            }
        }
    }
}