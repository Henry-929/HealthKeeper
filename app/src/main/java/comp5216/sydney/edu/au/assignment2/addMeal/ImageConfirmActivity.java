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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class ImageConfirmActivity extends Activity {

    public String foodNameInChinse,addName,addQuantity,addCategory;
    public Bitmap foodImage;

    private Spinner categorySpinner;
    private ArrayAdapter<String> spinneradapter = null;
    private static final String [] addFoodCategory ={"Breakfast","Lunch","Dinner","Other"};

    private ImageView foodImageDisplay;
    private EditText foodNameDisplay,foodQuantityDisplay;

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
        foodNameDisplay = (EditText)findViewById(R.id.photo_add_food_name);
        foodQuantityDisplay= (EditText)findViewById(R.id.photo_add_food_quantity);

        categorySpinner = (Spinner)findViewById(R.id.photo_add_food_category);
        spinneradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addFoodCategory);

        categorySpinner.setAdapter(spinneradapter);
        categorySpinner.setVisibility(View.VISIBLE);//设置默认显示
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                addCategory = addFoodCategory[arg2];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        foodNameDisplay.setText(foodNameInChinse);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });

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
    }
}
