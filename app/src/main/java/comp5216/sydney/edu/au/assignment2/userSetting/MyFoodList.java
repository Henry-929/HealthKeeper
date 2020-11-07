package comp5216.sydney.edu.au.assignment2.userSetting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.addMeal.UsersFood;

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
        getBreakfastData();

        //get lunch data
        listView_lunch = (ListView) findViewById(R.id.listView_lunch);
        arrayAdapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList2);
        listView_lunch.setAdapter(arrayAdapter2);
        getLunchData();

        //get dinner data
        listView_dinner = (ListView) findViewById(R.id.listView_dinner);
        arrayAdapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList3);
        listView_dinner.setAdapter(arrayAdapter3);
        getDinnerData();

        //get other data
        listView_other = (ListView) findViewById(R.id.listView_other);
        arrayAdapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList4);
        listView_other.setAdapter(arrayAdapter4);
        getOtherData();


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

    public void getBreakfastData(){
        //get userID
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
                                String name_quantity = breakfast.foodname+"  "+breakfast.quantity+" pcs";
                                arrayList1.add(captureName(name_quantity));
                                arrayAdapter1.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getLunchData(){
        //get userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Lunch")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood lunch = data.getValue(UsersFood.class);
                                String name_quantity = lunch.foodname+"  "+lunch.quantity+" pcs";
                                arrayList2.add(captureName(name_quantity));
                                arrayAdapter2.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getDinnerData(){
        //get userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Dinner")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood dinner = data.getValue(UsersFood.class);
                                String name_quantity = dinner.foodname+"  "+dinner.quantity+" pcs";
                                arrayList3.add(captureName(name_quantity));
                                arrayAdapter3.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getOtherData(){
        //get userID
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(uid)
                .orderByChild("category")
                .equalTo("Other")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot data: snapshot.getChildren()){
                                UsersFood other = data.getValue(UsersFood.class);
                                String name_quantity = other.foodname+"  "+other.quantity+" pcs";
                                arrayList4.add(captureName(name_quantity));
                                arrayAdapter4.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    //Capitalize the first letter
    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return  name;

    }


}
