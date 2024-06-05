package com.sucen.sisamob;

import java.util.List;

import com.sucen.sisamob.R;

import producao.EditaRecAdapter;
import producao.Recipiente;
import producao.RecipienteList;
import utilitarios.MyToast;
import utilitarios.NumberPicker;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import entidades.GrupoRec;
import entidades.TipoRec;

public class RecipienteFragment extends Fragment {
    Spinner spGrupo, spTipo;
    NumberPicker npExistente, npAgua, npLarva;
    EditText etAmostra;
    Button btSalva, btVolta, btDeleta;
    List<String> valGrupo, valTipo;
    int TipoRec = 0; //pra atualizar o selecionado.
    ListView lvRecipiente;
    TextView tvRecip;
    Long idFk;
    int idRec;
    EditaRecAdapter edLista;
    Recipiente rec;
    Boolean isEdit = false;


    private OnFragmentInteractionListener mListener;

    public RecipienteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_recipiente, container, false);

        idRec = 0;//getArguments() != null ? getArguments().getInt("Id") : 0;
        idFk = getArguments().getLong("fkId",0);
        rec = new Recipiente(idRec);

        setupComps(v);


        return v;
    }

    private void Edita(){
        etAmostra.setText(rec.getAmostra());
        npExistente.setValue(rec.getExistente());
        npAgua.setValue(rec.getAgua());
        npLarva.setValue(rec.getLarva());
        int prod = valGrupo.indexOf(String.valueOf(rec.getId_grupo()));
        spGrupo.setSelection(prod);
        addItensOnTipo(String.valueOf(rec.getId_grupo()));
        TipoRec = valTipo.indexOf(String.valueOf(rec.getId_tipo()));
        btDeleta.setEnabled(true);
        isEdit = true;
    }


    private void setupComps(View v) {
        spGrupo = (Spinner) v.findViewById(R.id.spGrupo);
        spTipo = (Spinner) v.findViewById(R.id.spTipo);
        npExistente = (NumberPicker) v.findViewById(R.id.npExist);
        npAgua = (NumberPicker) v.findViewById(R.id.npAgua);
        npLarva = (NumberPicker) v.findViewById(R.id.npLarva);
        etAmostra = (EditText) v.findViewById(R.id.etAmostra);
        btSalva = (Button) v.findViewById(R.id.btSalvar);
        btSalva.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaRec(v);
            }
        });

        tvRecip = (TextView) v.findViewById(R.id.tvRecipientes);
        lvRecipiente = (ListView) v.findViewById(R.id.lvRecipiente);

        btVolta = (Button) v.findViewById(R.id.btVoltar);
        btVolta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                voltar();
            }
        });

        btDeleta = (Button) v.findViewById(R.id.btDelRec);
        btDeleta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rec.delete()){
                    criaLista();
                    limpaCampos();
                    idRec = 0;
                }
            }
        });

        spGrupo.setOnItemSelectedListener(onMudaGrupo);

        addItensOnGrupo();

        if(idRec>0){
            Edita();
            btDeleta.setEnabled(true);
        } else {
            btDeleta.setEnabled(false);
        }
        criaLista();
        lvRecipiente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                RecipienteList item = (RecipienteList) edLista.getItem(position);
              //  int id_rec = i
                //chamaRecipiente(imovel.get_id(), id_rec);
                idRec = item.getId();
                rec = new Recipiente(idRec);
                Edita();
            }
        });
    }

    private void criaLista() {
        int tabela 	= getArguments().getInt("tabela",0);
        edLista = new EditaRecAdapter(idFk, tabela);

        tvRecip.setText("Recipientes");
        lvRecipiente.setAdapter(edLista);
        calculeHeightListView();
    }

    OnItemSelectedListener onMudaGrupo = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            addItensOnTipo(valGrupo.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    private void addItensOnTipo(String grp){
        if (isEdit){
            isEdit = false;
        } else {
            TipoRec = 0;
        }

        TipoRec tipo = new TipoRec(getActivity());

        List<String> list = tipo.combo(grp);
        valTipo = tipo.id_Tipo;

        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(dados);
        spTipo.setSelection(TipoRec);
    }

    private void addItensOnGrupo() {
        GrupoRec grupo = new GrupoRec(getActivity());

        List<String> list = grupo.combo();
        valGrupo = grupo.id_grupo;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrupo.setAdapter(dados);
        spGrupo.setSelection(7);
    }

    private void calculeHeightListView() {
        int totalHeight = 0;
        if (edLista.getCount()>0) {
            ListAdapter adapter = lvRecipiente.getAdapter();
            int lenght = adapter.getCount();
            for (int i = 0; i < lenght; i++) {
                View listItem = adapter.getView(i, null, lvRecipiente);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = lvRecipiente.getLayoutParams();
            params.height = totalHeight + (lvRecipiente.getDividerHeight() * (adapter.getCount() - 1));
            lvRecipiente.setLayoutParams(params);
            lvRecipiente.requestLayout();
        }
    }

    public void salvaRec(View v){
        //pega os valores de quem chamou

        Long fkId 	= getArguments().getLong("fkId",0);
        int tabela 	= getArguments().getInt("tabela",0);
        int id 		= getArguments().getInt("Id",0);

        Recipiente rec = new Recipiente(idRec);
        rec.setId_grupo(Integer.parseInt(valGrupo.get(spGrupo.getSelectedItemPosition())));
        rec.setId_tipo(Integer.parseInt(valTipo.get(spTipo.getSelectedItemPosition())));
        rec.setExistente(npExistente.getValue());
        rec.setAgua(npAgua.getValue());
        rec.setLarva(npLarva.getValue());
        rec.setAmostra(etAmostra.getText().toString());
        rec.setTabela(tabela);
        rec.setId_fk(fkId);
        String msg = idRec>0 ? "Alterado com sucesso." : "Inserido com sucesso.";
        if (rec.manipula()){
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
            toast.show(msg);
            limpaCampos();
           // btDeleta.setEnabled(true);
            //idRec = (int)rec.get_id();
        }
        criaLista();
    }

    private void limpaCampos(){
        etAmostra.setText("");
        spGrupo.setSelection(7);
        spTipo.setSelection(0);
        npExistente.setValue(0);
        npAgua.setValue(0);
        npLarva.setValue(0);
        btDeleta.setEnabled(false);
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
