package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class FoodDisplayActivity extends AppCompatActivity {


    public static String uid;
    DatabaseReference databaseReference;
    public String foodname,quantity,category;
    public String calorie,carbohydrate,fat,protein;
    public ArrayList<UsersFood> usersFoodArrayList = new ArrayList<>();
    public ArrayList<CustomFood> customFoodArrayList = new ArrayList<>();

    ListView listView_breakfast,listView_lunch,listView_dinner;
    FoodAdapter foodAdapter;
    ArrayList<UsersFood> arrayList;

    private LinearLayout ll_quit;
    ProgressBar progressBar;
    private TextView calorieIntake,calorieTotal,calorieLeft;
    private String userIntake,userTotal,userLeft;
    private int leftInt;
    private float bar_width;

    public Bitmap bmp;
    public String mCalorie,mProtein,mCarbohydrate,mFat;
    public FirebaseStorage storage;
    public StorageReference storageReference;
    private TextView custom_get_food_name,custom_get_food_calorie,custom_get_food_protein,
            custom_get_food_carbohydrate,custom_get_food_fat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        ll_quit = (LinearLayout)findViewById(R.id.ll_display_food_cancel);
        ll_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDisplayActivity.this, MainActivity.class);
                if (intent != null) {
                    FoodDisplayActivity.this.startActivity(intent);
                }
            }
        });

        calorieIntake = (TextView)findViewById(R.id.meal_display_already_intake);
        calorieTotal = (TextView)findViewById(R.id.meal_display_total_intake);
        calorieLeft= (TextView)findViewById(R.id.meal_display_left_intake);
        progressBar = (ProgressBar) findViewById(R.id.calorie_progress_bar);
        getIntakefromDatabaseandDisplay();

        //todo..
        //listview breakfast lunch dinner

        //---------------------------------------------------------------------------

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        custom_get_food_name = (TextView) findViewById(R.id.custom_get_food_name);
        custom_get_food_calorie = (TextView) findViewById(R.id.custom_get_food_calorie);
        custom_get_food_protein = (TextView) findViewById(R.id.custom_get_food_protein);
        custom_get_food_carbohydrate = (TextView) findViewById(R.id.custom_get_food_carbohydrate);
        custom_get_food_fat = (TextView) findViewById(R.id.custom_get_food_fat);

        getItent();

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

    public void getIntakefromDatabaseandDisplay(){

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
                                            //clear
                                            //al_usersFood.clear_al_UsersFood();
//                                            System.out.println("==============al======"+al_usersFood.toString());

//                                            al_usersFood.setFoodname(foodname);
                                            if(dataSnapshot.exists()){

//                                                System.out.println("==============dataS======"+dataSnapshot.getValue().toString());

                                                for(DataSnapshot data:dataSnapshot.getChildren()){
//                                                    System.out.println("==============data======"+data.getValue().toString());

                                                    String name = data.child("foodname").getValue().toString();
                                                    calorie = data.child("calorie").getValue().toString();
                                                    carbohydrate = data.child("carbs").getValue().toString();
                                                    fat = data.child("fat").getValue().toString();
                                                    protein = data.child("protein").getValue().toString();
//                                                    System.out.println("===allinfo====="+name+"+"+calorie+"+"+carbohydrate+"+"+fat+"+"+protein);


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

                                                    double totQuan,totCalorie = 0,totCarbs,totFat,totPro;
                                                    double totQuan_b,totCalorie_b=0;
                                                    double totQuan_l,totCalorie_l=0;
                                                    double totQuan_d,totCalorie_d=0;
                                                    double totQuan_o,totCalorie_o=0;

                                                    double allCalorieCount = 0;
                                                    double allCalorieCount_b = 0;
                                                    double allCalorieCount_l = 0;
                                                    double allCalorieCount_d = 0;
                                                    double allCalorieCount_o = 0;

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

                                                                }
                                                            }
                                                            allCalorieCount = allCalorieCount + totCalorie;
//                                                            System.out.println("=====allCalorieCount=="+allCalorieCount);

                                                        }
                                                        //set total intake
                                                        //已经摄入的
                                                        calorieIntake.setText(Double.toString(allCalorieCount));

                                                        //推荐总量 默认2500
                                                        //todo.. 按照年龄大小分总量（从数据库获取）
                                                        calorieTotal.setText("2500 cal");

                                                        //剩余量
                                                        Double d_calorieLeft = 2500- allCalorieCount;
                                                        calorieLeft.setText(Double.toString(d_calorieLeft)+" cal");

                                                        //progressBar
                                                        int progress = (int)allCalorieCount/25;
//                                                        progressBar.incrementProgressBy(progress);
                                                        progressBar.setProgress(progress);


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

                                                                    }
                                                                }
                                                                allCalorieCount_b = allCalorieCount_b + totCalorie_b;
//                                                                System.out.println("=====BREAKFASTCalorieCount=="+allCalorieCount_b);


                                                                //todo..
                                                                //show 早餐的list （icon，foodname，calorie）
                                                                //午餐，晚餐，other同理
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
                                                                        totCalorie_d = totCalorie_b * totQuan_d;

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

    public void getItent(){
        //获取数据
        Intent intent = getIntent();
        //从intent取出bundle
        Bundle data = intent.getBundleExtra("data");
        if (data != null) {

            String addFoodName = data.getString("foodname");
            custom_get_food_name.setText(addFoodName);
            System.out.println("---------------------------------------------------"+addFoodName);
            getCalorie_Manually(addFoodName);
//            Bitmap getbmp = getFoodImage(addImage);

        }

        //从intent取出bundle
        Bundle data2 = intent.getBundleExtra("data_image");
        if (data2 != null) {

            String addFoodName2 = data2.getString("foodname2");
            custom_get_food_name.setText(addFoodName2);
            System.out.println("---------------------------------------------------"+addFoodName2);
            getCalorie_ImageConfirm(addFoodName2);
//            Bitmap getbmp = getFoodImage(addImage);

        }
    }

    public void getCalorie_Manually(String message){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Food").child(message);

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
//                        Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                        String d_Key = messageSnapshot.getKey();
                        if(d_Key.equals("calorie")){
                            mCalorie = messageSnapshot.getValue().toString();
                            custom_get_food_calorie.setText(mCalorie);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }

                        if(d_Key.equals("protein")){
                            mProtein = messageSnapshot.getValue().toString();
                            custom_get_food_protein.setText(mProtein);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }

                        if(d_Key.equals("carbs")){
                            mCarbohydrate = messageSnapshot.getValue().toString();
                            custom_get_food_carbohydrate.setText(mCarbohydrate);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }

                        if(d_Key.equals("fat")){
                            mFat = messageSnapshot.getValue().toString();
                            custom_get_food_fat.setText(mFat);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    public void getCalorie_ImageConfirm(String message){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Food").child(message);

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
//                        Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                        String d_Key = messageSnapshot.getKey();
                        if(d_Key.equals("calorie")){
                            mCalorie = messageSnapshot.getValue().toString();
                            custom_get_food_calorie.setText(mCalorie);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }

                        if(d_Key.equals("protein")){
                            mProtein = messageSnapshot.getValue().toString();
                            custom_get_food_protein.setText(mProtein);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }

                        if(d_Key.equals("carbs")){
                            mCarbohydrate = messageSnapshot.getValue().toString();
                            custom_get_food_carbohydrate.setText(mCarbohydrate);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }

                        if(d_Key.equals("fat")){
                            mFat = messageSnapshot.getValue().toString();
                            custom_get_food_fat.setText(mFat);
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
    }

    public Bitmap getFoodImage(final String message){
        final String[] Food = {null};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Food").child(message);

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
//                        Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                        String d_Key = messageSnapshot.getKey();
                        if(d_Key.equals("foodname")){
                            Food[0] = messageSnapshot.getValue().toString();
//                            Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+messageCalorie,Toast.LENGTH_SHORT).show();

                            StorageReference appleRef = storageReference.child("FoodImage/icon_Apple.jpeg");
                            StorageReference bananaRef = storageReference.child("FoodImage/icon_Banana.jpeg");
                            StorageReference burgerRef = storageReference.child("FoodImage/icon_Burger.jpeg");
                            StorageReference onionRef = storageReference.child("FoodImage/icon_Onion.jpeg");


                            final long ONE_MEGABYTE = 1024 * 1024;
                            if(Food[0].equals("Apple")){

                                appleRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                        food_image.setImageBitmap(bmp);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getApplicationContext(), "Loading FoodImage Male ERROR!!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else if(Food[0].equals("Banana")){

                                bananaRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                        food_image.setImageBitmap(bmp);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getApplicationContext(), "Loading FoodImage Male ERROR!!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else if(Food[0].equals("Hamburger")){

                                burgerRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                        food_image.setImageBitmap(bmp);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getApplicationContext(), "Loading FoodImage Male ERROR!!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else if(Food[0].equals("Onion")){

                                onionRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                        food_image.setImageBitmap(bmp);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getApplicationContext(), "Loading FoodImage Male ERROR!!", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );
        return bmp;
    }



}
