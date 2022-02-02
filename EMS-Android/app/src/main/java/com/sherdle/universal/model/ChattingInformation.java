package com.sherdle.universal.model;

import android.support.annotation.VisibleForTesting;

public class ChattingInformation {
    private String chat_id;
    private String user_id;
    private String cst_id;
    private String message;
    private String chattime;
    private String user_name;
    private String user_profile_image_sender;
    private String attachment;
    private String Image;
    private String removed ;
    private String edited_admin;
    private String edited ;

    public ChattingInformation(String chat_id, String user_id, String cst_id, String message, String chattime
            , String user_name, String user_profile_image_sender, String attachment, String Image , String removed, String edited_admin, String edited) {
        this.chat_id=chat_id;
        this.user_id = user_id;
        this.cst_id = cst_id;
        this.message = message;
        this.chattime = chattime;
        this.user_name=user_name;
        this.user_profile_image_sender = user_profile_image_sender;
        this.attachment=attachment;
        this.Image=Image;
        this.removed=removed;
        this.edited_admin=edited_admin;
        this.edited=edited;

    }

    public String getEdited() {
        return edited;
    }

    public String getImage() {
        return Image;
    }

    public String getAttachment() {
        return attachment;
    }

    public String getUser_profile_image_sender() {
        return user_profile_image_sender;
    }

    public String getChat_id() {
        return chat_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCst_id() {
        return cst_id;
    }

    public String getMessage() {
        return message;
    }

    public String getChattime() {
        return chattime;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getRemoved() {
        return removed;
    }

    public String getEdited_admin() {
        return edited_admin;
    }
}

