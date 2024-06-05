package com.sucen.sisamob;

import java.util.List;

import com.sucen.sisamob.R;

import entidades.Area_nav;
import entidades.Produto;
import entidades.Quart_nav;
import utilitarios.Storage;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import entidades.Area;
import entidades.Censitario;
import entidades.Quarteirao;

public class AtividadeFolhaFragment extends Fragment{

    private Spinner spArea, spCens, spQuart, spAreaNav;
    private Button btProssegue;
    private RadioGroup rgTipoTrab;
    private TextView txAreanav, txArea, txCens;
    Spinner spProdFocal, spProdPeri, spProdNeb;
    List<String> valFocal, valPeri, valNeb, valLog;
    //private RadioButton rbRotina, rbPendencia;
    List<String> valArea, valCens, valQuart, valAreaNav;
    int ativ;

    private OnFragmentInteractionListener mListener;

    public AtividadeFolhaFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_trabfolha, container, false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupComps(v);
        // Updating the action bar title
     //   getActivity().getActionBar().setTitle("Local");

        return v;
    }

    private void setupComps(View v) {
        spAreaNav = (Spinner) v.findViewById(R.id.spAreaNav);
        spAreaNav.setOnItemSelectedListener(onMudaAreaNav);
        txAreanav = (TextView) v.findViewById(R.id.textView7);
        spArea = (Spinner) v.findViewById(R.id.spArea);
        spArea.setOnItemSelectedListener(onMudaArea);
        txArea = (TextView) v.findViewById(R.id.textView6);
        spCens = (Spinner) v.findViewById(R.id.spCensitario);
        spCens.setOnItemSelectedListener(onMudaCens);
        txCens = (TextView) v.findViewById(R.id.textView5);
        spQuart = (Spinner) v.findViewById(R.id.spQuarteirao);
        spQuart.setOnItemSelectedListener(onMudaQuart);
        rgTipoTrab = (RadioGroup) v.findViewById(R.id.rgTipoTrab);
        spProdFocal = (Spinner) v.findViewById(R.id.spProdFocal);
        spProdPeri = (Spinner) v.findViewById(R.id.spProdPeri);
        spProdNeb = (Spinner) v.findViewById(R.id.spProdNeb);


        btProssegue = (Button) v.findViewById(R.id.btProssegue);
        btProssegue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaImovel();
            }
        });
        String mun = Storage.recupera("municipio");
        ativ = Integer.parseInt(Storage.recupera("atividade"));
        if (ativ==6 || ativ==7) {
            addItemsOnAreaNav(mun);
            spArea.setVisibility(View.GONE);
            txArea.setVisibility(View.GONE);

            spCens.setVisibility(View.GONE);
            txCens.setVisibility(View.GONE);
        } else {
            addItemsOnArea(mun);
            addItemsOnCens("1"); //arrumar aqui
            addItemsOnQuart("1");
            spAreaNav.setVisibility(View.GONE);
            txAreanav.setVisibility(View.GONE);
        }

        addItensOnFocal();
        addItensOnPeri();
        addItensOnNeb();
    }

    private void addItemsOnAreaNav(String mun) {
        Area_nav area = new Area_nav(getActivity());

        List<String> list = area.combo(mun);
        valAreaNav = area.id_Area_nav;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAreaNav.setAdapter(dados);
    }

    private void addItemsOnArea(String mun) {
        Area area = new Area(getActivity());

        List<String> list = area.combo(mun);
        valArea = area.id_Area;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spArea.setAdapter(dados);
    }

    OnItemSelectedListener onMudaArea = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            addItemsOnCens(valArea.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    };

    OnItemSelectedListener onMudaAreaNav = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            addItemsOnQuartNav(valAreaNav.get(position));
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

    OnItemSelectedListener onMudaCens = new OnItemSelectedListener() {
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

    private void addItemsOnQuartNav(String area) {
        Quart_nav quart = new Quart_nav(getActivity());

        List<String> list = quart.combo(area);
        valQuart = quart.id_Quart;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuart.setAdapter(dados);
    }

    OnItemSelectedListener onMudaQuart = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }

    };

    private void addItensOnFocal() {
        Produto prod = new Produto(getActivity());

        List<String> list = prod.combo("1");
        valFocal = prod.id_prod;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdFocal.setAdapter(dados);
    }

    private void addItensOnPeri() {
        Produto prod = new Produto(getActivity());

        List<String> list = prod.combo("2");
        valPeri = prod.id_prod;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdPeri.setAdapter(dados);
    }

    private void addItensOnNeb() {
        Produto prod = new Produto(getActivity());

        List<String> list = prod.combo("3");
        valNeb = prod.id_prod;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdNeb.setAdapter(dados);
    }

    public void chamaImovel(){
        int ativ = Integer.parseInt(Storage.recupera("atividade"));
        if (ativ == 6 || ativ==7){
            Storage.insere("area_nav", valAreaNav.get(spAreaNav.getSelectedItemPosition()));
        } else {
            Storage.insere("area_nav", "0");
        }
        Storage.insere("quarteirao", valQuart.get(spQuart.getSelectedItemPosition()));
        Storage.insere("imovel", 0);
        String tt;
        switch (rgTipoTrab.getCheckedRadioButtonId()) {
            case R.id.rbDemanda:
                tt="3";
                break;
            case R.id.rbPendencia:
                tt="2";
                break;
            default:
                tt="1";
                break;
        }
        Storage.insere("prod_focal",valFocal.get(spProdFocal.getSelectedItemPosition()));
        Storage.insere("prod_peri",valPeri.get(spProdPeri.getSelectedItemPosition()));
        Storage.insere("prod_neb",valNeb.get(spProdNeb.getSelectedItemPosition()));
        Storage.insere("id_tipo", tt);
        //int quart = Integer.parseInt(valQuart.get(spQuart.getSelectedItemPosition()));
        Fragment frag = new ImovelFolhaFragment();

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
