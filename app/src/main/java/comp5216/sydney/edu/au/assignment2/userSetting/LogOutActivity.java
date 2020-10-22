package comp5216.sydney.edu.au.assignment2.userSetting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.LoginActivity;

public class LogOutActivity extends AppCompatActivity {
    // Define variables
//    LinearLayout lLay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Use "activity_user_setting" as the layout
        setContentView(R.layout.activity_user_setting);

        //define button
        final Button button=findViewById(R.id.btn_userSetting_logOut);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LogOutActivity", "Log out ");
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(LogOutActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        // Add the buttons
                        .setPositiveButton(R.string.delete, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(LogOutActivity.this, LoginActivity.class);
                                        LogOutActivity.this.startActivity(intent);
                                    }
                                })
                        .setNegativeButton(R.string.cancel, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    // User cancelled the dialog
                                    // Nothing happens
                                    }
                                });
                // Create the AlertDialog object
                builder.create().show();
            }
        });
    }

}
