package com.westepper.step.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.westepper.step.R;
import com.westepper.step.adapters.FriendRcvAdapter;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/11/21.
 */

public class AddFriendActivity extends SuperActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.rcv_result)
    RecyclerView rcv_result;

    FriendRcvAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String account = v.getText().toString();
                    if(TextUtils.isEmpty(account)){
                        Toast.makeText(AddFriendActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    }else if(SuperActivity.userInfo.getUserId().equals(account)){
                        Toast.makeText(AddFriendActivity.this, "不能添加自己为好友", Toast.LENGTH_SHORT).show();
                    }else
                        searchFriend(account);
                }
                return false;
            }
        });

        rcv_result.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void searchFriend(String account){
        NimUserInfoCache.getInstance().getUserInfoFromRemote(account, new RequestCallback<NimUserInfo>() {
            @Override
            public void onSuccess(NimUserInfo user) {
                if (user == null) {
                    AlertDialog dlg = new AlertDialog.Builder(AddFriendActivity.this).setTitle("该用户不存在").setMessage("请检查你输入的账号是否正确").setCancelable(true).setNeutralButton("确认", null).create();
                    dlg.show();
                } else {
                    if(adapter == null) {
                        adapter = new FriendRcvAdapter(user);
                        rcv_result.setAdapter(adapter);
                    }else
                        adapter.addFriend(user);
                }
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                if (code == 408) {
                    Toast.makeText(AddFriendActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddFriendActivity.this, "on failed:" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
                Toast.makeText(AddFriendActivity.this, "on exception:" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
