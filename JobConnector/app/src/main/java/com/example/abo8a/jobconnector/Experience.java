package com.example.abo8a.jobconnector;

/**
 * Created by abo8a on 3/21/2018.
 */

public class Experience {

    private int id;
    private String job_In;
    private String experience_duration;
    private String company_name;
    private String job_country;
    private String job_city;
    private String description;


    public Experience(int id, String job_In, String  experience_duration, String company_name, String job_country, String job_city, String description) {

        this.id = id;
        this.job_In = job_In;
        this.experience_duration = experience_duration;
        this.company_name = company_name;
        this.job_country = job_country;
        this.job_city = job_city;
        this.description = description;

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJob_In() {
        return job_In;
    }

    public void setJob_In(String job_In) {
        this.job_In = job_In;
    }


    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getExperience_duration() {
        return experience_duration;
    }

    public void setExperience_duration(String experience_duration) {
        this.experience_duration = experience_duration;
    }

    public String getJob_country() {
        return job_country;
    }

    public void setJob_country(String job_country) {
        this.job_country = job_country;
    }

    public String getJob_city() {
        return job_city;
    }

    public void setJob_city(String job_city) {
        this.job_city = job_city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
