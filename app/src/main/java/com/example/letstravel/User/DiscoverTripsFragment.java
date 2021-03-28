package com.example.letstravel.User;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.letstravel.Adapters.AdapterTripPlaces;
import com.example.letstravel.Admin.Models.ModelTripPlaces;
import com.example.letstravel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverTripsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverTripsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoverTripsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverTripsFragment newInstance(String param1, String param2) {
        DiscoverTripsFragment fragment = new DiscoverTripsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView tripsRv;
    EditText searchBarEt;
    String placeSection = "Trips";

    private ArrayList<ModelTripPlaces> tripPlacesArrayList;
    private AdapterTripPlaces adapterTripPlaces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_trips, container, false);

        tripsRv = view.findViewById(R.id.tripsRv);
        searchBarEt = view.findViewById(R.id.searchBarEt);

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

        loadAllTrips();

        return view;
    }

    private void loadAllTrips() {

        try {

            tripPlacesArrayList = new ArrayList<>();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
            ref.child(placeSection)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            tripPlacesArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()){
                                ModelTripPlaces modelTripPlaces = ds.getValue(ModelTripPlaces.class);
                                Collections.shuffle(tripPlacesArrayList);
                                tripPlacesArrayList.add(modelTripPlaces);
                            }
                            adapterTripPlaces = new AdapterTripPlaces(getActivity(), tripPlacesArrayList, placeSection);
                            tripsRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            tripsRv.setAdapter(adapterTripPlaces);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }catch(Exception e){

        }



    }

}