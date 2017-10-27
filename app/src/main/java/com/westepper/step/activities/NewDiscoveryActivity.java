package com.westepper.step.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.uilib.mxgallery.utils.CameraGalleryUtils;
import com.uilib.utils.BitmapUtils;
import com.uilib.utils.DisplayUtil;
import com.westepper.step.R;
import com.westepper.step.adapters.DisPhotoRcvAdapter;
import com.westepper.step.base.Constants;
import com.westepper.step.base.SuperActivity;
import com.uilib.mxmenuitem.MyMenuItem;
import com.westepper.step.customViews.TitleBar;
import com.westepper.step.logics.NewDiscoveryLogic;
import com.westepper.step.models.NewDiscoveryModel;
import com.westepper.step.utils.ActivityManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Mikiller on 2017/9/12.
 */

public class NewDiscoveryActivity extends SuperActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rcv_photo)
    RecyclerView rcv_photo;
    @BindView(R.id.ll_del)
    LinearLayout ll_del;
    @BindView(R.id.ll_pick)
    LinearLayout ll_pick;
    @BindView(R.id.picker)
    NumberPicker picker;

    int disKind;
    String tmpFile;
    List<File> fileList;
    DisPhotoRcvAdapter adapter;
    int dateNum, peopleNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null)
            disKind = savedInstanceState.getInt(Constants.DIS_KIND, Constants.MOOD);
        else if(getIntent() != null) {
            disKind = getIntent().getIntExtra(Constants.DIS_KIND, Constants.MOOD);
            tmpFile = getIntent().getStringExtra(CameraGalleryUtils.TMP_FILE);
            fileList = (List<File>) getIntent().getSerializableExtra(CameraGalleryUtils.THUMB_FILE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discovery);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.DIS_KIND, disKind);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView() {
        titleBar.setTitleListener(new TitleBar.TitleListener() {
            @Override
            protected void onBackClicked() {
                back();
            }

            @Override
            protected void onSubClicked() {
                sendNewDiscovery();
            }
        });
        titleBar.setTitle(disKind == Constants.MOOD ? "新心情" : "新约行");
        titleBar.setSubTxtEnabled(true);
//
        final GridLayoutManager glMgr = new GridLayoutManager(this, 3);
        glMgr.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (adapter.isHeadView(position) || adapter.isFootView(position)) ? glMgr.getSpanCount() : 1;
            }
        });
        rcv_photo.setLayoutManager(glMgr);
        rcv_photo.setHasFixedSize(true);
        adapter = new DisPhotoRcvAdapter(this, true, 3, DisplayUtil.dip2px(this, 7));
        adapter.setDisKind(disKind);
        adapter.setItemMovedListener(new DisPhotoRcvAdapter.onItemMovedListener() {
            boolean needDel = false;

            @Override
            public void onPressed(int pos) {
                ll_del.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMove(int pos, int y) {
                int dy = DisplayUtil.getScreenHeight(NewDiscoveryActivity.this) - 360 - titleBar.getHeight();
                ll_del.setPressed(needDel = y > dy);
                ((TextView)ll_del.getChildAt(0)).setText(needDel ? "松手即可删除" : "拖动到此处删除");
            }

            @Override
            public void onReleased(int fPos) {
                if(needDel) {
                    adapter.removeItem(fPos);
                    needDel = false;
                }
                ll_del.setVisibility(View.GONE);
            }
        });
        adapter.setFootClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_pos:
                        Map<String, Object> args = new HashMap<String, Object>();
                        args.put(Constants.POIITEM, adapter.getPoiItem());
                        ActivityManager.startActivityforResult(NewDiscoveryActivity.this, SearchPoiActivity.class, Constants.SEARCH_POI, args);
                    break;
                    case R.id.menu_date:
                        createDatePicker((MyMenuItem) v);
                        break;
                    case R.id.menu_people:
                        createPeoplePicker((MyMenuItem) v);
                        break;
                    case R.id.menu_publish:
                        createPrivacyPicker((MyMenuItem) v);
                        break;
                }
                ll_pick.setVisibility(v.getId() == R.id.tv_pos ? View.GONE : View.VISIBLE);
            }
        });
        rcv_photo.setAdapter(adapter);
        adapter.createTouchHelper().attachToRecyclerView(rcv_photo);
    }

    private void sendNewDiscovery(){
        NewDiscoveryModel model = new NewDiscoveryModel(2, disKind, adapter.getInfo(), "0", System.currentTimeMillis());
        model.setPoiTitle(adapter.getPoiItem().getTitle());

        String img = "data:image/jpeg;base64,";
        ArrayList<String> imgList = new ArrayList<>();
        if(!TextUtils.isEmpty(tmpFile)) {
            img = img.concat(BitmapUtils.getBmpBase64Str(tmpFile,
                    DisplayUtil.getScreenWidth(this) * 2 / 3, DisplayUtil.getScreenHeight(this) / 3));
            imgList.add(img);
        }else{
            for(String path : adapter.getItems()){
                String tmp = img.concat(BitmapUtils.getBmpBase64Str(path,
                        DisplayUtil.getScreenWidth(this) * 2 / 3, DisplayUtil.getScreenHeight(this) / 3));
                imgList.add(tmp);
            }
        }
        model.setImgList(imgList);
        String json = new Gson().toJson(model);
        Log.e(TAG, json);
        NewDiscoveryLogic logic = new NewDiscoveryLogic(this, model);
        logic.sendRequest();
    }

    private void createPeoplePicker(final MyMenuItem menu){
        final String[] values = new String[]{"不限", "1", "2", "3", "4", "", "", ""};
        createPicker(values, menu, 4, false);
    }

    private void createDatePicker(final MyMenuItem menu){
        String[] dates = new String[8];
        dates[0] = "不限";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        for(int i = 1; i < dates.length; i++) {
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            dates[i] = sdf.format(calendar.getTime());
        }
        createPicker(dates, menu, 7, true);
    }

    private void createPrivacyPicker(final MyMenuItem menu){
        final String[] values = new String[]{"公开", "好友可见", "", "", "", "", "", ""};
        createPicker(values, menu, 2, false);
    }

    private void createPicker(final String[] values, final MyMenuItem menu, int maxValue, final boolean isDate){
        picker.setDisplayedValues(values);
        picker.setMaxValue(maxValue);
        picker.setValue(isDate ? dateNum : peopleNum);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        picker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    if(isDate)
                        dateNum = view.getValue();
                    else
                        peopleNum = view.getValue();
                    menu.setSubText(values[view.getValue()]);
                    ll_pick.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if(!TextUtils.isEmpty(tmpFile)){
            adapter.addItem(tmpFile);
            tmpFile = null;
        }else{
            adapter.addItems(fileList);
            fileList.clear();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
            return;
        String filePath = "";
        if(data.getSerializableExtra(CameraGalleryUtils.TMP_FILE) != null) {
            filePath = (String) data.getSerializableExtra(CameraGalleryUtils.TMP_FILE);
            adapter.addItem(filePath);
        }
        else if( data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE) != null) {
            List<File> files = (List<File>) data.getSerializableExtra(CameraGalleryUtils.THUMB_FILE);
            adapter.addItems(files);
        }else if(requestCode == Constants.SEARCH_POI){
            adapter.setPoiItem((PoiItem) data.getParcelableExtra(Constants.POIITEM));
        }

    }
}
