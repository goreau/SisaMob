package com.sucen.sisamob;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entidades.Quarteirao;
import producao.Alado;
import producao.AladoIm;
import producao.Condicao;
import producao.Coordenadas;
import producao.Recipiente;
import producao.VcFolha;
import producao.VcImovel;
import producao.VcOvitrampa;
import utilitarios.MyToast;
import webservice.Utils;
import webservice.onServiceCallCompleted;


public class ExportaFragment extends Fragment {
    static Context context;
    MyToast toast;
    private ProgressDialog load;
    private TextView tvConecta, tvResumo;
    private Button btExpo;
    private Spinner spQuadra;
    private int registros = 0;
    int rec = 0;
    String webUri;
   // int tabela;
    postAsync pAsync;
    String resultado = "Enviados:\n";

    HashMap<String, String> map = new HashMap<String, String>();
    List<String> valQuart;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ExportaFragment() {
        // Required empty public constructor
    }

    public static ExportaFragment newInstance(String param1, String param2) {
        ExportaFragment fragment = new ExportaFragment();
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
        View v = inflater.inflate(R.layout.fragment_envio, container, false);
        setupComps(v);
        return v;
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

    private void setupComps(View v) {
        context = getActivity();
        toast = new MyToast(context, Toast.LENGTH_SHORT);

        //Initialize Progress Dialog properties
        load = new ProgressDialog(context);
        load.setMessage("Sincronizando dados com servidor remoto. Aguarde...");
        load.setCancelable(false);

        tvConecta = (TextView) v.findViewById(R.id.tvConecta);
        tvResumo = (TextView) v.findViewById(R.id.tvResEnvio);
        btExpo = (Button) v.findViewById(R.id.btSincroniza);
        btExpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sincro(v);
            }
        });
        spQuadra = (Spinner) v.findViewById(R.id.spEnviaQuadra);

        montaDados();
        if(isConnected()){
            //  tvIsConnected.setBackgroundColor(0xFF00CC00);
            Drawable img = context.getResources().getDrawable(R.drawable.verde);
            tvConecta.setText("Conectado");
            tvConecta.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
        else{
            Drawable img = context.getResources().getDrawable(R.drawable.vermelho);
            tvConecta.setText("Não conectado");
            tvConecta.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void sincro(View v){
        sincVcFolha();
    }

    public void sincVcFolha(){
        tvResumo.setText("Sincronizados:\n");
        final VcFolha controller = new VcFolha(0);
        controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String qt = valQuart.get(spQuadra.getSelectedItemPosition());
            String dados = controller.composeJSONfromSQLite(qt);
            //System.out.println(dados);
          //  webUri = "http://200.144.1.24/dados/exporta.php?tipo=vc_folha&dados=" + dados;
         //   pAsync.setmTipo("vc_folha");
         //   pAsync.setmDados(dados);
        //    pAsync.execute();
            final postAsync download = new postAsync();
            download.mTipo  = "vc_folha";
            download.mDados = dados;
          //  download.context = context;
            download.mTab=1;
            download.execute();
        }else{
         //   toast.show("Visitas a imóveis: 0 registros!");
        }
     //   toast.show("Visitas a imóveis Ok!");
        sincVcImovel();
        registros++;
    }


    private void sincVcImovel(){

        final VcImovel controller = new VcImovel(0);
        controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String dados = controller.composeJSONfromSQLite();

            final postAsync download = new postAsync();
            download.mTipo  = "vc_imovel";
            download.mDados = dados;
            download.mTab=2;
            download.execute();
        }else{
          //  toast.show("Visita Imóveis: 0 registros!");
        }
      //  toast.show("Visita Imóveis Ok!");
        sincOvitrampa();
        registros++;
    }

    public void sincOvitrampa(){

        final VcOvitrampa controller = new VcOvitrampa(0);
        controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String dados = controller.composeJSONfromSQLite();

            final postAsync download = new postAsync();
            download.mTipo  = "vc_ovitrampa";
            download.mDados = dados;
            download.mTab=3;
            download.execute();

        }else{
         //   toast.show("Ovitrampa: 0 registros!");
        }
      //  toast.show("Ovitrampa Ok!");
        sincCoordenadas();
        registros++;
    }

    public void sincCoordenadas(){

        final Coordenadas controller = new Coordenadas(0);
      //  controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String dados = controller.composeJSONfromSQLite();

            final postAsync download = new postAsync();
            download.mTipo  = "coordenadas";
            download.mDados = dados;
            download.mTab=4;
            download.execute();
        }else{
        //    toast.show("Coordenadas: 0 registros!");
        }
      //  toast.show("Coordenadas Ok!");
        sincAlado();
        registros++;
    }

    private void sincAlado() {
        final Alado controller = new Alado(0);
        controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String dados = controller.composeJSONfromSQLite();
              System.out.println(dados);
            //  webUri = "http://200.144.1.24/dados/exporta.php?tipo=vc_folha&dados=" + dados;
            //   pAsync.setmTipo("vc_folha");
            //   pAsync.setmDados(dados);
            //    pAsync.execute();
            final postAsync download = new postAsync();
            download.mTipo  = "alado";
            download.mDados = dados;
            //  download.context = context;
            download.mTab=5;
            download.execute();
        }else{
            //   toast.show("Visitas a imóveis: 0 registros!");
        }
        //   toast.show("Visitas a imóveis Ok!");
        sincAladoIm();
        registros++;
    }

    private void sincAladoIm() {
        final AladoIm controller = new AladoIm(0);
        controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String dados = controller.composeJSONfromSQLite();
          //   System.out.println(dados);
            //  webUri = "http://200.144.1.24/dados/exporta.php?tipo=vc_folha&dados=" + dados;
            //   pAsync.setmTipo("vc_folha");
            //   pAsync.setmDados(dados);
            //    pAsync.execute();
            final postAsync download = new postAsync();
            download.mTipo  = "alado_im";
            download.mDados = dados;
            //  download.context = context;
            download.mTab=6;
            download.execute();
        }else{
            //   toast.show("Visitas a imóveis: 0 registros!");
        }
        //   toast.show("Visitas a imóveis Ok!");
        registros++;
    }

   /* public void sincRecipientes(){
        final Recipiente controller = new Recipiente(0);
        //  controller.getAllImoveis();
        if(controller.dbSyncCount() != 0){
            load.show();
            String dados = controller.composeJSONfromSQLite();
           // System.out.println(dados);
            webUri = "http://200.144.1.24/dados/exporta.php?tipo=recipiente&dados=" + dados;
            final GetJson download = new GetJson();
            download.context = context;
            download.tab=5;
            download.execute();
        }else{
            toast.show("Recipientes Ok!");
        }
        toast.show("Sincronização concluída!");
        registros++;
    }*/

    private void montaDados(){
        int quant;
        String resumo = "";

        VcImovel vcImovel = new VcImovel(0);
        quant = vcImovel.dbSyncCount();
        if (quant>0){
            map.put("vc_imovel", vcImovel.composeJSONfromSQLite());
            resumo += "\n  Imóveis Cadastrados: " + quant + " registros";
        }
        VcOvitrampa vcOvi = new VcOvitrampa(0);
        quant = vcOvi.dbSyncCount();
        if (quant>0){
            map.put("vc_ovitrampa", vcOvi.composeJSONfromSQLite());
            resumo += "\n  Ovitrampas: " + quant + " registros";
        }
        VcFolha vcFolha = new VcFolha(0);
        quant = vcFolha.dbSyncCount();
        if (quant>0){
            map.put("vc_folha", vcFolha.composeJSONfromSQLite("0"));
            resumo += "\n  Visita a imóveis: " + quant + " registros";
            addItemsOnQuart();
        }
        Coordenadas rec = new Coordenadas(0);
        quant = rec.dbSyncCount();
        if (quant>0){
            map.put("coordenadas", rec.composeJSONfromSQLite());
            resumo += "\n  Coordenadas: " + quant + " registros";
        }
      /*  Condicao cond = new Condicao(0);
        quant = cond.dbSyncCount();
        if (quant>0){
            map.put("condicao", cond.composeJSONfromSQLite());
            resumo += "\n  Classificação de imóveis: " + quant + " registros";
        }*/
        Alado al = new Alado(0);
        quant = al.dbSyncCount();
        if (quant>0){
            map.put("alado", al.composeJSONfromSQLite());
            resumo += "\n  Capt Alados (Pré e Pós): " + quant + " registros";
        }
        AladoIm alim = new AladoIm(0);
        quant = alim.dbSyncCount();
        if (quant>0){
            map.put("alado_im", alim.composeJSONfromSQLite());
            resumo += "\n  Capt Alado Im Cad: " + quant + " registros";
        }
        tvResumo.setText(tvResumo.getText().toString() + resumo);
    }

    private void addItemsOnQuart() {
        VcFolha folha = new VcFolha(0);

        List<String> list = folha.combo();
        valQuart = folha.id_Quart;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuadra.setAdapter(dados);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class GetJson extends AsyncTask<Void, Void, String> {
        private Context context;
        private int tab;
        @Override
        protected void onPreExecute(){
            load.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            Utils util = new Utils(context);
            return util.sendInformacao(webUri,tab);
        }

        @Override
        protected void onPostExecute(String result){
            tvResumo.append(result+"\n");
            load.dismiss();
        }
    }

    public class postAsync extends AsyncTask<String, Boolean, Boolean> implements onServiceCallCompleted
    {
        private onServiceCallCompleted mListener = this;
      //  private static final String URL = "http://200.144.1.24/dados/exporta.php?tipo=";
        private String mTipo;
        private String mDados;
        private int mTab;

        @Override
        protected Boolean doInBackground(final String... params)
        {
            //final String url = "https://vigent.saude.sp.gov.br/sisapi/exporta.php?tipo="+mTipo; //"http://200.144.1.24/sisapi/api/recebe/dados.php?tipo="+mTipo;//
            final String url = "http://vigent.saude.sp.gov.br/sisapi/api/recebe/dados.php?tipo="+mTipo;
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(context);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            try
                            {
                                Utils util = new Utils(context);
                                String retorno = util.parseRetorno(response,mTab);
                                mListener.onServiceCallComplete(retorno);
                            }
                            catch (JsonParseException e)
                            {
                                Log.e(mTipo, e.getMessage());
                                tvResumo.append(e.getMessage());
                                load.dismiss();
                            }
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    tvResumo.append("Erro: "+error.getMessage());
                    toast.show("Verifique se os registros foram recebidos.");
                    load.dismiss();
                }
            })
            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> postParams = new HashMap<>();
                    postParams.put("dados", mDados);
                    return postParams;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

            return true;
        }

        @Override
        public boolean onServiceCallComplete(String response)
        {
            tvResumo.append(response+"\n");
            load.dismiss();
            toast.show("Sincronização concluída!");
            return true;
        }

    }
}
