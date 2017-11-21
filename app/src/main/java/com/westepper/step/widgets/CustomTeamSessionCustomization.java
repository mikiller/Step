package com.westepper.step.widgets;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.custom.DefaultTeamSessionCustomization;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nimlib.sdk.team.model.Team;
import com.westepper.step.R;

import java.util.ArrayList;

/**
 * Created by Mikiller on 2017/11/21.
 */

public class CustomTeamSessionCustomization extends DefaultTeamSessionCustomization {
    public CustomTeamSessionCustomization() {
        // ActionBar右侧按钮，跳转至群信息界面
        SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId) {
                Team team = TeamDataCache.getInstance().getTeamById(sessionId);
                if (team != null && team.isMyTeam()) {
                    NimUIKit.startTeamInfo(context, sessionId);
                } else {
                    Toast.makeText(context, com.netease.nim.uikit.R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
                }
            }
        };
        infoButton.iconId = R.mipmap.ic_teamdetail;
        buttons = new ArrayList<>();
        buttons.add(infoButton);
    }
}
