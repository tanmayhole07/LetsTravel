package com.example.letstravel.Filters;

import android.widget.Filter;

import com.example.letstravel.Adapters.AdapterFavPlaces;
import com.example.letstravel.Admin.Models.ModelFavPlaces;

import java.util.ArrayList;

public class FilterFacPlaces extends Filter {

    private AdapterFavPlaces adapter;
    private ArrayList<ModelFavPlaces> filterList;

    public FilterFacPlaces(AdapterFavPlaces adapter, ArrayList<ModelFavPlaces> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        Filter.FilterResults results = new Filter.FilterResults();
        if (charSequence!=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<ModelFavPlaces> filterModels = new ArrayList<>();
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

        adapter.topPlacesArrayList = (ArrayList<ModelFavPlaces>)filterResults.values;

        adapter.notifyDataSetChanged();

    }
}
