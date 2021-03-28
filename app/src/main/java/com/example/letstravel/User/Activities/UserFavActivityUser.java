package com.example.letstravel.User.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.letstravel.Adapters.AdapterTripPlaces;
import com.example.letstravel.Admin.Activities.UserFavPlacesActivity;
import com.example.letstravel.Admin.Models.ModelTripPlaces;
import com.example.letstravel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserFavActivityUser extends AppCompatActivity {

    RecyclerView userFavPlacesRv;
    EditText searchBarEt;
    String placeSection = "UserFavPlaces";

    private ArrayList<ModelTripPlaces> tripPlacesArrayList;
    private AdapterTripPlaces adapterTripPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav_user);

        userFavPlacesRv = findViewById(R.id.userFavPlacesRv);

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
                        adapterTripPlaces = new AdapterTripPlaces(UserFavActivityUser.this, tripPlacesArrayList, placeSection);
                        userFavPlacesRv.setLayoutManager(new GridLayoutManager(UserFavActivityUser.this, 2));
                        userFavPlacesRv.setAdapter(adapterTripPlaces);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}