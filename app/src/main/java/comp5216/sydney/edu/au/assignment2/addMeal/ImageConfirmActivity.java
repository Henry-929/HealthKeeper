package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class ImageConfirmActivity extends Activity {

    public static String uid;
    DatabaseReference databaseReference;

    public String foodNameInChinse,addFoodName,addFoodQuantity,addFoodCategory;
    public Bitmap foodImage;

    private Spinner categorySpinner;
    private ArrayAdapter<String> spinneradapter = null;
    private static final String [] FoodCategory ={"Breakfast","Lunch","Dinner","Other"};

    private ImageView foodImageDisplay;
    private EditText editTextFoodName,editTextFoodQuantity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_byphoto);

        Bundle bundle = this.getIntent().getExtras();
//        foodName = bundle.getString("foodName");
        foodNameInChinse = this.getIntent().getExtras().get("foodName").toString();
//        foodImage = bundle.getParcelable("foodPhoto");

        final Button confirmBtn=findViewById(R.id.btn_add_food_photo_confirm);
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_photo_cancel);
        Button addCustom = (Button)findViewById(R.id.btn_add_custom_food);

        foodImageDisplay= (ImageView)findViewById(R.id.add_food_display_photo);
        editTextFoodName = (EditText)findViewById(R.id.photo_add_food_name);
        editTextFoodQuantity= (EditText)findViewById(R.id.photo_add_food_quantity);

        categorySpinner = (Spinner)findViewById(R.id.photo_add_food_category);
        spinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,FoodCategory);

        categorySpinner.setAdapter(spinneradapter);
        categorySpinner.setVisibility(View.VISIBLE);//设置默认显示
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
        editTextFoodName.setText(foodNameInChinse);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
//            public void onClick(View v){
//                //todo 弹窗
//                if(true){
//                    Intent intent = new Intent(ImageConfirmActivity.this, MainActivity.class);
//                    if (intent != null) {
//                        ImageConfirmActivity.this.startActivity(intent);
//                    }
//                }
//            }

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageConfirmActivity.this);
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

        addCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //todo 弹窗
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
                //将user-food 该用户输入的食物信息存入数据库
                UserFoodAdd();
                //UserFoodAdd_toDatabase();
            }
        });
    }

    public void UserFoodAdd(){

        addFoodName = editTextFoodName.getText().toString();
        addFoodQuantity = editTextFoodQuantity.getText().toString();

        //if 用户输入不为空
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

        //将user-food 该用户输入的食物信息存入数据库
        UserFoodAdd_toDatabase();
    }
    public void UserFoodAdd_toDatabase(){

        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //每存放一个食物信息 就创建一个新节点
                        String newKey = databaseReference.child("Users").child(uid).push().getKey();

                        UsersFood usersFood = new UsersFood(addFoodName, addFoodQuantity, addFoodCategory);

                        databaseReference.child("Users").child(uid).child(newKey).setValue(usersFood)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //跳转到food display页面
                                        Intent intent = new Intent(ImageConfirmActivity.this, FoodDisplayActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }


}
