package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comp5216.sydney.edu.au.assignment2.R;

public class UserCustomizeActivity extends AppCompatActivity {

    DatabaseReference databaseReference;

    private EditText editTextFoodName,editTextFoodCalorie,editTextFoodProtein,editTextFoodCarbo,editTextFoodFat;
    //Get food information: calories, protein, carbohydrates, fats
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

                //Put Food information: calories, protein, carbohydrates, fat into the Food database
                customFoodAdd();
                //customFoodAdd_toDatabase();

            }
        });

    }

    public void customFoodAdd(){

        addFoodName = editTextFoodName.getText().toString().toLowerCase();
        addCalorie = editTextFoodCalorie.getText().toString();
        addProtein = editTextFoodProtein.getText().toString();
        addCarbohydrate = editTextFoodCarbo.getText().toString();
        addFat = editTextFoodFat.getText().toString();

        //if User input is not null
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

        //Store the food information for Custom Food into the database
        customFoodAdd_toDatabase();
    }
    public void customFoodAdd_toDatabase(){

        //A new node is created for each custom Food information stored
        String newKey = databaseReference.child("Food").push().getKey();

        CustomFood customFood = new CustomFood(addFoodName, addCalorie, addProtein,addCarbohydrate,addFat);

        databaseReference.child("Food").child(newKey).setValue(customFood)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //The popover tells the user "Food has been successfully added"

                        //manuallyInput view
                        Intent intent = new Intent(UserCustomizeActivity.this, ManuallyInputActivity.class);
                        startActivity(intent);
                    }
                });
    }
}