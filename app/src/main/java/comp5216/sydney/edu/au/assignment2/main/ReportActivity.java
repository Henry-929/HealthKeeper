package comp5216.sydney.edu.au.assignment2.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.User;

public class ReportActivity extends AppCompatActivity {
    public static String uid;
    public TextView textView_bmi,textView_weight;
    //用于获取数据库存储的体重信息和bmi值
    public String weight,bmi;

    public DatabaseReference databaseReference;

    LinearLayout toMain;
    Button switchToCalorieView;
    Button switchToNutrientView;
    LinearLayout CalorieView;
    LinearLayout NutrientView;
    ImageView LabelCalorie;
    ImageView LabelNutrient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        toMain = (LinearLayout)findViewById(R.id.report_ll_quit);
        switchToCalorieView = (Button)findViewById(R.id.btn_report_calorie);
        switchToNutrientView =  (Button)findViewById(R.id.btn_report_nutrient);
        CalorieView = (LinearLayout)findViewById(R.id.ll_report_calorie);
        NutrientView = (LinearLayout)findViewById(R.id.ll_report_nutrient);
        LabelCalorie = (ImageView)findViewById(R.id.label_report_calorie);
        LabelNutrient = (ImageView)findViewById(R.id.label_report_nutrient);
        textView_weight = (TextView) findViewById(R.id.report_display_current_weight);
        textView_bmi = (TextView) findViewById(R.id.report_display_current_BMI);
        initCalorie();

        //获取BMI和weight from database
        get_Weight_BMI_fromDatabase();

        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                if (intent != null) {
                    ReportActivity.this.startActivity(intent);
                }
            }
        });

        switchToCalorieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NutrientView.setVisibility(View.GONE);
                LabelNutrient.setVisibility(View.INVISIBLE);
                LabelCalorie.setVisibility(View.VISIBLE);
                CalorieView.setVisibility(View.VISIBLE);
            }
        });

        switchToNutrientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalorieView.setVisibility(View.GONE);
                LabelCalorie.setVisibility(View.INVISIBLE);
                LabelNutrient.setVisibility(View.VISIBLE);
                NutrientView.setVisibility(View.VISIBLE);
                initNutrient();
            }
        });

    }

    public void get_Weight_BMI_fromDatabase(){
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
                            Toast.makeText(ReportActivity.this,"displayHeight_BMI ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void initCalorie(){
        //todo set My Weight

        //todo set Calorie (Today)
        //下面的是例子，percent就是占比，status就是吃多吃少
        //status: true means user eat too much
        //        false means user eat too less
        setBreakfast(30,true);
        setLunch(20,false);
        setDinner(40,true);
        setOther(10,true);
        //todo set Food intake (Today)
        setFoodIntake(5,true);
    }

    public void initNutrient(){
        //todo set Nutrient
        //这也是例子，具体的值你们传过来直接输进去就ok，然后我只暂时做了2个set，因为不知道后续有多少量
        setNutrientCal(6000,14000,true);
        setNutrientProtein(3000,5000,false);
    }



    public void setBreakfast(int percent, boolean status){
        TextView percentBreakfast = (TextView)findViewById(R.id.report_display_percent_breakfast);
        TextView statusBreakfast = (TextView)findViewById(R.id.report_display_status_breakfast);
        percentBreakfast.setText("("+percent+"%)");
        if(status){
            statusBreakfast.setText("+");
        }else{
            statusBreakfast.setText("-");
        }
    }
    public void setLunch(int percent, boolean status){
        TextView percentLunch = (TextView)findViewById(R.id.report_display_percent_lunch);
        TextView statusLunch = (TextView)findViewById(R.id.report_display_status_lunch);
        percentLunch.setText("("+percent+"%)");
        if(status){
            statusLunch.setText("+");
        }else{
            statusLunch.setText("-");
        }
    }
    public void setDinner(int percent, boolean status){
        TextView percentDinner = (TextView)findViewById(R.id.report_display_percent_dinner);
        TextView statusDinner = (TextView)findViewById(R.id.report_display_status_dinner);
        percentDinner.setText("("+percent+"%)");
        if(status){
            statusDinner.setText("+");
        }else{
            statusDinner.setText("-");
        }
    }
    public void setOther(int percent, boolean status){
        TextView percentOther = (TextView)findViewById(R.id.report_display_percent_other);
        TextView statusOther = (TextView)findViewById(R.id.report_display_status_other);
        percentOther.setText("("+percent+"%)");
        if(status){
            statusOther.setText("+");
        }else{
            statusOther.setText("-");
        }
    }

    public void setFoodIntake(int number,boolean status){
        TextView mealNumber = (TextView)findViewById(R.id.report_display_meal_number);
        TextView mealStatus = (TextView)findViewById(R.id.report_display_meal_status);
        mealNumber.setText(number+" types");
        if(status){
            mealStatus.setText("+");
        }else{
            mealStatus.setText("-");
        }
    }

    public void setNutrientCal(int total,int goal,boolean status){
        TextView calorieTotal = (TextView)findViewById(R.id.report_display_nutrient_calorie_total);
        TextView calorieGoal = (TextView)findViewById(R.id.report_display_nutrient_calorie_goal);
        TextView calorieStatus = (TextView)findViewById(R.id.report_display_nutrient_calorie_condition);
        calorieTotal.setText(""+total);
        calorieGoal.setText(""+goal);
        if(status){
            calorieStatus.setText("+");
            System.out.println("1");
        }else{
            calorieStatus.setText("-");
        }
    }

    public void setNutrientProtein(int total,int goal,boolean status){
        TextView proteinTotal = (TextView)findViewById(R.id.report_display_nutrient_protein_total);
        TextView proteinGoal = (TextView)findViewById(R.id.report_display_nutrient_calorie_goal);
        TextView proteinStatus = (TextView)findViewById(R.id.report_display_nutrient_calorie_condition);
        proteinTotal.setText(""+total);
        proteinGoal.setText(""+goal);
        if(status){
            proteinStatus.setText("+");
        }else{
            proteinStatus.setText("-");
        }
    }
}
