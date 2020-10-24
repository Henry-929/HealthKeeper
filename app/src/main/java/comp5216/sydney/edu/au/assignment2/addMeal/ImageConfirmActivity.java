package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import comp5216.sydney.edu.au.assignment2.R;

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
    }
}
