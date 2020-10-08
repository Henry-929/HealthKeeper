package comp5216.sydney.edu.au.assignment2.login;

public class User {
    public String username,email,password,confirm_password,security;

    public User(String username,String email, String password,String confirm_password,String security){
        this.username=username;
        this.email=email;
        this.password=password;
        this.confirm_password=confirm_password;
        this.security=security;
    }
}
