package com.example.letstravel.User.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.letstravel.R;

public class BookTripActivityUser extends AppCompatActivity {

    String placeSection, placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip_user);

        placeId = getIntent().getStringExtra("placeId");
        placeSection = getIntent().getStringExtra("placeSection");

    }
}