package com.sucen.sisamob;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import entidades.Area;
import entidades.Censitario;
import entidades.Quarteirao;
import utilitarios.Storage;


public class LocAladoFragment extends Fragment {
    private Spinner spArea, spCens, spQuart;
    private Button btProssegue;
    private TextView txArea, txCens;
    List<String> valArea, valCens, valQuart;
    private RadioGroup rgAtividade;
    RadioButton chkPre, chkPos, chkBri, chkOt;
    int ativ;

    private OnFragmentInteractionListener mListener;

    public LocAladoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loc_alado, container, false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupComps(v);
        // Updating the action bar title
        //   getActivity().getActionBar().setTitle("Local");

        return v;
    }

    private void setupComps(View v) {
        spArea = (Spinner) v.findViewById(R.id.spArea);
        spArea.setOnItemSelectedListener(onMudaArea);
        txArea = (TextView) v.findViewById(R.id.textView6);
        spCens = (Spinner) v.findViewById(R.id.spCensitario);
        spCens.setOnItemSelectedListener(onMudaCens);
        txCens = (TextView) v.findViewById(R.id.textView5);
        spQuart = (Spinner) v.findViewById(R.id.spQuarteirao);
        spQuart.setOnItemSelectedListener(onMudaQuart);

        rgAtividade = (RadioGroup) v.findViewById(R.id.rgAlAtividade);
        chkPre = (RadioButton) v.findViewById(R.id.rbPre);
        chkPos = (RadioButton) v.findViewById(R.id.rbPos);
        chkBri = (RadioButton) v.findViewById(R.id.rbBRI);
        chkOt = (RadioButton) v.findViewById(R.id.rbOt);

        btProssegue = (Button) v.findViewById(R.id.btProssegue);
        btProssegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaImovel();
            }
        });
        String mun = Storage.recupera("municipio");
        ativ = Integer.parseInt(Storage.recupera("atividade"));

            addItemsOnArea(mun);
            addItemsOnCens("1"); //arrumar aqui
            addItemsOnQuart("1");

    }

    public void chamaImovel(){
        int refAtiv = Integer.parseInt(Storage.recupera("atividade"));
        int ativ = 0;

        switch (rgAtividade.getCheckedRadioButtonId()) {
            case R.id.rbPre:
                ativ = 9;
                break;
            case R.id.rbPos:
                ativ = 10;
                break;
            case R.id.rbBRI:
                ativ = 14;
                break;
            case R.id.rbOt:
                ativ = 11;
                break;
        }
        Storage.insere("ref_ativ", refAtiv);
        Storage.insere("atividade", ativ);

        Storage.insere("quarteirao", valQuart.get(spQuart.getSelectedItemPosition()));
        Storage.insere("imovel", 0);
        String tt;

        //int quart = Integer.parseInt(valQuart.get(spQuart.getSelectedItemPosition()));
        android.app.Fragment frag = new AladoFragment();

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //ft.addToBackStack(null);
        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, frag);

        // Committing the transaction
        ft.commit();
    }

    private void addItemsOnArea(String mun) {
        Area area = new Area(getActivity());

        List<String> list = area.combo(mun);
        valArea = area.id_Area;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArea.setAdapter(dados);
    }

    AdapterView.OnItemSelectedListener onMudaArea = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            addItemsOnCens(valArea.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    };

    private void addItemsOnCens(String area) {
        Censitario cens = new Censitario(getActivity());

        List<String> list = cens.combo(area);
        valCens = cens.id_Cens;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCens.setAdapter(dados);
    }

    AdapterView.OnItemSelectedListener onMudaCens = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            addItemsOnQuart(valCens.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    private void addItemsOnQuart(String cens) {
        Quarteirao quart = new Quarteirao(getActivity());

        List<String> list = quart.combo(cens, ativ);
        valQuart = quart.id_Quart;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuart.setAdapter(dados);
    }

    AdapterView.OnItemSelectedListener onMudaQuart = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
