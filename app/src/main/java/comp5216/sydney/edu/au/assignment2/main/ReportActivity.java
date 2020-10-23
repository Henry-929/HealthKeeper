package comp5216.sydney.edu.au.assignment2.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import comp5216.sydney.edu.au.assignment2.R;

public class ReportActivity extends Activity {

    LinearLayout toMain;
    Button switchToCalorieView;
    Button switchToNutrientView;
    LinearLayout CalorieView;
    LinearLayout NutrientView;
    ImageView LabelCalorie;
    ImageView LabelNutrient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_report);

        toMain = (LinearLayout)findViewById(R.id.report_ll_quit);
        switchToCalorieView = (Button)findViewById(R.id.btn_report_calorie);
        switchToNutrientView =  (Button)findViewById(R.id.btn_report_nutrient);
        CalorieView = (LinearLayout)findViewById(R.id.ll_report_calorie);
        NutrientView = (LinearLayout)findViewById(R.id.ll_report_nutrient);
        LabelCalorie = (ImageView)findViewById(R.id.label_report_calorie);
        LabelNutrient = (ImageView)findViewById(R.id.label_report_nutrient);
        initCalorie();


        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                if (intent != null) {
                    ReportActivity.this.startActivity(intent);
                }
            }
        });

        switchToCalorieView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NutrientView.setVisibility(View.GONE);
                LabelNutrient.setVisibility(View.INVISIBLE);
                LabelCalorie.setVisibility(View.VISIBLE);
                CalorieView.setVisibility(View.VISIBLE);
            }
        });

        switchToNutrientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalorieView.setVisibility(View.GONE);
                LabelCalorie.setVisibility(View.INVISIBLE);
                LabelNutrient.setVisibility(View.VISIBLE);
                NutrientView.setVisibility(View.VISIBLE);
                initNutrient();
            }
        });
    }

    public void initCalorie(){
        //todo set My Weight

        //todo set Calorie (Today)
        //下面的是例子，percent就是占比，status就是吃多吃少
        //status: true means user eat too much
        //        false means user eat too less
        setBreakfast(30,true);
        setLunch(20,false);
        setDinner(40,true);
        setOther(10,true);
        //todo set Food intake (Today)
        setFoodIntake(5,true);
    }

    public void initNutrient(){
        //todo set Nutrient
        //这也是例子，具体的值你们传过来直接输进去就ok，然后我只暂时做了2个set，因为不知道后续有多少量
        setNutrientCal(6000,14000,true);
        setNutrientProtein(3000,5000,false);
    }



    public void setBreakfast(int percent, boolean status){
        TextView percentBreakfast = (TextView)findViewById(R.id.report_display_percent_breakfast);
        TextView statusBreakfast = (TextView)findViewById(R.id.report_display_status_breakfast);
        percentBreakfast.setText("("+percent+"%)");
        if(status){
            statusBreakfast.setText("+");
        }else{
            statusBreakfast.setText("-");
        }
    }
    public void setLunch(int percent, boolean status){
        TextView percentLunch = (TextView)findViewById(R.id.report_display_percent_lunch);
        TextView statusLunch = (TextView)findViewById(R.id.report_display_status_lunch);
        percentLunch.setText("("+percent+"%)");
        if(status){
            statusLunch.setText("+");
        }else{
            statusLunch.setText("-");
        }
    }
    public void setDinner(int percent, boolean status){
        TextView percentDinner = (TextView)findViewById(R.id.report_display_percent_dinner);
        TextView statusDinner = (TextView)findViewById(R.id.report_display_status_dinner);
        percentDinner.setText("("+percent+"%)");
        if(status){
            statusDinner.setText("+");
        }else{
            statusDinner.setText("-");
        }
    }
    public void setOther(int percent, boolean status){
        TextView percentOther = (TextView)findViewById(R.id.report_display_percent_other);
        TextView statusOther = (TextView)findViewById(R.id.report_display_status_other);
        percentOther.setText("("+percent+"%)");
        if(status){
            statusOther.setText("+");
        }else{
            statusOther.setText("-");
        }
    }

    public void setFoodIntake(int number,boolean status){
        TextView mealNumber = (TextView)findViewById(R.id.report_display_meal_number);
        TextView mealStatus = (TextView)findViewById(R.id.report_display_meal_status);
        mealNumber.setText(number+" times");
        if(status){
            mealStatus.setText("+");
        }else{
            mealStatus.setText("-");
        }
    }

    public void setNutrientCal(int total,int goal,boolean status){
        TextView calorieTotal = (TextView)findViewById(R.id.report_display_nutrient_calorie_total);
        TextView calorieGoal = (TextView)findViewById(R.id.report_display_nutrient_calorie_goal);
        TextView calorieStatus = (TextView)findViewById(R.id.report_display_nutrient_calorie_condition);
        calorieTotal.setText(""+total);
        calorieGoal.setText(""+goal);
        if(status){
            calorieStatus.setText("+");
        }else{
            calorieStatus.setText("-");
        }
    }

    public void setNutrientProtein(int total,int goal,boolean status){
        TextView proteinTotal = (TextView)findViewById(R.id.report_display_nutrient_protein_total);
        TextView proteinGoal = (TextView)findViewById(R.id.report_display_nutrient_calorie_goal);
        TextView proteinStatus = (TextView)findViewById(R.id.report_display_nutrient_calorie_condition);
        proteinTotal.setText(""+total);
        proteinGoal.setText(""+goal);
        if(status){
            proteinStatus.setText("+");
        }else{
            proteinStatus.setText("-");
        }
    }
}
