package com.westepper.step.responses;

import com.westepper.step.models.SignModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/9/27.
 */

public class Commit implements Serializable {
    private String commentId;
    private String comment_blog_id;
    private int comment_blog_kind;
    private SignModel userInfo;
    private String comment_content;
    private String msg;
    private long create_time;
    private int responses_count;
    private List<SubCommit> comment_responses;

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

    public int getComment_blog_kind() {
        return comment_blog_kind;
    }

    public void setComment_blog_kind(int comment_blog_kind) {
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public List<SubCommit> getComment_responses() {
        return comment_responses;
    }

    public void setComment_responses(List<SubCommit> comment_responses) {
        this.comment_responses = comment_responses;
    }

    public void setResponses_count(int responses_count) {
        this.responses_count = responses_count;
    }

    public class SubCommit implements Serializable{
        private String id;
        private SignModel from_user_info, to_user_info;
        private String response_content;
        private long create_time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public SignModel getFrom_user_info() {
            return from_user_info;
        }

        public void setFrom_user_info(SignModel from_user_info) {
            this.from_user_info = from_user_info;
        }

        public SignModel getTo_user_info() {
            return to_user_info;
        }

        public void setTo_user_info(SignModel to_user_info) {
            this.to_user_info = to_user_info;
        }

        public String getResponse_content() {
            return response_content;
        }

        public void setResponse_content(String content) {
            this.response_content = content;
        }

        public long getCreate_time() {
            return create_time * 1000l;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }
    }
}
