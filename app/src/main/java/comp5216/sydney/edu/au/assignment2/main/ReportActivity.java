package comp5216.sydney.edu.au.assignment2.main;

import android.app.Activity;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.CustomFood;
import comp5216.sydney.edu.au.assignment2.addMeal.FoodDisplayActivity;
import comp5216.sydney.edu.au.assignment2.addMeal.ManuallyInputActivity;
import comp5216.sydney.edu.au.assignment2.addMeal.UsersFood;
import comp5216.sydney.edu.au.assignment2.login.User;
import comp5216.sydney.edu.au.assignment2.loginFirstTimeUserInfo.InfoActivity_2;

public class ReportActivity extends AppCompatActivity {
    public static String uid;
    public TextView textView_bmi,textView_weight;
    //用于获取数据库存储的体重信息和bmi值
    public String weight,bmi;
    public String foodname,quantity,category;
    public String calorie,carbohydrate,fat,protein;

//    public double breakfastCalorie = 0;
//    public double allmealCalorie= 0;

    public ArrayList<al_UsersFood> allFoodArrayList = new ArrayList<>();

//    public double d_quantity,d_calorie,d_carbohydrate,d_fat,d_protein;


    DatabaseReference databaseReference;

    LinearLayout toMain;
    Button switchToCalorieView;
    Button switchToNutrientView;
    LinearLayout CalorieView;
    LinearLayout NutrientView;
    ImageView LabelCalorie;
    ImageView LabelNutrient;
    TextView percentBreakfast,statusBreakfast;

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

        percentBreakfast = (TextView)findViewById(R.id.report_display_percent_breakfast);
        statusBreakfast = (TextView)findViewById(R.id.report_display_status_breakfast);


        //获取BMI和weight from database
        get_Weight_BMI_fromDatabase();

        //获取Calorie（Today）
        initCalorie();

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
        //todo set Calorie (Today)
        //下面的是例子，percent就是占比，status就是吃多吃少
        //status: true means user eat too much
        //        false means user eat too less
        //setBreakfast();
        calBreakfast();
        setLunch(20,false);
        setDinner(40,true);
        setOther(10,true);
        //todo set Food intake (Today)
        setFoodIntake(5,true);
    }

    public void calBreakfast() {

        //step：
        //【UsersDB】下 所有的 breakfast 占 所有的meal 的比例
        //1- 将一个user的所有食物信息 加入 allFoodArrayList中
        //2- 遍历allFoodArrayList中，计算比例


        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //1- 将一个user的所有食物信息 加入 allFoodArrayList中
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot d : snapshot.getChildren()) {
                            //d.getKey()是userInfo的key
                            final String userInfo_Key = d.getKey();

                            final al_UsersFood al_usersFood = new al_UsersFood();
                            for (DataSnapshot dd : d.getChildren()) {
                                String dd_Key = dd.getKey();
                                String dd_Value = dd.getValue().toString();

                                //所有的meal
                                if (dd_Key.equals("foodname")) {
                                    foodname = dd_Value;

                                    al_usersFood.setFoodname(foodname);
                                    //Toast.makeText(ReportActivity.this, al_usersFood.getFoodname()+"!name!"+foodname, Toast.LENGTH_LONG).show();


                                    //【Food DB】中获取 calorie,carbohydrate,fat,protein信息
                                    databaseReference.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            for (DataSnapshot food_d : snapshot.getChildren()) {

                                                for (DataSnapshot food_dd : food_d.getChildren()) {

                                                    String food_dd_Key = food_dd.getKey();
                                                    String food_dd_Value = food_dd.getValue().toString();

                                                    if (food_dd_Key.equals("calorie")) {
                                                        calorie = food_dd_Value;
                                                    }
                                                    if (food_dd_Key.equals("carbs")) {
                                                        carbohydrate = food_dd_Value;
                                                    }
                                                    if (food_dd_Key.equals("fat")) {
                                                        fat = food_dd_Value;
                                                    }
                                                    if (food_dd_Key.equals("protein")) {
                                                        protein = food_dd_Value;
                                                    }

                                                    //【Food】中的foodname == 【Users】中的foodname
                                                    //【Food DB】中对比foodname信息


                                                    databaseReference.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            //breakfindFood:
                                                            for (DataSnapshot food_d2 : snapshot.getChildren()) {

                                                                for (DataSnapshot food_dd2 : food_d2.getChildren()) {

                                                                    String food_dd2_Key = food_dd2.getKey();
                                                                    String food_dd2_Value = food_dd2.getValue().toString();

                                                                    if (food_dd2_Value.equals(al_usersFood.getFoodname())) {
                                                                        Toast.makeText(ReportActivity.this, al_usersFood.getFoodname()+"al_usersFood.getFoodname()", Toast.LENGTH_LONG).show();


                                                                        //break breakfindFood;
                                                                        al_usersFood.setCalorie(calorie);
                                                                        al_usersFood.setCarbohydrate(carbohydrate);
                                                                        al_usersFood.setFat(fat);
                                                                        al_usersFood.setProtein(protein);

                                                                        Toast.makeText(ReportActivity.this, calorie+"!CALORIE!"+carbohydrate+"!fat"+fat+"!P"+protein, Toast.LENGTH_LONG).show();

                                                                        Toast.makeText(ReportActivity.this, al_usersFood.getCalorie()+"!carb!"+al_usersFood.getCarbs()+"!fat"+al_usersFood.getFat()+"!P"+al_usersFood.getProtein(), Toast.LENGTH_LONG).show();


                                                                    }
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });





                                                }//=====end of一个食物的信息循环======


                                            }//==========end of 所有食物的信息 循环=====


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                                if (dd_Key.equals("quantity")) {
                                    quantity = dd.getValue().toString();
                                    al_usersFood.setQuantity(quantity);
                                    //Toast.makeText(ReportActivity.this, al_usersFood.getQuantity()+"!quan!"+quantity, Toast.LENGTH_LONG).show();

                                }
                                if (dd_Key.equals("category")) {
                                    category = dd.getValue().toString();
                                    al_usersFood.setCategory(category);
                                    //Toast.makeText(ReportActivity.this, al_usersFood.getCategory()+"!category!"+category, Toast.LENGTH_LONG).show();

                                }

                            }
                            // 一个foodname循环结束
                            //开始将 信息 加入 allFoodArrayList中
                            if (al_usersFood.getFoodname() != null) {
                                allFoodArrayList.add(al_usersFood);
                            }
                            //Toast.makeText(ReportActivity.this, allFoodArrayList.size() + "!size", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(ReportActivity.this, allFoodArrayList.size() + "!嗷size嗷", Toast.LENGTH_LONG).show();


                        //2- 遍历allFoodArrayList中，计算比例
//                        double breakfastCalorieCount = 0;
//                        double allmealCalorieCount = 0;
//                        for (al_UsersFood item : allFoodArrayList) {
//
//                            double d_quantity = Double.parseDouble(item.getQuantity());
//
//                            double d_all_calorie = Double.parseDouble(item.getCalorie());
//                            Toast.makeText(ReportActivity.this, d_all_calorie + "!d_all_calorie", Toast.LENGTH_LONG).show();

//                            d_all_calorie = d_all_calorie * d_quantity;
//                            allmealCalorieCount = allmealCalorieCount + d_all_calorie;
//                            Toast.makeText(ReportActivity.this, allmealCalorieCount + "!allmealCalorieCount", Toast.LENGTH_LONG).show();
//
//                            if (item.getCategory().equals("Breakfast")) {
//                                double d_breakfast_calorie = d_all_calorie;
//                                breakfastCalorieCount = breakfastCalorieCount + d_breakfast_calorie;
//                                Toast.makeText(ReportActivity.this, breakfastCalorieCount + "!breakfast", Toast.LENGTH_LONG).show();
//
//                            }

//                        }
                        //计算 比例
                        //并保留1位小数：
//                        DecimalFormat df = new DecimalFormat("0.0");
//                        double d_breakfastPercent = breakfastCalorieCount / allmealCalorieCount;
//                        d_breakfastPercent = d_breakfastPercent * 100;
//                        String breakfastPercent = df.format(d_breakfastPercent);//format 返回的是字符串
//
//                        percentBreakfast.setText("(" + breakfastPercent + "%)");
//                        boolean status = false;
//                        if (d_breakfastPercent < 27.7) {
//                            status = true;
//                        }
//                        if (status) {
//                            statusBreakfast.setText("+");
//                        } else {
//                            statusBreakfast.setText("-");
//                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


    }

//    public double []convertTodouble(String quantity,String calorie,String carbohydrate,String fat,String protein){
//
//
//        double d_quantity,d_calorie,d_carbohydrate,d_fat,d_protein;
//        d_quantity = Double.parseDouble(quantity);
//
//        d_calorie = Double.parseDouble(calorie);
//        d_carbohydrate = Double.parseDouble(carbohydrate);
//        d_fat = Double.parseDouble(fat);
//        d_protein = Double.parseDouble(protein);
//
//        d_calorie = d_calorie * d_quantity;
//        d_carbohydrate = d_carbohydrate * d_quantity;
//        d_fat = d_fat * d_quantity;
//        d_protein = d_protein * d_quantity;
//
//        return d_calorie;
//
//    }

//    public void setBreakfast(){
//
//
//        double d_breakfastPercent = calBreakfast();
//        boolean status = false;
//        if(d_breakfastPercent < 27.5 )
//            status = true;
//
//        TextView percentBreakfast = (TextView)findViewById(R.id.report_display_percent_breakfast);
//        TextView statusBreakfast = (TextView)findViewById(R.id.report_display_status_breakfast);
//        percentBreakfast.setText("("+d_breakfastPercent+"%)");
//        if(status){
//            statusBreakfast.setText("+");
//        }else{
//            statusBreakfast.setText("-");
//        }
//    }

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
        TextView mealNumber = (TextView)findViewById(R.id.report_display_diversity_number);
        TextView mealStatus = (TextView)findViewById(R.id.report_display_energy_status);
        mealNumber.setText(number+" types");
        if(status){
            mealStatus.setText("+");
        }else{
            mealStatus.setText("-");
        }
    }



    //======================以下是 NUTRIENT View 分割线 ========================

    public void initNutrient(){
        //todo set Nutrient
        //这也是例子，具体的值你们传过来直接输进去就ok，然后我只暂时做了2个set，因为不知道后续有多少量
        setNutrientCal(6000,14000,true);
        setNutrientProtein(3000,5000,false);
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
