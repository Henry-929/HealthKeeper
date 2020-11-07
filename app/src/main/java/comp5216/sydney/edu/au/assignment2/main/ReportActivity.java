package comp5216.sydney.edu.au.assignment2.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.CustomFood;
import comp5216.sydney.edu.au.assignment2.addMeal.MyCallBack;
import comp5216.sydney.edu.au.assignment2.addMeal.UsersFood;
import comp5216.sydney.edu.au.assignment2.login.User;

public class ReportActivity extends AppCompatActivity {

    public static String uid;
    public String weight,bmi,height;
    public String foodname,quantity,category;
    public String calorie,carbohydrate,fat,protein;

    public ArrayList<UsersFood> usersFoodArrayList = new ArrayList<>();
    public ArrayList<CustomFood> customFoodArrayList = new ArrayList<>();


    DatabaseReference databaseReference;

    //bmi bar
    ImageView bubble_lean,bubble_normal,bubble_overweight,bubble_obese;
    TextView text_lean,text_normal,text_overweight,text_obese;
    ImageView bar_lean,bar_normal,bar_overweight,bar_obese;

    LinearLayout toMain;
    Button switchToCalorieView;
    Button switchToNutrientView;
    LinearLayout CalorieView;
    LinearLayout NutrientView;
    ImageView LabelCalorie;
    ImageView LabelNutrient;
    TextView percentBreakfast,statusBreakfast,percentLunch,statusLunch,percentDinner,statusDinner,percentOther,statusOther;
    TextView diversityNumber,energyStatus;

    TextView currentWeightTV,predictedWeightTV;

    TextView calorieTotal,calorieGoal,calorieStatus;
    TextView proteinTotal,proteinGoal,proteinStatus;
    TextView carbsTotal,carbsGoal,carbsStatus;
    TextView fatTotal, fatGoal,fatStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        //bmi bar
        bubble_lean = (ImageView) findViewById(R.id.bubble_lean);
        bubble_normal = (ImageView) findViewById(R.id.bubble_normal);
        bubble_overweight = (ImageView) findViewById(R.id.bubble_overweight);
        bubble_obese = (ImageView) findViewById(R.id.bubble_obesity);

        text_lean = (TextView) findViewById(R.id.text_lean);
        text_normal = (TextView) findViewById(R.id.text_normal);
        text_overweight = (TextView) findViewById(R.id.text_overweight);
        text_obese = (TextView) findViewById(R.id.text_obesity);

        bar_lean = (ImageView) findViewById(R.id.bar_lean);
        bar_normal = (ImageView) findViewById(R.id.bar_normal);
        bar_overweight = (ImageView) findViewById(R.id.bar_overweight);
        bar_obese = (ImageView) findViewById(R.id.bar_obesity);


        toMain = (LinearLayout)findViewById(R.id.report_ll_quit);
        switchToCalorieView = (Button)findViewById(R.id.btn_report_calorie);
        switchToNutrientView =  (Button)findViewById(R.id.btn_report_nutrient);
        CalorieView = (LinearLayout)findViewById(R.id.ll_report_calorie);
        NutrientView = (LinearLayout)findViewById(R.id.ll_report_nutrient);
        LabelCalorie = (ImageView)findViewById(R.id.label_report_calorie);
        LabelNutrient = (ImageView)findViewById(R.id.label_report_nutrient);

        percentBreakfast = (TextView)findViewById(R.id.report_display_percent_breakfast);
        statusBreakfast = (TextView)findViewById(R.id.report_display_status_breakfast);
        percentLunch = (TextView)findViewById(R.id.report_display_percent_lunch);
        statusLunch = (TextView)findViewById(R.id.report_display_status_lunch);
        percentDinner = (TextView)findViewById(R.id.report_display_percent_dinner);
        statusDinner = (TextView)findViewById(R.id.report_display_status_dinner);
        percentOther = (TextView)findViewById(R.id.report_display_percent_other);
        statusOther = (TextView)findViewById(R.id.report_display_status_other);

        diversityNumber = (TextView)findViewById(R.id.report_display_diversity_number);
        energyStatus = (TextView)findViewById(R.id.report_display_energy_status);

        currentWeightTV = (TextView)findViewById(R.id.current_weight);
        predictedWeightTV = (TextView)findViewById(R.id.predicted_weight);

        calorieTotal = (TextView)findViewById(R.id.report_display_nutrient_calorie_total);
        calorieGoal = (TextView)findViewById(R.id.report_display_nutrient_calorie_goal);
        calorieStatus = (TextView)findViewById(R.id.report_display_nutrient_calorie_condition);

        proteinTotal = (TextView)findViewById(R.id.report_display_nutrient_protein_total);
        proteinGoal = (TextView)findViewById(R.id.report_display_nutrient_protein_goal);
        proteinStatus = (TextView)findViewById(R.id.report_display_nutrient_protein_condition);

        carbsTotal = (TextView)findViewById(R.id.report_display_nutrient_carbohydrate_total);
        carbsGoal = (TextView)findViewById(R.id.report_display_nutrient_carbohydrate_goal);
        carbsStatus = (TextView) findViewById(R.id.report_display_nutrient_carbohydrate_condition);

        fatTotal = (TextView) findViewById(R.id.report_display_nutrient_fat_total);
        fatGoal = (TextView) findViewById(R.id.report_display_nutrient_fat_goal);
        fatStatus = (TextView) findViewById(R.id.report_display_nutrient_fat_condition);


        //display weight and BMI from database
        get_Weight_BMI_fromDatabase();

        //get Calorie and Nutrient info from database
        initCalorie_initNutrient();

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

            }
        });

    }

    public void get_Weight_BMI_fromDatabase(){
        //get userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Get the weight and BMI of the current user from the database
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){

                            for(DataSnapshot d: snapshot.getChildren()){

                                String userInfo_Key = d.getKey();
                                if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")) {

                                    databaseReference.child("Users").child(uid)
                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot d: dataSnapshot.getChildren()) {

                                                String d_Key = d.getKey();
                                                if(d_Key.equals("weight")){
                                                    weight = d.getValue().toString();
                                                    currentWeightTV.setText(weight+"kg");

                                                }
                                                if(d_Key.equals("height")){
                                                    height = d.getValue().toString();
                                                }

                                                if(d_Key.equals("bmi")){
                                                    bmi = d.getValue().toString();

                                                    Double d_bmi = Double.parseDouble(bmi);
                                                    DecimalFormat df = new DecimalFormat("0.00");
                                                    String str_bmi = df.format(d_bmi);
                                                    if(d_bmi < 18.5){
                                                        bar_lean.setVisibility(View.VISIBLE);
                                                        bubble_lean.setVisibility(View.VISIBLE);
                                                        text_lean.setText(str_bmi);
                                                        text_lean.setVisibility(View.VISIBLE);
                                                    }
                                                    else if(18.5 <= d_bmi && d_bmi<25){
                                                        bar_normal.setVisibility(View.VISIBLE);
                                                        bubble_normal.setVisibility(View.VISIBLE);
                                                        text_normal.setText(str_bmi);
                                                        text_normal.setVisibility(View.VISIBLE);

                                                    }
                                                    else if(25 <= d_bmi && d_bmi <30){
                                                        bar_overweight.setVisibility(View.VISIBLE);
                                                        bubble_overweight.setVisibility(View.VISIBLE);
                                                        text_overweight.setText(str_bmi);
                                                        text_overweight.setVisibility(View.VISIBLE);

                                                    }
                                                    else if(30 <= d_bmi){
                                                        bar_obese.setVisibility(View.VISIBLE);
                                                        bubble_obese.setVisibility(View.VISIBLE);
                                                        text_obese.setText(str_bmi);
                                                        text_obese.setVisibility(View.VISIBLE);

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
                            Toast.makeText(ReportActivity.this,"displayHeight_BMI ERROR!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    public void getWeight(final WeightCallBack weightCallBack){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Get the weight of the current user from the database
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        if(user!=null){

                            for(DataSnapshot d: snapshot.getChildren()){

                                String userInfo_Key = d.getKey();
                                if(!userInfo_Key.equals("userID") && !userInfo_Key.equals("username") && !userInfo_Key.equals("email") && !userInfo_Key.equals("password")&& !userInfo_Key.equals("confirm_password")&& !userInfo_Key.equals("security")) {

                                    databaseReference.child("Users").child(uid)
                                            .child(userInfo_Key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot d: dataSnapshot.getChildren()) {

                                                String d_Key = d.getKey();
                                                if(d_Key.equals("weight")){
                                                    weight = d.getValue().toString();
                                                    weightCallBack.onCallback(weight);
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

    public void getQuantityCategory(final MyCallBack myCallBack){

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {

                            final UsersFood usersFood = new UsersFood();
                            for (DataSnapshot dd : d.getChildren()) {
                                String dd_Key = dd.getKey();
                                String dd_Value = dd.getValue().toString();

                                if (dd_Key.equals("foodname")) {
                                    foodname = dd_Value;
                                    usersFood.setFoodname(foodname);

                                    System.out.println("==============foodname======" + foodname);

                                    //query quantity & category of the food
                                    Query q1 = databaseReference.child("Users").child(uid)
                                            .orderByChild("foodname")
                                            .equalTo(foodname);

                                    q1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    quantity = dataSnapshot1.child("quantity").getValue().toString();
                                                    category = dataSnapshot1.child("category").getValue().toString();

                                                    usersFood.setQuantity(quantity);
                                                    usersFood.setCategory(category);
                                                    System.out.println("=====quantity*category====" + quantity + " " + category);

                                                    usersFood.incrementFoodCount();
                                                    usersFoodArrayList.add(usersFood);

                                                    myCallBack.onCallback(usersFoodArrayList);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

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

    public void initCalorie_initNutrient(){

        //[steps]
        //1. select * food information from UsersDB
        //2. select * from FoodDB where foodname = foodname from UsersDB


        //1. select * food information from UsersDB
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {

                            final CustomFood customFood = new CustomFood();
                            for (DataSnapshot dd : d.getChildren()) {
                                String dd_Key = dd.getKey();
                                String dd_Value = dd.getValue().toString();

                                //Traverse the UsersDB and get all food information
                                if (dd_Key.equals("foodname")) {
                                    foodname = dd_Value;
                                    customFood.setFoodname(foodname);

                                    //2. select * from FoodDB where foodname = foodname from UsersDB
                                    Query query = databaseReference.child("Food")
                                            .orderByChild("foodname")
                                            .equalTo(foodname);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){

                                                for(DataSnapshot data:dataSnapshot.getChildren()){
                                                    System.out.println("==============data======"+data.getValue().toString());

                                                    String name = data.child("foodname").getValue().toString();
                                                    calorie = data.child("calorie").getValue().toString();
                                                    carbohydrate = data.child("carbs").getValue().toString();
                                                    fat = data.child("fat").getValue().toString();
                                                    protein = data.child("protein").getValue().toString();
                                                    System.out.println("===allinfo====="+name+"+"+calorie+"+"+carbohydrate+"+"+fat+"+"+protein);

                                                    customFood.setFoodname(name);
                                                    customFood.setCalorie(calorie);
                                                    customFood.setCarbs(carbohydrate);
                                                    customFood.setFat(fat);
                                                    customFood.setProtein(protein);
                                                    System.out.println("===alINFO====="+customFood.getFoodname()+"+"+customFood.getCalorie()+"+"+customFood.getCarbs()+"+"+customFood.getFat()+"+"+customFood.getProtein());


                                                    customFood.incrementFoodCount();
                                                    System.out.println("==============foodcount======"+customFood.getFoodCount());
                                                    customFoodArrayList.add(customFood);
                                                    System.out.println("==============allFoodArrayList======"+customFoodArrayList.size());

                                                }
                                            }

                                            System.out.println("==========a嗷嗷=="+customFoodArrayList.size());


                                            getQuantityCategory(new MyCallBack() {
                                                @Override
                                                public void onCallback(ArrayList<UsersFood> usersFoodArrayList1) {

                                                    double totQuan,totCalorie = 0;
                                                    double totQuan_b,totCalorie_b=0;
                                                    double totQuan_l,totCalorie_l=0;
                                                    double totQuan_d,totCalorie_d=0;
                                                    double totQuan_o,totCalorie_o=0;

                                                    double allCalorieCount = 0;
                                                    double allCalorieCount_b = 0;
                                                    double allCalorieCount_l = 0;
                                                    double allCalorieCount_d = 0;
                                                    double allCalorieCount_o = 0;

                                                    double totProtein=0,allProteinCount = 0;
                                                    double totCarbs = 0, allCarbsCount =0;
                                                    double totFat = 0, allFatCount =0;



                                                    int typesCount = 0;
                                                    if(usersFoodArrayList1.size() == customFoodArrayList.size()){
                                                        typesCount = customFoodArrayList.size();
                                                        System.out.println("==========呵呵=="+customFoodArrayList.size()+usersFoodArrayList1.size());

                                                        //calculate calorie of all food
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String tmp_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            System.out.println("=====customFoodArrayList=="+tmp_foodname);

                                                            totQuan = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            for(int j =0; j < customFoodArrayList.size();j++){
                                                                if(customFoodArrayList.get(j).getFoodname().equals(tmp_foodname)){
                                                                    totCalorie = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                    totCalorie = totCalorie * totQuan;

                                                                    totProtein = Double.parseDouble(customFoodArrayList.get(j).getProtein());
                                                                    totProtein = totProtein * totQuan;

                                                                    totCarbs = Double.parseDouble(customFoodArrayList.get(j).getCarbs());
                                                                    totCarbs = totCarbs * totQuan;

                                                                    totFat = Double.parseDouble(customFoodArrayList.get(j).getFat());
                                                                    totFat = totFat * totQuan;

                                                                }
                                                            }
                                                            allCalorieCount = allCalorieCount + totCalorie;
                                                            allProteinCount = allProteinCount + totProtein;
                                                            allCarbsCount = allCarbsCount + totCarbs;
                                                            allFatCount = allFatCount + totFat;
                                                            System.out.println("=====allCalorieCount=="+allCalorieCount);

                                                        }

                                                        //setFoodIntake diversity
                                                        diversityNumber.setText(typesCount+" types");
                                                        System.out.println("=========typesCount=="+typesCount);
                                                        System.out.println("=========allCalorieCount=="+allCalorieCount);

                                                        energyStatus.setText(Double.toString(allCalorieCount));

                                                        //setNutrient
                                                        //calorie
                                                        calorieTotal.setText(Double.toString(allCalorieCount));
                                                        calorieGoal.setText("2078");
                                                        boolean calStatus = false;
                                                        if( (2078 - allCalorieCount) > 0 ){
                                                            calStatus =true;
                                                        }
                                                        if(calStatus){
                                                            calorieStatus.setText("+");
                                                            //predictedWeight - loss weight
                                                            Double left = (2078 - allCalorieCount)/1000;
                                                            final Double less = left*0.25;
                                                            getWeight(new WeightCallBack() {
                                                                @Override
                                                                public void onCallback(String weight) {
                                                                    DecimalFormat df = new DecimalFormat("0.00");
                                                                    String str_predict = df.format(Double.parseDouble(weight)-less);
                                                                    predictedWeightTV.setText(str_predict+"kg");
                                                                }
                                                            });
                                                        }else{
                                                            calorieStatus.setText("-");
                                                            //predictedWeight - earn weight
                                                            Double left = (allCalorieCount - 2078)/1000;
                                                            final Double more = left*0.5;
                                                            getWeight(new WeightCallBack() {
                                                                @Override
                                                                public void onCallback(String weight) {
                                                                    DecimalFormat df = new DecimalFormat("0.0");
                                                                    String str_predict = df.format(Double.parseDouble(weight)+more);
                                                                    predictedWeightTV.setText(str_predict+"kg");
                                                                }
                                                            });

                                                        }
                                                        DecimalFormat df = new DecimalFormat("0.0");
                                                        //Protein
                                                        proteinTotal.setText(df.format(allProteinCount));
                                                        proteinGoal.setText("37");
                                                        boolean proStatus = false;
                                                        if( (37 - allProteinCount) > 0 ){
                                                            proStatus =true;
                                                        }
                                                        if(proStatus){
                                                            proteinStatus.setText("+");
                                                        }else{
                                                            proteinStatus.setText("-");
                                                        }
                                                        //Carbs
                                                        carbsTotal.setText(df.format(allCarbsCount));
                                                        carbsGoal.setText("290");
                                                        boolean carbohydrateStatus = false;
                                                        if( (290 - allCarbsCount) > 0 ){
                                                            carbohydrateStatus =true;
                                                        }
                                                        if(carbohydrateStatus){
                                                            carbsStatus.setText("+");
                                                        }else{
                                                            carbsStatus.setText("-");
                                                        }
                                                        //Fat
                                                        fatTotal.setText(df.format(allFatCount));
                                                        fatGoal.setText("27");
                                                        boolean fattttStatus = false;
                                                        if( (27 - allFatCount) > 0 ){
                                                            fattttStatus =true;
                                                        }
                                                        if(fattttStatus){
                                                            fatStatus.setText("+");
                                                        }else{
                                                            fatStatus.setText("-");
                                                        }

                                                        //calculate calorie of breakfast
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String breakfast_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String breakfast_category = usersFoodArrayList1.get(i).getCategory();

                                                            totQuan_b = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(breakfast_category.equals("Breakfast")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(breakfast_foodname)){
                                                                        totCalorie_b = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_b = totCalorie_b * totQuan_b;

                                                                    }
                                                                }
                                                                allCalorieCount_b = allCalorieCount_b + totCalorie_b;
                                                                System.out.println("=====BREAKFASTCalorieCount=="+allCalorieCount_b);
                                                            }

                                                        }
                                                        //calculate calorie of lunch
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String lunch_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String lunch_category = usersFoodArrayList1.get(i).getCategory();

                                                            totQuan_l = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(lunch_category.equals("Lunch")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(lunch_foodname)){
                                                                        totCalorie_l = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_l = totCalorie_l * totQuan_l;

                                                                    }
                                                                }
                                                                allCalorieCount_l = allCalorieCount_l + totCalorie_l;
                                                                System.out.println("=====LUNCHCalorieCount=="+allCalorieCount_l);
                                                            }

                                                        }
                                                        //calculate calorie of dinner
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String dinner_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String dinner_category = usersFoodArrayList1.get(i).getCategory();

                                                            totQuan_d = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(dinner_category.equals("Dinner")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(dinner_foodname)){
                                                                        totCalorie_d = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_d = totCalorie_d * totQuan_d;

                                                                    }
                                                                }
                                                                allCalorieCount_d = allCalorieCount_d + totCalorie_d;
                                                                System.out.println("=====DINNERCalorieCount=="+allCalorieCount_d);
                                                            }

                                                        }
                                                        //calculate calorie of snacks/others
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String other_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String other_category = usersFoodArrayList1.get(i).getCategory();

                                                            totQuan_o = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(other_category.equals("Other")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(other_foodname)){
                                                                        totCalorie_o = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_o = totCalorie_o * totQuan_o;

                                                                    }
                                                                }
                                                                allCalorieCount_o = allCalorieCount_o + totCalorie_o;
                                                                System.out.println("=====OtherCalorieCount=="+allCalorieCount_o);
                                                            }

                                                        }

                                                    }

                                                    //Calculate the proportion and status of breakfast,lunch,dinner,snacks
                                                    DecimalFormat df = new DecimalFormat("0.0");
                                                    double d_breakfastPercent = allCalorieCount_b/allCalorieCount;
                                                    double d_lunchPercent = allCalorieCount_l/allCalorieCount;
                                                    double d_dinnerPercent = allCalorieCount_d/allCalorieCount;
                                                    double d_otherPercent = allCalorieCount_o/allCalorieCount;

                                                    d_breakfastPercent = d_breakfastPercent * 100;
                                                    d_lunchPercent = d_lunchPercent * 100;
                                                    d_dinnerPercent = d_dinnerPercent * 100;
                                                    d_otherPercent = d_otherPercent * 100;

                                                    String str_breakfastPercent = df.format(d_breakfastPercent);//format返回String
                                                    String str_lunchPercent = df.format(d_lunchPercent);
                                                    String str_dinnerPercent = df.format(d_dinnerPercent);
                                                    String str_otherPercent = df.format(d_otherPercent);

                                                    //breakfast：lunch：dinner ：other = 2.75  :3.5: 2.75：1
                                                    if(d_breakfastPercent >0 || d_breakfastPercent ==0){
                                                        System.out.println("=======d_breakfastPercent=="+d_breakfastPercent);
                                                        boolean status = false;
                                                        if(d_breakfastPercent < 27.5)
                                                            status = true;
                                                        percentBreakfast.setText("("+str_breakfastPercent+"%)");
                                                        if(status){
                                                            statusBreakfast.setText("+");

                                                        }else{
                                                            statusBreakfast.setText("-");
                                                        }
                                                    }
                                                    //lunch
                                                    if(d_lunchPercent >0 || d_lunchPercent ==0){
                                                        System.out.println("=======d_lunchPercent=="+d_lunchPercent);
                                                        boolean status = false;
                                                        if(d_lunchPercent < 35.0)
                                                            status = true;
                                                        percentLunch.setText("("+str_lunchPercent+"%)");
                                                        if(status){
                                                            statusLunch.setText("+");

                                                        }else{
                                                            statusLunch.setText("-");
                                                        }
                                                    }
                                                    //dinner
                                                    if(d_dinnerPercent >0 || d_dinnerPercent ==0){
                                                        System.out.println("=======d_dinnerPercent=="+d_dinnerPercent);
                                                        boolean status = false;
                                                        if(d_dinnerPercent < 27.5)
                                                            status = true;
                                                        percentDinner.setText("("+str_dinnerPercent+"%)");
                                                        if(status){
                                                            statusDinner.setText("+");

                                                        }else{
                                                            statusDinner.setText("-");
                                                        }
                                                    }
                                                    //other
                                                    if(d_otherPercent >0 || d_otherPercent ==0){
                                                        System.out.println("=======d_otherPercent=="+d_otherPercent);
                                                        boolean status = false;
                                                        if(d_otherPercent < 10)
                                                            status = true;
                                                        percentOther.setText("("+str_otherPercent+"%)");
                                                        if(status){
                                                            statusOther.setText("+");

                                                        }else{
                                                            statusOther.setText("-");
                                                        }
                                                    }

                                                }
                                            });

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }//=========[end of] if (dd_Key.equals("foodname")) {


                            }//========[end of]for (DataSnapshot dd : d.getChildren())
                        }//=========[end of]for (DataSnapshot d : snapshot.getChildren())


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }



}
