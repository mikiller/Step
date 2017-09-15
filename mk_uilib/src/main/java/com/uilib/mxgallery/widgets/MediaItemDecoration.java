/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uilib.mxgallery.widgets;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class MediaItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;
    private float mSpacing;
    private boolean mIncludeEdge;

    public MediaItemDecoration(int spanCount, float spacing, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % mSpanCount; // item column
        if (mIncludeEdge) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = (int) (mSpacing - column * mSpacing / mSpanCount);
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (int) ((column + 1) * mSpacing / mSpanCount);

            if (position < mSpanCount) { // top edge
                outRect.top = (int) mSpacing;
            }
            if(position > parent.getAdapter().getItemCount() - mSpanCount)
                outRect.bottom = (int) mSpacing;
//            outRect.bottom = position < mSpanCount ? mSpacing : mSpacing + 90; // item bottom
        } else {
            // column * ((1f / spanCount) * spacing)
            outRect.left = (int) (column * mSpacing / mSpanCount);
            // spacing - (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (int) (mSpacing - (column + 1) * mSpacing / mSpanCount);
//            if (position >= mSpanCount) {

//            outRect.top = (int) mSpacing; // item top
//            outRect.bottom = 0;
//           }

        }
        outRect.top = (int) mSpacing;
    }
}
