package comp5216.sydney.edu.au.assignment2.login;

import comp5216.sydney.edu.au.assignment2.loginFirstTimeUserInfo.*;

public class User {

    public String userID;
    public String notFirstTime;
    public String username,email,password,confirm_password;

    public User(){}
    public User(String username,String email, String password,String confirm_password){
        this.userID = "null";
        this.username=username;
        this.email=email;
        this.password=password;
        this.confirm_password=confirm_password;
        this.notFirstTime = "false";
    }


    public void setUserID(String userID) {
        this.userID = userID;
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

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public void setNotFirstTime(String notFirstTime) {
        this.notFirstTime = notFirstTime;
    }

    public String userID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public String getNotFirstTime() {
        return notFirstTime;
    }

}
