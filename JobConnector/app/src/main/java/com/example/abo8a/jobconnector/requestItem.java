package com.example.abo8a.jobconnector;

public class requestItem {

    private int cid;
    private String email;
    private String cname;
    private String date;
   private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public requestItem(int cid, String email, String cname, String date, String status) {
        this.cid = cid;
        this.email = email;
        this.cname = cname;
        this.date = date;
        this.status = status;
    }
}
