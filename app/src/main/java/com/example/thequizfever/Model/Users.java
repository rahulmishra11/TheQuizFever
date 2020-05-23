package com.example.thequizfever.Model;

public  class  Users {
    private String Name,Phone,Pass;

    public  Users()  {

    }

    public Users(String name, String phone, String pass) {
        this.Name = name;
        this.Phone = phone;
        this.Pass = pass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
