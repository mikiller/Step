package com.westepper.step.responses;

import com.westepper.step.models.SignModel;

import java.io.Serializable;

/**
 * Created by Mikiller on 2017/9/27.
 */

public class Commit implements Serializable {
    private String commentId;
    private String comment_blog_id;
    private String comment_blog_kind;
    private SignModel userInfo;
    private String comment_content;
    private long create_time;
    private int responses_count;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment_blog_id() {
        return comment_blog_id;
    }

    public void setComment_blog_id(String comment_blog_id) {
        this.comment_blog_id = comment_blog_id;
    }

    public String getComment_blog_kind() {
        return comment_blog_kind;
    }

    public void setComment_blog_kind(String comment_blog_kind) {
        this.comment_blog_kind = comment_blog_kind;
    }

    public SignModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SignModel userInfo) {
        this.userInfo = userInfo;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public long getCreate_time() {
        return create_time * 1000l;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getResponses_count() {
        return responses_count;
    }

    public void setResponses_count(int responses_count) {
        this.responses_count = responses_count;
    }
}
