package com.laowang.logindemo.ui.token;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.databinding.FragmentTokenimportBinding;

public class TokenImportFragment extends Fragment {

    private FragmentTokenimportBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState){
        TokenImportViewModel tokenImportViewModel = new ViewModelProvider(this).get(TokenImportViewModel.class);

        binding = FragmentTokenimportBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        final TextView textView = binding.textTokenImport;
        tokenImportViewModel.getText().observe(getViewLifecycleOwner(),textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
