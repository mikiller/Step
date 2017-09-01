package com.netlib.mkokhttp.builder;

import com.netlib.mkokhttp.request.PostStringRequest;
import com.netlib.mkokhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by zhy on 15/12/14.
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder>
{
    public static MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content)
    {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType)
    {
        this.mediaType = mediaType;
        return this;
    }

    public PostStringBuilder() {
    }

    public PostStringBuilder(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public RequestCall build()
    {
        return new PostStringRequest(url, tag, params, headers, content, mediaType,id).build();
    }


}
