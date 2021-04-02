package com.example.letstravel.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letstravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PlaceDescriptionActivityUser extends AppCompatActivity {

    private ImageView placeIv, backIv, ratingIv;
    private TextView placeNameTv, ratingTv, placeAvailablityTv, priceTv, aboutTv;
    private Button bookTripBtn ;
    String placeSection, placeId;

    private ProgressDialog pd;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description_user);

        placeId = getIntent().getStringExtra("placeId");
        placeSection = getIntent().getStringExtra("placeSection");

        placeIv = findViewById(R.id.placeIv);
        backIv = findViewById(R.id.backIv);
        ratingIv = findViewById(R.id.ratingIv);
        bookTripBtn = findViewById(R.id.bookTripBtn);

        pd  = new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setCanceledOnTouchOutside(false);

        bookTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaceDescriptionActivityUser.this, BookTripActivityUser.class);
                intent.putExtra("placeId", placeId);
                intent.putExtra("placeSection",placeSection);
                startActivity(intent);
            }
        });

        placeNameTv = findViewById(R.id.placeNameTv);
        ratingTv = findViewById(R.id.ratingTv);
        placeAvailablityTv = findViewById(R.id.placeAvailablityTv);
        priceTv = findViewById(R.id.priceTv);
        aboutTv = findViewById(R.id.aboutTv);

        loadPlaceDescription();
    }

    private void loadPlaceDescription() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(placeSection).child(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String placeName = "" + snapshot.child("placeName").getValue();
                String placeDescription = "" + snapshot.child("placeDescription").getValue();
                String eventOrganizationName = "" + snapshot.child("countryName").getValue();
                String placeAvailablity = "" + snapshot.child("placeAvailablity").getValue();
                String price = "" + snapshot.child("price").getValue();
                String eventLocation = "" + snapshot.child("eventLocation").getValue();
                String eventImage = "" + snapshot.child("placeImage").getValue();

                placeNameTv.setText(placeName);
                aboutTv.setText("                "+placeDescription);
                placeAvailablityTv.setText(placeAvailablity);
                priceTv.setText(price);
                ratingTv.setText("4.5");

//                try {
//                    Picasso.get().load(eventImage).into(placeIv);
//                } catch (Exception e) {
//
//                }

                try {
                    Picasso.get().load(eventImage).placeholder(R.drawable.admin_person_bg).into(placeIv);
                } catch (Exception e) {
                    placeIv.setImageResource(R.drawable.admin_person_bg);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}