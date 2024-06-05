package com.sucen.sisamob;

import com.sucen.sisamob.R;

import producao.Condicao;
import utilitarios.MyToast;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CondicaoFragment extends Fragment {
    Button btSalva, btVolta, btRecip;
    RadioGroup rgCasa, rgQuintal, rgSombra, rgPavimento;
    CheckBox chkGalinha, chkCao, chkOutro;
    RadioButton chkCasaBoa, chkCasaMedia, chkCasaRuim;
    RadioButton chkQuintalBom, chkQuintalMedio, chkQuintalRuim;
    RadioButton chkSombraBoa, chkSombraMedia, chkSombraRuim;
    RadioButton chkPavBom, chkPavMedio, chkPavRuim;

    Long fkId;
    int Edt;
    Condicao cond;
    int IdCond;

    private OnFragmentInteractionListener mListener;

    public CondicaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_condicao, container, false);
        Edt = getArguments() != null ? getArguments().getInt("Edt") : 0;
        setupComps(v);
        // Updating the action bar title
       // getActivity().getActionBar().setTitle("Classifica��o");

        return v;
    }

    private void Edita(){
        cond = new Condicao(fkId);

        chkGalinha.setChecked(cond.getGalinha()>0);
        chkCao.setChecked(cond.getCao()>0);
        chkOutro.setChecked(cond.getOutros()>0);

        int sit = cond.getCond_casa();
        switch (sit) {
            case 1:
                chkCasaBoa.setChecked(true);
                break;
            case 2:
                chkCasaMedia.setChecked(true);
                break;
            default:
                chkCasaRuim.setChecked(true);
                break;
        }

        sit = cond.getCond_quintal();
        switch (sit) {
            case 1:
                chkQuintalBom.setChecked(true);
                break;
            case 2:
                chkQuintalMedio.setChecked(true);
                break;
            default:
                chkQuintalRuim.setChecked(true);
                break;
        }

        sit = cond.getCond_sombra();
        switch (sit) {
            case 1:
                chkSombraBoa.setChecked(true);
                break;
            case 2:
                chkSombraMedia.setChecked(true);
                break;
            default:
                chkSombraRuim.setChecked(true);
                break;
        }

        sit = cond.getPavimento();
        switch (sit) {
            case 1:
                chkPavRuim.setChecked(true);
                break;
            case 2:
                chkPavMedio.setChecked(true);
                break;
            default:
                chkPavBom.setChecked(true);
                break;
        }
    }

    private void setupComps(View v) {
        fkId 		= getArguments().getLong("fkId",0);
        rgCasa		= (RadioGroup) v.findViewById(R.id.rgCasa);
        rgQuintal	= (RadioGroup) v.findViewById(R.id.rgQuintal);
        rgSombra	= (RadioGroup) v.findViewById(R.id.rgSombra);
        rgPavimento	= (RadioGroup) v.findViewById(R.id.rgPavimento);
        chkGalinha	= (CheckBox) v.findViewById(R.id.chkGalinha);
        chkCao		= (CheckBox) v.findViewById(R.id.chkCao);
        chkOutro	= (CheckBox) v.findViewById(R.id.chkOutros);

        chkCasaBoa = (RadioButton) v.findViewById(R.id.chkCasaBoa);
        chkCasaMedia = (RadioButton) v.findViewById(R.id.chkCasaMedia);
        chkCasaRuim = (RadioButton) v.findViewById(R.id.chkCasaRuim);

        chkQuintalBom = (RadioButton) v.findViewById(R.id.chkQuintalBom);
        chkQuintalMedio = (RadioButton) v.findViewById(R.id.chkQuintalMedio);
        chkQuintalRuim = (RadioButton) v.findViewById(R.id.chkQuintalRuim);

        chkSombraBoa = (RadioButton) v.findViewById(R.id.chkSombraPouca);
        chkSombraMedia = (RadioButton) v.findViewById(R.id.chkSombraMedia);
        chkSombraRuim = (RadioButton) v.findViewById(R.id.chkSombraTotal);

        chkPavBom = (RadioButton) v.findViewById(R.id.chkchkPavimentoTotal);
        chkPavMedio = (RadioButton) v.findViewById(R.id.chkchkPavimentoParc);
        chkPavRuim = (RadioButton) v.findViewById(R.id.chkPavimentoSem);


        btSalva 	= (Button) v.findViewById(R.id.btSalvar);
        btSalva.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaCond(v);
            }
        });

        btRecip = (Button) v.findViewById(R.id.btRecipiente);
        btRecip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaRecipiente();
            }
        });

        btVolta = (Button) v.findViewById(R.id.btVolta);
        btVolta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        if(Edt==1){
            Edita();
        }
    }

    public void salvaCond(View v){
        //pega os valores de quem chamou
        int sit;

        cond = new Condicao(fkId);
        switch (rgCasa.getCheckedRadioButtonId()) {
            case R.id.chkCasaBoa:
                sit=1;
                break;
            case R.id.chkCasaMedia:
                sit=2;
                break;
            default:
                sit=3;
                break;
        }
        cond.setCond_casa(sit);

        switch (rgQuintal.getCheckedRadioButtonId()) {
            case R.id.chkQuintalBom:
                sit=1;
                break;
            case R.id.chkQuintalMedio:
                sit=2;
                break;
            default:
                sit=3;
                break;
        }
        cond.setCond_quintal(sit);

        switch (rgSombra.getCheckedRadioButtonId()) {
            case R.id.chkSombraPouca:
                sit=1;
                break;
            case R.id.chkSombraMedia:
                sit=2;
                break;
            default:
                sit=3;
                break;
        }
        cond.setCond_sombra(sit);

        switch (rgPavimento.getCheckedRadioButtonId()) {
            case R.id.chkPavimentoSem:
                sit=1;
                break;
            case R.id.chkchkPavimentoParc:
                sit=2;
                break;
            default:
                sit=3;
                break;
        }
        cond.setPavimento(sit);

        sit = chkGalinha.isChecked() ? 1 : 0;
        cond.setGalinha(sit);

        sit = chkCao.isChecked() ? 1 : 0;
        cond.setCao(sit);

        sit = chkOutro.isChecked() ? 1 : 0;
        cond.setOutros(sit);

        cond.setId_fk(fkId);
        String msg = cond.get_id()>0 ? "Alterado com sucesso." : "Inserido com sucesso.";
        if (cond.manipula()){
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
            toast.show(msg);
        }
    }

    public void chamaRecipiente(){
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("fkId", fkId);
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

    private void voltar(){
        Fragment frag;
        int tabela = getArguments().getInt("tabela",0);
        if (tabela==1){
            frag = new ImovelCadFragment();
        } else {
            frag = new ImovelFolhaFragment();
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, frag);
        ft.commit();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
