package comp5216.sydney.edu.au.assignment2.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.AddMealActivity;
import comp5216.sydney.edu.au.assignment2.login.User;
import comp5216.sydney.edu.au.assignment2.userSetting.UserActivity;

public class MainActivity extends AppCompatActivity {

    public static String uid;

    public TextView textView_bmi,textView_weight,textView_name;
    public String weight,height,bmi,username;//用于获取数据库存储的身高体重信息


    public DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        textView_name=(TextView) findViewById(R.id.main_display_user_name);
        textView_weight = (TextView) findViewById(R.id.main_display_user_weight);
        textView_bmi = (TextView) findViewById(R.id.main_display_user_BMI);

        getUsername_fromDatabse();
        getWeight_BMI_fromDatabase();


        //define button
        final Button mtReportBtn=findViewById(R.id.btn_main_to_report);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        mtReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                if (intent != null) {
                    MainActivity.this.startActivity(intent);
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case R.id.navigation_add:
                        startActivity(new Intent(MainActivity.this, AddMealActivity.class));
                        break;
                    case R.id.navigation_user:
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
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
                            Toast.makeText(MainActivity.this,"displayHeight_BMI ERROR!!!",Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+weight,Toast.LENGTH_SHORT).show();

                                                }

                                                if(d_Key.equals("bmi")){
                                                    bmi = d.getValue().toString();
                                                    textView_bmi.setText(bmi);
                                                    Toast.makeText(MainActivity.this,"嗷嗷"+d.getKey()+"/"+d.getValue().toString()+"/"+height,Toast.LENGTH_SHORT).show();

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