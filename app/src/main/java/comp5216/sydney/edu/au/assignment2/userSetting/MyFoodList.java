package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.CustomFood;
import comp5216.sydney.edu.au.assignment2.addMeal.MarshmallowPermission;
import comp5216.sydney.edu.au.assignment2.addMeal.MyCallBack;
import comp5216.sydney.edu.au.assignment2.addMeal.UsersFood;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;
import comp5216.sydney.edu.au.assignment2.main.ReportActivity;
import comp5216.sydney.edu.au.assignment2.main.WeightCallBack;

public class MyFoodList extends AppCompatActivity {

    public static String uid;
    public String foodname,quantity,category;
    public String calorie,carbohydrate,fat,protein;
    public int CountBreakfast=0,CountLunch=0,CountDinner=0,CountOther=0;



    public TextView breakfast1,breakfast2,breakfast3;
    public TextView lunch1,lunch2,lunch3;
    public TextView dinner1,dinner2,dinner3;
    public TextView other1,other2,other3;

    public ArrayList<UsersFood> usersFoodArrayList = new ArrayList<>();
    public ArrayList<CustomFood> customFoodArrayList = new ArrayList<>();



    public DatabaseReference databaseReference;

    LinearLayout MyFoodList_quit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_meal);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        breakfast1 = (TextView) findViewById(R.id.breakfast1);
        breakfast2 = (TextView) findViewById(R.id.breakfast2);
        breakfast3 = (TextView) findViewById(R.id.breakfast3);

        lunch1 = (TextView) findViewById(R.id.lunch1);
        lunch2 = (TextView) findViewById(R.id.lunch2);
        lunch3 = (TextView) findViewById(R.id.lunch3);

        dinner1 = (TextView) findViewById(R.id.dinner1);
        dinner2 = (TextView) findViewById(R.id.dinner2);
        dinner3 = (TextView) findViewById(R.id.dinner3);

        other1 = (TextView) findViewById(R.id.other1);
        other2 = (TextView) findViewById(R.id.other2);
        other3 = (TextView) findViewById(R.id.other3);


        MyFoodList_quit = (LinearLayout)findViewById(R.id.MyFoodList_quit);

        MyFoodList_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MyFoodList.this, UserActivity.class);
                if (intent != null) {
                    MyFoodList.this.startActivity(intent);
                }
            }
        });


        getFoodList_fromDatabase();



    }


    public void getQuantityCategory(final MyCallBack myCallBack){
        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            //d.getKey()是userInfo的key
                            final String userInfo_Key = d.getKey();

                            //final al_UsersFood al_usersFood = new al_UsersFood();
                            final UsersFood usersFood = new UsersFood();
                            for (DataSnapshot dd : d.getChildren()) {
                                String dd_Key = dd.getKey();
                                String dd_Value = dd.getValue().toString();


                                if (dd_Key.equals("foodname")) {
                                    foodname = dd_Value;
                                    usersFood.setFoodname(foodname);
                                    //al_usersFood.setFoodname(foodname);

                                    //Toast.makeText(ReportActivity.this, al_usersFood.getFoodname() + "!name!" + foodname, Toast.LENGTH_LONG).show();
//                                    System.out.println("==============foodname======" + foodname);


                                    //获取 quantity &category
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
//                                                    al_usersFood.setQuantity(quantity);
//                                                    al_usersFood.setCategory(category);
//                                                    System.out.println("=====quantity*category====" + quantity + " " + category);

                                                    //al_usersFood.incrementFoodCount();

                                                    //allFoodArrayList.add(al_usersFood);
                                                    usersFood.incrementFoodCount();
                                                    usersFoodArrayList.add(usersFood);
                                                    //System.out.println("===allfoodlist=====" + usersFood.toString());

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



    public void getFoodList_fromDatabase(){

        //1. select * from Users
        //2. select * from Food where foodname = "从User哪里获取到的"


        //1. select * from Users
        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot d : snapshot.getChildren()) {
                            //d.getKey()是userInfo的key
                            final String userInfo_Key = d.getKey();

                            //final al_UsersFood al_usersFood = new al_UsersFood();
                            final CustomFood customFood = new CustomFood();
                            for (DataSnapshot dd : d.getChildren()) {
                                String dd_Key = dd.getKey();
                                String dd_Value = dd.getValue().toString();


                                //所有的meal
                                if (dd_Key.equals("foodname")) {
                                    foodname = dd_Value;
                                    customFood.setFoodname(foodname);

                                    //2. select * from Food where foodname = "从User哪里获取到的"
                                    Query query = databaseReference.child("Food")
                                            .orderByChild("foodname")
                                            .equalTo(foodname);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){

                                                //System.out.println("==============dataS======"+dataSnapshot.getValue().toString());

                                                for(DataSnapshot data:dataSnapshot.getChildren()){
                                                    //System.out.println("==============data======"+data.getValue().toString());

                                                    String name = data.child("foodname").getValue().toString();
                                                    calorie = data.child("calorie").getValue().toString();
                                                    carbohydrate = data.child("carbs").getValue().toString();
                                                    fat = data.child("fat").getValue().toString();
                                                    protein = data.child("protein").getValue().toString();
                                                    //System.out.println("===allinfo====="+name+"+"+calorie+"+"+carbohydrate+"+"+fat+"+"+protein);


                                                    customFood.setFoodname(name);
                                                    customFood.setCalorie(calorie);
                                                    customFood.setCarbs(carbohydrate);
                                                    customFood.setFat(fat);
                                                    customFood.setProtein(protein);

//                                                    System.out.println("===alINFO====="+customFood.getFoodname()+"+"+customFood.getCalorie()+"+"+customFood.getCarbs()+"+"+customFood.getFat()+"+"+customFood.getProtein());

                                                    //al_usersFood.incrementFoodCount();
                                                    customFood.incrementFoodCount();
//                                                    System.out.println("==============foodcount======"+customFood.getFoodCount());
                                                    //allFoodArrayList.add(al_UsersFood);
                                                    customFoodArrayList.add(customFood);
//                                                    System.out.println("==============allFoodArrayList======"+customFoodArrayList.size());

                                                }
                                            }

//                                            System.out.println("==========a嗷嗷=="+customFoodArrayList.size());


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


                                                    //for(usersFoodArrayList)
                                                    //【？？】为啥两种食物？各个print两遍？？！！
                                                    int typesCount = 0;
                                                    if(usersFoodArrayList1.size() == customFoodArrayList.size()){
                                                        typesCount = customFoodArrayList.size();
//                                                        System.out.println("==========呵呵=="+customFoodArrayList.size()+usersFoodArrayList1.size());

                                                        //计算所有meal的Calorie
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String tmp_foodname = usersFoodArrayList1.get(i).getFoodname();
//                                                            System.out.println("=====customFoodArrayList=="+tmp_foodname);

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
//                                                            System.out.println("=====allCalorieCount=="+allCalorieCount);

                                                        }


//                                                        //setFoodIntake diversity
//                                                        diversityNumber.setText(typesCount+" types");
//                                                        System.out.println("=========typesCount=="+typesCount);
//                                                        System.out.println("=========allCalorieCount=="+allCalorieCount);
//
//
//
//


                                                        //计算早餐Calorie
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String breakfast_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String breakfast_category = usersFoodArrayList1.get(i).getCategory();
                                                            //System.out.println("=====customFoodArrayList=="+tmp_foodname);

                                                            totQuan_b = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(breakfast_category.equals("Breakfast")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(breakfast_foodname)){
                                                                        totCalorie_b = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_b = totCalorie_b * totQuan_b;
                                                                        //todo..
                                                                        CountBreakfast++;
                                                                        System.out.println("=====数字CountBreakfast=="+CountBreakfast);
                                                                        if(CountBreakfast == 1){
                                                                            breakfast1.setVisibility(View.VISIBLE);
//                                                                            breakfast1.setText("AAAA");
                                                                            breakfast1.setText(customFoodArrayList.get(j).getFoodname());

                                                                            System.out.println("=====啊啊啊啊啊CountBreakfast=="+customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountBreakfast == 2){
                                                                            breakfast2.setVisibility(View.VISIBLE);
                                                                            breakfast2.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountBreakfast == 3){
                                                                            breakfast3.setVisibility(View.VISIBLE);
                                                                            breakfast3.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }

                                                                    }
                                                                }
                                                                allCalorieCount_b = allCalorieCount_b + totCalorie_b;
//                                                                System.out.println("=====BREAKFASTCalorieCount=="+allCalorieCount_b);
                                                            }

                                                        }
                                                        //计算午餐餐Calorie
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String lunch_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String lunch_category = usersFoodArrayList1.get(i).getCategory();
                                                            //System.out.println("=====customFoodArrayList=="+tmp_foodname);

                                                            totQuan_l = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(lunch_category.equals("Lunch")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(lunch_foodname)){
                                                                        totCalorie_l = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_l = totCalorie_l * totQuan_l;

                                                                        CountLunch++;
                                                                        System.out.println("===========CountLunch==="+CountLunch);
                                                                        if(CountLunch == 1){
                                                                            lunch1.setVisibility(View.VISIBLE);
                                                                            lunch1.setText(customFoodArrayList.get(j).getFoodname());
                                                                            System.out.println("=====啊啊啊啊啊CountLunch=="+customFoodArrayList.get(j).getFoodname());

                                                                        }
                                                                        if(CountLunch == 2){
                                                                            lunch2.setVisibility(View.VISIBLE);
                                                                            lunch2.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountLunch == 3){
                                                                            lunch3.setVisibility(View.VISIBLE);
                                                                            lunch3.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }


                                                                    }
                                                                }
                                                                allCalorieCount_l = allCalorieCount_l + totCalorie_l;
//                                                                System.out.println("=====LUNCHCalorieCount=="+allCalorieCount_l);
                                                            }

                                                        }
                                                        //计算晚餐Calorie
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String dinner_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String dinner_category = usersFoodArrayList1.get(i).getCategory();
                                                            //System.out.println("=====customFoodArrayList=="+tmp_foodname);

                                                            totQuan_d = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(dinner_category.equals("Dinner")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(dinner_foodname)){
                                                                        totCalorie_d = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_d = totCalorie_d * totQuan_d;

                                                                        CountDinner++;
                                                                        System.out.println("===========CountDinner==="+CountDinner);
                                                                        if(CountDinner == 1){
                                                                            dinner1.setVisibility(View.VISIBLE);
                                                                            dinner1.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountDinner == 2){
                                                                            dinner2.setVisibility(View.VISIBLE);
                                                                            dinner2.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountDinner == 3){
                                                                            dinner3.setVisibility(View.VISIBLE);
                                                                            dinner3.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }

                                                                    }
                                                                }
                                                                allCalorieCount_d = allCalorieCount_d + totCalorie_d;
//                                                                System.out.println("=====DINNERCalorieCount=="+allCalorieCount_d);
                                                            }

                                                        }
                                                        //计算other餐Calorie
                                                        for(int i=0; i < usersFoodArrayList1.size();i++){
                                                            String other_foodname = usersFoodArrayList1.get(i).getFoodname();
                                                            String other_category = usersFoodArrayList1.get(i).getCategory();
                                                            //System.out.println("=====customFoodArrayList=="+tmp_foodname);

                                                            totQuan_o = Double.parseDouble(usersFoodArrayList1.get(i).getQuantity());

                                                            if(other_category.equals("Other")){

                                                                for(int j =0; j < customFoodArrayList.size();j++){
                                                                    if(customFoodArrayList.get(j).getFoodname().equals(other_foodname)){
                                                                        totCalorie_o = Double.parseDouble(customFoodArrayList.get(j).getCalorie());
                                                                        totCalorie_o = totCalorie_o * totQuan_o;

                                                                        CountOther++;
                                                                        System.out.println("===========otherCount==="+CountOther);
                                                                        if(CountOther == 1){
                                                                            other1.setVisibility(View.VISIBLE);
                                                                            other1.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountOther == 2){
                                                                            other2.setVisibility(View.VISIBLE);
                                                                            other2.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }
                                                                        if(CountOther == 3){
                                                                            other3.setVisibility(View.VISIBLE);
                                                                            other3.setText(customFoodArrayList.get(j).getFoodname());
                                                                        }

                                                                    }
                                                                }
                                                                allCalorieCount_o = allCalorieCount_o + totCalorie_o;
//                                                                System.out.println("=====OtherCalorieCount=="+allCalorieCount_o);
                                                            }

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
