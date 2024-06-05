package com.sucen.sisamob;

import com.sucen.sisamob.R;

import producao.RelatorioAdapter;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class RelProducaoFragment extends Fragment{

    private OnFragmentInteractionListener mListener;

    public RelProducaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelatorioAdapter rel = new RelatorioAdapter();
        View v = inflater.inflate(R.layout.activity_relatorio, container, false);
        TextView titulo = (TextView) v.findViewById(R.id.tvTitulo);
        TextView detalhe  = (TextView) v.findViewById(R.id.tvDetalhe);
        TextView total  = (TextView) v.findViewById(R.id.tvTotal);

        ListView lista  = (ListView) v.findViewById(R.id.lvLista);

        titulo.setText("RESUMO DAS VISITAS CADASTRADAS");

        lista.setAdapter(rel);

        int tot = 0;
        for (int i=0;i<rel.getCount();i++){
            tot += rel.getRegistros(i);
        }

        total.setText("Total: " + tot + " imóveis visitados.");
        // Updating the action bar title
      //  getActivity().getActionBar().setTitle("Relat�rio");

        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
