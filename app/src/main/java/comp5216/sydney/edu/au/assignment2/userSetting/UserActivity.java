package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.*;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(UserActivity.this, MainActivity.class));
                        break;
                    case R.id.navigation_add:
                        startActivity(new Intent(UserActivity.this, AddMealActivity.class));
                        break;
                    case R.id.navigation_user:
                        startActivity(new Intent(UserActivity.this, UserActivity.class));
                        break;
                }
                return true;
            }
        });
    }
}