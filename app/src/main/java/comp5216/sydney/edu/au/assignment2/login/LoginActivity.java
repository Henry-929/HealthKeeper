package comp5216.sydney.edu.au.assignment2.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.InfoActivity_1;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;



public class LoginActivity extends AppCompatActivity {

    //define login status
    public final int LOGIN_SUCCESS_CODE = 100;
    public final int LOGIN_FAIL_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger_signin);

        //define user input name& password
        //todo xxxxxx
        //define buttons
        final Button SignInBtn = findViewById(R.id.btn_sign_in);
        final Button SignUpBtn = findViewById(R.id.btn_sign_up);
        final Button ResetBtn = findViewById(R.id.btn_reset_password);
        final Button testBtn = findViewById(R.id.btn_test_to_main);
        final Button testBtn2 = findViewById(R.id.btn_test_to_info);

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                if (register != null) {
                    LoginActivity.this.startActivity(register);
                }
            }
        });

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user name is empty
                //todo
                //if user password is empty
                //todo
                //get user login status
                //todo
            }
        });

        ResetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent resetpassword = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                if (resetpassword != null) {
                    LoginActivity.this.startActivity(resetpassword);
                }
            }
        });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                if ( i != null) {
                    LoginActivity.this.startActivity(i);
                }
            }
        });

        testBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this, InfoActivity_1.class);
                if ( i != null) {
                    LoginActivity.this.startActivity(i);
                }
            }
        });
    }
}