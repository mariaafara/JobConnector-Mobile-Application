package com.example.abo8a.jobconnector;

/**
 * Created by abo8a on 3/18/2018.
 */
public class Skill {

    private  String level;
    private  String skill;
    private  int id;

    public Skill(int id, String skill, String level) {
        this.id = id;
        this.skill = skill;
        this.level = level;

    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}



