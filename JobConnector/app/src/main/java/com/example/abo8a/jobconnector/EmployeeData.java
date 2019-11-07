package com.example.abo8a.jobconnector;

/**
 * Created by lenovo on 3/19/2018.
 */

public class EmployeeData {
    private int eid;
    private String email;
    private String fname;
    private String lname;
    private String city;
    private String category;
    private String totalexperience;
    private String languages;
    private double rate;
    private String request;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EmployeeData(int eid, String fname, String lname, String city, String category, String email, String totalexperience,
                        String languages,double rate,String request) {
        this.eid=eid;
        this.fname = fname;
        this.lname = lname;
        this.city = city;
        this.category = category;
        this.languages=languages;
        this.totalexperience = totalexperience;
        this.email=email;
        this.rate=rate;
        this.request=request;


    }
    public String  getRequest() {
        return request;
    }
    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTotalexperience() {
        return totalexperience;
    }

    public void setTotalexperience(String totalexperience) {
        this.totalexperience = totalexperience;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }
}