package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.User;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;
import comp5216.sydney.edu.au.assignment2.main.ReportActivity;

public class MyProfileActivity extends AppCompatActivity {

    public static String uid;

    public EditText textView_name,textView_gender,textView_height,textView_weight,textView_age;
    public String username,gender,height,weight,age;//用于获取数据库存储的身高体重信息
    LinearLayout cancelEdit;
    Button confirmEdit;

    public DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting_info);

        cancelEdit = (LinearLayout)findViewById(R.id.profile_ll_edit_quit);
        cancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //todo 不保存信息，直接退出
                Intent intent = new Intent(MyProfileActivity.this, UserActivity.class);
                if (intent != null) {
                    MyProfileActivity.this.startActivity(intent);
                }
            }
        });

        confirmEdit = (Button)findViewById(R.id.profile_btn_edit_confirm);
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //todo 保存信息，退出
                Intent intent = new Intent(MyProfileActivity.this, UserActivity.class);
                if (intent != null) {
                    MyProfileActivity.this.startActivity(intent);
                }
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference();

        textView_name=(EditText) findViewById(R.id.profile_display_name);
        textView_gender = (EditText) findViewById(R.id.profile_display_gender);
        textView_height = (EditText) findViewById(R.id.profile_display_height);
        textView_weight = (EditText) findViewById(R.id.profile_display_weight);
        textView_age = (EditText) findViewById(R.id.profile_display_birth);

        getUsername_fromDatabse();
        getUserInfo_fromDatabase();

        //add click listener on LinerLayout
        final LinearLayout llEditName = findViewById(R.id.profile_ll_edit_name);
        llEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //todo 修改弹框
            }
        });

        final LinearLayout llEditGender = findViewById(R.id.profile_ll_edit_gender);
        llEditGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });

        final LinearLayout llEditHeight = findViewById(R.id.profile_ll_edit_height);
        llEditHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });

        final LinearLayout llEditWight = findViewById(R.id.profile_ll_edit_weight);
        llEditWight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });

        final LinearLayout llEditBirth = findViewById(R.id.profile_ll_edit_birth);
        llEditBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });
    }

    public void getUsername_fromDatabse(){
        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //从数据库获取当前用户的username
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){

                            for(DataSnapshot d: snapshot.getChildren()){
                                //d.getKey()是userInfo[层级的key]

                                String userInfo_Key = d.getKey();
                                if(userInfo_Key.equals("username")) {
                                    username = d.getValue().toString();
                                    textView_name.setHint(username);
                                }
                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"displayUsername ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void getUserInfo_fromDatabase(){
        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //从数据库获取当前用户的 weight height
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

                                    databaseReference.child("Users").child(uid)
                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                String d_Key = d.getKey();
                                                if(d_Key.equals("gender")){
                                                    gender = d.getValue().toString();
                                                    textView_gender.setHint(gender);
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+weight,Toast.LENGTH_SHORT).show();

                                                }
                                                if(d_Key.equals("height")){
                                                    height = d.getValue().toString();
                                                    textView_height.setHint(height+" cm");
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+weight,Toast.LENGTH_SHORT).show();

                                                }
                                                if(d_Key.equals("weight")){
                                                    weight = d.getValue().toString();
                                                    textView_weight.setHint(weight+" kg");
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+weight,Toast.LENGTH_SHORT).show();

                                                }

                                                if(d_Key.equals("age")){
                                                    age = d.getValue().toString();
                                                    textView_age.setHint(age);
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+height,Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"displayUserInfo ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }



}
