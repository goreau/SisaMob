package com.sucen.sisamob;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import entidades.Imovel;
import producao.AladoIm;
import utilitarios.MyToast;
import utilitarios.NumberPicker;
import utilitarios.Storage;

public class AladoImFragment extends Fragment {
    EditText etUmidade, etTemperatura, etAmLarva, etAmIntra, etAmPeri;
    TextView tvLat, tvLong;
    Spinner spImovel;

    Button btSalva, btExclui;

    RadioGroup rgSituacao, rgSubAtiv;
    NumberPicker npMoradores, npRecLarva;

    RadioButton chkTrab, chkPend, chkPE, chkIE;

    String id_municipio, id_atividade;
    List<String> valImovel;
    int MY_PERMISSIONS_REQUEST_GPS;

    private AlertDialog alerta;

    Long idEdt;
    AladoIm imovel;
    int status;

    private AladoImFragment.OnFragmentInteractionListener mListener;

    public AladoImFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alado_im, container, false);
        idEdt = getArguments() != null ? getArguments().getLong("Id") : 0;
        imovel = new AladoIm(idEdt);
        setupComps(v);
        // Updating the action bar title
        //  getActivity().getActionBar().setTitle("Im�vel");
        startGPS(v); //ligar aqui quando tiver GPS
        return v;
    }

    private void setupComps(View v) {
        spImovel = (Spinner) v.findViewById(R.id.spImovel);

        etUmidade = (EditText) v.findViewById(R.id.txtUmidade);
        etTemperatura = (EditText) v.findViewById(R.id.txtTemperatura);
        etAmLarva = (EditText) v.findViewById(R.id.txtAmLarva);
        etAmIntra = (EditText) v.findViewById(R.id.txtAmIntra);
        etAmPeri = (EditText) v.findViewById(R.id.txtAmPeri);

        rgSituacao = (RadioGroup) v.findViewById(R.id.rgSituacao);
        rgSubAtiv = (RadioGroup) v.findViewById(R.id.rgSubAtividade);

        npMoradores = (NumberPicker) v.findViewById(R.id.npMoradores);
        npRecLarva = (NumberPicker) v.findViewById(R.id.npRecipLarva);

        chkTrab = (RadioButton) v.findViewById(R.id.rbTrab);
        chkPend = (RadioButton) v.findViewById(R.id.rbPend);
        chkPE = (RadioButton) v.findViewById(R.id.rbPE);
        chkIE = (RadioButton) v.findViewById(R.id.rbIE);

        tvLat = (TextView) v.findViewById(R.id.tvlatitude);
        tvLong = (TextView) v.findViewById(R.id.tvLongitude);

        btSalva = (Button) v.findViewById(R.id.btNovoAlado);
        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaImovel(v);
            }
        });


        btExclui = (Button) v.findViewById(R.id.btExcluiAlado);
        btExclui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apagaImovel(v);
            }
        });

        rgSubAtiv.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                String ativ;
                if (checkedId == R.id.rbPE){
                    ativ="1";
                } else {
                    ativ="2";
                }
                addItensOnImovel(ativ);
            }
        });

        if (idEdt > 0) {
            Edita();
            //	etImovel.setText(9);
            btExclui.setEnabled(true);
            //   btConsRec.setEnabled(true);
        } else {
            btExclui.setEnabled(false);
            //   btConsRec.setEnabled(false);
        }

    }

    private void Edita() {
        Storage.insere("agente",imovel.getAgente());
        Storage.insere("data",imovel.getDt_cadastro());

        etUmidade.setText(imovel.getUmidade().toString());
        etTemperatura.setText(imovel.getTemperatura().toString());
        etAmLarva.setText(imovel.getAm_larva());
        etAmIntra.setText(imovel.getAm_intra());
        etAmPeri.setText(imovel.getAm_peri());
        npMoradores.setValue(imovel.getMoradores());
        npRecLarva.setValue(imovel.getRec_larva());

        status = imovel.getStatus();

        int sit = imovel.getId_situacao();
        switch (sit) {
            case 1:
                chkTrab.setChecked(true);
                break;
            case 2:
                chkPend.setChecked(true);
                break;
        }
        sit = imovel.getId_sub_ativ();
        switch (sit) {
            case 1:
                chkPE.setChecked(true);
                break;
            case 2:
                chkIE.setChecked(true);
                break;
        }
        int prod = valImovel.indexOf(Integer.toString(imovel.getId_imovel()));
        spImovel.setSelection(prod);
    }

    public void salvaImovel(View v){
        AladoIm imovel = new AladoIm(idEdt);
        int sit = 1;
        imovel.setId_imovel(Integer.parseInt(valImovel.get(spImovel.getSelectedItemPosition())));
        imovel.setUmidade(Float.parseFloat(etUmidade.getText().toString()));
        imovel.setTemperatura(Float.parseFloat(etTemperatura.getText().toString()));
        imovel.setMoradores(npMoradores.getValue());
        imovel.setRec_larva(npRecLarva.getValue());
        imovel.setAm_larva(etAmLarva.getText().toString());
        imovel.setAm_intra(etAmIntra.getText().toString());
        imovel.setAm_peri(etAmPeri.getText().toString());

        imovel.setAgente(Storage.recupera("agente"));
        imovel.setDt_cadastro(Storage.recupera("data"));
        imovel.setId_municipio(Integer.parseInt(Storage.recupera("municipio")));
        imovel.setId_atividade(Integer.parseInt(Storage.recupera("atividade")));
        imovel.setId_execucao(Integer.parseInt(Storage.recupera("execucao")));
        imovel.setLatitude((String) tvLat.getText());
        imovel.setLongitude((String) tvLong.getText());
        imovel.setStatus(status);

        if (idEdt == 0) { //so incrementa se nao for edicao
            Storage.insere("imovel", imovel.getId_imovel());
        }
        switch (rgSituacao.getCheckedRadioButtonId()) {
            case R.id.rbTrab:
                sit = 1;
                break;
            case R.id.rbPend:
                sit = 2;
                break;
        }
        imovel.setId_situacao(sit);

        switch (rgSubAtiv.getCheckedRadioButtonId()) {
            case R.id.rbPE:
                sit = 1;
                break;
            case R.id.rbIE:
                sit = 2;
                break;
        }
        imovel.setId_sub_ativ(sit);

        String msg = idEdt > 0 ? "Alterado com sucesso." : "Inserido com sucesso.";
        if (imovel.manipula()) {
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
            toast.show(msg);
        }
       // limpa();
    }

    /*private void limpa(){
        etImovel.setText(novoImovel());
        etCasa.setText("");
        chkTrab.setChecked(true);
        npMoradores.setValue(0);
        npRecLarva.setValue(0);
        etUmidade.setText("");
        etTemperatura.setText("");
        etAmLarva.setText("");
        etAmIntra.setText("");
        etAmPeri.setText("");
    }*/

    private void apagaImovel(View v) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //define o titulo
        builder.setTitle("Excluir");
        //define a mensagem
        builder.setMessage("Deseja mesmo apagar esse imovel?");
        //define um bot�o como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                AladoIm imovel = new AladoIm(idEdt);
                imovel.delete();
            }
        });
        //define um bot�o como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
                toast.show("Ação cancelada!");
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();

    }

    private void addItensOnImovel(String subAtiv) {
        Imovel im = new Imovel(getActivity());
        id_municipio = Storage.recupera("municipio");

        //  Log.w("Mun/Ativ:",id_municipio+"/"+id_atividade);
        List<String> list = im.combo(id_municipio,subAtiv);
        valImovel = im.id_Im;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImovel.setAdapter(dados);
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
        if (context instanceof AladoFragment.OnFragmentInteractionListener) {
            mListener = (AladoImFragment.OnFragmentInteractionListener) context;
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

    public void startGPS(View v) {
        final View v1 = v;
        Context ctx = getActivity();
        LocationManager lManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        } else {
            LocationListener lListener = new LocationListener() {

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onLocationChanged(Location locat) {
                    updateView(locat, v1);
                }
            };
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_GPS);
                }

                //  MyToast toast = new MyToast(ctx, Toast.LENGTH_SHORT);
                // toast.show("É necessário autorizar o uso do GPS");
                return;
            }
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);
            //lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,lListener);
        }
    }

    //  M�todo que faz a atualiza��o da tela para o usu�rio.
    public void updateView(Location locat, View v){
        DecimalFormat df = new DecimalFormat("##0.00000");

        Double latitude 	= locat.getLatitude();
        Double longitude 	= locat.getLongitude();

        tvLat.setText(df.format(latitude));
        tvLong.setText(df.format(longitude));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}