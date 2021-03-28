package com.example.letstravel.Admin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.letstravel.Adapters.AdapterTripPlaces;
import com.example.letstravel.Admin.Models.ModelTripPlaces;
import com.example.letstravel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFavPlacesActivity extends AppCompatActivity {

    FloatingActionButton addPlaceFab;
    RecyclerView userFavPlacesRv;

    String placeSection = "UserFavPlaces";

    private ArrayList<ModelTripPlaces> tripPlacesArrayList;
    private AdapterTripPlaces adapterTripPlaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav_places);

        userFavPlacesRv = findViewById(R.id.userFavPlacesRv);
        addPlaceFab = findViewById(R.id.addPlaceFab);

        addPlaceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserFavPlacesActivity.this, AddPlaceActivity.class));
                Intent intent = new Intent(UserFavPlacesActivity.this, AddPlaceActivity.class);
                intent.putExtra("placeSection", placeSection);
                startActivity(intent);
            }
        });

        loadFavPlaces();

    }

    private void loadFavPlaces() {

        tripPlacesArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(placeSection)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tripPlacesArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelTripPlaces modelOngoingEvent = ds.getValue(ModelTripPlaces.class);
                            tripPlacesArrayList.add(modelOngoingEvent);
                        }
                        adapterTripPlaces = new AdapterTripPlaces(UserFavPlacesActivity.this, tripPlacesArrayList, placeSection);
                        userFavPlacesRv.setLayoutManager(new GridLayoutManager(UserFavPlacesActivity.this, 2));
                        userFavPlacesRv.setAdapter(adapterTripPlaces);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}
