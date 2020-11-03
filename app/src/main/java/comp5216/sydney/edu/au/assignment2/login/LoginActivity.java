package comp5216.sydney.edu.au.assignment2.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.loginFirstTimeUserInfo.*;
import comp5216.sydney.edu.au.assignment2.main.*;



public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
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
        Email=(EditText) findViewById(R.id.logger_signin_email);
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

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    //Verify email address
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Verified successfully",
                                Toast.LENGTH_SHORT).show();

                        databaseReference =  FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        //判断是否是第一次登录 从database中 【读取数据】
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot data: snapshot.getChildren()){

                                    if(data.getKey().equals("notFirstTime")){


                                        if(data.getValue().equals("false")){
                                            //是第一次登录，进入user info add界面

                                            //进入user info add界面
                                            //Toast.makeText(LoginActivity.this,"Signed in successfully, please add your information!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(LoginActivity.this, InfoActivity_1.class));
                                            break;
                                            // The user is new

                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this,"Sign in successfully,welcome back!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                            // This is an existing user

                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else{
                        Toast.makeText(LoginActivity.this, "Please verify your email address",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(LoginActivity.this,"Sign in failed, incorrect email or password!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}