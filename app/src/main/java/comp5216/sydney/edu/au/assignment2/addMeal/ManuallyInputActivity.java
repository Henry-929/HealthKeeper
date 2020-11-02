package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import comp5216.sydney.edu.au.assignment2.R;

public class ManuallyInputActivity extends AppCompatActivity {

    public static String Calorie,Food;
    public static String uid;

    public DatabaseReference databaseReference;
    public FirebaseStorage storage;
    public StorageReference storageReference;

    private EditText editTextFoodName, editTextFoodQuantity;
    public Bitmap bmp;

    //define food info
    public String addFoodName,addFoodQuantity,addFoodCategory;
    public Spinner categorySpinner;
    private ArrayAdapter<String> spinneradapter = null;

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

//        addFoodName = editTextFoodName.getText().toString();
//        addFoodQuantity = editTextFoodQuantity.getText().toString();

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


                //将user-food 该用户输入的食物信息存入数据库
                //sendMessage();
                UserFoodAdd();
                //UserFoodAdd_toDatabase();



            }
        });
    }

    public void sendMessage() {
        editTextFoodName = (EditText)findViewById(R.id.custom_add_food_name);
        String message = editTextFoodName.getText().toString();
        editTextFoodQuantity = (EditText)findViewById(R.id.custom_add_food_quantity);
        String quantity = editTextFoodQuantity.getText().toString();
        categorySpinner = (Spinner)findViewById(R.id.custom_add_food_category);
        String category = categorySpinner.getSelectedItem().toString();

//        getFoodImage(message);
//        getCalorie(message);
//        Toast.makeText(ManuallyInputActivity.this,"嗷嗷"+Calorie,Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();

        //跳转到food display页面
        Intent intent = new Intent(ManuallyInputActivity.this, FoodDisplayActivity.class);

        bundle.putString("foodname", message);
        bundle.putString("calorie", message);
        bundle.putString("icon", message);
        bundle.putString("quantity",quantity);
        bundle.putString("category",category);

        intent.putExtra("data", bundle);
        //Use the setResult() method with a response code and the Intent with the response data
        startActivity(intent);
//        Toast.makeText(this, "Added:"+message, Toast.LENGTH_SHORT).show();
        finish();
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