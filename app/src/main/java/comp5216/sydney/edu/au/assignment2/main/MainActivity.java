package comp5216.sydney.edu.au.assignment2.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.*;
import comp5216.sydney.edu.au.assignment2.login.*;
import comp5216.sydney.edu.au.assignment2.news.*;
import comp5216.sydney.edu.au.assignment2.userSetting.*;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static String uid;

    public TextView textView_bmi,textView_weight,textView_name;
    public ImageView imageView_userImage;
    public String weight,gender,bmi,username;//用于获取数据库存储的身高体重信息

    private static final int REQUEST_TAKE_PHOTO = 101;
    MarshmallowPermission marshmallowPermission = new MarshmallowPermission(this);
    public DatabaseReference databaseReference;
    public FirebaseStorage storage;
    public StorageReference storageReference;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        //1.获取新闻数据用list封装
        ArrayList<NewsBean> allNews = NewsUtils.getAllNews(mContext);
        //2.找到控件
        ListView lv_news = (ListView) findViewById(R.id.lv_news);
        //3.创建一个adapter设置给listview
        NewsAdapter newsAdapter = new NewsAdapter(mContext, allNews);
        lv_news.setAdapter(newsAdapter);
        //4.设置listview条目的点击事件
        lv_news.setOnItemClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        textView_name=(TextView) findViewById(R.id.main_display_user_name);
        textView_weight = (TextView) findViewById(R.id.main_display_user_weight);
        textView_bmi = (TextView) findViewById(R.id.main_display_user_BMI);

        getUsername_fromDatabse();
        getWeight_BMI_fromDatabase();

        imageView_userImage = (ImageView) findViewById(R.id.user_image);
        getUserImage_fromDatabase();


        //define button
        final Button mtReportBtn=findViewById(R.id.btn_main_to_report);
        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        mtReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                if (intent != null) {
                    MainActivity.this.startActivity(intent);
                }
            }
        });
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(0).getItemId());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_add:
                        userChoice();
                        break;
                    case R.id.navigation_user:
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        NewsBean bean = (NewsBean) parent.getItemAtPosition(position);

        String url = bean.news_url;

        //跳转浏览器
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }

    public void userChoice(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(getDrawable(R.drawable.icon_alert));
        builder.setTitle("Record Method");
        final String []items=new String[]{"Manual","Photo"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            //which指的是用户选择的条目的下标
            //dialog:触发这个方法的对话框
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    onManualClick();
                    dialog.dismiss(); //当用户选择了一个值后，对话框消失
                }
                if (which == 1){
                    onTakePhotoClick();
                    dialog.dismiss(); //当用户选择了一个值后，对话框消失
                }
            }
        });
        builder.show();
    }

    public void onManualClick(){
        Intent intent = new Intent(MainActivity.this, ManuallyInputActivity.class);
        if (intent != null) {
            startActivity(intent);
        }
    }

    /*
    Take Photo action
     */

    public void onTakePhotoClick(){
        // Check permissions
        if (!marshmallowPermission.checkPermissionForCamera()
                || !marshmallowPermission.checkPermissionForExternalStorage()) {
            marshmallowPermission.requestPermissionForCamera();
        }  else {
            Intent intent = new Intent(MainActivity.this, CameraActivity.class);
            startActivityForResult(intent,REQUEST_TAKE_PHOTO);
        }
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
                            Toast.makeText(MainActivity.this,"displayUsername ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    public void getWeight_BMI_fromDatabase(){
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
                                                if(d_Key.equals("weight")){
                                                    weight = d.getValue().toString();
                                                    textView_weight.setText(weight);
                                                    //Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+weight,Toast.LENGTH_SHORT).show();

                                                }

                                                if(d_Key.equals("bmi")){
                                                    bmi = d.getValue().toString();
                                                    textView_bmi.setText(bmi);
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
                            Toast.makeText(MainActivity.this,"displayHeight_BMI ERROR!!!",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MainActivity.this,"displayHeight_BMI ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}