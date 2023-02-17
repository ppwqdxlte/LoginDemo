package com.laowang.logindemo.ui.management;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.model.ManagedUser;
import com.laowang.logindemo.databinding.FragmentManagementViewpagerBinding;
import com.laowang.logindemo.ui.MyViewModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class TabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentManagementViewpagerBinding binding;

    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    private MngViewModel mngViewModel;

    private MyViewModel myViewModel;

    public static TabFragment newInstance(int index) {
        TabFragment fragment = new TabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mngViewModel = new ViewModelProvider(getParentFragment()).get(MngViewModel.class);
        pageViewModel = new ViewModelProvider(getParentFragment()).get(PageViewModel.class);
        myViewModel = new ViewModelProvider(getParentFragment()).get(MyViewModel.class);
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
        binding = FragmentManagementViewpagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        renderTabs();
        binding.sectionBtnCancel.setOnClickListener(v -> cleanInputs());
        observeUserFormState();
        observeUserMngResult();
        observeSelectedName();
        listenTextChanged();
        listenConfirmClicked();
        listenDeleteClicked();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void cleanInputs() {
        binding.sectionUsernameSelected.setText(null);
        binding.sectionUsernameNew.setText(null);
        binding.sectionPasswordOld.setText(null);
        binding.sectionPasswordNew.setText(null);
        binding.sectionPasswordRepeat.setText(null);
        binding.sectionAdministrator.setChecked(false);
        binding.sectionRegularUser.setChecked(true);
    }

    private void observeUserFormState() {
        pageViewModel.getmUserFormState().observe(getViewLifecycleOwner(), userFormState -> {
            if (userFormState == null) return;
            boolean isDataValid = userFormState.getDataValid() != null && userFormState.getDataValid();
            binding.sectionBtnConfirm.setEnabled(isDataValid);
            binding.sectionBtnDelete.setEnabled(isDataValid);
            if (userFormState.getNewNameError() != null && userFormState.getNewNameError() != 1) {
                binding.sectionUsernameNew.setError(getString(userFormState.getNewNameError()));
            }
            if (userFormState.getNewPwdError() != null && userFormState.getNewPwdError() != 1) {
                binding.sectionPasswordNew.setError(getString(userFormState.getNewPwdError()));
            }
            if (userFormState.getRepeatPwdError() != null && userFormState.getRepeatPwdError() != 1) {
                binding.sectionPasswordRepeat.setError(getString(userFormState.getRepeatPwdError()));
            }
            if (userFormState.getOldPwdError() != null && userFormState.getOldPwdError() != 1) {
                binding.sectionPasswordOld.setError(getString(userFormState.getOldPwdError()));
            }
            if (userFormState.getRoleError() != null && userFormState.getRoleError() != 1) {
                binding.sectionPermissionSpan.setError(getString(userFormState.getRoleError()));
            }
            if (userFormState.getSelectedNameError() != null && userFormState.getSelectedNameError() != 1) {
                binding.sectionUsernameSelected.setError(getString(userFormState.getSelectedNameError()));
            }
        });
    }

    private void observeUserMngResult() {
        pageViewModel.getmUserMngResult().observe(getViewLifecycleOwner(), userMngResult -> {
            if (userMngResult == null) return;
            if (userMngResult.getErrorCode() != null && userMngResult.getErrorCode() != 1) {
                int errorCode = userMngResult.getErrorCode();
                Toast.makeText(getActivity(), errorCode, Toast.LENGTH_SHORT).show();
            }
            if (userMngResult.getSuccessCode() != null) {
                int successCode = userMngResult.getSuccessCode();
                Toast.makeText(getActivity(), successCode, Toast.LENGTH_SHORT).show();
                Map<String, TableRow> oldMap = mngViewModel.getTableRows().getValue();
                Map<TableRow, String> reverseMap = new TreeMap<>((o1, o2) -> {
                    TextView SN1 = (TextView) (o1.getChildAt(0));
                    TextView SN2 = (TextView) (o2.getChildAt(0));
                    int i = Integer.parseInt(SN1.getText().toString()) - Integer.parseInt(SN2.getText().toString());
                    if (i < 0) {
                        return 1;
                    } else if (i > 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
                Map<String, TableRow> newCopy = new HashMap<>();
                for (String s : oldMap.keySet()) {
                    reverseMap.put(oldMap.get(s), s);
                }
                for (TableRow tableRow : reverseMap.keySet()) {
                    newCopy.put(reverseMap.get(tableRow), tableRow);
                }
                mngViewModel.setmTableRows(newCopy);
            }
            pageViewModel.setmUserMngResult(null);
            cleanInputs();
        });
    }

    private void observeSelectedName() {
        mngViewModel.getmSelectedName().observe(getViewLifecycleOwner(), s -> binding.sectionUsernameSelected.setText(s));
    }

    private void listenTextChanged() {
        TextWatcher textChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = binding.sectionUsernameNew.getText().toString();
                String newPwd = binding.sectionPasswordNew.getText().toString();
                String repeatPwd = binding.sectionPasswordRepeat.getText().toString();
                String oldPwd = binding.sectionPasswordOld.getText().toString();
                String selectedName = binding.sectionUsernameSelected.getText().toString();
                if (binding.sectionPermissionRole.getVisibility() == View.VISIBLE) {            // create user
                    pageViewModel.formDataChanged(mngViewModel, username, newPwd, repeatPwd);
                } else if (binding.sectionPasswordOld.getVisibility() == View.VISIBLE
                        && binding.sectionUsernameSelected.getVisibility() == View.VISIBLE) {   // modify user
                    pageViewModel.formDataChanged(mngViewModel, selectedName, oldPwd, newPwd, repeatPwd);
                } else if (binding.sectionPasswordOld.getVisibility() == View.VISIBLE
                        && binding.sectionUsernameSelected.getVisibility() != View.VISIBLE) {  // change password
                    pageViewModel.formDataChanged(oldPwd, newPwd, repeatPwd, mngViewModel);
                } else {                                                                        // delete user
                    pageViewModel.formDataChanged(selectedName);
                }
            }
        };
        binding.sectionUsernameNew.addTextChangedListener(textChangedListener);
        binding.sectionPasswordNew.addTextChangedListener(textChangedListener);
        binding.sectionPasswordRepeat.addTextChangedListener(textChangedListener);
        binding.sectionPasswordOld.addTextChangedListener(textChangedListener);
        binding.sectionUsernameSelected.addTextChangedListener(textChangedListener);
    }

    private void listenConfirmClicked() {
        binding.sectionBtnConfirm.setOnClickListener(v -> {
            String newUsername = binding.sectionUsernameNew.getText().toString();
            String newPwd = binding.sectionPasswordNew.getText().toString();
            String repeatPwd = binding.sectionPasswordRepeat.getText().toString();
            RadioButton regular = binding.sectionRegularUser;
            String selectedName = binding.sectionUsernameSelected.getText().toString();
            String oldPwd = binding.sectionPasswordOld.getText().toString();
            if (binding.sectionPermissionRole.getVisibility() == View.VISIBLE) {
                pageViewModel.createUser(mngViewModel, getContext(), newUsername, newPwd, repeatPwd, regular.isChecked() ? 0 : 1);
            } else if (binding.sectionPasswordOld.getVisibility() == View.VISIBLE
                    && binding.sectionUsernameSelected.getVisibility() == View.VISIBLE) {
                pageViewModel.modifyUser(mngViewModel, getContext(), selectedName, oldPwd, newPwd, repeatPwd);
            } else {
                pageViewModel.changePassword(mngViewModel, getContext(), newPwd, repeatPwd);
            }
        });
    }

    private void listenDeleteClicked() {
        binding.sectionBtnDelete.setOnClickListener(v -> pageViewModel.deleteUser(mngViewModel, getContext(), binding.sectionUsernameSelected.getText().toString()));
    }

    private void renderTabs() {
        if (loginRepository.getUser().getLevel().contains("1")) {
            int sectionNumber = this.getArguments() != null ? this.getArguments().getInt(ARG_SECTION_NUMBER) : 0;
            if (sectionNumber == 1) {
                renderCreateUserTab();
            } else if (sectionNumber == 2) {
                renderModifyUserTab();
            } else {
                renderDeleteUserTab();
            }
        } else {
            renderChangePasswordTab();
        }
    }

    private void renderCreateUserTab() {
        binding.sectionUsernameSelected.setVisibility(View.GONE);
        binding.sectionPasswordOld.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }

    private void renderModifyUserTab() {
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }

    private void renderDeleteUserTab() {
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPasswordOld.setVisibility(View.GONE);
        binding.sectionPasswordNew.setVisibility(View.GONE);
        binding.sectionPasswordRepeat.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnConfirm.setVisibility(View.GONE);
    }

    private void renderChangePasswordTab() {
        binding.sectionUsernameSelected.setVisibility(View.GONE);
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }
}