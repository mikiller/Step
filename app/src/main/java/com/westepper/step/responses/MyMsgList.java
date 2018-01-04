package com.westepper.step.responses;

import com.westepper.step.models.SignModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mikiller on 2018/1/3.
 */

public class MyMsgList implements Serializable {
    private SignModel toUser;
    private List<MyMessage> messageList;

    public SignModel getToUser() {
        return toUser;
    }

    public void setToUser(SignModel toUser) {
        this.toUser = toUser;
    }

    public List<MyMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MyMessage> messageList) {
        this.messageList = messageList;
    }

    public static class MyMessage implements Serializable{
        private String id;
        private String discoverId;
        private int discoverKind;
        private int messageType;
        private String content;
        private int isDelete;
        private long created_at;
        private SignModel fromUser;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDiscoverId() {
            return discoverId;
        }

        public void setDiscoverId(String discoverId) {
            this.discoverId = discoverId;
        }

        public int getDiscoverKind() {
            return discoverKind;
        }

        public void setDiscoverKind(int discoverKind) {
            this.discoverKind = discoverKind;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public long getCreated_at() {
            return created_at * 1000l;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public SignModel getFromUser() {
            return fromUser;
        }

        public void setFromUser(SignModel fromUser) {
            this.fromUser = fromUser;
        }
    }
}
