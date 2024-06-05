package com.sucen.sisamob;

import java.text.DecimalFormat;

import androidx.core.app.ActivityCompat;

import producao.VcFolha;
import utilitarios.MyToast;
import utilitarios.NumberPicker;
import utilitarios.Storage;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ImovelFolhaFragment extends Fragment {
    EditText etImovel, etCasa;
    TextView tvLat, tvLong;

    Button btRec;

    RadioGroup rgSituacao;
    NumberPicker npFocal, npPeri, npNeb;

    RadioButton chkTrab, chkFech, chkDes, chkTemp, chkParc,chkRec;
    CheckBox chkMec, chkAlt, chkFoc, chkPeri, chkNeb;

    int MY_PERMISSIONS_REQUEST_GPS;

    private AlertDialog alerta;

    String id_municipio, id_atividade, id_quarteirao;

    Long idEdt;
    VcFolha imovel;
    int status;

    private OnFragmentInteractionListener mListener;

    public ImovelFolhaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_folha, container, false);
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        idEdt = getArguments() != null ? getArguments().getLong("Id") : 0;
        imovel = new VcFolha(idEdt);
        setupComps(v);
        // Updating the action bar title
        //  getActivity().getActionBar().setTitle("Im�vel");

        startGPS(v); //ligar aqui quando tiver GPS
        return v;
    }

    private void Edita() {
        etImovel.setText(String.valueOf(imovel.getImovel()));
        etCasa.setText(imovel.getCasa());
        npFocal.setValue(imovel.getQt_focal());
        npPeri.setValue(imovel.getQt_peri());
        npNeb.setValue(imovel.getQt_neb());
        Storage.insere("atividade",Integer.toString(imovel.getId_atividade()));
        Storage.insere("agente",imovel.getAgente());
        Storage.insere("data",imovel.getDt_cadastro());
        Storage.insere("execucao",Integer.toString(imovel.getId_execucao()));
        Storage.insere("municipio",Integer.toString(imovel.getId_municipio()));
        Storage.insere("area_nav",Integer.toString(imovel.getId_area_nav()));
        Storage.insere("quarteirao",Integer.toString(imovel.getId_quarteirao()));
        Storage.insere("id_tipo",Integer.toString(imovel.getId_tipo()));

      /*  int prod = valFocal.indexOf(imovel.getId_prod_focal());
        spProdFocal.setSelection(prod);
        prod = valPeri.indexOf(imovel.getId_prod_peri());
        spProdPeri.setSelection(prod);
        prod = valNeb.indexOf(imovel.getId_prod_neb());
        spProdNeb.setSelection(prod);*/
        tvLat.setText(imovel.getLatitude());
        tvLong.setText(imovel.getLongitude());

        chkMec.setChecked(imovel.getMecanico() > 0);
        chkAlt.setChecked(imovel.getAlternativo() > 0);
        chkFoc.setChecked(imovel.getFocal() > 0);
        chkPeri.setChecked(imovel.getPeri() > 0);
        chkNeb.setChecked(imovel.getNeb() > 0);
        status = imovel.getStatus();

        int sit = imovel.getId_situacao();

        switch (sit) {
            case 1:
                chkTrab.setChecked(true);
                break;
            case 2:
                chkFech.setChecked(true);
                break;
            case 3:
                chkDes.setChecked(true);
                break;
            case 4:
                chkTemp.setChecked(true);
                break;
            case 5:
                chkParc.setChecked(true);
                break;
            default:
                chkRec.setChecked(true);
                break;
        }
    }

    private void setupComps(View v) {
        etImovel = (EditText) v.findViewById(R.id.etImovel);
        etCasa   = (EditText) v.findViewById(R.id.etCasa);
        rgSituacao = (RadioGroup) v.findViewById(R.id.rgSituacao);
        npFocal = (NumberPicker) v.findViewById(R.id.npFocal);
        npPeri = (NumberPicker) v.findViewById(R.id.npPerifocal);
        npNeb = (NumberPicker) v.findViewById(R.id.npNebulizacao);

        chkTrab = (RadioButton) v.findViewById(R.id.chkTrabalhado);
        chkFech = (RadioButton) v.findViewById(R.id.chkFechado);
        chkDes = (RadioButton) v.findViewById(R.id.chkDesocupado);
        chkTemp = (RadioButton) v.findViewById(R.id.chkTemporada);
        chkParc = (RadioButton) v.findViewById(R.id.chkParcial);
        chkRec = (RadioButton) v.findViewById(R.id.chkRecusa);

        chkMec = (CheckBox) v.findViewById(R.id.chkMecanico);
        chkAlt = (CheckBox) v.findViewById(R.id.chkAlternativo);
        chkPeri = (CheckBox) v.findViewById(R.id.chkPeri);
        chkFoc = (CheckBox) v.findViewById(R.id.chkFocal);
        chkNeb = (CheckBox) v.findViewById(R.id.chkNeb);

        tvLat = (TextView) v.findViewById(R.id.tvlatitude);
        tvLong = (TextView) v.findViewById(R.id.tvLongitude);

        btRec = (Button) v.findViewById(R.id.btCondicao);
        btRec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaImovel(v, 1);
            }
        });


        Button btNovo = (Button) v.findViewById(R.id.btNovo);
        btNovo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaImovel(v, 2);
            }
        });

        Button btDel = (Button) v.findViewById(R.id.btExcluiIm);
        btDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                apagaImovel(v);
            }
        });

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

        chkDes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(!chkDes.isChecked());
            }
        });

        chkTemp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(!chkTemp.isChecked());
            }
        });
        chkParc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(chkParc.isChecked());
            }
        });
        chkRec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btRec.setEnabled(!chkRec.isChecked());
            }
        });

        if (idEdt > 0) {
            Edita();
            //	etImovel.setText(9);
            btDel.setEnabled(true);
         //   btConsRec.setEnabled(true);
        } else {
            etImovel.setText(novoImovel());
            btDel.setEnabled(false);
         //   btConsRec.setEnabled(false);
        }
    }




    public void salvaImovel(View v, int tipo) {
        VcFolha imovel = new VcFolha(idEdt);
        int sit;

        imovel.setImovel(Integer.parseInt(etImovel.getText().toString()));
        imovel.setCasa(etCasa.getText().toString());
        imovel.setId_prod_focal(Integer.parseInt(Storage.recupera("prod_focal")));
        imovel.setId_prod_peri(Integer.parseInt(Storage.recupera("prod_peri")));
        imovel.setId_prod_neb(Integer.parseInt(Storage.recupera("prod_neb")));
        imovel.setQt_focal(npFocal.getValue());
        imovel.setQt_peri(npPeri.getValue());
        imovel.setQt_neb(npNeb.getValue());
        sit = chkMec.isChecked() ? 1 : 0;
        imovel.setMecanico(sit);
        sit = chkAlt.isChecked() ? 1 : 0;
        imovel.setAlternativo(sit);
        sit = chkFoc.isChecked() ? 1 : 0;
        imovel.setFocal(sit);
        sit = chkPeri.isChecked() ? 1 : 0;
        imovel.setPeri(sit);
        sit = chkNeb.isChecked() ? 1 : 0;
        imovel.setNeb(sit);

        imovel.setAgente(Storage.recupera("agente"));
        imovel.setDt_cadastro(Storage.recupera("data"));
        imovel.setId_execucao(Integer.parseInt(Storage.recupera("execucao")));
        imovel.setId_municipio(Integer.parseInt(Storage.recupera("municipio")));
        imovel.setId_atividade(Integer.parseInt(Storage.recupera("atividade")));
        imovel.setId_area_nav(Integer.parseInt(Storage.recupera("area_nav")));
        imovel.setId_quarteirao(Integer.parseInt(Storage.recupera("quarteirao")));
        imovel.setId_tipo(Integer.parseInt(Storage.recupera("id_tipo")));
        imovel.setLatitude((String) tvLat.getText());
        imovel.setLongitude((String) tvLong.getText());
        imovel.setStatus(status);

        if (idEdt == 0) { //so incrementa se nao for edicao
            Storage.insere("imovel", imovel.getImovel());
        }
        switch (rgSituacao.getCheckedRadioButtonId()) {
            case R.id.chkTrabalhado:
                sit = 1;
                break;
            case R.id.chkFechado:
                sit = 2;
                break;
            case R.id.chkDesocupado:
                sit = 3;
                break;
            case R.id.chkTemporada:
                sit = 4;
                break;
            case R.id.chkParcial:
                sit = 5;
                break;
            default:
                sit = 6;
                break;
        }
        imovel.setId_situacao(sit);

        String msg = idEdt > 0 ? "Alterado com sucesso." : "Inserido com sucesso.";
        if (imovel.manipula()) {
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
            toast.show(msg);
            if (tipo == 1) {
                //chamaRecipiente(imovel.get_id());
                chamaCondicao(imovel.get_id());
            } else {
                limpa();
            }
        }
    }

    private void limpa(){
        etImovel.setText(novoImovel());
        etCasa.setText("");
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

    private void apagaImovel(View v) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //define o titulo
        builder.setTitle("Excluir");
        //define a mensagem
        builder.setMessage("Deseja mesmo apagar esse im�vel?");
        //define um bot�o como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                VcFolha imovel = new VcFolha(idEdt);
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
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();

    }

    private String novoImovel() {
        int im = Storage.recuperaI("imovel") + 1;
        idEdt = (long) 0;
        return String.valueOf(im);
    }

    public void chamaCondicao(Long id) {
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("fkId", id);
        data.putInt("tabela", 2);
        if (idEdt > 0) {
            data.putInt("Edt", 1);
        }

       // Fragment frag = new CondicaoFragment();
        Fragment frag = new RecipienteFragment();

        // Setting the id
        frag.setArguments(data);
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

  /*  public void consultaRec(Long id) {
        MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);
        toast.show("Consultar recipiente");
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("fkId", id);


        Fragment frag = new ConsultaRecFragment();

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
    }*/


    public void chamaRecipiente(Long idFk, int id) {
        // Creating a Bundle object
        Bundle data = new Bundle();

        // Setting the index of the currently selected item of mDrawerList
        data.putLong("fkId", idFk);
        data.putInt("Id", id);
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

    //M�todo que faz a leitura de fato dos valores recebidos do GPS
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
