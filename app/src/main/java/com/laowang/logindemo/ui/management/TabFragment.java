package com.laowang.logindemo.ui.management;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class TabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentManagementViewpagerBinding binding;
    /**
     * LoginRepository对象贯穿真个app生命周期，故而可以在别的类中添加这种成员变量，如果换成
     */
    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    /**
     * 获得选项卡片的实例并确定 页面索引index
     *
     * @param index 选项卡片的index索引，从1开始
     * @return 选项卡片实例
     */
    public static TabFragment newInstance(int index) {
        TabFragment fragment = new TabFragment();
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
        pageViewModel = new ViewModelProvider(getParentFragment()).get(PageViewModel.class);
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
        /* 渲染tab卡片 */
        renderTabs();
        /* 单击CANCEL清空输入 */
        binding.sectionBtnCancel.setOnClickListener(v -> cleanInputs());
//        Log.e("pageModelView",pageViewModel.toString());
        /* TODO bug太难了，操。观察 UserFormState 表单状态变化，错误提示，CONFIRM按钮激活或沉睡 */
        observeUserFormState();
        /* 观察 UserMngResult 结果的变化，Toast显示弹出结果消息，成功还需清空 mUserFormState属性*/
        observeUserMngResult();
        /* EditText变化监听 */
        listenTextChanged();
        /* 单击 CONFIRM 提交，清空inputs，清空 mUserFormState属性 */
        listenConfirmClicked();
        /* 单击 DELETE 删除，清空inputs，清空 mUserFormState属性 */
        listenDeleteClicked();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        /* 根据网友关于【数据倒灌】的源码分析产生的灵感，就是在销毁 fragment 时候防止乱七八糟的设置而导致无法清空ViewModel对象，还没写交互代码，但愿可以解决之前的BUG
         * 【上句话简直是一厢情愿】，还没写tab逻辑呢，从home或者token页面跳回management页面时候，依然有数据倒灌！那到底应该怎么彻底清空呢？
         * 【上句话也是不明白】，input的用户密码啥的会保存，也许这种现象不算是 ViewModel 的【数据倒灌】！而PageViewModel中的fromState对象才是需要清空的！
         * 【根据业务需要】 ManagementViewModel对象（因为保存用户列表，create不能重名就得查它）不必清空，而tab页面的viewModel需要清空，故而只在这里调用即可 */
//        getViewModelStore().clear();
    }

    /**
     * 保存实例状态时候的操作
     *
     * @param outState 当前页面的外部页面（父页面）的一捆状态
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        默认保存父类View的状态，如果不注释掉，那么 this.getActivity()获取的父容器已经销毁了可能会报空指针异常
        super.onSaveInstanceState(outState);
    }

    /**
     * 清空表单输入
     */
    private void cleanInputs() {
        binding.sectionUsernameSelected.setText(null);
        binding.sectionUsernameNew.setText(null);
        binding.sectionPasswordOld.setText(null);
        binding.sectionPasswordNew.setText(null);
        binding.sectionPasswordRepeat.setText(null);
        binding.sectionAdministrator.setChecked(false);
        binding.sectionRegularUser.setChecked(true);
    }

    /**
     * 清空表单错误状态
     */
    private void cleanInputError() {
        pageViewModel.getmUserFormState().getValue().setNewNameError(null);
        pageViewModel.getmUserFormState().getValue().setNewPwdError(null);
        pageViewModel.getmUserFormState().getValue().setRepeatPwdError(null);
        pageViewModel.getmUserFormState().getValue().setOldPwdError(null);
        pageViewModel.getmUserFormState().getValue().setRoleError(null);
    }

    /**
     * 观察pageViewModel.表单状态对象的属性变化
     */
    private void observeUserFormState() {
        pageViewModel.getmUserFormState().observe(getViewLifecycleOwner(), userFormState -> {
//            Log.e("userFormState",userFormState.toString());
            if (userFormState == null) return;
//            binding.sectionBtnConfirm.setEnabled(userFormState.getDataValid());
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
        });
    }

    /**
     * 观察【通用】 操作结果
     */
    private void observeUserMngResult() {
        pageViewModel.getmUserMngResult().observe(getViewLifecycleOwner(), userMngResult -> {
            if (userMngResult == null) return;
            if (userMngResult.getErrorCode() != null && userMngResult.getErrorCode() != 1) {
                Toast.makeText(getContext(), userMngResult.getErrorCode(), Toast.LENGTH_SHORT).show();
            }
            if (userMngResult.getSuccessCode() != null) {
                Toast.makeText(getContext(), userMngResult.getSuccessCode(), Toast.LENGTH_SHORT).show();
            }
            // TODO 轻易别调用这个，可能有问题！
//            getActivity().setResult(Activity.RESULT_OK);
        });
    }

    /**
     * 监听文本改变，修改表单状态
     */
    private void listenTextChanged() {
        // 准备好监听器
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
//                Log.e("TextChanged:::Editable s===>  ",s.getClass().getName());
//                Log.e("",s.toString());
                // 影响 userFormState 的属性变化，EditText.getText()永远不为null，所以最少是空字符串
                pageViewModel.formDataChanged(new ViewModelProvider(getParentFragment()).get(MngViewModel.class),
                        binding.sectionUsernameNew.getText().toString(),
                        binding.sectionPasswordNew.getText().toString(),
                        binding.sectionPasswordRepeat.getText().toString(),
                        binding.sectionPasswordOld.getText().toString(),
                        binding.sectionUsernameSelected.getText().toString());
            }
        };
        // 给控件绑定监听器
        binding.sectionUsernameNew.addTextChangedListener(textChangedListener);
        binding.sectionPasswordNew.addTextChangedListener(textChangedListener);
        binding.sectionPasswordRepeat.addTextChangedListener(textChangedListener);
        binding.sectionPasswordOld.addTextChangedListener(textChangedListener);
    }

    /**
     * 监听提交按钮单击事件
     */
    private void listenConfirmClicked() {
        binding.sectionBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.sectionPermissionRole.getVisibility() == View.VISIBLE) {            // create user
                    /*pageViewModel.createUser(binding.sectionUsernameNew.getText().toString(),
                            );*/
                } else if (binding.sectionPasswordOld.getVisibility() == View.VISIBLE
                        && binding.sectionUsernameSelected.getVisibility() == View.VISIBLE) {   // modify user

                } else {        // change password

                }
            }
        });
    }

    /**
     * 监听删除按钮单击事件
     */
    private void listenDeleteClicked() {

    }

    /**
     * 渲染选项卡片容器，不同的卡片有不同的组件组合
     */
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