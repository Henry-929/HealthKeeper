package comp5216.sydney.edu.au.assignment2.addMeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;

public class FoodDisplayActivity extends Activity {
    public String Calorie,Protein,Carbohydrate,Fat;

    ListView listView_breakfast;
    FoodAdapter foodAdapter;
    ArrayList<UsersFood> arrayList = new ArrayList<>();

    private LinearLayout ll_quit,progress_bar;
    private TextView calorieIntake,calorieTotal,calorieLeft;
    private String userIntake,userTotal,userLeft;
    private int leftInt;
    private float bar_width;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        // Get the Intent that started this activity and extract the string
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(ManuallyInputActivity.EXTRA_MESSAGE_FOODNAME);
        listView_breakfast = (ListView) findViewById(R.id.listView_breakfast);
        foodAdapter = new FoodAdapter(this,arrayList);
        listView_breakfast.setAdapter(foodAdapter);

        Intent data = getIntent();
        // Extract name value from result extras
        String addFoodName = data.getExtras().getString("foodname");
        String addCalorie = data.getExtras().getString("calorie");
        Drawable addIcon = Drawable.createFromPath(data.getExtras().getString("icon"));

        UsersFood usersFood = new UsersFood(addFoodName,addCalorie,
                addIcon);
        foodAdapter.addfood(usersFood);
        Log.i("Added Item in list:", addFoodName);
        Toast.makeText(this, "Added:" + addCalorie, Toast.LENGTH_SHORT).show();


        ll_quit = (LinearLayout)findViewById(R.id.ll_display_food_cancel);
        ll_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDisplayActivity.this, MainActivity.class);
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

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == EDIT_ITEM_REQUEST_CODE){
//            if (resultCode == RESULT_OK){
//                data = getIntent();
//                // Extract name value from result extras
//                String addFoodName = data.getExtras().getString("foodname");
//                String addCalorie = data.getExtras().getString("calorie");
//                Drawable addIcon = Drawable.createFromPath(data.getExtras().getString("icon"));
//
//                UsersFood usersFood = new UsersFood(addFoodName,addCalorie,
//                        addIcon);
//                foodAdapter.add(usersFood);
//                Log.i("Added Item in list:", addFoodName);
//                Toast.makeText(this, "Added:" + addCalorie, Toast.LENGTH_SHORT).show();
//                foodAdapter.notifyDataSetChanged();
//            }
//        }
//    }

//    public static ArrayList<UsersFood> getAllFood(Context context,String foodname) {
//        ArrayList<UsersFood> arrayList = new ArrayList<UsersFood>();
//
//            UsersFood  usersFood = new UsersFood();
//            usersFood.foodname = foodname;
//            usersFood.calorie = "100";
//            usersFood.icon = ContextCompat.getDrawable(context, R.drawable.examplefood_burger);
//            //通过context对象将一个资源id转换成一个Drawable对象。;
//            arrayList.add(usersFood);
//
//        return arrayList;
//    }

//    public void getFoodInfofromDatabase(){
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Food").child("Hamburger");
//
//        myRef.addListenerForSingleValueEvent( new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot){
//                if(dataSnapshot.exists()){
//                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
//                        Toast.makeText(FoodDisplayActivity.this,"嗷嗷"+dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
//
//                        String d_Key = messageSnapshot.getKey();
//                        if(d_Key.equals("calorie")){
//                            Calorie = messageSnapshot.getValue().toString();
//                            TextView_FoodCalorie.setText(Calorie);
////                            Toast.makeText(AddMealActivity.this,"嗷嗷",Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        } );
//
//    }
}
