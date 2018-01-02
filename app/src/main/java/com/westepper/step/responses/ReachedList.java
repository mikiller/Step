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
    private List<ReachedId> reachedLists;
    private List<String> reachedAchievementIds;
    private List<ReachedId> reachedL2Id;
    private List<ReachedId> reachedL3Id;

    public ReachedList() {
        reachedLists = new ArrayList<>();
        reachedAchievementIds = new ArrayList<>();
        reachedL2Id = new ArrayList<>();
        reachedL3Id = new ArrayList<>();
    }

    public void updateReachedList(ReachedList rl){
        if (rl == null)
            return;
        rl.getReachedAchievementIds().addAll(reachedAchievementIds);
        reachedAchievementIds = new ArrayList<>(new LinkedHashSet<>(rl.getReachedAchievementIds()));
        updateReachedId(rl.getReachedLists(), 1);
        updateReachedId(rl.getReachedL2Id(), 2);
        updateReachedId(rl.getReachedL3Id(), 3);
    }

    public void updateReachedId(List<ReachedId> rIds, int level){
        if (rIds == null)
            return;
        switch (level){
            case 1:
                updateL1(rIds);
                break;
            case 2:
                updateL2(rIds);
                break;
            case 3:
                updateL3(rIds);
                break;
        }
    }

    private void updateL1(List<ReachedId> rIds){
        rIds.addAll(reachedLists);
        reachedLists = new ArrayList<>(new LinkedHashSet<>(rIds));
    }

    private void updateL2(List<ReachedId> rIds){
        rIds.addAll(reachedL2Id);
        reachedL2Id = new ArrayList<>(new LinkedHashSet<>(rIds));
    }

    private void updateL3(List<ReachedId> rIds){
        rIds.addAll(reachedL3Id);
        reachedL3Id = new ArrayList<>(new LinkedHashSet<>(rIds));
    }

    public boolean contains(String id, int level){
        boolean rst = false;
        switch (level){
            case 1:
                rst = reachedLists.contains(new ReachedId(id,0));
                break;
            case 2:
                rst = reachedL2Id.contains(new ReachedId(id,0));
                break;
            case 3:
                rst = reachedL3Id.contains(new ReachedId(id,0));
                break;
        }
        return rst;
    }

    public void addReachedId(String id, int level){
        switch (level){
            case 1:
                reachedLists.add(new ReachedId(id, 1));
                break;
            case 2:
                reachedL2Id.add(new ReachedId(id, 1));
                break;
            case 3:
                reachedL3Id.add(new ReachedId(id, 1));
                break;
        }
    }

    public List<ReachedId> getReachedLists() {
        return reachedLists;
    }

    public void setReachedLists(List<ReachedId> reachedLists) {
        this.reachedLists = reachedLists;
    }

    public List<ReachedId> getReachedL3Id() {
        return reachedL3Id == null ? new ArrayList<ReachedId>() : reachedL3Id;
    }

    public void setReachedL3Id(List<ReachedId> reachedL3Id) {
        this.reachedL3Id = reachedL3Id;
    }

    public List<ReachedId> getReachedL2Id() {
        return reachedL2Id == null ? new ArrayList<ReachedId>() : reachedL2Id;
    }

    public void setReachedL2Id(List<ReachedId> reachedL2Id) {
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
