package comp5216.sydney.edu.au.assignment2.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;

public class ResetPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger_reset);

        //define buttons
        final Button ResetConfirmBtn = findViewById(R.id.btn_reset_confirm);
        final Button BackToSignUpBtn = findViewById(R.id.btn_back_to_sign_in);

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
    }
}