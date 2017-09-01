package com.uilib.spannableeditbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.audiofx.NoiseSuppressor;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uilib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by Mikiller on 2016/7/6.
 */
public class SpannableEditbox extends LinearLayout implements View.OnClickListener{

    private boolean clickableSpan;
    private boolean bold;
    private boolean italic;
    private boolean underLine;
    private int foregroundColor = 0xffffff;
    private boolean needToolbar;
    private boolean undoAndRedo;
    private int boldBg = NO_ID;
    private int italicBg = NO_ID;
    private int underLineBg = NO_ID;
    private int mainTxtBg = NO_ID;
    private int leadBg = NO_ID;
    private int postBg = NO_ID;
    private int undoBg = NO_ID;
    private int redoBg = NO_ID;

    private RichEditor editText;
    private ImageButton undo, redo, btnMainTxt, btnLead, btnPost;
    private CheckBox ckbBold, ckbItalic, ckbUnderLine;
    private TextView mainTxtCount, leadTextCount;
    private LinearLayout toolbar;

    private boolean isBold = false, isItalic = false, isUnderLine = false;
    private int textColor = Color.BLACK;

    private String oldStr = "", mContents = "";
    private String tag1 = "【正文】", tag2 = "【导语】", tag3 = "【编后】";
    private ArrayList alertStrIds = new ArrayList<Integer>();

    public SpannableEditbox(Context context) {
        super(context);
    }

    public SpannableEditbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttr(context, attrs, 0);
        initView(context);
    }

    public SpannableEditbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomAttr(context, attrs, defStyleAttr);
        initView(context);
    }

    private void setCustomAttr(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpannableEditbox ,defStyleAttr, 0);
        for(int i = 0; i < typedArray.getIndexCount(); i++){
            int attr = typedArray.getIndex(i);
            if(R.styleable.SpannableEditbox_clickableSpan == attr)
                clickableSpan = typedArray.getBoolean(attr, false);
            else if(R.styleable.SpannableEditbox_bold == attr)
                bold = typedArray.getBoolean(attr, false);
            else if(R.styleable.SpannableEditbox_italic == attr)
                italic = typedArray.getBoolean(attr, false);
            else if(R.styleable.SpannableEditbox_underLine == attr)
                underLine = typedArray.getBoolean(attr, false);
            else if(R.styleable.SpannableEditbox_foregroundColor == attr)
                foregroundColor = typedArray.getColor(attr, 0xffffff);
            else if(R.styleable.SpannableEditbox_showToolbar == attr)
                needToolbar = typedArray.getBoolean(attr, false);
            else if(R.styleable.SpannableEditbox_undoAndRedo == attr)
                undoAndRedo = typedArray.getBoolean(attr, false);
            else if(R.styleable.SpannableEditbox_boldBg == attr)
                boldBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_italicBg == attr)
                italicBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_underLineBg == attr)
                underLineBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_mainTxtBg == attr)
                mainTxtBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_leadBg == attr)
                leadBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_postBg == attr)
                postBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_undoBg == attr)
                undoBg = typedArray.getResourceId(attr, NO_ID);
            else if(R.styleable.SpannableEditbox_redoBg == attr)
                redoBg = typedArray.getResourceId(attr, NO_ID);
            else {
                //do something if need
                break;
            }
        }
        typedArray.recycle();
    }

    private int checkTag(){
        int strId = 0;
        if(alertStrIds.size() == 0)
            return strId;
        if((isTagBeCleared(tag1) || isTagBeCleared(tag2) || isTagBeCleared(tag3)) && alertStrIds.contains(R.string.richedt_alert_cleartag)){
            strId = R.string.richedt_alert_cleartag;
        }
        else if(checkTagOrder() && alertStrIds.contains(R.string.richedt_alert_order)){
            strId = R.string.richedt_alert_order;
        }
        else if(checkMainTag() && alertStrIds.contains(R.string.richedt_alert_notext_before_main)){
            strId = R.string.richedt_alert_notext_before_main;
        }else if(checkTagCount() && alertStrIds.contains(R.string.richedt_alert_norepeat)){
            strId = R.string.richedt_alert_norepeat;
        }
        alertStrIds.remove((Object) strId);
        return strId;
    }

    private boolean isTagBeCleared(String tag){
        if(!mContents.contains(tag)) {
            return true;
        }
        return false;
    }

    private boolean checkTagOrder(){
        int pos1 = getTagPos(tag1), pos2 = getTagPos(tag2), pos3 = getTagPos(tag3);
        if((pos3 != -1) && (pos3 < (pos2 + pos1)) || ((pos2 != -1) && (pos2 < pos1))){
            return true;
        }
        return false;
    }

    private int getTagPos(String tag){
        return getStrByReplace(editText.getText(), "\n|\\s*").indexOf(tag);
//        return editText.getText().replaceAll("\\n\\s*", "").indexOf(tag);
    }

    private boolean checkMainTag(){
        if(getTagPos(tag1) > 0){
            return true;
        }
        return false;
    }

    private boolean checkTagCount(){
//        int t2 = editText.getText().replaceAll("\\n\\s*", "").lastIndexOf(tag2), t3 = editText.getText().replaceAll("\\n\\s*", "").lastIndexOf(tag3);
        int t2 = getStrByReplace(editText.getText(), "\n|\\s*").lastIndexOf(tag2), t3 = getStrByReplace(editText.getText(), "\n|\\s*").lastIndexOf(tag3);
        int t = editText.getText().length();
        if((editText.getText().split(tag1).length > 2) || (editText.getText().split(tag2).length > 2) || (editText.getText().split(tag3).length > 2)){
            return true;
        }else if(t2 != getTagPos(tag2) || t3 != getTagPos(tag3)){
                return true;

        }
        return false;
    }

    private int getMainCount(){
        if(getTagPos(tag1) == -1)
            return 0;
        else if(getTagPos(tag2) == -1){
            if(getTagPos(tag3) == -1 ){
                return getTextCount(tag1);
            }
            return getTextCount(tag1, tag3);
        }else{
            return getTextCount(tag1, tag2);
        }
    }

    private int getLeadCount(){
        if(getTagPos(tag2) == -1)
            return 0;
        else if(getTagPos(tag3) == -1){
            return getTextCount(tag2);
        }else{
            return getTextCount(tag2, tag3);
        }
    }

    private int getTextCount(String tag1, String tag2){
        int pos1 = getTagPos(tag1) + tag1.length(), pos2 = getTagPos(tag2);
        return pos2 - pos1;
    }

    private int getTextCount(String tag){
        int pos = getTagPos(tag) + tag.length();
        return getStrByReplace(editText.getText(), "\n|\\s*").length() - pos;
    }

    private String getStrByReplace(String src, String pattern){
        if(src.isEmpty())
            return src;
        Pattern ptn = Pattern.compile(pattern);
        Matcher matcher = ptn.matcher(src);
        return matcher.replaceAll("");
    }

    private void initView(Context context){
        alertStrIds.add(R.string.richedt_alert_cleartag);
        alertStrIds.add(R.string.richedt_alert_norepeat);
        alertStrIds.add(R.string.richedt_alert_notext_before_main);
        alertStrIds.add(R.string.richedt_alert_order);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spannable_editbox_layout, this, true);
        editText = (RichEditor) findViewById(R.id.editor);
        editText.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String os, String cs) {
                oldStr = os;
                mContents = cs;
                int id = checkTag();
                if(id != 0){
                    Toast.makeText(getContext(), getResources().getText(id), Toast.LENGTH_SHORT).show();
                }
                int mainCnt = getMainCount(), leadCnt = getLeadCount();
                if(mainCnt < 0)
                    mainCnt = 0;
                if(leadCnt < 0)
                    leadCnt = 0;
                int mainTime = (int)(mainCnt * 60f / 330f + 0.5), leadTime = (int)(leadCnt * 60f / 330f + 0.5);
                mainTxtCount.setText(String.format(getResources().getString(R.string.maintxt_model), mainCnt, mainTime));
                leadTextCount.setText(String.format(getResources().getString(R.string.lead_model), leadCnt, leadTime));
            }
        });
        editText.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String s, List<RichEditor.Type> list) {
                ckbBold.setChecked(list.contains(RichEditor.Type.BOLD));
                ckbItalic.setChecked(list.contains(RichEditor.Type.ITALIC));
                ckbUnderLine.setChecked(list.contains(RichEditor.Type.UNDERLINE));
            }
        });
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
                    ((WebView) v).requestDisallowInterceptTouchEvent(true);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showToolbar();
                        }
                    },200l);

                }
                return false;

            }
        });
        editText.focusEditor();
        ckbBold = (CheckBox) findViewById(R.id.action_bold);
        ckbBold.setOnClickListener(this);
        ckbBold.setVisibility(bold ? View.VISIBLE : View.GONE);
        ckbBold.setBackgroundResource(boldBg == NO_ID ? R.drawable.selector_default_spanedt_bold : boldBg);
        ckbItalic = (CheckBox) findViewById(R.id.action_italic);
        ckbItalic.setOnClickListener(this);
        ckbItalic.setVisibility(italic ? View.VISIBLE : View.GONE);
        ckbItalic.setBackgroundResource(italicBg == NO_ID ? R.drawable.selector_default_spanedt_italic : italicBg);
        ckbUnderLine = (CheckBox) findViewById(R.id.action_underline);
        ckbUnderLine.setOnClickListener(this);
        ckbUnderLine.setVisibility(underLine ? View.VISIBLE : View.GONE);
        ckbUnderLine.setBackgroundResource(underLineBg == NO_ID ? R.drawable.selector_default_spanedt_underline : underLineBg);
        btnMainTxt = (ImageButton) findViewById(R.id.action_mainTxt);
        btnMainTxt.setOnClickListener(this);
        btnMainTxt.setVisibility(mainTxtBg == NO_ID ? GONE : VISIBLE);
        btnMainTxt.setBackgroundResource(mainTxtBg);
        btnLead = (ImageButton) findViewById(R.id.action_lead);
        btnLead.setOnClickListener(this);
        btnLead.setVisibility(leadBg == NO_ID ? GONE : VISIBLE);
        btnLead.setBackgroundResource(leadBg);
        btnPost = (ImageButton) findViewById(R.id.action_post);
        btnPost.setOnClickListener(this);
        btnPost.setVisibility(postBg == NO_ID ? GONE : VISIBLE);
        btnPost.setBackgroundResource(postBg);
        undo = (ImageButton) findViewById(R.id.action_undo);
        undo.setOnClickListener(this);
        undo.setVisibility(undoAndRedo ? View.VISIBLE : View.GONE);
        undo.setBackgroundResource(undoBg == NO_ID ? R.drawable.selector_default_spanedt_undo : undoBg);
        redo = (ImageButton) findViewById(R.id.action_redo);
        redo.setOnClickListener(this);
        redo.setVisibility(undoAndRedo ? View.VISIBLE : View.GONE);
        redo.setBackgroundResource(redoBg == NO_ID ? R.drawable.selector_default_spanedt_redo : redoBg);
        mainTxtCount = (TextView) findViewById(R.id.tv_mainTextCount);
        leadTextCount = (TextView) findViewById(R.id.tv_LeadCount);
        toolbar = (LinearLayout) findViewById(R.id.toolbar);
        toolbar.setVisibility(needToolbar ? View.VISIBLE : View.GONE);
        toolbar.setBackgroundColor(foregroundColor);
    }

    @Override
    public void onClick(View v) {
        if(R.id.action_bold == v.getId()){
            isBold = !isBold;
            editText.setBold();
        }else if(R.id.action_italic == v.getId()){
            isItalic = !isItalic;
            editText.setItalic();
        }else if(R.id.action_underline == v.getId()){
            isUnderLine = !isUnderLine;
            editText.setUnderline();
        }else if(R.id.action_undo == v.getId()){
            editText.undo();
        }else if(R.id.action_redo == v.getId()){
            editText.redo();
        }else if(R.id.action_mainTxt == v.getId()){
            editText.insertMainTxt();
        }else if(R.id.action_lead == v.getId()){
            editText.insertLead();
        }else if(R.id.action_post == v.getId()){
            editText.insertPost();
        }
    }

    @Deprecated
    private SpannableString getSpannableString(String src){
        SpannableString spannaText = new SpannableString(src);
        Object span;
        if(isUnderLine)
            span = new UnderlineSpan();
        else if(isBold && !isItalic)
            span = new StyleSpan(Typeface.BOLD);
        else if(isItalic && !isBold)
            span = new StyleSpan(Typeface.ITALIC);
        else if(isBold && isItalic)
            span  =new StyleSpan(Typeface.BOLD_ITALIC);
        else
            span = new StyleSpan(Typeface.NORMAL);
        spannaText.setSpan(span, 0, src.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannaText;
    }

    public String getHtml(){
        return editText.getHtml();
    }

    public void showToolbar(){
        if(toolbar != null)
            toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar(){
        if(toolbar != null)
            toolbar.setVisibility(View.GONE);
    }

    public boolean isToolbarShow(){
        return toolbar.getVisibility() == View.VISIBLE;
    }

    public void setText(String text){
        if(!TextUtils.isEmpty(text))
            editText.setHtml(text);
    }

    public void focus(){
        editText.focusEditor();
    }

}
