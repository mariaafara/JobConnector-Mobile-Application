package com.example.abo8a.jobconnector;

/**
 * Created by abo8a on 3/21/2018.
 */

public class Education {

    private  int id;
    private  String major;
    private  String degree;
    private  String university_name;
    private  String starting_date;
    private  String completion_date;

    public Education(int id,String major,String degree,String university_name,String starting_date,String completion_date) {
        this.id=id;
        this.major=major;
        this.degree=degree;
        this.university_name=university_name;
        this.starting_date=starting_date;
        this.completion_date=completion_date;



    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
    }

    public String getCompletion_date() {
        return completion_date;
    }

    public void setCompletion_date(String completion_date) {
        this.completion_date = completion_date;
    }
}
