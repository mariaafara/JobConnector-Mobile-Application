package com.example.abo8a.jobconnector;

/**
 * Created by lenovo on 3/19/2018.
 */

public class ApplyData {

    private int pid;
    private int cid;
    private String status;
    private int nbr_applicants;


    public int getPid() {
        return pid;
    }

    public int getCid() {
        return cid;
    }

    public ApplyData(int pid, int cid, String status, int nbr_applicants) {
        this.pid = pid;
        this.cid = cid;
        this.status = status;
        this.nbr_applicants = nbr_applicants;
    }


    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNbr_applicants() {
        return nbr_applicants;
    }

    public void setNbr_applicants(int nbr_applicants) {
        this.nbr_applicants = nbr_applicants;
    }
}