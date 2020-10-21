package comp5216.sydney.edu.au.assignment2.login;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import comp5216.sydney.edu.au.assignment2.R;

public class SplashActivity extends LoginActivity {
    private final long SPLASH_DISP_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_splash);

        class TimerTaskHande extends TimerTask{
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTaskHande(), SPLASH_DISP_TIME);
    }

}
