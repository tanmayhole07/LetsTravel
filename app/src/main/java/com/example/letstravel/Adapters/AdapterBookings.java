package com.example.letstravel.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.Admin.Activities.BookingDescriptionActivity;
import com.example.letstravel.Admin.Activities.PlaceDescriptionActivity;
import com.example.letstravel.Admin.Models.ModelBooking;
import com.example.letstravel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterBookings extends RecyclerView.Adapter<AdapterBookings.HolderBooking> {

    private Context context;
    public ArrayList<ModelBooking> bookingArrayList;
    FirebaseAuth firebaseAuth;

    public AdapterBookings(Context context, ArrayList<ModelBooking> bookingArrayList) {
        this.context = context;
        this.bookingArrayList = bookingArrayList;
    }

    @NonNull
    @Override
    public HolderBooking onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_bookings, parent, false);
        firebaseAuth = FirebaseAuth.getInstance();
        return new HolderBooking(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBooking holder, int position) {

        ModelBooking modelBooking = bookingArrayList.get(position);
        String bookerName = modelBooking.getBookedByName();
        String bookerEmail = modelBooking.getBookedByEmail();
        String bookerPhone = modelBooking.getBookedByPhone();
        String bookedByNoOfPeople= modelBooking.getBookedByNoOfPeople();
        String placeSection = modelBooking.getPlaceSection();
        String placeId = modelBooking.getTripDestination();
        String timestamp = modelBooking.getTimeStamp();


        holder.bookerNameTv.setText(bookerName);
        holder.bookerEmailTv.setText(bookerEmail);
        holder.bookerPhoneTv.setText(bookerPhone);
        holder.totalNoOfBookingsTv.setText(bookedByNoOfPeople);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Events");
        ref.child(placeSection).child(placeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String placeName = "" + snapshot.child("placeName").getValue();

                holder.tripNameTv.setText(placeName);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BookingDescriptionActivity.class);
                intent.putExtra("placeId", placeId);
                intent.putExtra("placeSection",placeSection);
                intent.putExtra("timestamp",timestamp);
                context.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }

    class HolderBooking extends RecyclerView.ViewHolder {

        private TextView bookerNameTv, bookerEmailTv, bookerPhoneTv, totalNoOfBookingsTv, tripNameTv;

        public HolderBooking(@NonNull View itemView) {
            super(itemView);

            bookerNameTv = itemView.findViewById(R.id.bookerName);
            bookerEmailTv = itemView.findViewById(R.id.bookerEmail);
            bookerPhoneTv = itemView.findViewById(R.id.bookerPhone);
            totalNoOfBookingsTv = itemView.findViewById(R.id.totalNoOfBookings);
            tripNameTv = itemView.findViewById(R.id.tripName);

        }
    }

}
