package com.sucen.sisamob;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import consulta.ConsultaFragment;
import utilitarios.GerenciarBanco;
import utilitarios.MyToast;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, InicialFragment.OnFragmentInteractionListener,
        AtividadeFragment.OnFragmentInteractionListener, ImportaFragment.OnFragmentInteractionListener,
        ExportaFragment.OnFragmentInteractionListener, RelImportaFragment.OnFragmentInteractionListener,
        AtividadeFolhaFragment.OnFragmentInteractionListener, CondicaoFragment.OnFragmentInteractionListener,
        RecipienteFragment.OnFragmentInteractionListener, RelProducaoFragment.OnFragmentInteractionListener,
        ImovelCadFragment.OnFragmentInteractionListener, LimpezaFragment.OnFragmentInteractionListener,
        CoordenadasFragment.OnFragmentInteractionListener, ConsultaRecFragment.OnFragmentInteractionListener,
        OvitrampaFragment.OnFragmentInteractionListener, AladoFragment.OnFragmentInteractionListener,
        LocAladoFragment.OnFragmentInteractionListener, AladoImFragment.OnFragmentInteractionListener{

    public static Context sisamobContext;
    //ActionBarDrawerToggle mDrawerToggle;
    GerenciarBanco gerenciador;
    private MyToast toast;
    private long lastBackPressTime = 0;

    public static Context getSisamobContext() {
        return sisamobContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sisamobContext = getApplicationContext();
        inicial();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Fragment rFragment = new ConsultaFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, rFragment);
                ft.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
     //   VerificaBanco banco = new VerificaBanco();
     //   banco.execute();
    }

    private void inicial() {
        toast = new MyToast(sisamobContext, Toast.LENGTH_SHORT);
        Fragment frag = new InicialFragment();
        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();
        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, frag);
        // Committing the transaction
        ft.commit();
        new VerificaBanco().execute("");
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast.show("Para sair do aplicativo, pressione novamente.");

            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sobre) {
            mnuSobre();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void mnuSobre() {
        String versName = "SisaMob vs: " + BuildConfig.VERSION_NAME;
        new AlertDialog.Builder(this)
                .setTitle(versName)
                .setMessage(R.string.ctSobre)
                .setCancelable(true)
                .create().show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment rFragment = null;

        if (id == R.id.nav_atividade) {
            rFragment = new AtividadeFragment();
        } else if (id == R.id.nav_coords) {
            rFragment = new CoordenadasFragment();
        } else if (id == R.id.nav_consulta) {
            rFragment = new ConsultaFragment();
        } else if (id == R.id.nav_relat) {
            rFragment = new RelProducaoFragment();
        } else if (id == R.id.nav_manage) {
            rFragment = new LimpezaFragment();
        } else if (id == R.id.nav_importa) {
            rFragment = new ImportaFragment();
        } else if (id == R.id.nav_exporta) {
            rFragment = new ExportaFragment();
        }

        // Creating a Bundle object
        Bundle data = new Bundle();
        // Setting the index of the currently selected item of mDrawerList
        data.putInt("position", id);
        data.putLong("id_inseto", 0);
        // Setting the position to the fragment
        rFragment.setArguments(data);

        // Getting reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //ft.addToBackStack(null);
        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, rFragment);

        // Committing the transaction
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class VerificaBanco extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(sisamobContext);
            dialog.setMessage("Verificando base de dados...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }

        @Override
        protected String doInBackground(String... params) {
            gerenciador = new GerenciarBanco(getApplicationContext());
            return "Verificado";
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            toast.show(result);
            gerenciador.closeDB();
        }

    }

}