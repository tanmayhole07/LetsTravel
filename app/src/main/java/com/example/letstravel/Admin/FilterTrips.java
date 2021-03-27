package com.example.letstravel.Admin;

import android.widget.Filter;

import com.example.letstravel.Admin.Adapters.AdapterFavPlaces;

import java.util.ArrayList;

public class FilterTrips extends Filter{

//    private AdapterTripPlaces adapterTrip;
//    private ArrayList<ModelTripPlaces> filterList;

    private AdapterFavPlaces adapterFav;
    private ArrayList<ModelFavPlaces> filterListF;

//    public FilterTrips(AdapterTripPlaces adapter, ArrayList<ModelTripPlaces> filterList) {
//        this.adapterTrip = adapter;
//        this.filterList = filterList;
//    }

    public FilterTrips(AdapterFavPlaces adapterFav, ArrayList<ModelFavPlaces> filterListF) {
        this.adapterFav = adapterFav;
        this.filterListF = filterListF;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence charSequence) {
        Filter.FilterResults results = new Filter.FilterResults();
        if (charSequence!=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<ModelFavPlaces> filterModels = new ArrayList<>();
            for (int i=0; i<filterListF.size(); i++){

                if (filterListF.get(i).getPlaceName().toUpperCase().contains(charSequence) ||
                        filterListF.get(i).getCountryName().toUpperCase().contains(charSequence) ||
                        filterListF.get(i).getCountryName().toUpperCase().contains(charSequence)) {
                    filterModels.add(filterListF.get(i));
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;

        }else {
            results.count = filterListF.size();
            results.values = filterListF;
        }

        return results;

    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

//        adapterTrip.topPlacesArrayList = (ArrayList<ModelTripPlaces>)filterResults.values;
//
//        adapterTrip.notifyDataSetChanged();

        adapterFav.topPlacesArrayList = (ArrayList<ModelFavPlaces>)filterResults.values;

        adapterFav.notifyDataSetChanged();

    }


}
