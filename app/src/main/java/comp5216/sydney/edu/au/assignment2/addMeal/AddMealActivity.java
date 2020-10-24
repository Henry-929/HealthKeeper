package comp5216.sydney.edu.au.assignment2.addMeal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;
import comp5216.sydney.edu.au.assignment2.main.ReportActivity;

public class AddMealActivity extends AppCompatActivity {

    private EditText editTextFoodName, editTextFoodQuantity,editTextFoodCalorie,editTextFoodProtein,editTextFoodCarbohydrate,editTextFoodFat;
    public String addFoodName, addFoodQuantity, addFoodCalorie,addFoodProtein,addFoodCarbohydrate,addFoodFat;//用于数据库存储的String值

    DatabaseReference databaseReference,databaseSetTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_manual);

        //define confirm and cancel button
        final Button confirmBtn=findViewById(R.id.btn_add_food_manual_confirm);
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_manual_cancel);

        //define required food info
        editTextFoodName = (EditText)findViewById(R.id.manual_add_food_name);
        editTextFoodQuantity = (EditText)findViewById(R.id.manual_add_food_quantity);
        editTextFoodCalorie = (EditText)findViewById(R.id.manual_add_food_calorie);

        //define additional food info
        editTextFoodProtein = (EditText)findViewById(R.id.manual_add_food_protein);
        editTextFoodCarbohydrate = (EditText)findViewById(R.id.manual_add_food_carbohydrate);
        editTextFoodFat = (EditText)findViewById(R.id.manual_add_food_fat);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addMealManual();//【第一步】
                //userInfoAdd_toDatabase2();【第二步】
                //进入到MainActivity         【第三步】

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //todo 弹窗
               if(true){
                   Intent intent = new Intent(AddMealActivity.this, MainActivity.class);
                    if (intent != null) {
                        AddMealActivity.this.startActivity(intent);
                    }
                }
               }
        });
    }

    public void addMealManual(){
        //collect required food info
        addFoodName = editTextFoodName.getText().toString();
        addFoodQuantity = editTextFoodQuantity.getText().toString();
        addFoodCalorie = editTextFoodCalorie.getText().toString();

        //collect additional food info
        addFoodProtein = editTextFoodName.getText().toString();
        addFoodCarbohydrate = editTextFoodQuantity.getText().toString();
        addFoodFat = editTextFoodCalorie.getText().toString();


        if(addFoodName.isEmpty()){
            editTextFoodName.setError("Food name is required");
            editTextFoodName.requestFocus();
            // Toast.makeText(InfoActivity_1.this,"Gender is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }

        if(addFoodQuantity.isEmpty()){
            editTextFoodQuantity.setError("Food quantity is required");
            editTextFoodQuantity.requestFocus();
            // Toast.makeText(InfoActivity_1.this,"Gender is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }

        if(addFoodCalorie.isEmpty()){
            editTextFoodCalorie.setError("Food calorie is required");
            editTextFoodCalorie.requestFocus();
            return;
        }

        foodInfoAdd_toDatabase();

    }

    public void foodInfoAdd_toDatabase(){

    }


}