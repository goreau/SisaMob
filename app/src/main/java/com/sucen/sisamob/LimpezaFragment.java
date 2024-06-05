package com.sucen.sisamob;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import entidades.Municipio;
import producao.Alado;
import producao.AladoIm;
import producao.Coordenadas;
import producao.VcFolha;
import producao.VcImovel;
import producao.VcOvitrampa;
import utilitarios.MyToast;

/**
 * Created by henrique on 21/09/2016.
 */
public class LimpezaFragment extends Fragment {
    RadioGroup rgLimpa;
    RadioButton rbFolha, rbImovel, rbOvitrampa, rbCoordenada, rbAlado;
    Switch swTodos;
    Button btLimpa;

    MyToast toast;
    Context context;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LimpezaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LimpezaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LimpezaFragment newInstance(String param1, String param2) {
        LimpezaFragment fragment = new LimpezaFragment();
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
        View v = inflater.inflate(R.layout.fragment_limpeza, container, false);
        setupComps(v);
        return v;
    }

    private void setupComps(View v) {
        rgLimpa     = (RadioGroup) v.findViewById(R.id.rgLimpa);
        rbFolha     = (RadioButton) v.findViewById(R.id.rbFolha);
        rbImovel    = (RadioButton) v.findViewById(R.id.rbImovel);
        rbOvitrampa = (RadioButton) v.findViewById(R.id.rbOvitrampa);
        rbCoordenada= (RadioButton) v.findViewById(R.id.rbCoordenada);
        rbAlado     = (RadioButton) v.findViewById(R.id.rbAlados);

        swTodos     = (Switch) v.findViewById(R.id.swTodos);

        btLimpa     = (Button) v.findViewById(R.id.btLimpa);
        toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_SHORT);

        btLimpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tudo = (swTodos.isChecked() ? "1" : "0");
                if (tudo.equals("1")){
                    Drawable myIcon = getResources().getDrawable( android.R.drawable.ic_dialog_alert );
                    ColorFilter filter = new LightingColorFilter( Color.RED, Color.RED);
                    myIcon.setColorFilter(filter);
                    new AlertDialog.Builder(getActivity())
                            .setIcon(myIcon)
                            .setTitle("Excluir tudo")
                            .setMessage("Essa opção irá excluir registros não enviados para a base oficial. Confirma essa ação?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (rgLimpa.getCheckedRadioButtonId()) {
                                        case R.id.rbFolha:
                                            new Limpa().execute("2",tudo);
                                            break;
                                        case R.id.rbImovel:
                                            new Limpa().execute("1",tudo);
                                            break;
                                        case R.id.rbOvitrampa:
                                            new Limpa().execute("3",tudo);
                                            break;
                                        case R.id.rbCoordenada:
                                            new Limpa().execute("4",tudo);
                                            break;
                                        case R.id.rbAlados:
                                            new Limpa().execute("5",tudo);
                                            break;
                                        default:
                                            toast.show("É necessário escolher uma fonte para limpar.");
                                            break;
                                    }
                                }

                            })
                            .setNegativeButton("Não", null)
                            .show();
                } else {
                    switch (rgLimpa.getCheckedRadioButtonId()) {
                        case R.id.rbFolha:
                            new Limpa().execute("2",tudo);
                            break;
                        case R.id.rbImovel:
                            new Limpa().execute("1",tudo);
                            break;
                        case R.id.rbOvitrampa:
                            new Limpa().execute("3",tudo);
                            break;
                        case R.id.rbCoordenada:
                            new Limpa().execute("4",tudo);
                            break;
                        case R.id.rbAlados:
                            new Limpa().execute("5",tudo);
                            break;
                        default:
                            toast.show("É necessário escolher uma fonte para limpar.");
                            break;
                    }
                }
            }
        });
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class Limpa extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(PrincipalActivity.getSisamobContext());
            dialog.setMessage("Apagando registros...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }

        @Override
        protected String doInBackground(String... params) {
            int regs = 0;
            if (params[0] == "1") {
                VcImovel tab = new VcImovel(0);
                if (params[1] == "1"){
                    regs = tab.LimparTudo();
                } else {
                    regs = tab.Limpar();
                }
            } else if (params[0] == "2") {
                VcFolha tab = new VcFolha(0);
                if (params[1] == "1"){
                    regs = tab.LimparTudo();
                } else {
                    regs = tab.Limpar();
                }
            } else if (params[0] == "3") {
                VcOvitrampa tab = new VcOvitrampa(0);
                if (params[1] == "1"){
                    regs = tab.LimparTudo();
                } else {
                    regs = tab.Limpar();
                }
            } else if (params[0] == "5") {
                Alado tab = new Alado(0);
                AladoIm tab2 = new AladoIm(0);
                if (params[1] == "1"){
                    regs = tab.LimparTudo();
                    regs+= tab2.LimparTudo();
                } else {
                    regs = tab.Limpar();
                    regs+= tab2.Limpar();
                }
            } else {
                Coordenadas tab = new Coordenadas(0);
                if (params[1] == "1"){
                    regs = tab.LimparTudo();
                } else {
                    regs = tab.Limpar();
                }
            }
            return (regs > 0 ? "Registros Excluídos!!" : "Nenhum registro a excluir!!");
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            MyToast toast = new MyToast(PrincipalActivity.getSisamobContext(), Toast.LENGTH_LONG);
            toast.show(result);
        }
    }

}
