package comp5216.sydney.edu.au.assignment2.addMeal;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Timer;
import java.util.TimerTask;

import comp5216.sydney.edu.au.assignment2.R;

public class ManuallyInputActivity extends AppCompatActivity {

    public static String uid;

    public DatabaseReference databaseReference;
    public FirebaseStorage storage;
    public StorageReference storageReference;

    private EditText editTextFoodName, editTextFoodQuantity;

    //define food info
    public String addFoodName,addFoodQuantity,addFoodCategory;
    public Spinner categorySpinner;
    private ArrayAdapter<String> spinneradapter = null;

    public boolean FoodExisted;

    private static final String [] FoodCategory ={"Breakfast","Lunch","Dinner","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_manually);

        //define confirm and cancel button
        Button confirmBtn=findViewById(R.id.btn_add_food_custom_confirm);
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_custom_cancel);
        Button addCustom = (Button)findViewById(R.id.btn_add_custom_food);

        //define required food info
        editTextFoodName = (EditText)findViewById(R.id.custom_add_food_name);
        editTextFoodQuantity = (EditText)findViewById(R.id.custom_add_food_quantity);
        categorySpinner = (Spinner)findViewById(R.id.custom_add_food_category);
        spinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,FoodCategory);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        categorySpinner.setAdapter(spinneradapter);
        categorySpinner.setVisibility(View.VISIBLE);//Set default display
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


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManuallyInputActivity.this);
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
            public void onClick(View view) {
                Intent intent = new Intent(ManuallyInputActivity.this, UserCustomizeActivity.class);
                    if (intent != null) {
                        ManuallyInputActivity.this.startActivity(intent);
                    }
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //【steps】
                //判断DB中是否有用户输入的食物
                //（有）- 将该用户输入的食物信息存入 Users数据库
                //（无）- 弹窗 提示用户"Food db中不存在该食物，令用户add custom food"


                FoodExistOrNot();

                //UserFoodAdd();
                //UserFoodAdd_toDatabase();



            }
        });
    }

    public void sendMessage() {
        editTextFoodName = (EditText)findViewById(R.id.custom_add_food_name);
        String message = editTextFoodName.getText().toString().toLowerCase();
        editTextFoodQuantity = (EditText)findViewById(R.id.custom_add_food_quantity);
        String quantity = editTextFoodQuantity.getText().toString();
        categorySpinner = (Spinner)findViewById(R.id.custom_add_food_category);
        String category = categorySpinner.getSelectedItem().toString();


        // Instantiate a Bundle
        Bundle bundle = new Bundle();
        // Instantiate an intent
        Intent intent = new Intent(ManuallyInputActivity.this, FoodDisplayActivity.class);
        // Save the data in Bundle
        bundle.putString("foodname", message);
        bundle.putString("calorie", message);
        bundle.putString("icon", message);
        bundle.putString("quantity",quantity);
        bundle.putString("category",category);
        // Put the bundle into the intent
        intent.putExtra("data", bundle);
        //Use the setResult() method with a response code and the Intent with the response data
        startActivity(intent);
        finish();
    }
    public void FoodExistOrNot(){
        //判断DB中是否有用户输入的食物

        FoodExisted = false;
        addFoodName = editTextFoodName.getText().toString().toLowerCase();

        //遍历Food db 查找食物是否存在
        databaseReference.child("Food").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    for(DataSnapshot dd : d.getChildren()){
                        String dd_key = dd.getKey();
                        String dd_value = dd.getValue().toString();

                        //System.out.println(dd_key+"==========addFoodName======="+dd_value);

                        if(dd_key.equals("foodname") && dd_value.equals(addFoodName)){

                            // db中存在该食物
                            FoodExisted = true;
                            //System.out.println(dd_key+"==========FoodExisted = true;======="+dd_value);

                        }

                    }//=======[end of] for(DataSnapshot dd : d.getChildren()){
                }//=====[end of] for(DataSnapshot d : dataSnapshot.getChildren()){

                //（无）- 弹窗 提示用户"db中不存在该食物，令用户add custom food"
                if(FoodExisted == false){

                    //System.out.println("======food NOOO exist===="+addFoodName);
                    // 并跳转到 UserCustomizeActivity
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ManuallyInputActivity.this);
                            builder.setTitle("Sorry , the food does not exist in the database currently ! ")
                                    .setMessage("Please customize your food in the next page.")//It will jump to Add Custom Food Page in 2 seconds
                                    .setCancelable(true);

                            builder.create().show();

                            final Timer t = new Timer();

                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {

                                    Looper.prepare();
                                    //结束当前的activity
                                    //finish();

                                    builder.create().dismiss();
                                    t.cancel();

                                    Intent intent = new Intent(ManuallyInputActivity.this, UserCustomizeActivity.class);
                                    ManuallyInputActivity.this.startActivity(intent);

                                    Looper.loop();
                                }
                            },3200);

                }
                //（有）- 将user-food 该用户输入的食物信息存入数据库
                if(FoodExisted == true){

                    //System.out.println("======food exist ^-^===="+addFoodName);
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
        //判断（点击confirm按钮后，
        // 若foodname中输入的是一个 在Food db中检索不到的食物，则提示弹窗“用户需要custom food”）
        // todo



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
//                                        Intent intent = new Intent(ManuallyInputActivity.this, FoodDisplayActivity.class);
//                                        startActivity(intent);
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