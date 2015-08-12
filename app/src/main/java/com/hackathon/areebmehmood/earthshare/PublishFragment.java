package com.hackathon.areebmehmood.earthshare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Areeb Mehmood on 8/8/2015.
 */
public class PublishFragment extends Fragment {

    private Button submitButton;

    private EditText name;
    private EditText street_number;
    private EditText street_name;
    private EditText city;
    private EditText state;
    private EditText zip;
    private EditText details;

    private Switch refuge;
    private Switch medical;
    private Switch food;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_publish, container, false);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.planets_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submitButton = (Button) rootView.findViewById(R.id.submit_button);

        name = (EditText) rootView.findViewById(R.id.full_name);
        street_number = (EditText) rootView.findViewById(R.id.street_number);
        street_name = (EditText) rootView.findViewById(R.id.street_name);
        city = (EditText) rootView.findViewById(R.id.city);
        state = (EditText) rootView.findViewById(R.id.state);
        zip = (EditText) rootView.findViewById(R.id.zip);
        details = (EditText) rootView.findViewById(R.id.details);

        refuge = (Switch) rootView.findViewById(R.id.refuge);
        medical = (Switch) rootView.findViewById(R.id.medical);
        food = (Switch) rootView.findViewById(R.id.food);

        final SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        StringBuilder address = new StringBuilder();
                        name.setHint("Full Name");
                        street_number.setHint("Street #");
                        street_name.setHint("Street name");
                        city.setHint("City");
                        state.setHint("State");
                        zip.setHint("ZIP");
                        details.setHint("Details");
                        if (street_number.getText().toString() != null)
                            address.append(street_number.getText().toString());
                        if (street_name.getText().toString() != null)
                            address.append(street_name.getText().toString());
                        if (city.getText().toString() != null)
                            address.append(city.getText().toString());
                        if (state.getText().toString() != null)
                            address.append(state.getText().toString());
                        if (zip.getText().toString() != null)
                            address.append(zip.getText().toString());
                        ((FullscreenActivity) getActivity()).onButtonClicked(name.getText().toString(), address.toString(), details.getText().toString(), refuge.isChecked(), medical.isChecked(), food.isChecked());
                        name.setText(null);
                        street_number.setText(null);
                        street_name.setText(null);
                        city.setText(null);
                        zip.setText(null);
                        details.setText(null);
                        state.setText(null);
                        refuge.setChecked(false);
                        medical.setChecked(false);
                        food.setChecked(false);
                    }
                });

        return rootView;
    }


}
