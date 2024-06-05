package com.sucen.sisamob;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.sucen.sisamob.R;

import utilitarios.Storage;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import entidades.Atividade;
import entidades.Municipio;

public class AtividadeFragment extends Fragment {
    private EditText etAgente, etData;
    private Spinner spAtiv, spMun;
    private DatePickerDialog dpData;
    private RadioGroup rgExecucao;
    private RadioButton chkSucen, chkMunicipio, chkPacs;
    private Button btProssegue;//, btTopo, btBottom;
    List<String> valAtiv, valMun;

    private SimpleDateFormat dateFormatter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AtividadeFragment() {
    }

    public static AtividadeFragment newInstance(String param1, String param2) {
        AtividadeFragment fragment = new AtividadeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_trabalho, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupComps(v);
        // Updating the action bar title
       // getActivity().getActionBar().setTitle("Atividades");

        return v;
    }

    private void setupComps(View v) {

        etAgente = (EditText) v.findViewById(R.id.etAgente);
        spAtiv = (Spinner) v.findViewById(R.id.spAtividade);
        spAtiv.setOnItemSelectedListener(onMudaAtiv);
        //acAtiv = (AutoCompleteTextView) v.findViewById(R.id.acAtiv);
        spMun = (Spinner) v.findViewById(R.id.spMunicipio);
        spMun.setOnItemSelectedListener(onMudaMun);
        etData = (EditText) v.findViewById(R.id.etData);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        etData.setInputType(InputType.TYPE_NULL);
        rgExecucao  = (RadioGroup) v.findViewById(R.id.rgExecucao);
        chkSucen    = (RadioButton) v.findViewById(R.id.chkSucen);
        chkMunicipio= (RadioButton) v.findViewById(R.id.chkMunicipio);
        chkPacs     = (RadioButton) v.findViewById(R.id.chkPsf);

        //define valores de campos
        etAgente.setText(Storage.recupera("agente"));
        etData.setText(Storage.dataAtual());
        int exec = 0;
        try {
            exec = Integer.valueOf(Storage.recupera("execucao"));
        } catch (Exception ex){
            exec = 1;
        }
        switch (exec) {
            case 1:
                chkSucen.setChecked(true);
                break;
            case 2:
                chkMunicipio.setChecked(true);
                break;
            default:
                chkPacs.setChecked(true);
                break;
        }


        btProssegue = (Button) v.findViewById(R.id.btProssegue);
        btProssegue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaCabeca();
            }
        });



        setDateTimeField();
        addItemsOnAtiv();
        addItemsOnMun();
    }

    private void addItemsOnMun() {
        Municipio mun = new Municipio(getActivity());

        List<String> list = mun.combo();
        valMun = mun.id_Mun;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMun.setAdapter(dados);
    }

    OnItemSelectedListener onMudaMun = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,
                                   long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    public void addItemsOnAtiv() {
        Atividade ativ = new Atividade(getActivity());

        List<String> list = ativ.combo("0");
        valAtiv = ativ.id_Ativ;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAtiv.setAdapter(dados);
        //acAtiv.setAdapter(dados);
        spAtiv.setSelection(Storage.recuperaI("valAtiv"),true);
    }

    OnItemSelectedListener onMudaAtiv = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position,
                                   long id) {
            //String indice = valAtiv.get(position);
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            spAtiv.setSelection(0, true);
        }

    };

    public void chamaCabeca(){
        //insere valores do form para recuperar depois
        Storage.insere("agente", etAgente.getText().toString());
        Storage.insere("municipio", valMun.get(spMun.getSelectedItemPosition()));
        Storage.insere("atividade", valAtiv.get(spAtiv.getSelectedItemPosition()));
        Storage.insere("valAtiv", spAtiv.getSelectedItemPosition());
        Storage.insere("data", etData.getText().toString());
        switch (rgExecucao.getCheckedRadioButtonId()) {
            case R.id.chkSucen:
                Storage.insere("execucao", "1");
                break;
            case R.id.chkMunicipio:
                Storage.insere("execucao", "2");
                break;
            default:
                Storage.insere("execucao", "3");
                break;
        }
        chamaImovel();
    }

    public void chamaImovel(){
        int ativ = Integer.parseInt(valAtiv.get(spAtiv.getSelectedItemPosition()));
        Fragment frag = null;

        if (ativ<4 || ativ==12){
            frag = new ImovelCadFragment();
        } else if (ativ==4){
            frag = new OvitrampaFragment();
        } else if (ativ==9) {
            frag = new LocAladoFragment();
        } else if (ativ==10) {
            frag = new AladoImFragment();
        } else {
            frag = new AtividadeFolhaFragment();
        }
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

    private void setDateTimeField() {
        etData.setOnClickListener(onMudaData);

        Calendar newCalendar = Calendar.getInstance();
        dpData = new DatePickerDialog(getActivity(), new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etData.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    OnClickListener onMudaData = new OnClickListener(){

        @Override
        public void onClick(View v) {
            dpData.show();
        }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
