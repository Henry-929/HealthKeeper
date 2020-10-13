package comp5216.sydney.edu.au.assignment2.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.User;

public class InfoActivity_2 extends AppCompatActivity {

    public static String uid;

    private EditText Birthday,Weight;
    public String weight,birthday;//用于数据库存储的String值

    DatabaseReference databaseReference,databaseSetTrue;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_2);

        //define button
        final Button startBtn=findViewById(R.id.btn_birth_to_main);

        //获取生日&体重
        Birthday = (EditText)findViewById(R.id.birth_selected_date);
        Weight = (EditText)findViewById(R.id.inout_user_weight);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userInfoAdd2();//【第一步】
                //userInfoAdd_toDatabase2();【第二步】
                //进入到MainActivity         【第三步】

            }
        });
    }

    public void userInfoAdd2(){
        weight = Weight.getText().toString();
        birthday = Birthday.getText().toString();

        if(birthday.isEmpty()){
            Birthday.setError("Birthday is required");
            Birthday.requestFocus();
            // Toast.makeText(InfoActivity_1.this,"Gender is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }
        //if 用户输入不为空
        if(weight.isEmpty()){
            Weight.setError("Weight is required");
            Weight.requestFocus();
            return;
        }

        userInfoAdd_toDatabase2();

//        Intent intent = new Intent(InfoActivity_2.this, MainActivity.class);
//        InfoActivity_2.this.startActivity(intent);

    }

    public void userInfoAdd_toDatabase2(){

        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){
                            //将birthday weight 写入database

                            for(DataSnapshot d: snapshot.getChildren()){
                                //d.getKey()是userInfo的key

                                String userInfo_Key = d.getKey();
                                if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")) {
                                    //将 birthday 写入数据库
                                    databaseReference.child("Users").child(uid)
                                            .child(userInfo_Key).child("birthday").setValue(birthday);

                                    //将 weight 写入数据库
                                    databaseReference.child("Users").child(uid)
                                            .child(userInfo_Key).child("weight").setValue(weight);


                                    //从database中 写入数据/更新数据
                                    //将数据库中的notFirstTime更新为"true"
                                    databaseSetTrue = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    databaseSetTrue.child("notFirstTime").setValue("true")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(InfoActivity_2.this, "notFirstTime = TRUE !", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(InfoActivity_2.this, MainActivity.class);
                                                    InfoActivity_2.this.startActivity(intent);
                                                }
                                            });
                                }

                            }
                        }else{
                            Toast.makeText(InfoActivity_2.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }
}