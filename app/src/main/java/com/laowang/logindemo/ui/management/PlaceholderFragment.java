package com.laowang.logindemo.ui.management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.laowang.logindemo.MainActivity;
import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.databinding.FragmentManagementViewpagerBinding;
import com.laowang.logindemo.ui.login.LoggedInUserView;
import com.laowang.logindemo.ui.login.LoginActivity;
import com.laowang.logindemo.ui.management.formstate.CreateFormState;
import com.laowang.logindemo.ui.management.result.CreateResult;
import com.laowang.logindemo.util.ResourceProvider;

import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentManagementViewpagerBinding binding;

    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    private ManagementViewModel fatherViewModel;

    /**
     * 获得选项卡片的实例并确定 页面索引index
     *
     * @param index 选项卡片的index索引，从1开始
     * @return 选项卡片实例
     */
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * newInstance()构造方法之后，View视图创建之前。初始化或获取已有的 PageViewModel对象，并给该视图模型注入 index索引
     *
     * @param savedInstanceState 一捆fragment实例状态
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* 初始化 pageViewModel视图模型对象 */
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        /* 视图模型设置index */
        pageViewModel.setIndex(index);
    }

    /**
     * 创建UI视图，用ViewModel对象【监视】属性变化来【影响】UI显示
     *
     * @param inflater           UI视图充气机
     * @param container          UI视图容器（包括父视图，视图管理器等）
     * @param savedInstanceState 一捆fragment实例状态
     * @return UI视图
     */
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentManagementViewpagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /* UI选项卡片的文字区域 */
        final TextView textView = binding.sectionLabel;
        /* 【后端】注入数据到【UI前端】 从onCreate()调用viewModel.index_setter()后viewModel.text_getter()的结果就产生变化了...new Observer<String>() {...} */
        /* 此处逻辑非常有【意思】：一旦后端ViewModel的getter()获取的mText有变化，UI界面就发生变化 */
        pageViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        /* 选项卡片的显示内容在这里修改，因为2个卡片UI都不一样，最好动态代码修改，而不是写第三层的fragment，
            因为我估计childFragmentManager只能管理到直接子类页面，更深的后代页面就管不到了。 */
        renderTabContainer();
        // 绑定通用事件监听器
        cancelButtonOnClick();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 渲染选项卡片容器，不同的卡片有不同的组件组合
     */
    private void renderTabContainer() {
        if (loginRepository.getUser().getLevel().contains("1")) {
            int sectionNumber = this.getArguments() != null ? this.getArguments().getInt(ARG_SECTION_NUMBER) : 0;
            if (sectionNumber == 1) {
                renderCreateUserTabcard();
            } else if (sectionNumber == 2) {
                renderModifyUserTabcard();
            } else {
                renderDeleteUserTabcard();
            }
        } else {
            renderChangePasswordTabcard();
        }
    }

    private void renderCreateUserTabcard() {
        // 先隐藏不必要的小部件
        binding.sectionUsernameSelected.setVisibility(View.GONE);
        binding.sectionPasswordOld.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
        final EditText newUsername = binding.sectionUsernameNew;
        final EditText newPwd = binding.sectionPasswordNew;
        final EditText repeatPwd = binding.sectionPasswordRepeat;
        final TextView roleSpan = binding.sectionPermissionSpan;
        final RadioGroup rolsGroup = binding.sectionPermissionRole;
        final Button confirm = binding.sectionBtnConfirm;
        // 绑定状态监视器
        pageViewModel.getCreateFormState().observe(getViewLifecycleOwner(), createFormState -> {
            if (createFormState == null) {
                return;
            }
            confirm.setEnabled(createFormState.isDataValid());
            if (createFormState.getUsernameError() != null) {
                newUsername.setError(getString(createFormState.getUsernameError()));
            }
            if (createFormState.getPwdError() != null) {
                newPwd.setError(getString(createFormState.getPwdError()));
            }
            if (createFormState.getNotEqualError() != null) {
                repeatPwd.setError(getString(createFormState.getNotEqualError()));
            }
            if (createFormState.getPermissionRoleError() != null) {
                roleSpan.setError(getString(createFormState.getPermissionRoleError()));
            }
        });
        pageViewModel.getCreateResult().observe(getViewLifecycleOwner(), createResult -> {
            if (createResult == null) {
                return;
            }
            if (createResult.getError() != null) {
                showCreateFailed(createResult.getError());
            }
            if (createResult.getSuccess() != null) {
                updateUiWithUserList(createResult.getSuccess());
                clearTabCardInput();
            }
        });
        // 绑定事件监听器
        TextWatcher afterTextChangedListner = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                pageViewModel.createDateChanged(fatherViewModel,newUsername.getText().toString(),newPwd.getText().toString(),
                        repeatPwd.getText().toString());
            }
        };
        newUsername.addTextChangedListener(afterTextChangedListner);
        newPwd.addTextChangedListener(afterTextChangedListner);
        repeatPwd.addTextChangedListener(afterTextChangedListner);
        confirm.setOnClickListener(v -> pageViewModel.create(fatherViewModel,newUsername.getText().toString(),newPwd.getText().toString(),
                repeatPwd.getText().toString(),rolsGroup.getCheckedRadioButtonId()));
    }

    private void renderModifyUserTabcard() {
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }

    private void renderDeleteUserTabcard() {
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPasswordOld.setVisibility(View.GONE);
        binding.sectionPasswordNew.setVisibility(View.GONE);
        binding.sectionPasswordRepeat.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnConfirm.setVisibility(View.GONE);
    }

    private void renderChangePasswordTabcard() {
        binding.sectionUsernameSelected.setVisibility(View.GONE);
        binding.sectionUsernameNew.setVisibility(View.GONE);
        binding.sectionPermissionSpan.setVisibility(View.GONE);
        binding.sectionPermissionRole.setVisibility(View.GONE);
        binding.sectionBtnDelete.setVisibility(View.GONE);
    }

    private void cancelButtonOnClick() {
        binding.sectionBtnCancel.setOnClickListener(v -> {
            clearTabCardInput();
        });
    }

    private void clearTabCardInput() {
        binding.sectionUsernameSelected.setText(null);
        binding.sectionUsernameNew.setText(null);
        binding.sectionPasswordOld.setText(null);
        binding.sectionPasswordNew.setText(null);
        binding.sectionPasswordRepeat.setText(null);
        binding.sectionAdministrator.setChecked(false);
        binding.sectionRegularUser.setChecked(false);
    }

    private void showCreateFailed(@StringRes Integer errorString) {
        Toast.makeText(getContext(),errorString,Toast.LENGTH_SHORT).show();
    }

    private void updateUiWithUserList(LoggedInUserView success) {
        String successMsg = "Creat "+ success.getDisplayName()+ " successfully!";
        Toast.makeText(getContext(), successMsg,Toast.LENGTH_LONG).show();
    }

    public void setFatherViewModel(ManagementViewModel fatherViewModel) {
        this.fatherViewModel = fatherViewModel;
    }
}