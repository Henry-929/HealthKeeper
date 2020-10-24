package comp5216.sydney.edu.au.assignment2.addMeal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comp5216.sydney.edu.au.assignment2.R;

public class AddMealActivity extends AppCompatActivity {

    private EditText Food, Amount,Weight;
    public String food, amount, weight;//用于数据库存储的String值

    DatabaseReference databaseReference,databaseSetTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_manual);

        //define button
        final Button saveBtn=findViewById(R.id.btn_food_to_addmeal);

        Food = (EditText)findViewById(R.id.input_food_name);
        Amount = (EditText)findViewById(R.id.input_food_amount);
        Weight = (EditText)findViewById(R.id.input_food_weight);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addMealManual();//【第一步】
                //userInfoAdd_toDatabase2();【第二步】
                //进入到MainActivity         【第三步】

            }
        });
    }

    public void addMealManual(){
        food = Food.getText().toString();
        amount = Amount.getText().toString();
        weight = Weight.getText().toString();

        if(food.isEmpty()){
            Food.setError("Birthday is required");
            Food.requestFocus();
            // Toast.makeText(InfoActivity_1.this,"Gender is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }

        if(amount.isEmpty()){
            Amount.setError("Birthday is required");
            Amount.requestFocus();
            // Toast.makeText(InfoActivity_1.this,"Gender is not chosen!",Toast.LENGTH_LONG).show();
            return;
        }

        if(weight.isEmpty()){
            Weight.setError("Weight is required");
            Weight.requestFocus();
            return;
        }

        foodInfoAdd_toDatabase();

    }

    public void foodInfoAdd_toDatabase(){

    }


}