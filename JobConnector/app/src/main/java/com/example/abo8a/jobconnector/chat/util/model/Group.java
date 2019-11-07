package com.example.abo8a.jobconnector.chat.util.model;



public class Group extends Room {
    public String id;
    public ListFriend listFriend;

    public Group(){
        listFriend = new ListFriend();
    }
}
