package comp5216.sydney.edu.au.assignment2.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.LoginActivity;
import comp5216.sydney.edu.au.assignment2.login.RegisterActivity;

public class InfoActivity_1 extends AppCompatActivity {

    private EditText Height;
    private String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_1);

        //define buttons
        final Button nextBtn=findViewById(R.id.btn_gender_to_birth);

        //get user info_1
        final LinearLayout maleLL=findViewById(R.id.gender_ll_male);
        final LinearLayout femaleLL=findViewById(R.id.gender_ll_female);

        //todo 获取性别，通过监听LinearLayout的点击来获得

        //todo 获取身高

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(InfoActivity_1.this, InfoActivity_2.class);
                //todo if 用户输入不为空
                    if (intent != null) {
                        InfoActivity_1.this.startActivity(intent);
                    }
            }
        });
    }
}
