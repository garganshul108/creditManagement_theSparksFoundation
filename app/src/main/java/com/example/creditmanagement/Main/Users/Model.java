package com.example.creditmanagement.Main.Users;

import java.io.Serializable;

public class Model implements Serializable , Comparable<Model> {
    private String name , credit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getCredit() {
        return credit;
    }

    void setCredit(String credit) {
        this.credit = credit;
    }

    @Override
    public int compareTo(Model o) {
        return this.getName().compareTo(o.getName());
    }
}
