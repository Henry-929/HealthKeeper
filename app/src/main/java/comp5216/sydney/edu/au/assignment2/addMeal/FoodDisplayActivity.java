package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import comp5216.sydney.edu.au.assignment2.R;

public class FoodDisplayActivity extends Activity {

    private LinearLayout ll_quit,progress_bar;
    private TextView calorieIntake,calorieTotal,calorieLeft;
    private String userIntake,userTotal,userLeft;
    private int leftInt;
    private float bar_width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        ll_quit = (LinearLayout)findViewById(R.id.ll_display_food_cancel);
        ll_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDisplayActivity.this, FoodDisplayActivity.class);
                if (intent != null) {
                    FoodDisplayActivity.this.startActivity(intent);
                }
            }
        });

        calorieIntake = (TextView)findViewById(R.id.meal_display_already_intake);
        calorieTotal = (TextView)findViewById(R.id.meal_display_total_intake);
        calorieLeft= (TextView)findViewById(R.id.meal_display_left_intake);
        userIntake = "1250";
        userTotal="2500";
        calorieIntake.setText(userIntake);
        calorieTotal.setText(userTotal);
        leftInt = Integer.parseInt(userTotal)-Integer.parseInt(userIntake);
        userLeft = String.valueOf(leftInt);
        calorieLeft.setText(userLeft);

        progress_bar = (LinearLayout)findViewById(R.id.calorie_progress_bar);
        bar_width = (float)Integer.parseInt(userIntake)/(float)Integer.parseInt(userTotal)*250;
        System.out.println(bar_width+"111111111111111111111111111111111");
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) progress_bar.getLayoutParams();
        Resources r = getResources();
        linearParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,bar_width,r.getDisplayMetrics());
        progress_bar.setLayoutParams(linearParams);
    }
}
