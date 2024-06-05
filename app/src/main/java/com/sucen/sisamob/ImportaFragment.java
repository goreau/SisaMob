package com.sucen.sisamob;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import utilitarios.MyToast;
import webservice.Utils;

public class ImportaFragment extends Fragment{
    RadioGroup rgTipo, rgNivel;
    RadioButton rbSistema, rbCadastro;
    AutoCompleteTextView etLocal;
    Button btImporta;
    TextView tvConecta;
    Switch swLimpa;

    int nivel;
    String webUri;
    String resultado = "Recebidos:\n";
    List<String> municipio, id_municipio, colegiado, id_colegiado, drs, id_drs, regional, id_regional;

    boolean limpar = true;

    MyToast toast;

    static Context context;
    private ProgressDialog load;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImportaFragment() {
        // Required empty public constructor
    }

    public static ImportaFragment newInstance(String param1, String param2) {
        ImportaFragment fragment = new ImportaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_importa, container, false);
        setupComps(v);
        return v;
    }

    private void setupComps(View v) {
        this.context = PrincipalActivity.getSisamobContext();
        tvConecta   = (TextView) v.findViewById(R.id.tvConecta);
        rgTipo      = (RadioGroup) v.findViewById(R.id.rgTipo);
        rgNivel     = (RadioGroup) v.findViewById(R.id.rgNivel);
        rbSistema   = (RadioButton) v.findViewById(R.id.rbSistema);
        rbCadastro  = (RadioButton) v.findViewById(R.id.rbCadastro);
        etLocal     = (AutoCompleteTextView) v.findViewById(R.id.etLocal);
        btImporta   = (Button) v.findViewById(R.id.btImporta);
        swLimpa     = (Switch) v.findViewById(R.id.swLimpa);

        if(isConnected()){
            //  tvIsConnected.setBackgroundColor(0xFF00CC00);
            Drawable img = this.context.getResources().getDrawable(R.drawable.verde);
            tvConecta.setText("Conectado");
            tvConecta.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        else{
            Drawable img = this.context.getResources().getDrawable(R.drawable.vermelho);
            tvConecta.setText("Não conectado");
            tvConecta.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }

        swLimpa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
                if (isChecked) {
                    limpar = true;
                } else {
                    Drawable myIcon = getResources().getDrawable( android.R.drawable.ic_dialog_alert );
                    ColorFilter filter = new LightingColorFilter( Color.RED, Color.RED);
                    myIcon.setColorFilter(filter);
                    new AlertDialog.Builder(getActivity())
                            .setIcon(myIcon)//  android.R.drawable.ic_dialog_alert)
                            .setTitle("Adicionar informação sem excluir as existentes?")
                            .setMessage("Essa opção só deve ser escolhida por usuários que trabalham em mais de um município, caso contrário pode haver duplicação de registros. Deseja manter essa opção?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    limpar = false;
                                }

                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    limpar = true;
                                    swLimpa.setChecked(true);
                                }
                            })
                            .show();
                }
            }
        });

        btImporta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chamaProcessa(v);
            }
        });

        rgNivel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.chkRegional:
                        addItemsOnLocal(regional);
                        break;
                    case R.id.chkDRS:
                        addItemsOnLocal(drs);
                        break;
                    case R.id.chkColegiado:
                        addItemsOnLocal(colegiado);
                        break;
                    default:
                        addItemsOnLocal(municipio);
                        break;
                }
            }
        });

        criaEntradas();
    }

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

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void criaEntradas() {
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            //carrega os xml
            InputStream in_mun = getActivity().getApplicationContext().getAssets().open("municipio.xml");
            InputStream in_idmun = getActivity().getApplicationContext().getAssets().open("id_municipio.xml");
            InputStream in_col = getActivity().getApplicationContext().getAssets().open("colegiado.xml");
            InputStream in_idcol = getActivity().getApplicationContext().getAssets().open("id_colegiado.xml");
            InputStream in_drs = getActivity().getApplicationContext().getAssets().open("drs.xml");
            InputStream in_iddrs = getActivity().getApplicationContext().getAssets().open("id_drs.xml");
            InputStream in_reg = getActivity().getApplicationContext().getAssets().open("regional.xml");
            InputStream in_idreg = getActivity().getApplicationContext().getAssets().open("id_regional.xml");

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            //popula os List
            parser.setInput(in_mun, null);
            municipio = parseXML(parser);

            parser.setInput(in_idmun, null);
            id_municipio = parseXML(parser);

            parser.setInput(in_col, null);
            colegiado = parseXML(parser);

            parser.setInput(in_idcol, null);
            id_colegiado = parseXML(parser);

            parser.setInput(in_drs, null);
            drs = parseXML(parser);

            parser.setInput(in_iddrs, null);
            id_drs = parseXML(parser);

            parser.setInput(in_reg, null);
            regional = parseXML(parser);

            parser.setInput(in_idreg, null);
            id_regional = parseXML(parser);

            addItemsOnLocal(municipio);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
         //   Log.e("ERRO","Erro Pull Parser");
        } catch (IOException e) {
            e.printStackTrace();
          //  Log.e("ERRO","Erro IO");
        }
    }

    public void addItemsOnLocal(List<String> list) {
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etLocal.setAdapter(dados);
    }

    private List<String> parseXML(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String parc="";
        List<String> generico = new ArrayList<String>();

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.TEXT:
                    parc = parser.getText().trim();
                    if (!parc.equals("")){
                        generico.add(parc);
                    }
                    break;
            }
            eventType = parser.next();
        }
        return generico;
    }

    public void chamaProcessa(View v){
        String id = "1";
        int pos, nivel=1;
        final GetJson download = new GetJson();
        download.context = context;
        toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);

        if (rgTipo.getCheckedRadioButtonId()!=R.id.chkSistema) {
            switch (rgNivel.getCheckedRadioButtonId()) {
                case R.id.chkRegional:
                    nivel = 4;
                    pos = regional.indexOf(etLocal.getText().toString());
                    if (pos<0){
                        toast.show("É necessário escolher um local para importar.");
                        return;
                    }
                    id = id_regional.get(pos);
                    break;
                case R.id.chkDRS:
                    nivel = 3;
                    pos = drs.indexOf(etLocal.getText().toString());
                    if (pos<0){
                        toast.show("É necessário escolher um local para importar.");
                        return;
                    }
                    id = id_drs.get(pos);
                    break;
                case R.id.chkColegiado:
                    nivel = 2;
                    pos = colegiado.indexOf(etLocal.getText().toString());
                    if (pos<0){
                        toast.show("É necessário escolher um local para importar.");
                        return;
                    }
                    id = id_colegiado.get(pos);
                    break;
                default:
                    nivel = 1;
                    pos = municipio.indexOf(etLocal.getText().toString());
                    if (pos<0){
                        toast.show("É necessário escolher um local para importar.");
                        return;
                    }
                    id = id_municipio.get(pos);
                    break;
            }
        }
        switch (rgTipo.getCheckedRadioButtonId()) {
            case R.id.chkTerritorio:
                webUri = "http://vigent.saude.sp.gov.br/sisapi/api/cadastro/base.php?nivel=" + nivel + "&id=" + id;
                break;
            case R.id.chkCadImovel:
                webUri = "http://vigent.saude.sp.gov.br/sisapi/api/cadastro/imovel.php?nivel=" + nivel + "&id=" + id;
                break;
            case R.id.chkCadNav:
                webUri = "http://vigent.saude.sp.gov.br/sisapi/api/cadastro/area_nav.php?nivel=" + nivel + "&id=" + id;
                break;
            default:
                webUri = "http://vigent.saude.sp.gov.br/sisapi/api/sistema/base.php";
                break;
        }

        /*switch (rgTipo.getCheckedRadioButtonId()) {
            case R.id.chkTerritorio:
                webUri = "http://200.144.1.24/sisapi/api/cadastro/base.php?nivel=" + nivel + "&id=" + id;
                break;
            case R.id.chkCadImovel:
                webUri = "http://200.144.1.24/sisapi/api/cadastro/imovel.php?nivel=" + nivel + "&id=" + id;
                break;
            case R.id.chkCadNav:
                webUri = "http://200.144.1.24/sisapi/api/cadastro/area_nav.php?nivel=" + nivel + "&id=" + id;
                break;
            default:
                webUri = "http://200.144.1.24/sisapi/api/sistema/base.php";
                break;
        }*/


        download.execute();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //importação
    private class GetJson extends AsyncTask<Void, Void, String> {
        private Context context;
        @Override
        protected void onPreExecute(){
            load = ProgressDialog.show(getActivity(), "Por favor, aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected String doInBackground(Void... params) {
            Utils util = new Utils(context);
            util.limpar = limpar;
            return util.getInformacao(webUri);
        }

        @Override
        protected void onPostExecute(String result){
            mostraResultado(result);
           // Log.d("Resultado", result);
            load.dismiss();
        }
    }

    private void mostraResultado(String resultado){
        Fragment rFragment = new RelImportaFragment();
        Bundle data = new Bundle();
        // Setting the index of the currently selected item of mDrawerList
        data.putString("resultado", resultado);

        // Setting the position to the fragment
        rFragment.setArguments(data);

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, rFragment);

        // Committing the transaction
        ft.commit();
    }

}
