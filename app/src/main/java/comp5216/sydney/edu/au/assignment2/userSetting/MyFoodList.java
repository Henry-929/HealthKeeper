package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    DatabaseReference databaseReference;
    ListView listView_breakfast,listView_lunch,listView_dinner,listView_other;

    ArrayList<String> arrayList1 = new ArrayList<String>();
    ArrayList<String> arrayList2 = new ArrayList<String>();
    ArrayList<String> arrayList3 = new ArrayList<String>();
    ArrayList<String> arrayList4 = new ArrayList<String>();

    ArrayAdapter<String> arrayAdapter1;
    ArrayAdapter<String> arrayAdapter2;
    ArrayAdapter<String> arrayAdapter3;
    ArrayAdapter<String> arrayAdapter4;

    LinearLayout MyFoodList_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_meal);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //get breakfast data
        listView_breakfast = (ListView) findViewById(R.id.listView_breakfast);
        arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList1);
        listView_breakfast.setAdapter(arrayAdapter1);
        getbreakfasetdata();

        //get lunch data
        listView_lunch = (ListView) findViewById(R.id.listView_lunch);
        arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList2);
        listView_lunch.setAdapter(arrayAdapter2);
        getlunchdata();

        //get dinner data
        listView_dinner = (ListView) findViewById(R.id.listView_dinner);
        arrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList3);
        listView_dinner.setAdapter(arrayAdapter3);
        getdinnerdata();

        //get other data
        listView_other = (ListView) findViewById(R.id.listView_other);
        arrayAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList4);
        listView_other.setAdapter(arrayAdapter4);
        getotherdata();


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

    public void getbreakfasetdata(){
        //获取userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Breakfast")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood breakfast = data.getValue(UsersFood.class);
                                arrayList1.add(breakfast.foodname);
                                arrayAdapter1.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getlunchdata(){
        //获取userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Lunch")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood breakfast = data.getValue(UsersFood.class);
                                arrayList2.add(breakfast.foodname);
                                arrayAdapter2.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getdinnerdata(){
        //获取userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Dinner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood breakfast = data.getValue(UsersFood.class);
                                arrayList3.add(breakfast.foodname);
                                arrayAdapter3.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getotherdata(){
        //获取userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Other")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood breakfast = data.getValue(UsersFood.class);
                                arrayList4.add(breakfast.foodname);
                                arrayAdapter4.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}
