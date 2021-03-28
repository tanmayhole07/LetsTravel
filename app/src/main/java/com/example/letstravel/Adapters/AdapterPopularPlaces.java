package com.example.letstravel.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.Filters.FilterPopularPlaces;
import com.example.letstravel.Admin.Models.ModelTripPlaces;
import com.example.letstravel.Admin.Activities.PlaceDescriptionActivity;
import com.example.letstravel.R;
import com.example.letstravel.User.Activities.PlaceDescriptionActivityUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPopularPlaces extends RecyclerView.Adapter<AdapterPopularPlaces.HolderTopPlace> implements Filterable {

    private Context context;
    public ArrayList<ModelTripPlaces> topPlacesArrayList, filterList;
    private String placeSection;
    private FilterPopularPlaces filter;

    FirebaseAuth firebaseAuth;

    public AdapterPopularPlaces(Context context, ArrayList<ModelTripPlaces> topPlacesArrayList, String placeSection) {
        this.context = context;
        this.topPlacesArrayList = topPlacesArrayList;
        this.filterList = topPlacesArrayList;
        this.placeSection = placeSection;
    }

    @NonNull
    @Override
    public AdapterPopularPlaces.HolderTopPlace onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_popular_places, parent, false);
        firebaseAuth = FirebaseAuth.getInstance();
        return new AdapterPopularPlaces.HolderTopPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPopularPlaces.HolderTopPlace holder, int position) {

        ModelTripPlaces modelTripPlaces = topPlacesArrayList.get(position);
        String placeName = modelTripPlaces.getPlaceName();
        String placeDescription = modelTripPlaces.getPlaceDescription();
        String countryName = modelTripPlaces.getCountryName();
        String price = modelTripPlaces.getPrice();
        String placeIv = modelTripPlaces.getPlaceImage();
        String placeId = modelTripPlaces.getTimeStamp();

        holder.placeNameTv.setText(placeName);
        holder.countryNameTv.setText(countryName);
        holder.priceTv.setText(price);

        try {
            Picasso.get().load(placeIv).placeholder(R.drawable.ic_photo_grey).into(holder.placeIv);
        } catch (Exception e) {
            holder.placeIv.setImageResource(R.drawable.ic_photo_grey);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    String accountTye = ""+ds.child("accountTye").getValue();
                                    if (accountTye.equals("Admin")){
                                        Intent intent = new Intent(context, PlaceDescriptionActivity.class);
                                        intent.putExtra("placeId", placeId);
                                        intent.putExtra("placeSection",placeSection);
                                        context.startActivity(intent);
                                    }
                                    else {
                                        Intent intent = new Intent(context, PlaceDescriptionActivityUser.class);
                                        intent.putExtra("placeId", placeId);
                                        intent.putExtra("placeSection",placeSection);
                                        context.startActivity(intent);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return topPlacesArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterPopularPlaces(this, filterList);
        }
        return filter;
    }

    class HolderTopPlace extends RecyclerView.ViewHolder {

        private ImageView placeIv;
        private TextView placeNameTv, countryNameTv, priceTv;

        public HolderTopPlace(@NonNull View itemView) {
            super(itemView);

            placeNameTv = itemView.findViewById(R.id.placeNameTv);
            placeIv = itemView.findViewById(R.id.placeIv);
            countryNameTv = itemView.findViewById(R.id.countryNameTv);
            priceTv = itemView.findViewById(R.id.priceTv);

        }
    }

}
