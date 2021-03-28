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

import com.example.letstravel.Filters.FilterFacPlaces;
import com.example.letstravel.Admin.Models.ModelFavPlaces;
import com.example.letstravel.Admin.Activities.PlaceDescriptionActivity;
import com.example.letstravel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterFavPlaces extends RecyclerView.Adapter<AdapterFavPlaces.HolderFavPlace> implements Filterable {

    private Context context;
    public ArrayList<ModelFavPlaces> topPlacesArrayList, filterList;
    private String placeSection;
    private FilterFacPlaces filter;

    public AdapterFavPlaces(Context context, ArrayList<ModelFavPlaces> topPlacesArrayList, String placeSection) {
        this.context = context;
        this.topPlacesArrayList = topPlacesArrayList;
        this.filterList = topPlacesArrayList;
        this.placeSection = placeSection;
    }

    @NonNull
    @Override
    public HolderFavPlace onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_places_row_item, parent, false);

        return new HolderFavPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderFavPlace holder, int position) {

        ModelFavPlaces modelTripPlaces = topPlacesArrayList.get(position);
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
                Intent intent = new Intent(context, PlaceDescriptionActivity.class);
                intent.putExtra("placeId", placeId);
                intent.putExtra("placeSection",placeSection);
                context.startActivity(intent);
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
            filter = new FilterFacPlaces(this, filterList);
        }
        return filter;
    }

//    @Override
//    public Filter getFilter() {
//        if (filter == null) {
//            filter = new FilterTrips(this, filterList);
//        }
//        return filter;
//    }

    class HolderFavPlace extends RecyclerView.ViewHolder {

        private ImageView placeIv;
        private TextView placeNameTv, countryNameTv, priceTv;

        public HolderFavPlace(@NonNull View itemView) {
            super(itemView);

            placeNameTv = itemView.findViewById(R.id.placeNameTv);
            placeIv = itemView.findViewById(R.id.placeIv);
            countryNameTv = itemView.findViewById(R.id.countryNameTv);
            priceTv = itemView.findViewById(R.id.priceTv);

        }
    }


}
