package com.example.letstravel.Admin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstravel.Admin.ModelTopPlaces;
import com.example.letstravel.Admin.PlaceDescriptionActivity;
import com.example.letstravel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterTopPlace extends RecyclerView.Adapter<AdapterTopPlace.HolderTopPlace>{

    private Context context;
    public ArrayList<ModelTopPlaces> topPlacesArrayList;
    private String placeSection;

    public AdapterTopPlace(Context context, ArrayList<ModelTopPlaces> topPlacesArrayList, String placeSection) {
        this.context = context;
        this.topPlacesArrayList = topPlacesArrayList;
        this.placeSection = placeSection;
    }

    @NonNull
    @Override
    public HolderTopPlace onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recents_row_item, parent, false);

        return new HolderTopPlace(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderTopPlace holder, int position) {

        ModelTopPlaces modelTopPlaces = topPlacesArrayList.get(position);
        String placeName = modelTopPlaces.getPlaceName();
        String placeDescription = modelTopPlaces.getPlaceDescription();
        String countryName = modelTopPlaces.getCountryName();
        String price = modelTopPlaces.getPrice();
        String placeIv = modelTopPlaces.getPlaceImage();
        String placeId = modelTopPlaces.getTimeStamp();

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