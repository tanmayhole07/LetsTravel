package com.example.letstravel.Admin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letstravel.Adapters.AdapterBookings;
import com.example.letstravel.Adapters.AdapterTripPlaces;
import com.example.letstravel.Admin.Models.ModelBooking;
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
 * Use the {@link AdminBookingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminBookingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminBookingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminBookingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminBookingsFragment newInstance(String param1, String param2) {
        AdminBookingsFragment fragment = new AdminBookingsFragment();
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

    private ArrayList<ModelBooking> bookingArrayList;
    private AdapterBookings adapterBookings;

    RecyclerView bookingRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_bookings, container, false);

        bookingRv = view.findViewById(R.id.bookingRv);

        loadAllTrips();
        return view;
    }

    private void loadAllTrips() {

        try {

            bookingArrayList = new ArrayList<>();

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("TripBookings");
            ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            bookingArrayList.clear();
                            for (DataSnapshot ds : snapshot.getChildren()){
                                ModelBooking modelBooking = ds.getValue(ModelBooking.class);
                                Collections.shuffle(bookingArrayList);
                                bookingArrayList.add(modelBooking);
                            }
                            adapterBookings = new AdapterBookings(getActivity(), bookingArrayList);
                            bookingRv.setLayoutManager(new LinearLayoutManager(getActivity()));
                            bookingRv.setAdapter(adapterBookings);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }catch(Exception e){

        }



    }
}