package com.example.abo8a.jobconnector;

/**
 * Created by abo8a on 3/18/2018.
 */
public class Language {

    private  String level;
    private  String language;
    private  int id;

    public Language(int id, String language, String level) {
        this.id = id;
        this.language = language;
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}



