package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class ImageConfirmActivity extends Activity {

    public static String uid;
    DatabaseReference databaseReference;

    public String foodNameInChinse,addFoodName,addFoodQuantity,addFoodCategory;

    private Spinner categorySpinner;
    private ArrayAdapter<String> spinneradapter = null;
    private static final String [] FoodCategory ={"Breakfast","Lunch","Dinner","Other"};

    private ImageView foodImageDisplay;
    private EditText editTextFoodName,editTextFoodQuantity;

    public boolean FoodExisted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_byphoto);

        Bundle bundle = this.getIntent().getExtras();
        foodNameInChinse = this.getIntent().getExtras().get("foodName").toString();
        BitmapBinder bitmapBinder = (BitmapBinder) bundle.getBinder("bitmap");
        Bitmap bitmap = bitmapBinder.getBitmap();


        final Button confirmBtn=findViewById(R.id.btn_add_food_photo_confirm);
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_photo_cancel);
        Button addCustom = (Button)findViewById(R.id.btn_add_custom_food);

        foodImageDisplay= (ImageView)findViewById(R.id.add_food_display_photo);
        foodImageDisplay.setImageBitmap(bitmap);
        editTextFoodName = (EditText)findViewById(R.id.photo_add_food_name);
        editTextFoodQuantity= (EditText)findViewById(R.id.photo_add_food_quantity);

        categorySpinner = (Spinner)findViewById(R.id.photo_add_food_category);
        spinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,FoodCategory);

        categorySpinner.setAdapter(spinneradapter);
        categorySpinner.setVisibility(View.VISIBLE);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                addFoodCategory = FoodCategory[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        databaseReference = FirebaseDatabase.getInstance().getReference();

        Translation();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageConfirmActivity.this);
                builder.setTitle(R.string.dialog_cancel_title)
                        .setMessage(R.string.dialog_cancel_msg)
                        .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Prepare data intent for sending it back
                                Intent data = new Intent(ImageConfirmActivity.this, MainActivity.class);

                                // Activity finished ok, return the data
                                setResult(RESULT_CANCELED, data);
                                ImageConfirmActivity.this.startActivity(data);
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

        addCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(true){
                    Intent intent = new Intent(ImageConfirmActivity.this, UserCustomizeActivity.class);
                    if (intent != null) {
                        ImageConfirmActivity.this.startActivity(intent);
                    }
                }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FoodExistOrNot();
            }
        });
    }
    
    //to translate chinese API language to English
    public void Translation(){

        int flag = 0;
        if(foodNameInChinse.equals("苹果")){
            editTextFoodName.setText("apple");
            flag = 1;
        }
        if(foodNameInChinse.equals("牛油果")){
            editTextFoodName.setText("avocado");
            flag = 1;
        }
        if(foodNameInChinse.equals("香蕉")){
            editTextFoodName.setText("banana");
            flag = 1;
        }
        if(foodNameInChinse.equals("红菜头")){
            editTextFoodName.setText("beetroot");
            flag = 1;
        }
        if(foodNameInChinse.equals("蓝莓")){
            editTextFoodName.setText("blueberry");
            flag = 1;
        }
        if(foodNameInChinse.equals("茄子")){
            editTextFoodName.setText("brinjal");
            flag = 1;
        }
        if(foodNameInChinse.equals("西兰花")){
            editTextFoodName.setText("broccoli");
            flag = 1;
        }
        if(foodNameInChinse.equals("甜椒")){
            editTextFoodName.setText("capsicum");
            flag = 1;
        }
        if(foodNameInChinse.equals("胡萝卜")){
            editTextFoodName.setText("carrot");
            flag = 1;
        }
        if(foodNameInChinse.equals("芝士")){
            editTextFoodName.setText("cheese");
            flag = 1;
        }
        if(foodNameInChinse.equals("薯条")){
            editTextFoodName.setText("chips");
            flag = 1;
        }
        if(foodNameInChinse.equals("黄瓜")){
            editTextFoodName.setText("cucumber");
            flag = 1;
        }
        if(foodNameInChinse.equals("鱼")){
            editTextFoodName.setText("fish");
            flag = 1;
        }
        if(foodNameInChinse.equals("汉堡")){
            editTextFoodName.setText("hamburger");
            flag = 1;
        }
        if(foodNameInChinse.equals("蜂蜜")){
            editTextFoodName.setText("honey");
            flag = 1;
        }
        if(foodNameInChinse.equals("猕猴桃")){
            editTextFoodName.setText("kiwifruit");
            flag = 1;
        }
        if(foodNameInChinse.equals("洋葱")){
            editTextFoodName.setText("onion");
            flag = 1;
        }
        if(foodNameInChinse.equals("甜橙")){
            editTextFoodName.setText("orange");
            flag = 1;
        }
        if(foodNameInChinse.equals("土豆")){
            editTextFoodName.setText("potato");
            flag = 1;
        }
        if(foodNameInChinse.equals("松饼")){
            editTextFoodName.setText("pancake");
            flag = 1;
        }
        if(foodNameInChinse.equals("番茄")){
            editTextFoodName.setText("tomato");
            flag = 1;
        }
        if(foodNameInChinse.equals("猕猴桃")){
            editTextFoodName.setText("kiwifruit");
            flag = 1;
        }
        if(foodNameInChinse.equals("花椰菜")){
            editTextFoodName.setText("broccoli");
            flag = 1;
        }
        if(foodNameInChinse.equals("番薯")){
            editTextFoodName.setText("sweet potato");
            flag = 1;
        }
        if(foodNameInChinse.equals("草莓")){
            editTextFoodName.setText("strawberry");
            flag = 1;
        }
        if(foodNameInChinse.equals("披萨")){
            editTextFoodName.setText("pizza");
            flag = 1;
        }
        if(foodNameInChinse.equals("南瓜")){
            editTextFoodName.setText("pumpkin");
            flag = 1;
        }
        if(foodNameInChinse.equals("米饭")){
            editTextFoodName.setText("rice");
            flag = 1;
        }
        if(foodNameInChinse.equals("三明治")){
            editTextFoodName.setText("sandwich");
            flag = 1;
        }
        if(foodNameInChinse.equals("香肠")){
            editTextFoodName.setText("sausage");
            flag = 1;
        }
        if(foodNameInChinse.equals("虾")){
            editTextFoodName.setText("shrimp");
            flag = 1;
        }
        if(foodNameInChinse.equals("牛排")){
            editTextFoodName.setText("steak");
            flag = 1;
        }
        if(flag == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(ImageConfirmActivity.this);
            builder.setTitle(R.string.food_recognition_fail_title)
                    .setMessage(R.string.food_recognition_fail_content)
                    .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Prepare data intent for sending it back
                            Intent data = new Intent(ImageConfirmActivity.this, MainActivity.class);

                            // Activity finished ok, return the data
                            setResult(RESULT_CANCELED, data);
                            ImageConfirmActivity.this.startActivity(data);
                            // set result code and bundle data for response
                            // closes the activity, pass data to parent
                            finish();
                        }
                    });
            builder.create().show();
        }
    }

    public void sendMessage() {
        editTextFoodName = (EditText)findViewById(R.id.photo_add_food_name);
        String message = editTextFoodName.getText().toString().toLowerCase();
        editTextFoodQuantity = (EditText)findViewById(R.id.photo_add_food_quantity);
        String quantity = editTextFoodQuantity.getText().toString();
        categorySpinner = (Spinner)findViewById(R.id.photo_add_food_category);
        String category = categorySpinner.getSelectedItem().toString();

        // Instantiate a Bundle
        Bundle bundle = new Bundle();
        // Instantiate an intent
        Intent intent = new Intent(ImageConfirmActivity.this, FoodDisplayActivity.class);
        // Save the data in Bundle
        bundle.putString("foodname", message);
        bundle.putString("calorie", message);
        bundle.putString("icon", message);
        bundle.putString("quantity",quantity);
        bundle.putString("category",category);
        //Put the bundle into the intent
        intent.putExtra("data", bundle);
        //Use the setResult() method with a response code and the Intent with the response data
        startActivity(intent);
        finish();
    }

    public void FoodExistOrNot(){
        //Determine if the DB has any food entered by the user

        FoodExisted = false;
        addFoodName = editTextFoodName.getText().toString().toLowerCase();

        //Go through the Food DB to see if the Food is present
        databaseReference.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    for(DataSnapshot dd : d.getChildren()){
                        String dd_key = dd.getKey();
                        String dd_value = dd.getValue().toString();
                        if(dd_key.equals("foodname") && dd_value.equals(addFoodName)){

                            // Check for the presence of the food in the DB
                            FoodExisted = true;
                        }

                    }//=======[end of] for(DataSnapshot dd : d.getChildren()){
                }//=====[end of] for(DataSnapshot d : dataSnapshot.getChildren()){

                //(if not) - the popover prompts the user to "add custom Food if this food does not exist in DB"
                if(FoodExisted == false){
                    // Jump to  UserCustomizeActivity
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ImageConfirmActivity.this);
                    builder.setTitle("Sorry , the food does not exist in the database currently ! ")
                            .setMessage("Please customize your food in the next page.")//It will jump to Add Custom Food Page in 2 seconds
                            .setCancelable(true);

                    builder.create().show();

                    final Timer t = new Timer();

                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            Looper.prepare();

                            builder.create().dismiss();
                            t.cancel();

                            Intent intent = new Intent(ImageConfirmActivity.this, UserCustomizeActivity.class);
                            ImageConfirmActivity.this.startActivity(intent);

                            Looper.loop();
                        }
                    },3200);

                }
                //(if true) - Store user-food information that the user has entered into the database
                if(FoodExisted == true){
                    UserFoodAdd();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void UserFoodAdd(){

        addFoodName = editTextFoodName.getText().toString().toLowerCase();
        addFoodQuantity = editTextFoodQuantity.getText().toString();

        //if User input is not null
        if(addFoodName.isEmpty()){
            editTextFoodName.setError("Food Name is required");
            editTextFoodName.requestFocus();
            return;
        }
        if(addFoodQuantity.isEmpty()){
            editTextFoodQuantity.setError("Food Quantity is required");
            editTextFoodQuantity.requestFocus();
            return;
        }

        //Store the food information entered by the user-food in the database
        UserFoodAdd_toDatabase();
    }
    public void UserFoodAdd_toDatabase(){

        //Get userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //A new node is created for each food information stored
                        String newKey = databaseReference.child("Users").child(uid).push().getKey();

                        UsersFood usersFood = new UsersFood(addFoodName, addFoodQuantity, addFoodCategory);

                        databaseReference.child("Users").child(uid).child(newKey).setValue(usersFood)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Skip to the Food Display page
                                        sendMessage();
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
