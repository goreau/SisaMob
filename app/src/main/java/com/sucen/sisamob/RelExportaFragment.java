package com.sucen.sisamob;

import com.sucen.sisamob.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RelExportaFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieving the currently selected item number
        String registros = getArguments().getString("resultado");

        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.activity_importados, container, false);

        // Getting reference to the TextView of the Fragment
        TextView tv = (TextView) v.findViewById(R.id.etResult);

        // Setting currently selected river name in the TextView
        tv.setText(registros);

        // Updating the action bar title
        getActivity().getActionBar().setTitle("Resumo");

        return v;
    }
}
