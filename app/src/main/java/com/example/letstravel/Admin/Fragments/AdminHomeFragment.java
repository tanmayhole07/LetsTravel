package com.example.letstravel.Admin.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.letstravel.Admin.Adapters.AdapterFavPlaces;
import com.example.letstravel.Admin.Adapters.AdapterPopularPlaces;
import com.example.letstravel.Admin.Models.ModelFavPlaces;
import com.example.letstravel.Admin.Models.ModelTripPlaces;
import com.example.letstravel.Admin.Activities.TopPlacesActivity;
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
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
    private ArrayList<ModelTripPlaces> tripPlacesArrayList;
    private AdapterPopularPlaces adapterPopularPlaces;


    private  ArrayList<ModelFavPlaces> favPlacesArrayList;
    private AdapterFavPlaces adapterFavPlaces;

    private TextView popularPlaceTitleTv, seeAllTopPlacesTv, userFavTitleTv, seeAllUserFavPlacesTv;
    private RecyclerView popularPlaceRv, userFavRv;
    private EditText searchBarEt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        popularPlaceTitleTv = view.findViewById(R.id.popularPlaceTitleTv);
        seeAllTopPlacesTv = view.findViewById(R.id.seeAllTopPlacesTv);
        userFavTitleTv = view.findViewById(R.id.userFavTitleTv);
        seeAllUserFavPlacesTv = view.findViewById(R.id.seeAllUserFavPlacesTv);
        popularPlaceRv = view.findViewById(R.id.popularPlaceRv);
        userFavRv = view.findViewById(R.id.userFavRv);
        searchBarEt = view.findViewById(R.id.searchBarEt);

        searchBarEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try {
                    adapterPopularPlaces.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    adapterFavPlaces.getFilter().filter(s);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        seeAllTopPlacesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopPlacesActivity.class));
            }
        });

        seeAllUserFavPlacesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadTopPlaces();
        loadFavPlaces();

        return view;
    }

    private void loadTopPlaces() {

        tripPlacesArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child("TopPlaces")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tripPlacesArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelTripPlaces modelOngoingEvent = ds.getValue(ModelTripPlaces.class);
                            Collections.shuffle(tripPlacesArrayList);
                            tripPlacesArrayList.add(modelOngoingEvent);
                        }
                        adapterPopularPlaces = new AdapterPopularPlaces(getActivity(), tripPlacesArrayList, "TopPlaces");
//                        recentRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        popularPlaceRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                        popularPlaceRv.setAdapter(adapterPopularPlaces);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void loadFavPlaces() {

        favPlacesArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child("UserFavPlaces")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        favPlacesArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ModelFavPlaces modelFavPlaces = ds.getValue(ModelFavPlaces.class);
                            Collections.shuffle(favPlacesArrayList);
                            favPlacesArrayList.add(modelFavPlaces);
                        }
                        adapterFavPlaces = new AdapterFavPlaces(getActivity(), favPlacesArrayList, "UserFavPlaces");
                        userFavRv.setLayoutManager(new LinearLayoutManager(getActivity()));

                        userFavRv.setAdapter(adapterFavPlaces);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}