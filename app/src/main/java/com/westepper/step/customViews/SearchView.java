package com.westepper.step.customViews;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikiller.mkglidelib.imageloader.GlideImageLoader;
import com.uilib.joooonho.SelectableRoundedImageView;
import com.westepper.step.R;

import static com.westepper.step.R.id.iv_header;

/**
 * Created by Mikiller on 2017/9/4.
 */

public class SearchView extends LinearLayout {
//    private SelectableRoundedImageView iv_header;
    private ImageView iv_search;
    private EditText edt_search;

    public SearchView(Context context) {
        this(context, null, 0);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr){
        LayoutInflater.from(context).inflate(R.layout.layout_searchview, this, true);
//        iv_header = (SelectableRoundedImageView) findViewById(R.id.iv_header);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        edt_search = (EditText) findViewById(R.id.edt_search);
    }

    public void setHeader(String url){
//        GlideImageLoader.getInstance().loadImage(getContext(), url, R.mipmap.ic_default_head, iv_header, 0);
    }

    public void setOnClickListener(OnClickListener listener){
        edt_search.setOnClickListener(listener);
    }

    public void setSearchListener(TextView.OnEditorActionListener listener){
        edt_search.setOnEditorActionListener(listener);
    }
}
