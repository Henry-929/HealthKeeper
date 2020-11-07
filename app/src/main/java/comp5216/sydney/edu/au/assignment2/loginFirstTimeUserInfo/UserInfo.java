package comp5216.sydney.edu.au.assignment2.loginFirstTimeUserInfo;

import java.util.HashMap;

public class UserInfo {
    public String gender,age,height,weight,bmi;


    public UserInfo(String gender,String height){
        this.gender = gender;
        this.height = height;
        this.weight = "null";
        this.age = "null";
        this.bmi = "null";
    }

    public UserInfo(String gender,String age,String height,String weight){
        //this.username = username;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }


    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }



}
