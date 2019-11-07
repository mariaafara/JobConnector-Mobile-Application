package com.example.abo8a.jobconnector;

/**
 * Created by abo8a on 3/18/2018.
 */
public class Employee {

    private  int id;
    private  String fname;
    private  String lname;
    private  int age;
    private  String ph_nb;
    private  String email;
    private  String workIn;
    private  String country;
    private  String city;
    private  String image;

    public Employee(int id, String fname, String lname, int age,
                    String ph_nb, String email, String workIn,
                    String country, String city, String image) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.age = age;
        this.ph_nb = ph_nb;
        this.email = email;
        this.workIn = workIn;
        this.country = country;
        this.city = city;
        this.image = image;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPh_nb() {
        return ph_nb;
    }

    public void setPh_nb(String ph_nb) {
        this.ph_nb = ph_nb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkIn() {
        return workIn;
    }

    public void setWorkIn(String workIn) {
        this.workIn = workIn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
