package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.CustomFood;
import comp5216.sydney.edu.au.assignment2.addMeal.MarshmallowPermission;
import comp5216.sydney.edu.au.assignment2.addMeal.MyCallBack;
import comp5216.sydney.edu.au.assignment2.addMeal.UsersFood;
import comp5216.sydney.edu.au.assignment2.main.MainActivity;
import comp5216.sydney.edu.au.assignment2.main.ReportActivity;
import comp5216.sydney.edu.au.assignment2.main.WeightCallBack;

public class MyFoodList extends AppCompatActivity {



    public TextView breakfast1,breakfast2,breakfast3;
    public TextView lunch1,lunch2,lunch3;
    public TextView dinner1,dinner2,dinner3;
    public TextView other1,other2,other3;



    public DatabaseReference databaseReference;

    LinearLayout MyFoodList_quit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_meal);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        breakfast1 = (TextView) findViewById(R.id.breakfast1);
        breakfast2 = (TextView) findViewById(R.id.breakfast2);
        breakfast3 = (TextView) findViewById(R.id.breakfast3);

        lunch1 = (TextView) findViewById(R.id.lunch1);
        lunch2 = (TextView) findViewById(R.id.lunch2);
        lunch3 = (TextView) findViewById(R.id.lunch3);

        dinner1 = (TextView) findViewById(R.id.dinner1);
        dinner2 = (TextView) findViewById(R.id.dinner2);
        dinner3 = (TextView) findViewById(R.id.dinner3);

        other1 = (TextView) findViewById(R.id.other1);
        other2 = (TextView) findViewById(R.id.other2);
        other3 = (TextView) findViewById(R.id.other3);


        MyFoodList_quit = (LinearLayout)findViewById(R.id.MyFoodList_quit);

        MyFoodList_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MyFoodList.this, UserActivity.class);
                if (intent != null) {
                    MyFoodList.this.startActivity(intent);
                }
            }
        });


        //todo...
        // get FoodList from Database




    }






}
