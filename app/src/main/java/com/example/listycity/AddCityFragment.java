package com.example.listycity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void editCity(int pos, City newCityEdits);
    }
    private AddCityDialogListener listener;
    static AddCityFragment newInstance(City editingCity,  int position){
        Bundle bundle = new Bundle();
        AddCityFragment addCityFragment = new AddCityFragment();
        bundle.putSerializable("city", editingCity);
        bundle.putInt("city_pos", position);
        addCityFragment.setArguments(bundle);
        return addCityFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        City editingCity;
        if(args != null) {
            editingCity = (City) args.getSerializable("city");
        } else {
            editingCity = null;
        }
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if(editingCity != null){
        editCityName.setText(editingCity.getName());
        editProvinceName.setText(editingCity.getProvince());
}

        return builder
                .setView(view)
                .setTitle("Add/Edit a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if(editingCity != null && args.containsKey("city") && args.containsKey("city_pos")){
                        if(cityName.isEmpty()){
                            cityName = editingCity.getName();
                        }
                        if(provinceName.isEmpty()){
                            provinceName = editingCity.getProvince();
                        }
                        listener.editCity(args.getInt("city_pos"), new City(cityName, provinceName));
                    }
                    else{
                        listener.addCity( new City(cityName, provinceName));
                    }
                })
                .create();
    }
}