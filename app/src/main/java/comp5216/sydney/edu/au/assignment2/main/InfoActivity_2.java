package comp5216.sydney.edu.au.assignment2.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;

public class InfoActivity_2 extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_2);

        //define button
        final Button startBtn=findViewById(R.id.btn_birth_to_main);

        //todo 获取生日

        //todo 获取体重

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            Intent intent = new Intent(InfoActivity_2.this, MainActivity.class);
            //todo if 用户输入不为空
                if (intent != null) {
                    InfoActivity_2.this.startActivity(intent);
                }
            }
        });
    }
}