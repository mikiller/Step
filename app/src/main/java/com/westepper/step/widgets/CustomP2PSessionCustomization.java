package com.westepper.step.widgets;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.netease.nim.uikit.custom.DefaultP2PSessionCustomization;
import com.netease.nim.uikit.session.SessionCustomization;
import com.westepper.step.R;
import com.westepper.step.activities.P2PSessionDetailActivity;
import com.westepper.step.base.Constants;

import java.util.ArrayList;

/**
 * Created by Mikiller on 2017/11/21.
 */

public class CustomP2PSessionCustomization extends DefaultP2PSessionCustomization {
    public CustomP2PSessionCustomization() {
        SessionCustomization.OptionsButton infoButton = new SessionCustomization.OptionsButton() {
            @Override
            public void onClick(Context context, View view, String sessionId) {
//                Team team = TeamDataCache.getInstance().getTeamById(sessionId);
//                if (team != null && team.isMyTeam()) {
//                    NimUIKit.startTeamInfo(context, sessionId);
//                } else {
//                    Toast.makeText(context, com.netease.nim.uikit.R.string.team_invalid_tip, Toast.LENGTH_SHORT).show();
//                }
                Intent intent = new Intent(context, P2PSessionDetailActivity.class);
                intent.putExtra(Constants.USERINFO, sessionId);
                context.startActivity(intent);
            }
        };
        infoButton.iconId = R.mipmap.ic_p2pdetail;
        buttons = new ArrayList<>();
        buttons.add(infoButton);
    }
}
