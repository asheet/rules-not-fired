package com.andrew.model;

public class Person {
    private String FullName;
    private boolean isRightHanded;

    public Person() {
        
    }

    public Person(String fullName, boolean isRightHanded) {
        FullName = fullName;
        this.isRightHanded = isRightHanded;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public boolean isRightHanded() {
        return isRightHanded;
    }

    public void setRightHanded(boolean isRightHanded) {
        this.isRightHanded = isRightHanded;
    }
    
}