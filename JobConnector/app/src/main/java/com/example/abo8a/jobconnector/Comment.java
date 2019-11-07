package com.example.abo8a.jobconnector;

public class Comment {

    public int rating;
    public String Comment;
    public String date;
    public String companyEmail;
    public String CompanyName;

    public Comment(int rating, String comment, String date, String companyEmail, String companyName) {
        this.rating = rating;
        Comment = comment;
        this.date = date;
        this.companyEmail = companyEmail;
        CompanyName = companyName;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return Comment;
    }

    public String getDate() {
        return date;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getCompanyName() {
        return CompanyName;
    }
}
