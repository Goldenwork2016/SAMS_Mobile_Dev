package com.sherdle.universal.model;

public class SupportInformation {
    private String id;
    private String cst_id;
    private String user_type;
    private String ticket;
    private String subject;
    private String status;
    private String username;


    public SupportInformation(String ticket,String cst_id, String subject, String status,String id,String user_type,String user_name) {
        this.user_type=user_type;
        this.id=id;
        this.cst_id=cst_id;
        this.ticket = ticket;
        this.subject = subject;
        this.status = status;
        this.username=user_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCst_id(String cst_id) {
        this.cst_id = cst_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCst_id() {
        return cst_id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }
}
