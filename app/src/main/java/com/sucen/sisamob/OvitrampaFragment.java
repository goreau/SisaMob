package com.sucen.sisamob;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.sucen.sisamob.R;

import producao.VcOvitrampa;
import utilitarios.MyToast;
import utilitarios.Storage;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import entidades.Ovitrampa;

public class OvitrampaFragment extends Fragment {
    Spinner spObs;//spOvi,
    EditText tvInstala, tvRetira;
    RadioGroup rgLocal;
    RadioButton chkIntra, chkPeri;
    Button btSalva;
    List<String> valOvitrampa;
    List<String> strCadastro;
    AutoCompleteTextView etOvitrampa;
    private DatePickerDialog dpData, dpData2;

    String id_municipio;
    private SimpleDateFormat dateFormatter;
    Long idEdt;
    VcOvitrampa imovel;

    private OnFragmentInteractionListener mListener;

    public OvitrampaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_ovitrampa, container, false);
        idEdt = getArguments() != null ? getArguments().getLong("Id") : 0;
        imovel = new VcOvitrampa(idEdt);

        setupComps(v);
        // Updating the action bar title
//        getActivity().getActionBar().setTitle("Ovitrampas");

        return v;
    }

    private void Edita(){
        tvInstala.setText(imovel.getDt_instala());
        tvRetira.setText(imovel.getDt_retira());
        int prod = valOvitrampa.indexOf(String.valueOf(imovel.getId_ovitrampa()));
      //  spOvi.setSelection(prod);
        etOvitrampa.setText(strCadastro.get(prod));
        prod = imovel.getObs()-1;
        spObs.setSelection(prod);

        int sit = imovel.getPeri_intra();

        switch (sit) {
            case 1:
                chkIntra.setChecked(true);
                break;
            default:
                chkPeri.setChecked(true);
                break;
        }
    }

    private void setupComps(View v) {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        etOvitrampa     = (AutoCompleteTextView) v.findViewById(R.id.etOvitrampa);
       // spOvi 		= (Spinner) v.findViewById(R.id.spOvitrampa);
        spObs 		= (Spinner) v.findViewById(R.id.spObs);
        tvInstala 	= (EditText) v.findViewById(R.id.etInstala);
        tvInstala.setInputType(InputType.TYPE_NULL);
        tvRetira 	= (EditText) v.findViewById(R.id.etRetira);
        tvRetira.setInputType(InputType.TYPE_NULL);
        rgLocal 	= (RadioGroup) v.findViewById(R.id.rgLocal);

        chkIntra = (RadioButton) v.findViewById(R.id.chkIntra);
        chkPeri = (RadioButton) v.findViewById(R.id.chkPeri);

        btSalva = (Button) v.findViewById(R.id.btSalva);
        btSalva.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaOvi(v);
            }
        });
        setDateTimeField();
        addItensOnOvitrampa();
        addItensOnObs();

        if(idEdt>0){
            Edita();
        }

    }

    private void addItensOnOvitrampa(){
        Ovitrampa im = new Ovitrampa(getActivity());
        id_municipio = Storage.recupera("municipio");


        List<String> list = im.combo(id_municipio);
        strCadastro = list;
        valOvitrampa = im.id_Ovi;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // spOvi.setAdapter(dados);
        etOvitrampa.setAdapter(dados);
    }

    private void addItensOnObs(){

        List<String> list = Arrays.asList("-- Selecione --","1- Intervalo acima 7 dias","2- Ovitrampa ou palheta desaparecidas","3- Ovitrampa ou palheta danificadas",
                "4- Ovitrampa seca","5- Casa fechada","6- Ovitrampa cheia d'�gua","7- Ovitrampa pouca �gua");

        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spObs.setAdapter(dados);
    }

    private void salvaOvi(View v){
        VcOvitrampa ovit = new VcOvitrampa(idEdt); //arrumar aqui pra editar
        int pos = strCadastro.indexOf(etOvitrampa.getText().toString());
        ovit.setId_ovitrampa(Integer.parseInt(valOvitrampa.get(pos)));
       // ovit.setId_ovitrampa(Integer.parseInt(valOvitrampa.get(spOvi.getSelectedItemPosition())));
        ovit.setObs(spObs.getSelectedItemPosition());
        String dt = (tvInstala.getText().toString().length()<2 ? "01-01-1900" : tvInstala.getText().toString());
        ovit.setDt_instala(dt);
        dt=(tvRetira.getText().toString().length()<2 ? "01-01-1900" : tvRetira.getText().toString());
        ovit.setDt_retira(dt);
        ovit.setAgente(Storage.recupera("agente"));
        ovit.setId_execucao(Integer.parseInt(Storage.recupera("execucao")));
        int loc;
        switch (rgLocal.getCheckedRadioButtonId()) {
            case R.id.chkIntra:
                loc=1;
                break;
            default:
                loc=2;
                break;
        }
        ovit.setPeri_intra(loc);
        String msg = idEdt>0 ? "Alterado com sucesso." : "Inserido com sucesso.";
        if (ovit.manipula()){
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
            toast.show(msg);
        }
    }

    private void setDateTimeField() {
        tvInstala.setOnClickListener(onMudatvInstala);
        tvRetira.setOnClickListener(onMudatvRetira);
        Calendar newCalendar = Calendar.getInstance();
        dpData = new DatePickerDialog(getActivity(), new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvInstala.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dpData2 = new DatePickerDialog(getActivity(), new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvRetira.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    OnClickListener onMudatvInstala = new OnClickListener(){

        @Override
        public void onClick(View v) {
            dpData.show();
        }
    };

    OnClickListener onMudatvRetira = new OnClickListener(){

        @Override
        public void onClick(View v) {
            dpData2.show();
        }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
