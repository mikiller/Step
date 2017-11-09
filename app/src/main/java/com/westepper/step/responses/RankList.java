package com.westepper.step.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2017/11/9.
 */

public class RankList implements Serializable {
    private int userRank;
    List<Rank> rankList;

    public List<Rank> getRankList() {
        return rankList;
    }

    public void setRankList(List<Rank> rankList) {
        this.rankList = rankList;
    }

    public int getUserRank() {
        return userRank;
    }

    public void setUserRank(int userRank) {
        this.userRank = userRank;
    }

    public static class Rank implements Serializable{
        private String userId;
        private String nickName;
        private String headUrl;
        private int pos;
        private String reachecNum;
        private String achievedNum;

        public String getAchievedNum() {
            return achievedNum;
        }

        public void setAchievedNum(String achievedNum) {
            this.achievedNum = achievedNum;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getReachecNum() {
            return reachecNum;
        }

        public void setReachecNum(String reachecNum) {
            this.reachecNum = reachecNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
