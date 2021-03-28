package com.example.letstravel.Filters;

import android.widget.Filter;

import com.example.letstravel.Adapters.AdapterTripPlaces;
import com.example.letstravel.Admin.Models.ModelTripPlaces;

import java.util.ArrayList;

public class FilterTrips extends Filter{

    private AdapterTripPlaces adapterTrip;
    private ArrayList<ModelTripPlaces> filterList;


    public FilterTrips(AdapterTripPlaces adapter, ArrayList<ModelTripPlaces> filterList) {
        this.adapterTrip = adapter;
        this.filterList = filterList;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence charSequence) {
        Filter.FilterResults results = new Filter.FilterResults();
        if (charSequence!=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<ModelTripPlaces> filterModels = new ArrayList<>();
            for (int i=0; i<filterList.size(); i++){

                if (filterList.get(i).getPlaceName().toUpperCase().contains(charSequence) ||
                        filterList.get(i).getCountryName().toUpperCase().contains(charSequence) ||
                        filterList.get(i).getCountryName().toUpperCase().contains(charSequence)) {
                    filterModels.add(filterList.get(i));
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;

        }else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapterTrip.topPlacesArrayList = (ArrayList<ModelTripPlaces>)filterResults.values;

        adapterTrip.notifyDataSetChanged();

    }


}
