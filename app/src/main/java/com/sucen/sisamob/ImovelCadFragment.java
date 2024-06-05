package com.sucen.sisamob;

import java.util.List;

import com.sucen.sisamob.R;

import entidades.Imovel;
import entidades.Produto;

import producao.EditaRecAdapter;
import producao.RecipienteList;
import producao.VcImovel;
import utilitarios.MyToast;
import utilitarios.NumberPicker;
import utilitarios.Storage;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ImovelCadFragment extends Fragment {
    Spinner spProdFocal, spProdPeri, spProdNeb;
    RadioGroup rgSituacao;
    NumberPicker npFocal, npPeri, npNeb;
    List<String> valImovel, valFocal, valPeri, valNeb;
    List<String> strCadastro;
    RadioButton chkTrab, chkFech, chkRec;
    CheckBox chkMec, chkAlt, chkFoc, chkPeri, chkNeb;
    Button btRec, btNovo, btDel;
    AutoCompleteTextView etCadastro;

    String id_municipio, id_atividade;
    Long idEdt;
    VcImovel imovel;
    int status = 0;

    private OnFragmentInteractionListener mListener;

    public ImovelCadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_imovelcad, container, false);
        idEdt = getArguments() != null ? getArguments().getLong("Id") : 0;
        imovel = new VcImovel(idEdt);

        setupComps(v);
        // Updating the action bar title
    //    getActivity().getActionBar().setTitle("Im�vel");

        return v;
    }

    private void Edita(){
        npFocal.setValue(imovel.getQt_focal());
        npPeri.setValue(imovel.getQt_peri());
        int prod = valFocal.indexOf(Integer.toString(imovel.getId_prod_focal()));
        spProdFocal.setSelection(prod);
        prod = valPeri.indexOf(Integer.toString(imovel.getId_prod_peri()));
        spProdPeri.setSelection(prod);
        prod = valImovel.indexOf(Integer.toString(imovel.getId_imovel()));
       // spImovel.setSelection(prod);
        etCadastro.setText(strCadastro.get(prod));
        prod = valNeb.indexOf(Integer.toString(imovel.getId_prod_neb()));
        spProdNeb.setSelection(prod);
        chkMec.setChecked(imovel.getMecanico()>0);
        chkAlt.setChecked(imovel.getAlternativo()>0);
        chkFoc.setChecked(imovel.getFocal() > 0);
        chkPeri.setChecked(imovel.getPeri() > 0);
        chkNeb.setChecked(imovel.getNeb() > 0);

        Storage.insere("agente",imovel.getAgente());
        Storage.insere("data",imovel.getDt_cadastro());
        Storage.insere("execucao",Integer.toString(imovel.getId_execucao()));

        status = imovel.getStatus();

        int sit = imovel.getId_situacao();
        switch (sit) {
            case 1:
                chkTrab.setChecked(true);
                break;
            case 2:
                chkFech.setChecked(true);
                break;
            default:
                chkRec.setChecked(true);
                break;
        }
    }

    private void setupComps(View v) {
        etCadastro     = (AutoCompleteTextView) v.findViewById(R.id.etCadastro);
      //  spImovel = (Spinner) v.findViewById(R.id.spImovel);
        spProdFocal = (Spinner) v.findViewById(R.id.spProdFocal);
        spProdPeri = (Spinner) v.findViewById(R.id.spProdPeri);
        spProdNeb = (Spinner) v.findViewById(R.id.spProdNeb);
        rgSituacao = (RadioGroup) v.findViewById(R.id.rgSituacao);
        npFocal = (NumberPicker) v.findViewById(R.id.npFocal);
        npPeri = (NumberPicker) v.findViewById(R.id.npPerifocal);
        npNeb = (NumberPicker) v.findViewById(R.id.npNebulizacao);

        chkTrab = (RadioButton) v.findViewById(R.id.chkTrabalhado);
        chkFech = (RadioButton) v.findViewById(R.id.chkFechado);
        chkRec = (RadioButton) v.findViewById(R.id.chkRecusa);

        chkMec = (CheckBox) v.findViewById(R.id.chkMecanico);
        chkAlt = (CheckBox) v.findViewById(R.id.chkAlternativo);
        chkPeri = (CheckBox) v.findViewById(R.id.chkPeri);
        chkFoc = (CheckBox) v.findViewById(R.id.chkFocal);
        chkNeb = (CheckBox) v.findViewById(R.id.chkNeb);

        btRec = (Button) v.findViewById(R.id.btRecipiente);
        btRec.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                salvaImovel(v, 1);
            }
        });

        btNovo = (Button) v.findViewById(R.id.btNovo);
        btNovo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaImovel(v, 2);
            }
        });

        btDel = (Button) v.findViewById(R.id.btExcluiCad);
        btDel.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                deletaImovel(v);
            }
        });

        addItensOnImovel();
        addItensOnFocal();
        addItensOnPeri();
        addItensOnNeb();

        chkTrab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(chkTrab.isChecked());
            }
        });

        chkFech.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(!chkFech.isChecked());
            }
        });

        chkRec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(!chkRec.isChecked());
            }
        });

        if(idEdt>0){
            Edita();
            btDel.setEnabled(true);
        } else {
            btDel.setEnabled(false);
        }
    }

    private void addItensOnFocal() {
        Produto prod = new Produto(getActivity());

        List<String> list = prod.combo("1");
        valFocal = prod.id_prod;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdFocal.setAdapter(dados);
    }

    private void addItensOnPeri() {
        Produto prod = new Produto(getActivity());

        List<String> list = prod.combo("2");
        valPeri = prod.id_prod;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdPeri.setAdapter(dados);
    }

    private void addItensOnNeb() {
        Produto prod = new Produto(getActivity());

        List<String> list = prod.combo("3");
        valNeb = prod.id_prod;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProdNeb.setAdapter(dados);
    }

    private void addItensOnImovel() {
        Imovel im = new Imovel(getActivity());
        id_municipio = Storage.recupera("municipio");
        id_atividade = Storage.recupera("atividade");
      //  Log.w("Mun/Ativ:",id_municipio+"/"+id_atividade);
        List<String> list = im.combo(id_municipio,id_atividade);
        strCadastro = list;
        valImovel = im.id_Im;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spImovel.setAdapter(dados);
        etCadastro.setAdapter(dados);
    }

    public void deletaImovel(View v){
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //define o titulo
        builder.setTitle("Excluir");
        //define a mensagem
        builder.setMessage("Deseja mesmo apagar esse imóvel?");
        //define um bot�o como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                VcImovel imovel = new VcImovel(idEdt);
                imovel.deleteCascade();
            }
        });
        //define um bot�o como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
                toast.show("Ação cancelada!");
            }
        });
        AlertDialog alerta;
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void salvaImovel(View v, int tipo){
        VcImovel imovel = new VcImovel(idEdt);
        int exec;
        int pos = strCadastro.indexOf(etCadastro.getText().toString());
        imovel.setId_imovel(Integer.parseInt(valImovel.get(pos)));
        imovel.setId_prod_focal(Integer.parseInt(valFocal.get(spProdFocal.getSelectedItemPosition())));
        imovel.setId_prod_peri(Integer.parseInt(valPeri.get(spProdPeri.getSelectedItemPosition())));
        imovel.setId_prod_neb(Integer.parseInt(valNeb.get(spProdNeb.getSelectedItemPosition())));
        imovel.setQt_focal(npFocal.getValue());
        imovel.setQt_peri(npPeri.getValue());
        imovel.setQt_neb(npNeb.getValue());
        exec = chkMec.isChecked() ? 1 : 0;
        imovel.setMecanico(exec);
        exec = chkAlt.isChecked() ? 1 : 0;
        imovel.setAlternativo(exec);
        exec = chkFoc.isChecked() ? 1 : 0;
        imovel.setFocal(exec);
        exec = chkPeri.isChecked() ? 1 : 0;
        imovel.setPeri(exec);
        exec = chkNeb.isChecked() ? 1 : 0;
        imovel.setNeb(exec);
        imovel.setStatus(status);

        imovel.setAgente(Storage.recupera("agente"));
        imovel.setDt_cadastro(Storage.recupera("data"));
        imovel.setId_execucao(Integer.parseInt(Storage.recupera("execucao")));

        switch (rgSituacao.getCheckedRadioButtonId()) {
            case R.id.chkTrabalhado:
                exec=1;
                break;
            case R.id.chkFechado:
                exec=2;
                break;
            default:
                exec=3;
                break;
        }
        imovel.setId_situacao(exec);
        String msg = idEdt>0 ? "Alterado com sucesso." : "Inserido com sucesso.";
        if (imovel.manipula()){
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
            toast.show(msg);
            if (tipo == 1) {
                chamaRecipiente(imovel.get_id(),0);
            } else {
                limpa();
            }
        }
    }

    private void limpa(){
        //spImovel.setSelection(0);
        etCadastro.setText(null);
        chkTrab.setChecked(true);
        npFocal.setValue(0);
        npPeri.setValue(0);
        npNeb.setValue(0);
        btRec.setEnabled(true);
        chkMec.setChecked(false);
        chkAlt.setChecked(false);
        chkFoc.setChecked(false);
        chkPeri.setChecked(false);
        chkNeb.setChecked(false);
    }

    public void chamaRecipiente(Long idFk, int id){
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("fkId", idFk);
        data.putInt("Id", id);
        data.putInt("tabela", 1);

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
