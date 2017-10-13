package com.westepper.step.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.westepper.step.R;
import com.westepper.step.responses.Achieve;
import com.westepper.step.utils.AnimUtils;

import java.util.List;

/**
 * Created by Mikiller on 2017/10/12.
 */

public class AcheiveSettingLayout extends RelativeLayout {
    private LinearLayout ll_ach_setting;
    private RelativeLayout rl_ach_map;
    private RadioButton btn_ach_map;

    private List<Achieve> achievementList;
    private AcheivePackage lastPkg;
    private RadioButton lastItem;
    private onAchieveSettingListener achieveSettingListener;

    private int[] icons = new int[]{R.mipmap.ic_ach1, R.mipmap.ic_ach2, R.mipmap.ic_ach3, R.mipmap.ic_ach4, R.mipmap.ic_ach5};

    public AcheiveSettingLayout(Context context) {
        this(context, null, 0);
    }

    public AcheiveSettingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AcheiveSettingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_acheive_setting, this);
        ll_ach_setting = (LinearLayout) findViewById(R.id.ll_ach_setting);
        rl_ach_map = (RelativeLayout) findViewById(R.id.rl_ach_map);
        btn_ach_map = (RadioButton) findViewById(R.id.btn_ach_map);
        lastItem = btn_ach_map;

        btn_ach_map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onItemChecked(btn_ach_map, null, "探索地图");
                if(lastPkg != null && isChecked)
                    lastPkg.hideAchieveItems();
            }
        });

        getRootView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }

    public void setAchievementList(List<Achieve> achieveList){
        achievementList = achieveList;
        int i = 0;
        for(Achieve achieve : achieveList){
            AcheivePackage achPkg = new AcheivePackage(getContext());
            achPkg.setAchIcon(getContext().getResources().getDrawable(icons[i++]));
            achPkg.setAchieve(achieve);
            achPkg.setAchieveClickListener(new AcheivePackage.AchieveClickListener() {
                @Override
                public void onPackageClick(AcheivePackage pkg) {
                    if(lastPkg == null){
                        pkg.toggleAchieveItems();
                        lastPkg = pkg;
                    }else if(!pkg.equals(lastPkg)) {
                        lastPkg.hideAchieveItems();
                        pkg.showAchieveItems();
                        lastPkg = pkg;
                    }else{
                        lastPkg.toggleAchieveItems();
                    }
                }

                @Override
                public void onAchieveAreaClick(RadioButton item, String[] areaId, String achieveKind) {
                    onItemChecked(item, areaId, achieveKind);
                }
            });
            ll_ach_setting.addView(achPkg);
        }
    }

    private void onItemChecked(RadioButton item, String[] areaId, String achieveKind){
        if(!item.equals(lastItem)){
            lastItem.setChecked(false);
            lastItem = item;
        }
        if(achieveSettingListener != null)
            achieveSettingListener.onAchieveSelected(areaId, achieveKind);
    }

    public void setAchieveSettingListener(onAchieveSettingListener listener){
        achieveSettingListener = listener;
    }

    public interface onAchieveSettingListener{
        void onAchieveSelected(String[] areaId, String achieveKind);
    }
}
