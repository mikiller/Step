package com.westepper.step.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.contact.ContactsCustomization;
import com.netease.nim.uikit.contact.ContactsFragment;
import com.netease.nim.uikit.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.contact.core.item.ItemTypes;
import com.netease.nim.uikit.contact.core.model.ContactDataAdapter;
import com.netease.nim.uikit.contact.core.viewholder.AbsContactViewHolder;
import com.westepper.step.R;
import com.westepper.step.base.SuperActivity;
import com.westepper.step.customViews.TitleBar;

import java.util.ArrayList;
import java.util.List;

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
                    contactsFragment.getAdapter().load(true);
                }else {
                    tv_search_hint.setVisibility(View.GONE);
                    contactsFragment.getAdapter().query(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_search_hint.setText("搜索联系人");
        addContactsFragment();
    }

    private void addContactsFragment(){
        contactsFragment = new ContactsFragment();
        contactsFragment.setContactsCustomization(new ContactsCustomization() {
            @Override
            public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
                return FuncItem.FuncItemViewHolder.class;
            }

            @Override
            public List<AbsContactItem> onGetFuncItems() {
                List<AbsContactItem> items = new ArrayList<>();
                items.add(new FuncItem(FuncItem.NEW_FRIEND));
                items.add(new FuncItem(FuncItem.ADVANCED_TEAM));
                return items;
            }

            @Override
            public void onFuncItemClick(AbsContactItem item) {
                if(((FuncItem)item).type == FuncItem.NEW_FRIEND){
                    Log.e(TAG, "add new friend");
                }else if(((FuncItem)item).type == FuncItem.ADVANCED_TEAM){
                    Log.e(TAG, "start team chat");
                }
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.contact_list_fragment, contactsFragment).commit();
    }

    @Override
    protected void initData() {

    }

    public static class FuncItem extends AbsContactItem{
        public static final int NEW_FRIEND = 1, ADVANCED_TEAM = 2;
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
                if(item.type == NEW_FRIEND){
                    tv_item_title.setText("新的好友");
                    iv_img.setImageResource(R.drawable.ic_addfriend);
                }else if(item.type == ADVANCED_TEAM){
                    tv_item_title.setText("群聊");
                    iv_img.setImageResource(R.drawable.ic_teamchat);
                }
            }

        }
    }
}
