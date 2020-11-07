package comp5216.sydney.edu.au.assignment2.loginFirstTimeUserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import comp5216.sydney.edu.au.assignment2.R;
import comp5216.sydney.edu.au.assignment2.main.*;

public class InfoActivity_2 extends AppCompatActivity {

    public static String uid;

    private EditText Age,Weight;
    public String weight,height,bmi,age;//String values for database storage

    DatabaseReference databaseReference ,databaseSetTrue;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_2);

        //define button
        final Button startBtn=findViewById(R.id.btn_birth_to_main);

        //Get birthdays & weight
        Age = (EditText)findViewById(R.id.input_user_age);
        Weight = (EditText)findViewById(R.id.input_user_weight);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userInfoAdd2();
            }
        });
    }

    public void userInfoAdd2(){
        weight = Weight.getText().toString();
        age = Age.getText().toString();

        //if User input is not null
        if(age.isEmpty()){
            Age.setError("Birthday is required");
            Age.requestFocus();
            return;
        }

        if(weight.isEmpty()){
            Weight.setError("Weight is required");
            Weight.requestFocus();
            return;
        }

        userInfoAdd_toDatabase2();

    }

    public void userInfoAdd_toDatabase2(){

        //get userID
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child("Users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot d: snapshot.getChildren()){
                            //d.getKey() is the key of userInfo
                            final String userInfo_Key = d.getKey();

                            for(DataSnapshot dd: d.getChildren()){
                                final String dd_Key = dd.getKey();

                                if(dd_Key.equals("age")) {
                                    //Write the Birthday to the database
                                    databaseReference.child("Users").child(uid).child(userInfo_Key)
                                            .child("age").setValue(age);
                                }
                                if(dd_Key.equals("weight")) {
                                    //Write Weight to the database
                                    databaseReference.child("Users").child(uid).child(userInfo_Key)
                                            .child("weight").setValue(weight);
                                }
                                if(dd_Key.equals("bmi")) {
                                    //Write BMI into the database
                                    //First get height- then calculate BMI - then write BMI to the database
                                    databaseReference.child("Users").child(uid).child(userInfo_Key)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ddd: dataSnapshot.getChildren()) {

                                                String d_Key = ddd.getKey();
                                                if (d_Key.equals("height")) {
                                                    height = ddd.getValue().toString();

                                                    //calculate BMI
                                                    //String converts a Double and retains 2 decimal places:
                                                    DecimalFormat df = new DecimalFormat("0.00");

                                                    //Convert the String of weight height to a double
                                                    double d_weight = Double.parseDouble(weight);
                                                    weight = df.format(d_weight);

                                                    double d_height = Double.parseDouble(height);
                                                    d_height = d_height/100;

                                                    //calculate BMI
                                                    //BMI = (kg) / (m)x(m)
                                                    double d_bmi = d_weight / (d_height*d_height);
                                                    bmi =df.format(d_bmi); //format Returns a string

                                                    //Write BMI into the database
                                                    databaseReference.child("Users").child(uid)
                                                            .child(userInfo_Key).child("bmi").setValue(bmi);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                            }
                        }
                        // Write data from database/update data
                        // to notFirstTime update of a database to be "true"
                        databaseSetTrue = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        databaseSetTrue.child("notFirstTime").setValue("true")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(InfoActivity_2.this, MainActivity.class);
                                        InfoActivity_2.this.startActivity(intent);
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}