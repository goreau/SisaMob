package com.sucen.sisamob;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import java.util.List;
import entidades.Area_nav;
import utilitarios.Storage;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AtividadeNebFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AtividadeNebFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AtividadeNebFragment extends Fragment {
    private Spinner spArea;
    private Button btProssegue;
    private RadioGroup rgTipoTrab;
    List<String> valArea;

    private OnFragmentInteractionListener mListener;

    public AtividadeNebFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_atividade_neb, container, false);
        setupComps(v);
        // Updating the action bar title
        //   getActivity().getActionBar().setTitle("Local");

        return v;
    }

    private void setupComps(View v) {
        spArea = (Spinner) v.findViewById(R.id.spArea);
        rgTipoTrab = (RadioGroup) v.findViewById(R.id.rgTipoTrab);
        //	rbRotina = (RadioButton) v.findViewById(R.id.rbRotina);
        //	rbPendencia = (RadioButton) v.findViewById(R.id.rbPendencia);

        btProssegue = (Button) v.findViewById(R.id.btProssegue);
        btProssegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaImovel();
            }
        });
        String mun = Storage.recupera("municipio");

        addItemsOnArea(mun);
    }

    private void addItemsOnArea(String mun) {
        Area_nav area = new Area_nav(getActivity());

        List<String> list = area.combo(mun);
        valArea = area.id_Area_nav;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArea.setAdapter(dados);
    }

    public void chamaImovel(){
        Storage.insere("area_nav", valArea.get(spArea.getSelectedItemPosition()));
        Storage.insere("imovel", 0);
        String tt;
        switch (rgTipoTrab.getCheckedRadioButtonId()) {
            case R.id.rbPendencia:
                tt="2";
                break;
            default:
                tt="1";
                break;
        }
        Storage.insere("id_tipo", tt);
        //int quart = Integer.parseInt(valQuart.get(spQuart.getSelectedItemPosition()));
        android.app.Fragment frag = new ImovelFolhaFragment();

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, frag);

        // Committing the transaction
        ft.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
