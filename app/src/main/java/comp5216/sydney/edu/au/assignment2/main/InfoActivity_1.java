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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.LoginActivity;
import comp5216.sydney.edu.au.assignment2.login.RegisterActivity;
import comp5216.sydney.edu.au.assignment2.login.User;

public class InfoActivity_1 extends AppCompatActivity {
    public static String uid;
    public static String notFirstTime;

    private EditText Height;
    public String height,gender;//用于数据库存储的String值


    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_1);

        //define buttons
        //获取性别，通过监听RadioGroup的点击来获得
        final Button nextBtn=findViewById(R.id.btn_gender_to_birth);
        final RadioGroup genderGroup = findViewById(R.id.gender_radioGroup);
//        final RadioButton maleBtn = findViewById(R.id.label_gender_male);
//        final RadioButton femaleBtn = findViewById(R.id.label_gender_female);
        //获取身高
        Height = (EditText)findViewById(R.id.input_user_height);

        //get user info_1
        final LinearLayout maleLL=findViewById(R.id.gender_ll_male);
        final LinearLayout femaleLL=findViewById(R.id.gender_ll_female);

        databaseReference = FirebaseDatabase.getInstance().getReference();


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
                //notFirstTime = "true";
                userInfoAdd();
                //userInfoAdd_toDatabase();

            }
        });
    }


    public void userInfoAdd(){

        height = Height.getText().toString();

        //if 用户输入不为空
        if(height.isEmpty()){
            Height.setError("Height is required");
            Height.requestFocus();
            return;
        }
        if(gender.isEmpty()){
            // Toast.makeText(InfoActivity_1.this,"Gender is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }
        userInfoAdd_toDatabase();
//        Intent intent = new Intent(InfoActivity_1.this, InfoActivity_2.class);
//        InfoActivity_1.this.startActivity(intent);


    }

    public void userInfoAdd_toDatabase(){

        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){
                            //将gender height 写入database

                            String newKey = databaseReference.child("Users").child(uid).push().getKey();

                            UserInfo userInfo = new UserInfo(gender,height);

                            databaseReference.child("Users").child(uid).child(newKey).setValue(userInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(InfoActivity_1.this,"gender & height have saved in database successfully!",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(InfoActivity_1.this, InfoActivity_2.class);
                                            InfoActivity_1.this.startActivity(intent);
                                        }
                                    });

                        }else{
                            Toast.makeText(InfoActivity_1.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}
