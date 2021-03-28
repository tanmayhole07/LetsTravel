package com.example.letstravel.Filters;

import android.widget.Filter;

import com.example.letstravel.Adapters.AdapterPopularPlaces;
import com.example.letstravel.Admin.Models.ModelTripPlaces;

import java.util.ArrayList;

public class FilterPopularPlaces  extends Filter {

    private AdapterPopularPlaces adapterPopularTrip;
    private ArrayList<ModelTripPlaces> filterList;

    public FilterPopularPlaces(AdapterPopularPlaces adapter, ArrayList<ModelTripPlaces> filterList) {
        this.adapterPopularTrip = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
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

        adapterPopularTrip.topPlacesArrayList =  (ArrayList<ModelTripPlaces>)filterResults.values;

        adapterPopularTrip.notifyDataSetChanged();

    }
}
