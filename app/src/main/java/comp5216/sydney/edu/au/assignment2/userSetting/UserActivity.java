package comp5216.sydney.edu.au.assignment2.userSetting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.*;
import comp5216.sydney.edu.au.assignment2.login.*;
import comp5216.sydney.edu.au.assignment2.main.*;

public class UserActivity extends AppCompatActivity {

    public static String uid;

    public TextView textView_name;
    public ImageView imageView_userImage;
    public String gender,username;//用于获取数据库存储的username信息


    public DatabaseReference databaseReference;
    public FirebaseStorage storage;
    public StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        textView_name=(TextView) findViewById(R.id.userSetting_user_name);
        getUsername_fromDatabse();


        imageView_userImage = (ImageView) findViewById(R.id.userSetting_user_image);
        getUserImage_fromDatabase();


        //add click listener on LinerLayout
        final LinearLayout llprofile = findViewById(R.id.setting_ll_profile);
        llprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, MyProfileActivity.class);
                if (intent != null) {
                    UserActivity.this.startActivity(intent);
                }
            }
        });

        final LinearLayout llreport = findViewById(R.id.setting_ll_report);
        llreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserActivity.this, ReportActivity.class);
                if (intent != null) {
                    UserActivity.this.startActivity(intent);
                }
            }
        });

        final LinearLayout llLogout = findViewById(R.id.setting_ll_log_out);
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Log.i("LogOutActivity", "Log out ");
                //todo .. 弹窗给用户，提示信息"确定是否要登出？"

                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        // Add the buttons
                        .setPositiveButton(R.string.delete, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                        if (intent != null) {
                                            UserActivity.this.startActivity(intent);
                                        }
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


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(2).getItemId());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(UserActivity.this, MainActivity.class));
                        break;
                    case R.id.navigation_add:
                        //todo
                        break;
                    case R.id.navigation_user:
                        startActivity(new Intent(UserActivity.this, UserActivity.class));
                        break;
                }
                return true;
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
                                    textView_name.setText(username);
                                }
                            }
                        }else{
                            Toast.makeText(UserActivity.this,"display username ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getUserImage_fromDatabase(){


        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //从数据库获取当前用户的gender
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

                                                    StorageReference femaleRef = storageReference.child("UserImage/icon_female.png");
                                                    StorageReference maleRef = storageReference.child("UserImage/icon_male.png");


                                                    final long ONE_MEGABYTE = 1024 * 1024;
                                                    if(gender.equals("Male")){

                                                        maleRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                            @Override
                                                            public void onSuccess(byte[] bytes) {
                                                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                                imageView_userImage.setImageBitmap(bmp);

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(getApplicationContext(), "Loading UserImage Male ERROR!!", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                    }
                                                    else{//Female
                                                        femaleRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                            @Override
                                                            public void onSuccess(byte[] bytes) {
                                                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                                imageView_userImage.setImageBitmap(bmp);

                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception exception) {
                                                                Toast.makeText(getApplicationContext(), "Loading UserImage Female ERROR!!", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                    }

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
                            Toast.makeText(UserActivity.this,"display userImage ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





    }
}