package com.westepper.step.responses;

import com.mikiller.mkglidelib.utils.BitmapUtils;
import com.westepper.step.models.Privacy;
import com.westepper.step.models.SignModel;

/**
 * Created by Mikiller on 2017/10/10.
 */

public class UserInfo extends SignModel {
    public static int HEADIMG = 1, COVER = 2, ALL = 0;

    protected String cover;
    private String sign;
    private Privacy privacy_info;
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Privacy getPrivacy_info() {
        return privacy_info;
    }

    public void setPrivacy_info(Privacy privacy_info) {
        this.privacy_info = privacy_info;
    }

    public void getBase64Img(int type){
        if(type != COVER)
            headImg = BitmapUtils.getBmpBase64Str(headImg, 240, 240);
        if(type != HEADIMG)
            cover = BitmapUtils.getBmpBase64Str(cover, -1, -1);
    }

}
