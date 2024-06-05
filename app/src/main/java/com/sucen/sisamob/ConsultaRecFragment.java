package com.sucen.sisamob;

import com.sucen.sisamob.R;

import producao.EditaRecAdapter;
import producao.RecipienteList;
import utilitarios.GerenciarBanco;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ConsultaRecFragment extends Fragment{
    TextView tvRecip;
    ListView lvRecipiente;

    Context context;
    GerenciarBanco db;
    int j=0;

    private OnFragmentInteractionListener mListener;

    public ConsultaRecFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.activity_editarec, container, false);

        // Updating the action bar title
    //    getActivity().getActionBar().setTitle("Consulta Recipientes");
        setupComps(v);
        return v;
    }


    private void setupComps(View v){
        tvRecip = (TextView) v.findViewById(R.id.tvRecipientes);
        lvRecipiente = (ListView) v.findViewById(R.id.lvRecipiente);

        final Long fkId = getArguments().getLong("fkId",0);
        System.out.println(fkId);
        final EditaRecAdapter edLista = new EditaRecAdapter(fkId,2);

        if (edLista.getCount()>0){
            tvRecip.setText("Recipientes");
            lvRecipiente.setAdapter(edLista);
        } else {
            tvRecip.setText("");
        }
        //	System.out.println("passou aqui");
        lvRecipiente.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                RecipienteList item = (RecipienteList) edLista.getItem(position);
                int id_rec = item.getId();
                chamaRecipiente(fkId, id_rec);
            }
        });
    }

    public void chamaRecipiente(Long idFk, int id){
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("fkId", idFk);
        data.putInt("Id", id);
        data.putInt("tabela", 2);


        Fragment frag = new RecipienteFragment();

        // Setting the id
        frag.setArguments(data);
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
