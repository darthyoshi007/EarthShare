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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.planets_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        submitButton = (Button)rootView.findViewById(R.id.submit_button);

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
//                        name.setText(null);
                        name.setHint("Full Name");
                        street_number.setHint("Street #");
                        street_name.setHint("Street name");
                        city.setHint("City");
                        state.setHint("State");
                        zip.setHint("ZIP");
                        details.setHint("Details");
                        refuge.setChecked(pref.getBoolean("check", false));
                        medical.setChecked(pref.getBoolean("check", false));
                        food.setChecked(pref.getBoolean("check", false));
                        ((FullscreenActivity) getActivity()).onButtonClicked("" + name.getText(), street_number.getText() + " " + street_name.getText() + " " + city.getText() + " " + state.getText() + " " + zip.getText(), "" + details.getText());
                        name.setText(null);
                        street_number.setText(null);
                        street_name.setText(null);
                        city.setText(null);
                        zip.setText(null);
                        details.setText(null);
                        state.setText(null);
                    }
                });

        return rootView;
    }


}
