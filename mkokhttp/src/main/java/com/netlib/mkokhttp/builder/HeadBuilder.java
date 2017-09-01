package com.netlib.mkokhttp.builder;

import com.netlib.mkokhttp.utils.OkHttpUtils;
import com.netlib.mkokhttp.request.OtherRequest;
import com.netlib.mkokhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
