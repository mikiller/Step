package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/1.
 */

public class CommitList implements Serializable {
    private List<Commit> comment_list;

    public List<Commit> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<Commit> comment_list) {
        this.comment_list = comment_list;
    }
}
