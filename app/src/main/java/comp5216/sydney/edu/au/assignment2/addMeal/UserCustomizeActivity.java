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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class UserCustomizeActivity extends AppCompatActivity {
    public static String uid;

    private EditText editTextFoodName, editTextFoodQuantity;
    private TextView TextView_FoodCalorie,TextView_FoodProtein,TextView_FoodCarbohydrate,TextView_FoodFat;
    //获取食物信息：卡路里，蛋白质，碳水化物，脂肪
    public String Calorie,Protein,Carbohydrate,Fat;
//    private Spinner categorySpinner;
//    private ArrayAdapter<String> spinneradapter = null;

    private static final String [] addFoodCategory ={"Breakfast","Lunch","Dinner","Other"};

    public String addFoodName, addFoodQuantity, addFoodCalorie,FoodCategory,addFoodProtein,addFoodCarbohydrate,addFoodFat;//用于数据库存储的String值

    public DatabaseReference databaseReference,databaseSetTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_custom);

        //define confirm and cancel button
        final Button confirmBtn=findViewById(R.id.btn_add_food_custom_confirm);
        final LinearLayout cancelBtn = findViewById(R.id.ll_add_food_custom_cancel);

        //define required food info
        editTextFoodName = (EditText)findViewById(R.id.custom_add_food_name);
        TextView_FoodCalorie = (TextView)findViewById(R.id.custom_add_food_calorie);

        //define additional food info
        TextView_FoodProtein = (TextView)findViewById(R.id.custom_add_food_protein);
        TextView_FoodCarbohydrate = (TextView)findViewById(R.id.custom_add_food_carbohydrate);
        TextView_FoodFat = (TextView)findViewById(R.id.custom_add_food_fat);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //获取食物信息：卡路里，蛋白质，碳水化物，脂肪
        getFoodInfofromDatabase();

//        categorySpinner.setAdapter(spinneradapter);
//        categorySpinner.setVisibility(View.VISIBLE);//设置默认显示
//        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                       int arg2, long arg3) {
//                // TODO Auto-generated method stub
//                FoodCategory = addFoodCategory[arg2];
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                // TODO Auto-generated method stub
//
//            }
//        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UserCustomizeActivity.this, FoodDisplayActivity.class);
                if (intent != null) {
                    UserCustomizeActivity.this.startActivity(intent);
                }
            }
        });

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

    }

    public void getFoodInfofromDatabase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Food").child("Hamburger");

        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        Toast.makeText(UserCustomizeActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                        String d_Key = messageSnapshot.getKey();
                        if(d_Key.equals("calorie")){
                            Calorie = messageSnapshot.getValue().toString();
                            TextView_FoodCalorie.setText(Calorie);
//                            Toast.makeText(AddMealActivity.this,"嗷嗷",Toast.LENGTH_SHORT).show();

                        }

                        if(d_Key.equals("protein")){
                            Protein = messageSnapshot.getValue().toString();
                            TextView_FoodProtein.setText(Protein);
//                            Toast.makeText(AddMealActivity.this,"嗷嗷",Toast.LENGTH_SHORT).show();

                        }

                        if(d_Key.equals("carbohydrate")){
                            Carbohydrate = messageSnapshot.getValue().toString();
                            TextView_FoodCarbohydrate.setText(Carbohydrate);
//                            Toast.makeText(AddMealActivity.this,"嗷嗷",Toast.LENGTH_SHORT).show();

                        }

                        if(d_Key.equals("fat")){
                            Fat = messageSnapshot.getValue().toString();
                            TextView_FoodFat.setText(Fat);
//                            Toast.makeText(AddMealActivity.this,"嗷嗷",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        } );

    }

    public void addMealManual(){
        //collect required food info
        addFoodName = editTextFoodName.getText().toString();
        addFoodQuantity = editTextFoodQuantity.getText().toString();
//        addFoodCalorie = editTextFoodCalorie.getText().toString();

        //collect additional food info
        addFoodProtein = editTextFoodName.getText().toString();
        addFoodCarbohydrate = editTextFoodQuantity.getText().toString();
//        addFoodFat = editTextFoodCalorie.getText().toString();


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

//        if(addFoodCalorie.isEmpty()){
//            editTextFoodCalorie.setError("Food calorie is required");
//            editTextFoodCalorie.requestFocus();
//            return;
//        }

        foodInfoAdd_toDatabase();

    }

    public void foodInfoAdd_toDatabase(){

    }


}