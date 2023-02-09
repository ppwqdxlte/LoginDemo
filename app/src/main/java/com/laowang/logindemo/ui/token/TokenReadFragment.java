package com.laowang.logindemo.ui.token;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.databinding.FragmentTokenreadBinding;

public class TokenReadFragment extends Fragment {

    private FragmentTokenreadBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TokenReadViewModel tokenReadViewModel = new ViewModelProvider(this).get(TokenReadViewModel.class);

        binding = FragmentTokenreadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTokenRead;
        tokenReadViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}