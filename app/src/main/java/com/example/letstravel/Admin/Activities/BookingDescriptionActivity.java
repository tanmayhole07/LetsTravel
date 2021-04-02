package com.example.letstravel.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.TextView;

import com.example.letstravel.Adapters.AdapterBookings;
import com.example.letstravel.Admin.Models.ModelBooking;
import com.example.letstravel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class BookingDescriptionActivity extends AppCompatActivity {

    String placeSection, placeId, timestamp;
    private TextView nameTv1, ageTv1, nameTv2, ageTv2, nameTv3, ageTv3, nameTv4, ageTv4, nameTv5, ageTv5;

    private ArrayList<ModelBooking> bookingArrayList;

    private AdapterBookings adapterBookings;

    String p1name, p2name, p3name, p4name, p5name, p1age, p2age, p3age, p4age, p5age;
    String bookerName, bookerEmail, bookerPhone, bookedByNoOfPeople;

    private TextView bookerNameTv, bookerEmailTv, bookerPhoneTv, totalNoOfBookingsTv, tripNameTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_description);

        placeId = getIntent().getStringExtra("placeId");
        placeSection = getIntent().getStringExtra("placeSection");
        timestamp = getIntent().getStringExtra("timestamp");

        bookerNameTv =  findViewById(R.id.bookerName);
        bookerEmailTv = findViewById(R.id.bookerEmail);
        bookerPhoneTv = findViewById(R.id.bookerPhone);
        totalNoOfBookingsTv = findViewById(R.id.totalNoOfBookings);
        tripNameTv = findViewById(R.id.tripName);

        nameTv1 = findViewById(R.id.nameTv1);
        ageTv1 = findViewById(R.id.ageTv1);
        nameTv2 = findViewById(R.id.nameTv2);
        ageTv2 = findViewById(R.id.ageTv2);
        nameTv3 = findViewById(R.id.nameTv3);
        ageTv3 = findViewById(R.id.ageTv3);
        nameTv4 = findViewById(R.id.nameTv4);
        ageTv4 = findViewById(R.id.ageTv4);
        nameTv5 = findViewById(R.id.nameTv5);
        ageTv5 = findViewById(R.id.ageTv5);

        loadBookingDetails();

        loadTripName();


    }

    private void loadTripName() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(placeSection).child(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String placeName = "" + snapshot.child("placeName").getValue();

                tripNameTv.setText(placeName);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadBookingDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TripBookings");
        ref.child(timestamp).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                p1name = "" + snapshot.child("person1Name").getValue();
                p2name = "" + snapshot.child("person2Name").getValue();
                p3name = "" + snapshot.child("person3Name").getValue();
                p4name = "" + snapshot.child("person4Name").getValue();
                p5name = "" + snapshot.child("person5Name").getValue();
                p1age = "" + snapshot.child("person1Age").getValue();
                p2age = "" + snapshot.child("person2Age").getValue();
                p3age = "" + snapshot.child("person3Age").getValue();
                p4age = "" + snapshot.child("person4Age").getValue();
                p5age = "" + snapshot.child("person5Age").getValue();

                bookerName = "" + snapshot.child("bookedByName").getValue();
                bookerEmail = "" + snapshot.child("bookedByEmail").getValue();
                bookerPhone = "" + snapshot.child("bookedByPhone").getValue();
                bookedByNoOfPeople = "" + snapshot.child("bookedByNoOfPeople").getValue();

                nameTv1.setText(p1name);
                nameTv2.setText(p2name);
                nameTv3.setText(p3name);
                nameTv4.setText(p4name);
                nameTv5.setText(p5name);
                ageTv1.setText(p1age);
                ageTv2.setText(p2age);
                ageTv3.setText(p3age);
                ageTv4.setText(p4age);
                ageTv5.setText(p5age);

                bookerNameTv.setText(bookerName);
                bookerEmailTv.setText(bookerEmail);
                bookerPhoneTv.setText(bookerPhone);
                totalNoOfBookingsTv.setText(bookedByNoOfPeople);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}