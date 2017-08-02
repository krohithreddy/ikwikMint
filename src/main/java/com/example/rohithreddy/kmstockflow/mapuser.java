package com.example.rohithreddy.kmstockflow;

/**
 * Created by rohithreddy on 26/07/17.
 */

public class mapuser {
    private int id;
    private String username;
    private String outletname;
    private String phonenumber;

    public mapuser(int id,String outletname, String username, String phonenumber) {
        this.id = id;
        this.username = username;
        this.outletname = outletname;
        this.phonenumber = phonenumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOutletname() {
        return outletname;
    }

    public void setOutletname(String outletname) {
        this.outletname = outletname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
