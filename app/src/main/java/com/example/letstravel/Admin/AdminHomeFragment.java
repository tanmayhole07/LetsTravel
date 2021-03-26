package com.example.letstravel.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.letstravel.Admin.Adapters.AdapterTopPlace;
import com.example.letstravel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    private ArrayList<ModelTopPlaces> topPlacesArrayList;
    private AdapterTopPlace adapterTopPlace;

    private TextView recentTitleTv, seeAllRecentTv, topPlacesTitleTv, seeAllTopPlacesTv;
    private RecyclerView recentRv, top_placesRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        recentTitleTv = view.findViewById(R.id.recentTitleTv);
        seeAllRecentTv = view.findViewById(R.id.seeAllRecentTv);
        topPlacesTitleTv = view.findViewById(R.id.topPlacesTitleTv);
        seeAllTopPlacesTv = view.findViewById(R.id.seeAllTopPlacesTv);
        recentRv = view.findViewById(R.id.recentRv);
        top_placesRv = view.findViewById(R.id.top_placesRv);

        seeAllRecentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TopPlacesActivity.class));
            }
        });

        loadTopPlaces();

        return view;
    }

    private void loadTopPlaces() {

        topPlacesArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child("TopPlaces")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        topPlacesArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ModelTopPlaces modelOngoingEvent = ds.getValue(ModelTopPlaces.class);
                            topPlacesArrayList.add(modelOngoingEvent);
                        }
                        adapterTopPlace = new AdapterTopPlace(getActivity(), topPlacesArrayList, "TopPlaces");
//                        recentRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                        recentRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                        recentRv.setAdapter(adapterTopPlace);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}