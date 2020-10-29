package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.login.User;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;
import comp5216.sydney.edu.au.assignment2.main.ReportActivity;

public class ManuallyInputActivity extends AppCompatActivity {

    public static String uid;
    //public String str_id;
    public String Users_username;
    DatabaseReference databaseReference;

    private EditText editTextFoodName, editTextFoodQuantity;

    //define food info
    public String addFoodName,addFoodQuantity,addFoodCategory;
    private Spinner categorySpinner;
    private ArrayAdapter<String> spinneradapter = null;

    private static final String [] FoodCategory ={"Breakfast","Lunch","Dinner","Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_manually);

        //define confirm and cancel button
        final Button confirmBtn=findViewById(R.id.btn_add_food_custom_confirm);
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_custom_cancel);
        Button addCustom = (Button)findViewById(R.id.btn_add_custom_food);

        //define required food info
        editTextFoodName = (EditText)findViewById(R.id.custom_add_food_name);
        editTextFoodQuantity = (EditText)findViewById(R.id.custom_add_food_quantity);
        categorySpinner = (Spinner)findViewById(R.id.custom_add_food_category);
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

        addFoodName = editTextFoodName.getText().toString();
        addFoodQuantity = editTextFoodQuantity.getText().toString();


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
                //弹窗 提示用户已经将食物
                AlertDialog.Builder builder = new AlertDialog.Builder(ManuallyInputActivity.this);
                builder.setTitle(R.string.Manual_dialog_confirm)
                        .setMessage(R.string.Manual_check_healthReport)
                        .setPositiveButton(R.string.Manual_check_healthReport_btn, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if(addFoodName.isEmpty() || addFoodQuantity.isEmpty()) {
                                    Toast.makeText(ManuallyInputActivity.this,"FoodName or Quantity is empty ",Toast.LENGTH_SHORT).show();

                                }
                                else{

                                    Intent intent = new Intent(ManuallyInputActivity.this, ReportActivity.class);
                                    ManuallyInputActivity.this.startActivity(intent);

                                    finish();
                                }

                            }
                        });
                builder.create().show();
                UserFoodAdd();

                //将user-food 该用户输入的食物信息存入数据库
                //UserFoodAdd_toDatabase();
            }
        });
    }

    public void UserFoodAdd(){

        final String addFoodName2 = editTextFoodName.getText().toString();
        final String addFoodQuantity2 = editTextFoodQuantity.getText().toString();

        //if 用户输入不为空
        if(addFoodName2.isEmpty()){
            editTextFoodName.setError("Food Name is required");
            editTextFoodName.requestFocus();
            return;
        }
        if(addFoodQuantity2.isEmpty()){
            editTextFoodQuantity.setError("Food Quantity is required");
            editTextFoodQuantity.requestFocus();
            return;
        }

        //将user-food 该用户输入的食物信息存入数据库
        UserFoodAdd_toDatabase();
    }
    public void UserFoodAdd_toDatabase(){

        final String FoodName = editTextFoodName.getText().toString();
        final String FoodQuantity = editTextFoodQuantity.getText().toString();

        //final String Users_username;


        //获取userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data: snapshot.getChildren()){
                            if(data.getKey().equals("username")) {
                                Users_username = data.getValue().toString();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







    }



}