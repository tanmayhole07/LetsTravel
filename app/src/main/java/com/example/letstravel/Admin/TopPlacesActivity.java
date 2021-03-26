package com.example.letstravel.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.letstravel.Admin.Adapters.AdapterTopPlace;
import com.example.letstravel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopPlacesActivity extends AppCompatActivity {

    FloatingActionButton addPlaceFab;
    RecyclerView recentPlacesRv;

    String placeSection = "TopPlaces";

    private ArrayList<ModelTopPlaces> topPlacesArrayList;
    private AdapterTopPlace adapterTopPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_places);

        recentPlacesRv = findViewById(R.id.recentPlacesRv);
        addPlaceFab = findViewById(R.id.addPlaceFab);

        addPlaceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TopPlacesActivity.this, AddPlaceActivity.class));
                Intent intent = new Intent(TopPlacesActivity.this, AddPlaceActivity.class);
                intent.putExtra("placeSection",placeSection);
                startActivity(intent);
            }
        });

        loadTopPlaces();
    }

    private void loadTopPlaces() {

        topPlacesArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(placeSection)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        topPlacesArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelTopPlaces modelOngoingEvent = ds.getValue(ModelTopPlaces.class);
                            topPlacesArrayList.add(modelOngoingEvent);
                        }
                        adapterTopPlace = new AdapterTopPlace(TopPlacesActivity.this, topPlacesArrayList, placeSection);
                        recentPlacesRv.setLayoutManager(new GridLayoutManager(TopPlacesActivity.this, 2));
                        recentPlacesRv.setAdapter(adapterTopPlace);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}