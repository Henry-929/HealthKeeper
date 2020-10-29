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
import android.widget.TextView;
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
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class UserCustomizeActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    private EditText editTextFoodName,editTextFoodCalorie,editTextFoodProtein,editTextFoodCarbo,editTextFoodFat;
    //获取食物信息：卡路里，蛋白质，碳水化物，脂肪
    public String addFoodName,addCalorie,addProtein,addCarbohydrate,addFat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_custom);

        //define confirm and cancel button
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_custom_cancel);
        final Button confirmBtn=findViewById(R.id.btn_add_food_custom_confirm);

        //define required food info
        editTextFoodName = (EditText)findViewById(R.id.custom_add_food_name);
        editTextFoodCalorie = (EditText)findViewById(R.id.custom_add_food_calorie);

        //define details food info
        editTextFoodProtein = (EditText)findViewById(R.id.custom_add_food_protein);
        editTextFoodCarbo = (EditText)findViewById(R.id.custom_add_food_carbohydrate);
        editTextFoodFat = (EditText)findViewById(R.id.custom_add_food_fat);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserCustomizeActivity.this);
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
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //将食物信息：卡路里，蛋白质，碳水化物，脂肪 存入Food数据库
                customFoodAdd();
                //customFoodAdd_toDatabase();

            }
        });

    }

    public void customFoodAdd(){

        addFoodName = editTextFoodName.getText().toString();
        addCalorie = editTextFoodCalorie.getText().toString();
        addProtein = editTextFoodProtein.getText().toString();
        addCarbohydrate = editTextFoodCarbo.getText().toString();
        addFat = editTextFoodFat.getText().toString();

        //if 用户输入不为空
        if(addFoodName.isEmpty()){
            editTextFoodName.setError("Food Name is required");
            editTextFoodName.requestFocus();
            return;
        }
        if(addCalorie.isEmpty()){
            editTextFoodCalorie.setError("Food Calorie is required");
            editTextFoodCalorie.requestFocus();
            return;
        }
        if(addProtein.isEmpty()){
            editTextFoodProtein.setError("Food Protein is required");
            editTextFoodProtein.requestFocus();
            return;
        }
        if(addCarbohydrate.isEmpty()){
            editTextFoodCarbo.setError("Food Carbohydrate is required");
            editTextFoodCarbo.requestFocus();
            return;
        }
        if(addFat.isEmpty()){
            editTextFoodFat.setError("Food Fat is required");
            editTextFoodFat.requestFocus();
            return;
        }

        //将custom food的食物信息存入数据库
        customFoodAdd_toDatabase();
    }
    public void customFoodAdd_toDatabase(){

        //每存放一个custom food食物信息 就创建一个新节点
        String newKey = databaseReference.child("Food").push().getKey();

        CustomFood customFood = new CustomFood(addFoodName, addCalorie, addProtein,addCarbohydrate,addFat);

        databaseReference.child("Food").child(newKey).setValue(customFood)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //manuallyInput 页面
                        Intent intent = new Intent(UserCustomizeActivity.this, ManuallyInputActivity.class);
                        startActivity(intent);
                    }
                });
    }
}