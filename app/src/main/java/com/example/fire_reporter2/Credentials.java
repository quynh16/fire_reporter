package com.example.fire_reporter2;

import java.util.ArrayList;

public class Credentials {
    String Email, Password, Full_name;
    int num_reports;

    public Credentials(){
    }

    Credentials(String email, String password, String full_name) {
        this.Email = email;
        this.Password = password;
        this.Full_name = full_name;
        this.num_reports = 0;
    }

    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getPassword() {
        return Password;
    }
    public int getNum_reports() { return num_reports; }

    public void setPassword(String password) {
        Password = password;
    }
    public String getFull_name() {
        return Full_name;
    }
    public void setFull_name(String full_name) {
        Full_name = full_name;
    }
    public void setNum_reports(int num_reports) { this.num_reports = num_reports; }
}

