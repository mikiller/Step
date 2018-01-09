package com.westepper.step.models;

/**
 * Created by Mikiller on 2018/1/8.
 */

public class DeleteCommitModel extends DisModel {
    protected String commentId;
    protected String responseId;

    public DeleteCommitModel(String discoveryId, int discoveryKind) {
        super(discoveryId, discoveryKind);
    }

    public DeleteCommitModel(int discoveryKind){
        super(null, discoveryKind);
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }
}
