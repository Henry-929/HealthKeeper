package comp5216.sydney.edu.au.assignment2.loginFirstTimeUserInfo;


import java.util.HashMap;

public class UserInfo {
    public String gender,birthday,height,weight,bmi;


    public UserInfo(String gender,String height){
        this.gender = gender;
        this.height = height;
        this.weight = "null";
        this.birthday = "null";
        this.bmi = "null";
    }
    public UserInfo(String gender,String birthday,String height,String weight){
        //this.username = username;
        this.gender = gender;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }


    public String getBirthday() {
        return birthday;
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

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gender", gender);
        //result.put("birthday", birthday);
        result.put("height", height);
        //result.put("weight", weight);

        return result;
    }


}
