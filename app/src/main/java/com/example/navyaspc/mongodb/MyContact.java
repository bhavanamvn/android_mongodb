package com.example.navyaspc.mongodb;

/**
 * Created by Navya's PC on 9/3/2017.
 */


// / It's a pojo class used to access data using getter and setter methods.. It cannot extend other class. It cannot implements other class.. 

public class MyContact {

    
    public String first_name;
    public String last_name;
    public String phone_nubmer;


    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_nubmer() {
        return phone_nubmer;
    }
    public void setPhone_number(String phone_nubmer) {
        this.phone_nubmer = phone_nubmer;
    }

}
