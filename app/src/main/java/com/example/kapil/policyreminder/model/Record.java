package com.example.kapil.policyreminder.model;

/**
 * Created by KAPIL on 12-01-2018.
 */

public class Record {
    private int id;
    private String name;
    private String policyNum;
    private String expiryDate;
    private String company;
    private String type;
    private String mobileNum;
    private String email;

    public Record(int id, String name, String policyNum, String expiryDate, String company, String type, String mobileNum, String email) {
        this.id = id;
        this.name = name;
        this.policyNum = policyNum;
        this.expiryDate = expiryDate;
        this.company = company;
        this.type = type;
        this.mobileNum = mobileNum;
        this.email = email;
    }

    public Record(String name, String policyNum, String expiryDate, String company, String type, String mobileNum, String email) {
        this.name = name;
        this.policyNum = policyNum;
        this.expiryDate = expiryDate;
        this.company = company;
        this.type = type;
        this.mobileNum = mobileNum;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPolicyNum() {
        return policyNum;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCompany() {
        return company;
    }

    public String getType() {
        return type;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPolicyNum(String policyNum) {
        this.policyNum = policyNum;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
