package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.ImageConfirmActivity;
import comp5216.sydney.edu.au.assignment2.login.User;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;


public class MyProfileActivity extends AppCompatActivity {

    public static String uid;

    public EditText textView_name,textView_gender,textView_height,textView_weight,textView_age;
    public String username,gender,height,weight,age,bmi;//用于获取数据库存储的身高体重信息
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
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
                builder.setTitle(R.string.dialog_cancel_title)
                        .setMessage(R.string.dialog_cancel_msg)
                        .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Prepare data intent for sending it back
                                Intent data = new Intent();

                                // Activity finished ok, return the data
                                setResult(RESULT_CANCELED, data);
                                // set result code and bundle data for response
                                // closes the activity, pass data to parent
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
            }

        });

        confirmEdit = (Button)findViewById(R.id.profile_btn_edit_confirm);
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //【第一步
                userInfoChange();

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

    public void userInfoChange(){
        username = textView_name.getText().toString();
        gender = textView_gender.getText().toString();
        height = textView_height.getText().toString();
        weight = textView_weight.getText().toString();
        age = textView_age.getText().toString();

        if(username.isEmpty()){
            textView_name.setError("User name is required");
            textView_name.requestFocus();
//            Toast.makeText(MyProfileActivity.this,"Username",Toast.LENGTH_LONG).show();
            return;
        }
        if(gender.isEmpty()){
            textView_gender.setError("Gender is required");
            textView_gender.requestFocus();
//            Toast.makeText(MyProfileActivity.this,"Gender",Toast.LENGTH_LONG).show();
            return;
        }
        if(height.isEmpty()){
            textView_height.setError("Height is required");
            textView_height.requestFocus();
//            Toast.makeText(MyProfileActivity.this,"Height",Toast.LENGTH_LONG).show();
            return;
        }
        if(age.isEmpty()){
            textView_age.setError("Age is required");
            textView_age.requestFocus();
//            Toast.makeText(MyProfileActivity.this,"Age is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }
        //if 用户输入不为空
        if(weight.isEmpty()){
            textView_weight.setError("Weight is required");
            textView_weight.requestFocus();
            return;
        }

        //【第二步】
        usernameChange_toDatabase();

//        userAgeChange_toDatabase();
//        userGenderChange_toDatabase();
//        userHeightChange_toDatabase();
//        userWeightChange_toDatabase();
//        userBmiChange_toDatabase();

        userInfoChange_toDatabase();

        Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
        MyProfileActivity.this.startActivity(intent);

    }

    public void usernameChange_toDatabase(){
        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){

                            for(DataSnapshot d: snapshot.getChildren()) {
                                //d.getKey()是userInfo的key

                                final String userInfo_Key = d.getKey();
                                if (userInfo_Key.equals("username")) {
                                    //将 username 写入数据库
                                    databaseReference.child("Users").child(uid)
                                            .child("username").setValue(username);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void userAgeChange_toDatabase(){
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

                                final String userInfo_Key = d.getKey();
                                //if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")&& !userInfo_Key.equals("notFirstTime")) {
                                for(DataSnapshot dd:d.getChildren()){

                                    if(dd.getKey().equals(gender)){
                                        //将 gender 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("gender")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("gender").setValue(gender);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                    }
                                    if(dd.getKey().equals(height)){
                                        //将 height 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("height")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("height").setValue(height);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals(weight)){
                                        //将 weight 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("weight")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("weight").setValue(weight);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("age")){
                                        //将 age 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("age")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("age").setValue(age);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                    if(dd.getKey().equals("bmi")){
                                        //将 bmi写入数据库
                                        //先获取 height- 再计算bmi- 再将bmi写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                    String d_Key = d.getKey();
                                                    if (d_Key.equals("height")) {
                                                        height = d.getValue().toString();

                                                        //计算bmi
                                                        //将String转Double,并保留2位小数：
                                                        DecimalFormat df = new DecimalFormat("0.00");

                                                        //将weight height的String转换为double
                                                        double d_weight = Double.parseDouble(weight);
                                                        weight = df.format(d_weight);

                                                        double d_height = Double.parseDouble(height);
                                                        d_height = d_height/100;

                                                        //计算BMI
                                                        //BMI = (kg) / (m)x(m)
                                                        double d_bmi = d_weight / (d_height*d_height);
                                                        //bmi = Float.toString(f_bmi);
                                                        //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                                        bmi =df.format(d_bmi); //format 返回的是字符串

                                                        //将bmi写入数据库
                                                        databaseReference.child("Users").child(uid)
                                                                .child(userInfo_Key).child("bmi").setValue(bmi);


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }






//                                    //将 gender 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("gender").setValue(gender);
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("height").setValue(height);
//
//                                    //将 weight 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("weight").setValue(weight);
//
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("age").setValue(age);
//
//
//
//                                    //将 bmi写入数据库
//                                    //先获取 height- 再计算bmi- 再将bmi写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
//                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                                                String d_Key = d.getKey();
//                                                if (d_Key.equals("height")) {
//                                                    height = d.getValue().toString();
//
//                                                    //计算bmi
//                                                    //将String转Double,并保留2位小数：
//                                                    DecimalFormat df = new DecimalFormat("0.00");
//
//                                                    //将weight height的String转换为double
//                                                    double d_weight = Double.parseDouble(weight);
//                                                    weight = df.format(d_weight);
//
//                                                    double d_height = Double.parseDouble(height);
//                                                    d_height = d_height/100;
//
//                                                    //计算BMI
//                                                    //BMI = (kg) / (m)x(m)
//                                                    double d_bmi = d_weight / (d_height*d_height);
//                                                    //bmi = Float.toString(f_bmi);
//                                                    //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//                                                    bmi =df.format(d_bmi); //format 返回的是字符串
//
//                                                    //将bmi写入数据库
//                                                    databaseReference.child("Users").child(uid)
//                                                            .child(userInfo_Key).child("bmi").setValue(bmi);
//
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }

                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void userGenderChange_toDatabase(){
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

                                final String userInfo_Key = d.getKey();
                                //if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")&& !userInfo_Key.equals("notFirstTime")) {
                                for(DataSnapshot dd:d.getChildren()){

                                    if(dd.getKey().equals(gender)){
                                        //将 gender 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("gender")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("gender").setValue(gender);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                    }
                                    if(dd.getKey().equals(height)){
                                        //将 height 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("height")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("height").setValue(height);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals(weight)){
                                        //将 weight 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("weight")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("weight").setValue(weight);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("age")){
                                        //将 age 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("age")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("age").setValue(age);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                    if(dd.getKey().equals("bmi")){
                                        //将 bmi写入数据库
                                        //先获取 height- 再计算bmi- 再将bmi写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                    String d_Key = d.getKey();
                                                    if (d_Key.equals("height")) {
                                                        height = d.getValue().toString();

                                                        //计算bmi
                                                        //将String转Double,并保留2位小数：
                                                        DecimalFormat df = new DecimalFormat("0.00");

                                                        //将weight height的String转换为double
                                                        double d_weight = Double.parseDouble(weight);
                                                        weight = df.format(d_weight);

                                                        double d_height = Double.parseDouble(height);
                                                        d_height = d_height/100;

                                                        //计算BMI
                                                        //BMI = (kg) / (m)x(m)
                                                        double d_bmi = d_weight / (d_height*d_height);
                                                        //bmi = Float.toString(f_bmi);
                                                        //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                                        bmi =df.format(d_bmi); //format 返回的是字符串

                                                        //将bmi写入数据库
                                                        databaseReference.child("Users").child(uid)
                                                                .child(userInfo_Key).child("bmi").setValue(bmi);


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }






//                                    //将 gender 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("gender").setValue(gender);
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("height").setValue(height);
//
//                                    //将 weight 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("weight").setValue(weight);
//
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("age").setValue(age);
//
//
//
//                                    //将 bmi写入数据库
//                                    //先获取 height- 再计算bmi- 再将bmi写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
//                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                                                String d_Key = d.getKey();
//                                                if (d_Key.equals("height")) {
//                                                    height = d.getValue().toString();
//
//                                                    //计算bmi
//                                                    //将String转Double,并保留2位小数：
//                                                    DecimalFormat df = new DecimalFormat("0.00");
//
//                                                    //将weight height的String转换为double
//                                                    double d_weight = Double.parseDouble(weight);
//                                                    weight = df.format(d_weight);
//
//                                                    double d_height = Double.parseDouble(height);
//                                                    d_height = d_height/100;
//
//                                                    //计算BMI
//                                                    //BMI = (kg) / (m)x(m)
//                                                    double d_bmi = d_weight / (d_height*d_height);
//                                                    //bmi = Float.toString(f_bmi);
//                                                    //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//                                                    bmi =df.format(d_bmi); //format 返回的是字符串
//
//                                                    //将bmi写入数据库
//                                                    databaseReference.child("Users").child(uid)
//                                                            .child(userInfo_Key).child("bmi").setValue(bmi);
//
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }

                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void userHeightChange_toDatabase(){
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

                                final String userInfo_Key = d.getKey();
                                //if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")&& !userInfo_Key.equals("notFirstTime")) {
                                for(DataSnapshot dd:d.getChildren()){

                                    if(dd.getKey().equals(gender)){
                                        //将 gender 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("gender")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("gender").setValue(gender);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                    }
                                    if(dd.getKey().equals(height)){
                                        //将 height 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("height")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("height").setValue(height);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals(weight)){
                                        //将 weight 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("weight")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("weight").setValue(weight);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("age")){
                                        //将 age 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("age")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("age").setValue(age);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                    if(dd.getKey().equals("bmi")){
                                        //将 bmi写入数据库
                                        //先获取 height- 再计算bmi- 再将bmi写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                    String d_Key = d.getKey();
                                                    if (d_Key.equals("height")) {
                                                        height = d.getValue().toString();

                                                        //计算bmi
                                                        //将String转Double,并保留2位小数：
                                                        DecimalFormat df = new DecimalFormat("0.00");

                                                        //将weight height的String转换为double
                                                        double d_weight = Double.parseDouble(weight);
                                                        weight = df.format(d_weight);

                                                        double d_height = Double.parseDouble(height);
                                                        d_height = d_height/100;

                                                        //计算BMI
                                                        //BMI = (kg) / (m)x(m)
                                                        double d_bmi = d_weight / (d_height*d_height);
                                                        //bmi = Float.toString(f_bmi);
                                                        //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                                        bmi =df.format(d_bmi); //format 返回的是字符串

                                                        //将bmi写入数据库
                                                        databaseReference.child("Users").child(uid)
                                                                .child(userInfo_Key).child("bmi").setValue(bmi);


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }






//                                    //将 gender 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("gender").setValue(gender);
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("height").setValue(height);
//
//                                    //将 weight 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("weight").setValue(weight);
//
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("age").setValue(age);
//
//
//
//                                    //将 bmi写入数据库
//                                    //先获取 height- 再计算bmi- 再将bmi写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
//                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                                                String d_Key = d.getKey();
//                                                if (d_Key.equals("height")) {
//                                                    height = d.getValue().toString();
//
//                                                    //计算bmi
//                                                    //将String转Double,并保留2位小数：
//                                                    DecimalFormat df = new DecimalFormat("0.00");
//
//                                                    //将weight height的String转换为double
//                                                    double d_weight = Double.parseDouble(weight);
//                                                    weight = df.format(d_weight);
//
//                                                    double d_height = Double.parseDouble(height);
//                                                    d_height = d_height/100;
//
//                                                    //计算BMI
//                                                    //BMI = (kg) / (m)x(m)
//                                                    double d_bmi = d_weight / (d_height*d_height);
//                                                    //bmi = Float.toString(f_bmi);
//                                                    //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//                                                    bmi =df.format(d_bmi); //format 返回的是字符串
//
//                                                    //将bmi写入数据库
//                                                    databaseReference.child("Users").child(uid)
//                                                            .child(userInfo_Key).child("bmi").setValue(bmi);
//
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }

                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void userWeightChange_toDatabase(){
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

                                final String userInfo_Key = d.getKey();
                                //if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")&& !userInfo_Key.equals("notFirstTime")) {
                                for(DataSnapshot dd:d.getChildren()){

                                    if(dd.getKey().equals(gender)){
                                        //将 gender 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("gender")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("gender").setValue(gender);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                    }
                                    if(dd.getKey().equals(height)){
                                        //将 height 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("height")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("height").setValue(height);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals(weight)){
                                        //将 weight 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("weight")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("weight").setValue(weight);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("age")){
                                        //将 age 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("age")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("age").setValue(age);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                    if(dd.getKey().equals("bmi")){
                                        //将 bmi写入数据库
                                        //先获取 height- 再计算bmi- 再将bmi写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                    String d_Key = d.getKey();
                                                    if (d_Key.equals("height")) {
                                                        height = d.getValue().toString();

                                                        //计算bmi
                                                        //将String转Double,并保留2位小数：
                                                        DecimalFormat df = new DecimalFormat("0.00");

                                                        //将weight height的String转换为double
                                                        double d_weight = Double.parseDouble(weight);
                                                        weight = df.format(d_weight);

                                                        double d_height = Double.parseDouble(height);
                                                        d_height = d_height/100;

                                                        //计算BMI
                                                        //BMI = (kg) / (m)x(m)
                                                        double d_bmi = d_weight / (d_height*d_height);
                                                        //bmi = Float.toString(f_bmi);
                                                        //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                                        bmi =df.format(d_bmi); //format 返回的是字符串

                                                        //将bmi写入数据库
                                                        databaseReference.child("Users").child(uid)
                                                                .child(userInfo_Key).child("bmi").setValue(bmi);


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }






//                                    //将 gender 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("gender").setValue(gender);
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("height").setValue(height);
//
//                                    //将 weight 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("weight").setValue(weight);
//
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("age").setValue(age);
//
//
//
//                                    //将 bmi写入数据库
//                                    //先获取 height- 再计算bmi- 再将bmi写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
//                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                                                String d_Key = d.getKey();
//                                                if (d_Key.equals("height")) {
//                                                    height = d.getValue().toString();
//
//                                                    //计算bmi
//                                                    //将String转Double,并保留2位小数：
//                                                    DecimalFormat df = new DecimalFormat("0.00");
//
//                                                    //将weight height的String转换为double
//                                                    double d_weight = Double.parseDouble(weight);
//                                                    weight = df.format(d_weight);
//
//                                                    double d_height = Double.parseDouble(height);
//                                                    d_height = d_height/100;
//
//                                                    //计算BMI
//                                                    //BMI = (kg) / (m)x(m)
//                                                    double d_bmi = d_weight / (d_height*d_height);
//                                                    //bmi = Float.toString(f_bmi);
//                                                    //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//                                                    bmi =df.format(d_bmi); //format 返回的是字符串
//
//                                                    //将bmi写入数据库
//                                                    databaseReference.child("Users").child(uid)
//                                                            .child(userInfo_Key).child("bmi").setValue(bmi);
//
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }

                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    public void userBmiChange_toDatabase(){
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

                                final String userInfo_Key = d.getKey();
                                //if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")&& !userInfo_Key.equals("notFirstTime")) {
                                for(DataSnapshot dd:d.getChildren()){

                                    if(dd.getKey().equals(gender)){
                                        //将 gender 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("gender")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("gender").setValue(gender);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                    }
                                    if(dd.getKey().equals(height)){
                                        //将 height 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("height")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("height").setValue(height);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals(weight)){
                                        //将 weight 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("weight")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("weight").setValue(weight);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("age")){
                                        //将 age 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("age")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("age").setValue(age);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                    if(dd.getKey().equals("bmi")){
                                        //将 bmi写入数据库
                                        //先获取 height- 再计算bmi- 再将bmi写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                    String d_Key = d.getKey();
                                                    if (d_Key.equals("height")) {
                                                        height = d.getValue().toString();

                                                        //计算bmi
                                                        //将String转Double,并保留2位小数：
                                                        DecimalFormat df = new DecimalFormat("0.00");

                                                        //将weight height的String转换为double
                                                        double d_weight = Double.parseDouble(weight);
                                                        weight = df.format(d_weight);

                                                        double d_height = Double.parseDouble(height);
                                                        d_height = d_height/100;

                                                        //计算BMI
                                                        //BMI = (kg) / (m)x(m)
                                                        double d_bmi = d_weight / (d_height*d_height);
                                                        //bmi = Float.toString(f_bmi);
                                                        //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                                        bmi =df.format(d_bmi); //format 返回的是字符串

                                                        //将bmi写入数据库
                                                        databaseReference.child("Users").child(uid)
                                                                .child(userInfo_Key).child("bmi").setValue(bmi);


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }






//                                    //将 gender 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("gender").setValue(gender);
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("height").setValue(height);
//
//                                    //将 weight 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("weight").setValue(weight);
//
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("age").setValue(age);
//
//
//
//                                    //将 bmi写入数据库
//                                    //先获取 height- 再计算bmi- 再将bmi写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
//                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                                                String d_Key = d.getKey();
//                                                if (d_Key.equals("height")) {
//                                                    height = d.getValue().toString();
//
//                                                    //计算bmi
//                                                    //将String转Double,并保留2位小数：
//                                                    DecimalFormat df = new DecimalFormat("0.00");
//
//                                                    //将weight height的String转换为double
//                                                    double d_weight = Double.parseDouble(weight);
//                                                    weight = df.format(d_weight);
//
//                                                    double d_height = Double.parseDouble(height);
//                                                    d_height = d_height/100;
//
//                                                    //计算BMI
//                                                    //BMI = (kg) / (m)x(m)
//                                                    double d_bmi = d_weight / (d_height*d_height);
//                                                    //bmi = Float.toString(f_bmi);
//                                                    //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//                                                    bmi =df.format(d_bmi); //format 返回的是字符串
//
//                                                    //将bmi写入数据库
//                                                    databaseReference.child("Users").child(uid)
//                                                            .child(userInfo_Key).child("bmi").setValue(bmi);
//
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }

                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    public void userInfoChange_toDatabase(){
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

                                final String userInfo_Key = d.getKey();
                                //if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")&& !userInfo_Key.equals("notFirstTime")) {
                                for(DataSnapshot dd:d.getChildren()){

                                    if(dd.getKey().equals("gender")){
                                        //将 gender 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("gender")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("gender").setValue(gender);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });


                                    }
                                    if(dd.getKey().equals("height")){
                                        //将 height 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("height")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("height").setValue(height);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("weight")){
                                        //将 weight 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("weight")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("weight").setValue(weight);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });

                                    }
                                    if(dd.getKey().equals("age")){
                                        //将 age 写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for(DataSnapshot ss:snapshot.getChildren()){
                                                            String ss_key = ss.getKey();
                                                            if(ss_key.equals("age")){
                                                                //将 height 写入数据库
                                                                databaseReference.child("Users").child(uid)
                                                                        .child(userInfo_Key).child("age").setValue(age);

                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                    }
                                                });
                                    }
                                    if(dd.getKey().equals("bmi")){
                                        //将 bmi写入数据库
                                        //先获取 height- 再计算bmi- 再将bmi写入数据库
                                        databaseReference.child("Users").child(uid)
                                                .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot d: dataSnapshot.getChildren()) {
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                                                    String d_Key = d.getKey();
                                                    if (d_Key.equals("height")) {
                                                        String str_height = d.getValue().toString();

                                                        //计算bmi
                                                        //将String转Double,并保留2位小数：
                                                        DecimalFormat df = new DecimalFormat("0.00");

                                                        //将weight height的String转换为double
                                                        double d_weight = Double.parseDouble(weight);
                                                        weight = df.format(d_weight);

                                                        double d_height = Double.parseDouble(str_height);
                                                        d_height = d_height/100;

                                                        //计算BMI
                                                        //BMI = (kg) / (m)x(m)
                                                        double d_bmi = d_weight / (d_height*d_height);
                                                        //bmi = Float.toString(f_bmi);
                                                        //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
                                                        bmi =df.format(d_bmi); //format 返回的是字符串

                                                        //将bmi写入数据库
                                                        databaseReference.child("Users").child(uid)
                                                                .child(userInfo_Key).child("bmi").setValue(bmi);


                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }






//                                    //将 gender 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("gender").setValue(gender);
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("height").setValue(height);
//
//                                    //将 weight 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("weight").setValue(weight);
//
//
//                                    //将 birthday 写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).child("age").setValue(age);
//
//
//
//                                    //将 bmi写入数据库
//                                    //先获取 height- 再计算bmi- 再将bmi写入数据库
//                                    databaseReference.child("Users").child(uid)
//                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            for(DataSnapshot d: dataSnapshot.getChildren()) {
//                                                //Toast.makeText(MainActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                                                String d_Key = d.getKey();
//                                                if (d_Key.equals("height")) {
//                                                    height = d.getValue().toString();
//
//                                                    //计算bmi
//                                                    //将String转Double,并保留2位小数：
//                                                    DecimalFormat df = new DecimalFormat("0.00");
//
//                                                    //将weight height的String转换为double
//                                                    double d_weight = Double.parseDouble(weight);
//                                                    weight = df.format(d_weight);
//
//                                                    double d_height = Double.parseDouble(height);
//                                                    d_height = d_height/100;
//
//                                                    //计算BMI
//                                                    //BMI = (kg) / (m)x(m)
//                                                    double d_bmi = d_weight / (d_height*d_height);
//                                                    //bmi = Float.toString(f_bmi);
//                                                    //DecimalFormat decimalFormat= new  DecimalFormat( ".00" ); //构造方法的字符格式这里如果小数不足2位,会以0补足.
//                                                    bmi =df.format(d_bmi); //format 返回的是字符串
//
//                                                    //将bmi写入数据库
//                                                    databaseReference.child("Users").child(uid)
//                                                            .child(userInfo_Key).child("bmi").setValue(bmi);
//
//
//                                                }
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                        }
//                                    });
                                }

                            }
                        }else{
                            Toast.makeText(MyProfileActivity.this,"ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
