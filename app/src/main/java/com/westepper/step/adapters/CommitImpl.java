package com.westepper.step.adapters;

import com.westepper.step.responses.Commit;

import java.util.List;

/**
 * Created by Mikiller on 2018/1/8.
 */

public interface CommitImpl {
    void setCommits(List<Commit> commits);
    void addCommit(Commit commits);
}
