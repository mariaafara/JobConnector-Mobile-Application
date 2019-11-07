package com.example.abo8a.jobconnector;

/**
 * Created by lenovo on 3/19/2018.
 */

public class JobData {
    private int pid;
    private int cid;
    private String mSender;
    private String mTitle;
    private String mDetails;
    private String mTime;
    private String mexperience;
    private String mgender;
    private String mChatEmail;
    private String mDescription;
    private String mCity;
    private String mCategory;
    private String mLanguages;
    private String status;
    private int isActive;

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPid() {
        return pid;
    }

    public int getCid() {
        return cid;
    }

    public String getmChatEmail() {
        return mChatEmail;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmLanguages() {
        return mLanguages;
    }

    public JobData(int pid, int cid, String mSender, String mTitle, String mDetails, String mTime, String mexperience, String mgender, String mChatEmail, String mDescription, String mCity, String mCategory, String mLanguages,String status,int isActive) {
        this.pid=pid;
        this.cid=cid;
        this.mexperience=mexperience;
        this.mSender = mSender;
        this.mTitle = mTitle;
        this.mDetails = mDetails;
        this.mTime = mTime;
        this.mgender=mgender;
        this.mChatEmail=mChatEmail;

        this.status=status;
        this.mDescription=mDescription;
        this.mCity=mCity;
        this.mLanguages=mLanguages;
        this.mCategory=mCategory;
        this.isActive=isActive;

    }

    public String getmSender() {
        return mSender;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDetails() {
        return mDetails;
    }

    public String getmTime() {
        return mTime;
    }

    public String getMexperience() {
        return mexperience;
    }

    public String getMgender() {
        return mgender;
    }
}