package com.example.e_commerce;

public class UserHelperClass {
    String name, username ,email, password, age;

    public UserHelperClass(String name, String username,String email, String password, String age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
