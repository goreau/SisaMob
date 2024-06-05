package producao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sucen.sisamob.PrincipalActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;

public class AladoIm {
    long _id;

    String dt_cadastro, am_larva, am_intra, am_peri;
    int id_municipio, id_atividade, id_execucao, id_imovel,  id_situacao, moradores, rec_larva, id_sub_ativ;
    Float umidade, temperatura;
    String agente;
    String latitude;
    String longitude;
    int status;
    MyToast toast;
    Context context;

    public AladoIm(long _id) {
        super();
        this.context = PrincipalActivity.sisamobContext;
        this._id = _id;
        status = 0;
        if (_id>0){
            popula();
        }
    }

    public void popula(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        String selectQuery = "SELECT dt_cadastro, id_municipio, 1, id_atividade, id_sub_ativ, id_imovel, id_situacao, umidade, temperatura, moradores, rec_larva, am_larva, " +
                "am_intra, am_peri, latitude, longitude, agente, status, dt_insere, id_execucao FROM alado_im v where _id=" + this._id;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this.dt_cadastro 	= cursor.getString(0);
            this.id_municipio 	= cursor.getInt(1);
            this.id_atividade 	= cursor.getInt(3);
            this.id_sub_ativ	= cursor.getInt(4);
            this.id_imovel      = cursor.getInt(5);
            this.id_situacao 	= cursor.getInt(6);
            this.umidade     	= cursor.getFloat(7);
            this.temperatura    = cursor.getFloat(8);
            this.moradores   	= cursor.getInt(9);
            this.rec_larva 		= cursor.getInt(10);
            this.am_larva       = cursor.getString(11);
            this.am_intra 		= cursor.getString(12);
            this.am_peri		= cursor.getString(13);
            this.latitude		= cursor.getString(14);
            this.longitude		= cursor.getString(15);
            this.agente 		= cursor.getString(16);
            this.status         = cursor.getInt(17);
            this.id_execucao    = cursor.getInt(18);
        }
        db.close();

    }

    public boolean manipula() {
        GerenciarBanco db = new GerenciarBanco(this.context);
        try {
            ContentValues valores = new ContentValues();
            valores.put("dt_cadastro", this.dt_cadastro);
            valores.put("id_atividade", this.id_atividade);
            valores.put("id_municipio", this.id_municipio);
            valores.put("id_sub_ativ", this.id_sub_ativ);
            valores.put("id_imovel", this.id_imovel);
            valores.put("umidade", this.umidade);
            valores.put("id_situacao", this.id_situacao);
            valores.put("temperatura", this.temperatura);
            valores.put("moradores", this.moradores);
            valores.put("rec_larva", this.rec_larva);
            valores.put("am_larva", this.am_larva);
            valores.put("am_intra", this.am_intra);
            valores.put("am_peri", this.am_peri);
            valores.put("agente", this.agente);
            valores.put("status", this.status);
            valores.put("id_execucao", this.id_execucao);

            if (this._id > 0) {
                String[] args = { Long.toString(this._id) };
                db.getWritableDatabase().update("alado_im", valores, "_id=?", args);
            } else {
                valores.put("latitude", this.latitude);
                valores.put("longitude", this.longitude);
                this._id = db.getWritableDatabase().insertOrThrow("alado_im", null,
                        valores);
                Log.w("inserido",""+this._id );
            }
            return true;
        } catch (SQLException e) {
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }


    public boolean delete() {
        GerenciarBanco db = new GerenciarBanco(this.context);
        try {
            String[] args = { Long.toString(this._id) };

            db.getWritableDatabase().delete("alado_im", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<HashMap<String, String>> getAllImoveis() {
        GerenciarBanco db = new GerenciarBanco(this.context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT v._id as id, q.endereco as texto FROM alado_im v join imovel q on v.id_imovel=q.id_imovel";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("texto", cursor.getString(1));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return wordList;
    }

    public List<RelatorioList> getList(){
        GerenciarBanco db = new GerenciarBanco(this.context);

        String selectQuery = "SELECT 'Cap Alado(' || trim(a.nome)||')', ' T:(' || sum(case when id_situacao=1 then 1 else 0 end) || ') / NT: (' || sum(case when id_situacao>1 then 1 else 0 end) || ')', 'Total: ' || count(v._id) FROM alado_im v join atividade a on (a.id_atividade=v.id_sub_ativ) group by a.nome";
        //String selectQuery = "SELECT * FROM alado_im v ";
        Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);
        List<RelatorioList> lista = new ArrayList<RelatorioList>();

        if (cursor.moveToFirst()) {
            do {
                RelatorioList list = new RelatorioList(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                lista.add(list);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public String composeJSONfromSQLite() {
        Context context 	= PrincipalActivity.sisamobContext;
        GerenciarBanco db 	= new GerenciarBanco(context);

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT _id, dt_cadastro, id_municipio, id_atividade, id_imovel, id_sub_ativ, id_situacao, umidade, temperatura, moradores, rec_larva, am_larva, " +
                "am_intra, am_peri, latitude, longitude, agente, datetime(dt_insere,'localtime'), id_execucao FROM alado_im where status = 0";

        try {
            Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id_alado_im", cursor.getString(0));
                    map.put("dt_cadastro", cursor.getString(1));
                    map.put("id_municipio", cursor.getString(2));
                    map.put("id_atividade", cursor.getString(5));
                    map.put("id_imovel", cursor.getString(4));
                    map.put("id_ref_ativ", cursor.getString(3));
                    map.put("id_situacao", cursor.getString(6));
                    map.put("umidade", cursor.getString(7));
                    map.put("temperatura", cursor.getString(8));
                    map.put("moradores", cursor.getString(9));
                    map.put("rec_larva", cursor.getString(10));
                    map.put("am_larva", cursor.getString(11));
                    map.put("am_intra", cursor.getString(12));
                    map.put("am_peri", cursor.getString(13));
                    map.put("latitude", cursor.getString(14));
                    map.put("longitude", cursor.getString(15));
                    map.put("agente", cursor.getString(16).replace(" ", "_"));
                    map.put("dt_insere", cursor.getString(17));
                    map.put("id_execucao", cursor.getString(18));
                    wordList.add(map);
                    //Log.d("query",map.toString());
                } while (cursor.moveToNext());
            }
        } catch (SQLException e){
            toast = new MyToast(this.context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
        } finally {
            db.close();
        }

        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        // System.out.println(wordList);
        return gson.toJson(wordList);
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM alado_im where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public void atualizaStatus(String id, String status){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String updateQuery = "Update alado_im set status = '"+ status +"' where _id="+"'"+ id +"'";
        // Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);

        db.close();
    }

    public int Limpar(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int id = 0;
        int regs = 0;


        String sql = "DELETE FROM alado_im where status = 1";
        //   Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }

    public int LimparTudo(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int id = 0;
        int regs = 0;
        String sql =  "DELETE FROM alado_im";
        //  Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        db.close();
        return regs;
    }


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDt_cadastro() {
        return dt_cadastro;
    }

    public void setDt_cadastro(String dt_cadastro) {
        this.dt_cadastro = dt_cadastro;
    }

    public String getAm_larva() {
        return am_larva;
    }

    public void setAm_larva(String am_larva) {
        this.am_larva = am_larva;
    }

    public String getAm_intra() {
        return am_intra;
    }

    public void setAm_intra(String am_intra) {
        this.am_intra = am_intra;
    }

    public String getAm_peri() {
        return am_peri;
    }

    public void setAm_peri(String am_peri) {
        this.am_peri = am_peri;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }

    public int getId_atividade() {
        return id_atividade;
    }

    public void setId_atividade(int id_atividade) {
        this.id_atividade = id_atividade;
    }

    public int getId_imovel() {
        return id_imovel;
    }

    public void setId_imovel(int id_imovel) {
        this.id_imovel = id_imovel;
    }

    public int getId_situacao() {
        return id_situacao;
    }

    public void setId_situacao(int id_situacao) {
        this.id_situacao = id_situacao;
    }

    public int getMoradores() {
        return moradores;
    }

    public void setMoradores(int moradores) {
        this.moradores = moradores;
    }

    public int getRec_larva() {
        return rec_larva;
    }

    public void setRec_larva(int rec_larva) {
        this.rec_larva = rec_larva;
    }

    public int getId_sub_ativ() {
        return id_sub_ativ;
    }

    public void setId_sub_ativ(int id_sub_ativ) {
        this.id_sub_ativ = id_sub_ativ;
    }

    public Float getUmidade() {
        return umidade;
    }

    public void setUmidade(Float umidade) {
        this.umidade = umidade;
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId_execucao() {
        return id_execucao;
    }

    public void setId_execucao(int id_execucao) {
        this.id_execucao = id_execucao;
    }
}
