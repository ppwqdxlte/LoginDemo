package com.laowang.logindemo.ui.token;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.model.TokenType;
import com.laowang.logindemo.databinding.FragmentTokenreadBinding;

import java.util.Map;

public class TokenReadFragment extends Fragment {

    private FragmentTokenreadBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TokenReadViewModel.fragment = this;
        TokenReadViewModel tokenReadViewModel = new ViewModelProvider(this).get(TokenReadViewModel.class);

        binding = FragmentTokenreadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTokenRead;
        tokenReadViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        binding.btnQuery.setOnClickListener(v -> {
            String tokenStr = binding.editMeterString.getText().toString();
            if (binding.rbKct.isChecked()) {
                tokenReadViewModel.queryTokens(getContext(),TokenType.KCT, tokenStr);
            } else if (binding.rbTcc.isChecked()) {
                tokenReadViewModel.queryTokens(getContext(),TokenType.TCC, tokenStr);
            } else {
                tokenReadViewModel.setmTokenResult(new TokenResult(null, R.string.result_fail_blank_type));
            }
        });
        tokenReadViewModel.getmTokenResult().observe(getViewLifecycleOwner(), tokenResult -> {
            if (tokenResult == null) return;
            if (tokenResult.getErrorCode() != null){
                showTokenResult(tokenResult.getErrorCode());
            }
            if (tokenResult.getSuccessCode() != null){
                showTokenResult(tokenResult.getSuccessCode());
            }
        });
        final TableLayout tokenTable = binding.tbTokenList;
        tokenReadViewModel.getmTableRows().observe(getViewLifecycleOwner(), integerTableRowMap -> {
            int childCount = tokenTable.getChildCount();
            tokenTable.removeViews(0,childCount);
            for (int i = 0; i < integerTableRowMap.values().size(); i++) {
                TableRow tableRow = integerTableRowMap.get(i);
                if (tableRow.getParent() != null){
                    ((TableLayout)(tableRow.getParent())).removeAllViews();
                }
                tokenTable.addView(tableRow,tokenTable.getChildCount());
            }
        });
        /* TODO 监听 EXPORT 点击查询 */
        binding.btnExport.setOnClickListener(v -> {
            
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showTokenResult(int resId) {
        Toast.makeText(getContext(),resId,Toast.LENGTH_SHORT).show();
    }
}