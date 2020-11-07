package comp5216.sydney.edu.au.assignment2.addMeal;

import android.content.DialogInterface;
import android.content.Intent;
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
                addFoodCategory = FoodCategory[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

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
                FoodExistOrNot();
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

                            // If the food is exist in db
                            FoodExisted = true;
                        }

                    }//=======[end of] for(DataSnapshot dd : d.getChildren()){
                }//=====[end of] for(DataSnapshot d : dataSnapshot.getChildren()){

                //If false - the popover prompts the user "this food does not exist in DB, make the user add Custom Food"
                if(FoodExisted == false){
                    // jump to  UserCustomizeActivity
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

                                    builder.create().dismiss();
                                    t.cancel();

                                    Intent intent = new Intent(ManuallyInputActivity.this, UserCustomizeActivity.class);
                                    ManuallyInputActivity.this.startActivity(intent);

                                    Looper.loop();
                                }
                            },3200);

                }
                //(true) - Store user-food information entered by the user into the database
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

        //get userID
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
                                        //jump to food display view
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