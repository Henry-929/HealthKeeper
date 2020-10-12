package comp5216.sydney.edu.au.assignment2.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.InfoActivity_1;
import comp5216.sydney.edu.au.assignment2.main.InfoActivity_2;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;



public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText Email, Password;

    //define login status
    public final int LOGIN_SUCCESS_CODE = 100;
    public final int LOGIN_FAIL_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger_signin);
        mAuth = FirebaseAuth.getInstance();
        //define user input name& password
        //todo xxxxxx
        //define buttons
        final Button SignInBtn = findViewById(R.id.btn_sign_in);
        final Button SignUpBtn = findViewById(R.id.btn_sign_up);
        final Button ResetBtn = findViewById(R.id.btn_reset_password);
        final Button testBtn = findViewById(R.id.btn_test_to_main);
        final Button testInfoBtn = findViewById(R.id.btn_test_to_info);
        Email=(EditText) findViewById(R.id.logger_signin_email) ;
        Password=(EditText) findViewById(R.id.logger_signin_password);

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(register);
            }
        });

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });

        ResetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent resetpassword = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                LoginActivity.this.startActivity(resetpassword);
            }
        });

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });

        testInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, InfoActivity_1.class);
                LoginActivity.this.startActivity(i);

            }
        });
    }

    public void userlogin(){
        String email=Email.getText().toString().trim();
        String password=Password.getText().toString().trim();

        if(email.isEmpty()){
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please enter a valid email");
            Email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }
        if(password.length()<6){
            Password.setError("Minimum password length should be 6 characters");
            Password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUserMetadata metadata = mAuth.getCurrentUser().getMetadata();
                    if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
                        Toast.makeText(LoginActivity.this,"Signed in successfully, please add your information!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, InfoActivity_1.class));
                        // The user is new
                    } else {
                        Toast.makeText(LoginActivity.this,"Sign in successfully,welcome back!",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        // This is an existing user
                    }

                }else{
                    Toast.makeText(LoginActivity.this,"Sign in failed, incorrect email or password!",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}