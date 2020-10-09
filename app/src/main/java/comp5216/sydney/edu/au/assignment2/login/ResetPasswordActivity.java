package comp5216.sydney.edu.au.assignment2.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import comp5216.sydney.edu.au.assignment2.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger_reset);

        //define buttons
        final Button ResetConfirmBtn = findViewById(R.id.btn_reset_confirm);
        final Button BackToSignUpBtn = findViewById(R.id.btn_back_to_sign_in);

        Email=(EditText) findViewById(R.id.logger_reset_email);

        ResetConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //todo
            }
        });

        BackToSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                ResetPasswordActivity.this.startActivity(intent);
            }
        });

        ResetConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                sendPasswordReset();
            }
        });
    }

    public void sendPasswordReset() {
        // [START send_password_reset]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String emailAddress = Email.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Reset email instructions sent to " ,Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(ResetPasswordActivity.this,  " does not exist" ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END send_password_reset]
    }
}