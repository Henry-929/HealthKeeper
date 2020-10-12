package comp5216.sydney.edu.au.assignment2.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
        final RadioGroup genderGroup = findViewById(R.id.gender_radioGroup);
//        final RadioButton maleBtn = findViewById(R.id.label_gender_male);
//        final RadioButton femaleBtn = findViewById(R.id.label_gender_female);
        Height = (EditText)findViewById(R.id.input_user_height);

        //get user info_1
        final LinearLayout maleLL=findViewById(R.id.gender_ll_male);
        final LinearLayout femaleLL=findViewById(R.id.gender_ll_female);

        //todo 获取性别，通过监听LinearLayout的点击来获得

        //todo 获取身高


        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch(checkedID){
                    case R.id.label_gender_male: gender = "Male";break;
                    case R.id.label_gender_female: gender = "Female";break;
                }

                Toast.makeText(InfoActivity_1.this,gender,Toast.LENGTH_LONG).show();

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userInfoAdd();

            }
        });
    }


    public void userInfoAdd(){

        String height = Height.getText().toString();

        //if 用户输入不为空
        if(height.isEmpty()){
            Height.setError("Height is required");
            Height.requestFocus();
            return;
        }
        if(gender.isEmpty()){
            return;
        }
        Intent intent = new Intent(InfoActivity_1.this, InfoActivity_2.class);
        InfoActivity_1.this.startActivity(intent);

    }
}
