package com.example.abo8a.jobconnector;

/**
 * Created by lenovo on 3/19/2018.
 */

public class PostData {
    private int pid;
    private int cid;
    private String company_name;
    private String job_type;//position
    private int nbr_applicants;


    public int getPid() {
        return pid;
    }

    public int getCid() {
        return cid;
    }

    public PostData(int pid, int cid, String job_type, String company_name, int nbr_applicants) {
        this.pid = pid;
        this.cid = cid;
        this.job_type = job_type;
        this.company_name = company_name;
        this.nbr_applicants = nbr_applicants;
    }


    public void setPid(int pid) {
        this.pid = pid;
    }


    public String getCompany_name() {
        return company_name;
    }


    public String getJob_type() {
        return job_type;
    }


    public int getNbr_applicants() {
        return nbr_applicants;
    }

    public void setNbr_applicants(int nbr_applicants) {
        this.nbr_applicants = nbr_applicants;
    }
}