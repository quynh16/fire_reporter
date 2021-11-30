package com.example.fire_reporter2;

public class Credentials {
    private String Email = "Admin@gmail.com";
    private String Password = "12345678";
    private String Full_name = "Charlie Hua";

    Credentials(String email, String password, String full_name) {
        this.Email = email;
        this.Password = password;
        this.Full_name = full_name;
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

    public void setPassword(String password) {
        Password = password;
    }
    public String getFull_name() {
        return Full_name;
    }
    public void setFull_name(String full_name) {
        Full_name = full_name;
    }
}

