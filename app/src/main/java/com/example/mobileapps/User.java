package com.example.mobileapps;

public class User {
    public String userName;
    public String email;
    public String password;
    public static int ID = 1111;

    public User(){//default constructor
        userName = "";
        email = "";
        password = "";
    }

    public User(String userName, String email){ //alternate constructor
        this.userName = userName;
        this.email = email;
        ID++;
    }

    public int getID(){return ID;}//accessor method
}
