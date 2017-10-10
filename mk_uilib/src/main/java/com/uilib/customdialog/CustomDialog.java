package com.uilib.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uilib.R;
import com.uilib.utils.DisplayUtil;

/**
 * Created by Mikiller on 2017/7/14.
 */

public class CustomDialog extends Dialog {
    private Context mContext;
    private TextView title;
    private TextView msg;
    private Button btnCancel, btnSure;
    private String titleStr, msgStr;
    private View customView;
    private onButtonClickListener btnListener;
    private onCustomBtnsClickListener customListener;
//    private int layoutRes = View.NO_ID;
    private int[] btnLayoutId = new int[]{};
    private String[] btnTxts;

    public CustomDialog(Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public CustomDialog setLayoutRes(int layoutRes) {
//        this.layoutRes = layoutRes;
        customView = LayoutInflater.from(mContext).inflate(layoutRes == View.NO_ID ? R.layout.layout_custom_dlg : layoutRes, null);
        return this;
    }

    public View getCustomView(){
        return customView;
    }

    public CustomDialog setTitle(String str) {
        if (str != null)
            titleStr = str;
        return this;
    }

    public String getTitle() {
        return titleStr;
    }

    public CustomDialog setMsg(String str) {
        if (str != null)
            msgStr = str;
        return this;
    }

    public CustomDialog setOnCancelListener(View.OnClickListener listener) {
        if (btnCancel != null && listener != null)
            btnCancel.setOnClickListener(listener);
        return this;
    }

    public CustomDialog setOnSureListener(View.OnClickListener listener) {
        if (btnSure != null && listener != null)
            btnSure.setOnClickListener(listener);
        return this;
    }

    public CustomDialog setDlgButtonListener(final onButtonClickListener listener) {
        if (listener != null) {
            btnListener = listener;
        }
        return this;
    }

    public CustomDialog setOnCustomBtnClickListener(final onCustomBtnsClickListener listener, int... ids) {
        if (listener != null) {
            customListener = listener;
            btnLayoutId = ids;
        }
        return this;
    }

    public CustomDialog setCustomBtnText(String...txts){
        btnTxts = txts;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(layoutRes == View.NO_ID ? R.layout.layout_custom_dlg : layoutRes);
        setContentView(customView);
        getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams dlgLp = getWindow().getAttributes();
        dlgLp.width = DisplayUtil.getScreenWidth(mContext);
        dlgLp.height = DisplayUtil.getScreenHeight(mContext);
        getWindow().setAttributes(dlgLp);
        title = (TextView) findViewById(R.id.tv_dlg_title);
        if (title != null)
            title.setText(titleStr);
        msg = (TextView) findViewById(R.id.tv_dlg_msg);
        if (msg != null)
            msg.setText(msgStr);
        btnCancel = (Button) findViewById(R.id.btn_dlg_cancel);
        btnSure = (Button) findViewById(R.id.btn_dlg_sure);

        setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnListener != null)
                    btnListener.onCancel();
                CustomDialog.this.dismiss();

            }
        });

        setOnSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnListener != null)
                    btnListener.onSure();
                CustomDialog.this.dismiss();
            }
        });

        int i = 0;
        for (int id : btnLayoutId) {
            View btn = findViewById(id);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customListener.onBtnClick(v.getId());
                }
            });
            if(btnTxts != null && i < btnTxts.length)
                ((Button)btn).setText(btnTxts[i++]);
            btn.setVisibility(View.VISIBLE);
        }
    }

    public View getContent(int viewId) {
        return findViewById(viewId);
    }

    public interface onButtonClickListener {
        void onCancel();

        void onSure();
    }

    public interface onCustomBtnsClickListener {
        void onBtnClick(int id);
    }
}
