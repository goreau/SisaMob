package com.sucen.sisamob;

import java.text.DecimalFormat;
import java.util.List;

import producao.Coordenadas;
import utilitarios.MyToast;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import entidades.Imovel;
import entidades.Municipio;

public class CoordenadasFragment extends Fragment {
    Spinner spMun, spImovel;
    TextView tvLat, tvLong;
    Button btSalva, btObter;
    List<String> valMun, valImovel;
    int MY_PERMISSIONS_REQUEST_GPS;

    private OnFragmentInteractionListener mListener;

    public CoordenadasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_coordenadas, container, false);
        setupComps(v);
        // Updating the action bar title
      //  getActivity().getActionBar().setTitle("Coordenadas");

        return v;
    }

    private void setupComps(View v) {
        spMun = (Spinner) v.findViewById(R.id.spMunicipio);
        spImovel = (Spinner) v.findViewById(R.id.spImovel);
        tvLat = (TextView) v.findViewById(R.id.tvlatitude);
        tvLong = (TextView) v.findViewById(R.id.tvLongitude);

        btObter = (Button) v.findViewById(R.id.btObter);
        btObter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startGPS(v);
            }
        });

        btSalva = (Button) v.findViewById(R.id.btSalvar);
        btSalva.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaIm(v);
            }
        });
        spMun.setOnItemSelectedListener(onMudaMun);
        spImovel.setOnItemSelectedListener(onMudaImovel);
        addItensOnMun();
        addItensOnImovel(valMun.get(0));
    }

    private void addItensOnMun() {
        Municipio mun = new Municipio(getActivity());

        List<String> list = mun.combo();
        valMun = mun.id_Mun;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMun.setAdapter(dados);
    }

    OnItemSelectedListener onMudaMun = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position,
                                   long id) {
            addItensOnImovel(valMun.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    OnItemSelectedListener onMudaImovel = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            //addItensOnTipo(valGrupo.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };

    public void salvaIm(View v) {
        //pega os valores de quem chamou
        Coordenadas coord = new Coordenadas(0);
        coord.setId_imovel(Integer.parseInt(valImovel.get(spImovel.getSelectedItemPosition())));
        coord.setLatitude((String) tvLat.getText());
        coord.setLongitude((String) tvLong.getText());
        coord.setStatus(0);
        if (coord.manipula()) {
            MyToast toast = new MyToast(getActivity(), Toast.LENGTH_SHORT);
            toast.show("Inserido com sucesso");
        }
    }

    private void addItensOnImovel(String mun) {
        Imovel im = new Imovel(getActivity());

        List<String> list = im.combo(mun, "0");
        valImovel = im.id_Im;
        ArrayAdapter<String> dados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);

        dados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImovel.setAdapter(dados);

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
             //   toast.show("É necessário autorizar o uso do GPS");
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
