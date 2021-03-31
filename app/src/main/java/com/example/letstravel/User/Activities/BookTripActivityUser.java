package com.example.letstravel.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letstravel.Admin.Activities.AddPlaceActivity;
import com.example.letstravel.LoginActivity;
import com.example.letstravel.MainActivity;
import com.example.letstravel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;

public class BookTripActivityUser extends AppCompatActivity {

    String placeSection, placeId;
    private ImageView backIv;
    private EditText NameEt, PhoneEt, EmailEt, no_ofPeopleEt;
    private Button nextStepBtn;

    private EditText NameEt1, age1, NameEt2, age2, NameEt3, age3, NameEt4, age4, NameEt5, age5;
    private Button prevLayout, submitBtn, confirmSuccessButton;

    private ConstraintLayout layout_book_trip_1, layout_book_trip_2;

    private ProgressDialog pd;
    String mUID = "uid";
    FirebaseAuth firebaseAuth;
    private String placeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip_user);

        placeId = getIntent().getStringExtra("placeId");
        placeSection = getIntent().getStringExtra("placeSection");

        backIv = findViewById(R.id.backIv);
        NameEt = findViewById(R.id.NameEt);
        PhoneEt = findViewById(R.id.PhoneEt);
        EmailEt = findViewById(R.id.EmailEt);
        no_ofPeopleEt = findViewById(R.id.no_ofPeopleEt);
        nextStepBtn = findViewById(R.id.nextStepBtn);

        pd = new ProgressDialog(this);
        pd.setTitle("Please Wait");
        pd.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();

        NameEt1 = findViewById(R.id.NameEt1);
        age1 = findViewById(R.id.age1);
        NameEt2 = findViewById(R.id.NameEt2);
        age2 = findViewById(R.id.age2);
        NameEt3 = findViewById(R.id.NameEt3);
        age3 = findViewById(R.id.age3);
        NameEt4 = findViewById(R.id.NameEt4);
        age4 = findViewById(R.id.age4);
        NameEt5 = findViewById(R.id.NameEt5);
        age5 = findViewById(R.id.age5);
        prevLayout = findViewById(R.id.prevLayout);
        submitBtn = findViewById(R.id.submitBtn);


        layout_book_trip_1 = findViewById(R.id.layout_book_trip_1);
        layout_book_trip_2 = findViewById(R.id.layout_book_trip_2);

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout2();
            }
        });

        prevLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

        layout1();
        loadUserData();


    }


    private void layout1() {
        layout_book_trip_1.setVisibility(View.VISIBLE);
        layout_book_trip_2.setVisibility(View.GONE);

    }

    private void layout2() {
        layout_book_trip_1.setVisibility(GONE);
        layout_book_trip_2.setVisibility(View.VISIBLE);
    }

    String name, phone, email, no_ofPeople;
    String pName1, pName2, pName3, pName4, pName5, pAge1, pAge2, pAge3, pAge4, pAge5;

    private void inputData() {

        name = NameEt.getText().toString().trim();
        phone = PhoneEt.getText().toString().trim();
        email = EmailEt.getText().toString().trim();
        no_ofPeople = no_ofPeopleEt.getText().toString().trim();

        pName1 = NameEt1.getText().toString().trim();
        pAge1 = age1.getText().toString().trim();
        pName2 = NameEt2.getText().toString().trim();
        pAge2 = age2.getText().toString().trim();
        pName3 = NameEt3.getText().toString().trim();
        pAge3 = age3.getText().toString().trim();
        pName4 = NameEt4.getText().toString().trim();
        pAge4 = age4.getText().toString().trim();
        pName5 = NameEt5.getText().toString().trim();
        pAge5 = age5.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(BookTripActivityUser.this, " Name is required...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(BookTripActivityUser.this, "Please Enter your Phone No....", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(BookTripActivityUser.this, "Please enter email...", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(no_ofPeople)) {
            Toast.makeText(BookTripActivityUser.this, "Enter no of people involved in trip...", Toast.LENGTH_SHORT).show();
            return;
        }

        saveTripToDb();

    }


    private void saveTripToDb() {

        final String timeStamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("bookedByName", "" + name);
        hashMap.put("bookedByPhone", "" + phone);
        hashMap.put("bookedByEmail", "" + email);
        hashMap.put("bookedByNoOfPeople", "" + no_ofPeople);
        hashMap.put("timeStamp", "" + timeStamp);
        hashMap.put("tripDestination", "" + placeId);
        hashMap.put("placeSection", ""+placeSection);

        hashMap.put("person1Name", "" + pName1);
        hashMap.put("person2Name", "" + pName2);
        hashMap.put("person3Name", "" + pName3);
        hashMap.put("person4Name", "" + pName4);
        hashMap.put("person5Name", "" + pName5);
        hashMap.put("person1Age", "" + pAge1);
        hashMap.put("person2Age", "" + pAge2);
        hashMap.put("person3Age", "" + pAge3);
        hashMap.put("person4Age", "" + pAge4);
        hashMap.put("person5Age", "" + pAge5);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TripBookings");
        ref.child(timeStamp)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                LayoutInflater inflater = getLayoutInflater();
                                View view = inflater.inflate(R.layout.dialog_trip_book_successful,
                                        (ViewGroup)findViewById(R.id.relativeLayout1));

                                Toast toast = new Toast(BookTripActivityUser.this);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.setView(view);
                                toast.show();

                            }
                        }, 3000);

                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(BookTripActivityUser.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        //onBackPressed();
                    }
                });

    }


    private void loadUserData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = "" + ds.child("name").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String deliveryAddress = "" + ds.child("address").getValue();
                            String profileImage = "" + ds.child("image").getValue();

                            NameEt.setText(name);
                            EmailEt.setText(email);
                            PhoneEt.setText(phone);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }





}