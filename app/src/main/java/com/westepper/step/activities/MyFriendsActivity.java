package com.westepper.step.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.ContactsCustomization;
import com.netease.nim.uikit.contact.ContactsFragment;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.contact.core.viewholder.AbsContactViewHolder;
import com.netease.nim.uikit.custom.DefaultContactEventListener;
import com.westepper.step.R;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.utils.ActivityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/11/16.
 */

public class MyFriendsActivity extends SuperActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.edt_search)
    EditText edt_search;
    @BindView(R.id.tv_search_hint)
    TextView tv_search_hint;

    ContactsFragment contactsFragment;
    ContactsFragment teamFragment;
    boolean isTeam = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                if(isTeam){
                    replaceFragment();
                }else
                    back();
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_search_hint.setVisibility(TextUtils.isEmpty(s.toString()) ? View.VISIBLE : View.GONE);
                if(TextUtils.isEmpty(s.toString())){
                    tv_search_hint.setVisibility(View.VISIBLE);
                    if(isTeam)
                        teamFragment.getAdapter().load(true);
                    else
                        contactsFragment.getAdapter().load(true);
                }else {
                    tv_search_hint.setVisibility(View.GONE);
                    if(isTeam)
                        teamFragment.getAdapter().query(s.toString());
                    else
                        contactsFragment.getAdapter().query(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_search_hint.setText("搜索联系人");

        initContactsFragment();
        initTeamFragment();
        addContactsFragment();

        NimUIKit.setContactEventListener(new MyContactEventListener());
    }

    private void initContactsFragment(){
        contactsFragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", ItemTypes.FRIEND);
        contactsFragment.setArguments(bundle);
        contactsFragment.setContactsCustomization(new ContactsCustomization() {
            @Override
            public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
                return FuncItem.FuncItemViewHolder.class;
            }

            @Override
            public List<AbsContactItem> onGetFuncItems() {
                List<AbsContactItem> items = new ArrayList<>();
                items.add(new FuncItem(ItemTypes.TEAMS.ADVANCED_TEAM));
                return items;
            }

            @Override
            public void onFuncItemClick(AbsContactItem item) {
                if(((FuncItem)item).type == ItemTypes.TEAMS.ADVANCED_TEAM){
                    replaceFragment();
                }
            }
        });
    }

    private void initTeamFragment(){
        teamFragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", ItemTypes.TEAMS.ADVANCED_TEAM);
        teamFragment.setArguments(bundle);
    }

    private void addContactsFragment(){
        getSupportFragmentManager().beginTransaction().add(R.id.contact_list_fragment, contactsFragment).commit();
    }

    private void replaceFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contact_list_fragment, isTeam ? contactsFragment : teamFragment).commit();
        isTeam = !isTeam;
        tv_search_hint.setText(isTeam ? "搜索群组" : "搜索联系人");
    }

    @Override
    protected void initData() {

    }

    public static class FuncItem extends AbsContactItem{
        private int type;

        public FuncItem(int type) {
            this.type = type;
        }

        @Override
        public int getItemType() {
            return ItemTypes.FUNC;
        }

        @Override
        public String belongsGroup() {
            return null;
        }

        public static class FuncItemViewHolder extends AbsContactViewHolder<FuncItem>{
            private ImageView iv_img;
            private TextView tv_item_title;

            @Override
            public View inflate(LayoutInflater inflater) {
                View view = inflater.inflate(R.layout.item_contacts_func, null);
                iv_img = (ImageView) view.findViewById(R.id.iv_img);
                tv_item_title = (TextView) view.findViewById(R.id.tv_item_title);
                return view;
            }
            @Override
            public void refresh(ContactDataAdapter adapter, int position, FuncItem item) {
                if(item.type == ItemTypes.TEAMS.ADVANCED_TEAM){
                    tv_item_title.setText("群聊");
                    iv_img.setImageResource(R.mipmap.ic_teamchat);
                }
            }

        }
    }

    private class MyContactEventListener extends DefaultContactEventListener{
        @Override
        public void onItemClick(Context context, String account) {
            Map<String, Object> args = new HashMap<>();
            args.put(Constants.USERINFO, account);
            ActivityManager.startActivity(MyFriendsActivity.this, UserDetailActivity.class, args);
        }

    }
}
