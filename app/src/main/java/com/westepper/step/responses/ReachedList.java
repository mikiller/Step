package com.westepper.step.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/26.
 */

public class ReachedList implements Serializable {
    List<String> reachedLists;
    List<String> reachedAchievementIds;
    String reachedL2Id;
    String reachedL3Id;

    public ReachedList() {
        reachedLists = new ArrayList<>();
        reachedAchievementIds = new ArrayList<>();
    }

    public void updateReachedList(ReachedList rl){
        if (rl == null)
            return;
        reachedLists.addAll(rl.getReachedLists());
        reachedLists = new ArrayList<>(new LinkedHashSet<>(reachedLists));
        reachedAchievementIds.addAll(rl.getReachedAchievementIds());
        reachedAchievementIds = new ArrayList<>(new LinkedHashSet<>(reachedAchievementIds));
        reachedL2Id = rl.reachedL2Id;
        reachedL3Id = rl.reachedL3Id;
    }

    public void addReachedId(String id){
        reachedLists.add(id);
    }

    public boolean hasReachedId(String id){
        return reachedLists.contains(id);
    }

    public List<String> getReachedLists() {
        return reachedLists;
    }

    public void setReachedLists(List<String> reachedLists) {
        this.reachedLists = reachedLists;
    }

    public String getReachedL3Id() {
        return reachedL3Id;
    }

    public void setReachedL3Id(String reachedL3Id) {
        this.reachedL3Id = reachedL3Id;
    }

    public String getReachedL2Id() {
        return reachedL2Id;
    }

    public void setReachedL2Id(String reachedL2Id) {
        this.reachedL2Id = reachedL2Id;
    }

    public List<String> getReachedAchievementIds() {
        return reachedAchievementIds;
    }

    public void setReachedAchievementIds(List<String> reachedAchievementIds) {
        this.reachedAchievementIds = reachedAchievementIds;
    }
}
