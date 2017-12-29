package com.westepper.step.responses;

import android.util.Log;

import com.google.gson.Gson;
import com.westepper.step.base.Constants;
import com.westepper.step.utils.ContactsHelper;
import com.westepper.step.utils.MXPreferenceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Mikiller on 2017/10/26.
 */

public class ReachedList implements Serializable {
    List<ReachedId> reachedLists;
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
        rl.getReachedLists().addAll(reachedLists);
        reachedLists = new ArrayList<>(new LinkedHashSet<>(rl.getReachedLists()));
        rl.getReachedAchievementIds().addAll(reachedAchievementIds);
        reachedAchievementIds = new ArrayList<>(new LinkedHashSet<>(rl.getReachedAchievementIds()));
        reachedL2Id = rl.reachedL2Id;
        reachedL3Id = rl.reachedL3Id;
    }

    public boolean contains(String id){
        return reachedLists.contains(new ReachedId(id,0));
    }

    public void addReachedId(String id){
        reachedLists.add(new ReachedId(id, 1));
    }

    public List<ReachedId> getReachedLists() {
        return reachedLists;
    }

    public void setReachedLists(List<ReachedId> reachedLists) {
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

    public void saveToLocal(){
        MXPreferenceUtils.getInstance().setString(Constants.REACHED_LIST, new Gson().toJson(this));
    }
}
