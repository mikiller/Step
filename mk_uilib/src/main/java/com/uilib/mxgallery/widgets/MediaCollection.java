package com.uilib.mxgallery.widgets;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.uilib.mxgallery.models.ItemModel;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mikiller on 2017/5/12.
 */

public class MediaCollection implements Parcelable {
    public static final String SET_KEY = "model_set";
    private static final String LAST_MODEL = "lastModel";
    private static final String MAX_COUNT = "maxCount";
    public boolean isMultiple;
    public int maxSelectionCount;
    public boolean isNeedShowSelected = true;
    public int totalImage = 0;
    public Set<ItemModel> modelSet;
    private ItemModel lastModel;
    private ArrayList<String> selectedPath = new ArrayList<>();

    public static final Creator<MediaCollection> CREATOR = new Creator<MediaCollection>() {
        @Override
        public MediaCollection createFromParcel(Parcel in) {
            return new MediaCollection(in);
        }

        @Override
        public MediaCollection[] newArray(int size) {
            return new MediaCollection[size];
        }
    };

    public MediaCollection(boolean isMultiple, int maxSelectionCount) {
        this.isMultiple = isMultiple;
        setMaxSelectionCount(maxSelectionCount);
        modelSet = new LinkedHashSet<>();
    }

    protected MediaCollection(Parcel in) {
        isMultiple = in.readByte() != 0;
        maxSelectionCount = in.readInt();
        isNeedShowSelected = in.readByte() != 0;
        totalImage = in.readInt();
        lastModel = in.readParcelable(ItemModel.class.getClassLoader());
    }
//    public void onCreate(Bundle bundle) {
//        if (bundle == null) {
//            modelSet = new LinkedHashSet<>();
//        } else {
////            List<ItemModel> tmp = bundle.getParcelableArrayList(SET_KEY);
//            modelSet = new LinkedHashSet<>(tmp);
//        }
//    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SET_KEY, this);
        Log.e("collection", "save self");
//        outState.putParcelableArrayList(SET_KEY, new ArrayList(modelSet));
    }

    public void setMaxSelectionCount(int max) {
        maxSelectionCount = max;
    }

    public boolean isMaxSelection() {
        if (maxSelectionCount == -1)
            return false;
        else {
            return modelSet.size() == maxSelectionCount;
        }
    }

    public List<File> getModelFiles() {
        ArrayList<File> files = new ArrayList<>();
        for (ItemModel model : modelSet) {
            String path = model.getPath();
            File file = new File(path);
            files.add(file);
        }
        return files;
    }

    public void addMedia(ItemModel model) {
        if (!isContainMedia(model)) {
            modelSet.add(model);
            if (!isMultiple)
                lastModel = model;
        }
    }

    public void removeMedia(ItemModel model) {
        if (isContainMedia(model)) {
            modelSet.remove(model);
        }
    }

    public void removeLastMedia() {
        if (lastModel != null && isContainMedia(lastModel)) {
            modelSet.remove(lastModel);
            lastModel = null;
        }
    }

    public boolean isContainMedia(ItemModel model) {
        return modelSet.contains(model);
    }

    public boolean canSelectModel(ItemModel model) {
        if (isMaxSelection() && isMultiple)
            return isContainMedia(model);
        else
            return true;
    }

    public int getMediaPos(ItemModel model) {
        return new ArrayList<>(modelSet).indexOf(model);
    }

    public ArrayList<Integer> getSelectedModelIds() {
        if (modelSet.size() == 0)
            return null;
        else {
            ArrayList<Integer> ids = new ArrayList<>();
            for (ItemModel model : modelSet) {
                ids.add((int) model.id);
            }
            return ids;
        }
    }

    public void setSelectedModelPath(List<String> paths) {
        this.selectedPath.addAll(paths);
    }

    public ArrayList<String> getSelectedModelPath() {
        for (ItemModel model : modelSet) {
            selectedPath.add(model.path);
        }
        return selectedPath;

    }

    public int getSelectedCount() {
        return modelSet.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isMultiple ? 1 : 0));
        dest.writeInt(maxSelectionCount);
        dest.writeByte((byte) (isNeedShowSelected ? 1 : 0));
        dest.writeInt(totalImage);
        dest.writeParcelable(lastModel, flags);
    }
}
