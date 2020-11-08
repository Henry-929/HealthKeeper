package comp5216.sydney.edu.au.assignment2.userSetting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

        //delete item
        setupListViewListener();

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

    }

    private void setupListViewListener() {
        listView_breakfast.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId)
            {
                Log.i("MyFoodList", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyFoodList.this);
                builder.setTitle(R.string.dialog_delete_title_2)
                        .setMessage(R.string.dialog_delete_msg_2)
                        .setPositiveButton(R.string.delete_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //get click position food name
                                        String getItem = arrayList1.get(position);
                                        String str = getItem.substring(0,getItem.length()-7);
                                        String getFoodName = captureName2(str);

                                        //remove food name from database
                                        deleteBreakfastData(getFoodName);

                                        // Remove item from the ArrayList
                                        arrayList1.remove(position);

                                        // Notify listView adapter to update the list
                                        arrayAdapter1.notifyDataSetChanged();

                                    }
                                })
                        .setNegativeButton(R.string.cancel_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true; }
        });

        listView_lunch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId)
            {
                Log.i("MyFoodList", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyFoodList.this);
                builder.setTitle(R.string.dialog_delete_title_2)
                        .setMessage(R.string.dialog_delete_msg_2)
                        .setPositiveButton(R.string.delete_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //get click position food name
                                        String getItem = arrayList2.get(position);
                                        String str = getItem.substring(0,getItem.length()-7);
                                        String getFoodName = captureName2(str);

                                        //remove food name from database
                                        deleteLunchData(getFoodName);

                                        // Remove item from the ArrayList
                                        arrayList2.remove(position);

                                        // Notify listView adapter to update the list
                                        arrayAdapter2.notifyDataSetChanged();

                                    }
                                })
                        .setNegativeButton(R.string.cancel_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true; }
        });

        listView_dinner.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId)
            {
                Log.i("MyFoodList", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyFoodList.this);
                builder.setTitle(R.string.dialog_delete_title_2)
                        .setMessage(R.string.dialog_delete_msg_2)
                        .setPositiveButton(R.string.delete_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //get click position food name
                                        String getItem = arrayList3.get(position);
                                        String str = getItem.substring(0,getItem.length()-7);
                                        String getFoodName = captureName2(str);

                                        //remove food name from database
                                        deleteDinnerData(getFoodName);

                                        // Remove item from the ArrayList
                                        arrayList3.remove(position);

                                        // Notify listView adapter to update the list
                                        arrayAdapter3.notifyDataSetChanged();

                                    }
                                })
                        .setNegativeButton(R.string.cancel_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true; }
        });

        listView_other.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long rowId)
            {
                Log.i("MyFoodList", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MyFoodList.this);
                builder.setTitle(R.string.dialog_delete_title_2)
                        .setMessage(R.string.dialog_delete_msg_2)
                        .setPositiveButton(R.string.delete_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //get click position food name
                                        String getItem = arrayList4.get(position);
                                        String str = getItem.substring(0,getItem.length()-7);
                                        String getFoodName = captureName2(str);

                                        //remove food name from database
                                        deleteOtherData(getFoodName);

                                        // Remove item from the ArrayList
                                        arrayList4.remove(position);

                                        // Notify listView adapter to update the list
                                        arrayAdapter4.notifyDataSetChanged();

                                    }
                                })
                        .setNegativeButton(R.string.cancel_2, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // User cancelled the dialog
                                        // Nothing happens
                                    }
                                });
                builder.create().show();
                return true; }
        });

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

    public void deleteBreakfastData(final String foodName){
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
                                if (foodName.equals(data.child("foodname").getValue(String.class))) {
                                    data.getRef().removeValue();
                                }
                                
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

    public void deleteLunchData(final String foodName){
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
                                if (foodName.equals(data.child("foodname").getValue(String.class))) {
                                    data.getRef().removeValue();
                                }

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

    public void deleteDinnerData(final String foodName){
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
                                if (foodName.equals(data.child("foodname").getValue(String.class))) {
                                    data.getRef().removeValue();
                                }

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

    public void deleteOtherData(final String foodName){
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
                                if (foodName.equals(data.child("foodname").getValue(String.class))) {
                                    data.getRef().removeValue();
                                }

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

    //Lower case the first letter
    public static String captureName2(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return  name;

    }


}
