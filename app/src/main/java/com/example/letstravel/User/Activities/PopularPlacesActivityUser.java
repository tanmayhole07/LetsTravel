package com.example.letstravel.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.letstravel.Adapters.AdapterTripPlaces;
import com.example.letstravel.Admin.Activities.AddPlaceActivity;
import com.example.letstravel.Admin.Activities.TopPlacesActivity;
import com.example.letstravel.Admin.Models.ModelTripPlaces;
import com.example.letstravel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PopularPlacesActivityUser extends AppCompatActivity {

    FloatingActionButton addPlaceFab;
    RecyclerView recentPlacesRv;
    EditText searchBarEt;
    String placeSection = "TopPlaces";

    private ArrayList<ModelTripPlaces> tripPlacesArrayList;
    private AdapterTripPlaces adapterTripPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_places_user);

        recentPlacesRv = findViewById(R.id.recentPlacesRv);

        searchBarEt = findViewById(R.id.searchBarEt);

        searchBarEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try {
                    adapterTripPlaces.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadTopPlaces();
    }

    private void loadTopPlaces() {

        tripPlacesArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(placeSection)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tripPlacesArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelTripPlaces modelOngoingEvent = ds.getValue(ModelTripPlaces.class);
                            tripPlacesArrayList.add(modelOngoingEvent);
                        }
                        adapterTripPlaces = new AdapterTripPlaces(PopularPlacesActivityUser.this, tripPlacesArrayList, placeSection);
                        recentPlacesRv.setLayoutManager(new GridLayoutManager(PopularPlacesActivityUser.this, 2));
                        recentPlacesRv.setAdapter(adapterTripPlaces);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}