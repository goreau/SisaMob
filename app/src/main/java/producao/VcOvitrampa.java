package producao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sucen.sisamob.PrincipalActivity;

public class VcOvitrampa {
    long _id;
    String dt_instala;
    String dt_retira;
    int id_ovitrampa;
    int id_execucao;
    int peri_intra;
    int obs;
    String agente;
    int status;
    MyToast toast;
    Context context;

    public VcOvitrampa(long _id) {
        super();
        this.context = PrincipalActivity.sisamobContext;
        this._id = _id;
        if (_id>0){
            popula();
        }
    }

    public long get_id() {
        return _id;
    }
    public void set_id(long _id) {
        this._id = _id;
    }
    public String getDt_instala() {
        return dt_instala;
    }
    public void setDt_instala(String dt_instala) {
        this.dt_instala = dt_instala;
    }
    public String getDt_retira() {
        return dt_retira;
    }
    public void setDt_retira(String dt_retira) {
        this.dt_retira = dt_retira;
    }
    public int getId_ovitrampa() {
        return id_ovitrampa;
    }
    public void setId_ovitrampa(int id_ovitrampa) {
        this.id_ovitrampa = id_ovitrampa;
    }
    public int getId_execucao() {
        return id_execucao;
    }
    public void setId_execucao(int id_execucao) {
        this.id_execucao = id_execucao;
    }
    public int getPeri_intra() {
        return peri_intra;
    }
    public void setPeri_intra(int peri_intra) {
        this.peri_intra = peri_intra;
    }
    public int getObs() {
        return obs;
    }
    public void setObs(int obs) {
        this.obs = obs;
    }
    public String getAgente() {
        return agente;
    }
    public void setAgente(String agente) {
        this.agente = agente;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public void popula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT id_ovitrampa, id_execucao, dt_instala, dt_retira, "+
                "peri_intra, obs, agente, status FROM vc_ovitrampa v where _id=" + this._id;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this.id_ovitrampa 	= cursor.getInt(0);
            this.id_execucao 	= cursor.getInt(1);
            this.dt_instala		= cursor.getString(2);
            this.dt_retira 		= cursor.getString(3);
            this.peri_intra 	= cursor.getInt(4);
            this.obs 			= cursor.getInt(5);
            this.agente 		= cursor.getString(6);
            this.status 		= cursor.getInt(7);
        }
        db.close();

    }


    public boolean manipula() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try {
            ContentValues valores = new ContentValues();
            valores.put("dt_instala", this.dt_instala);
            valores.put("dt_retira", this.dt_retira);
            valores.put("id_ovitrampa", this.id_ovitrampa);
            valores.put("id_execucao", this.id_execucao);
            valores.put("peri_intra", this.peri_intra);
            valores.put("obs", this.obs);
            valores.put("agente", this.agente);

            if (this._id > 0) {
                valores.put("status", this.status);
                String[] args = { Long.toString(this._id) };
                db.getWritableDatabase().update("vc_ovitrampa", valores, "_id=?",
                        args);
            } else {
                valores.put("status", 0);
                this._id = db.getWritableDatabase().insert("vc_ovitrampa", null,
                        valores);
            }
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean delete() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try {
            String[] args = { Long.toString(this._id) };
            db.getWritableDatabase().delete("vc_ovitrampa", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<HashMap<String, String>> getAllImoveis() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT v._id as id, i.endereco as texto FROM vc_ovitrampa v join ovitrampa i on v.id_ovitrampa=i.id_ovitrampa";

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

    /**
     * Compose JSON out of SQLite records
     *
     * @return
     */
    public String composeJSONfromSQLite() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  _id, dt_instala, dt_retira, id_ovitrampa, id_execucao, "
                + "peri_intra, obs, agente, status, datetime(dt_insere,'localtime') FROM vc_ovitrampa where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_vc_ovitrampa", cursor.getString(0));
                map.put("dt_instala", cursor.getString(1));
                map.put("dt_retira", cursor.getString(2));
                map.put("id_ovitrampa", cursor.getString(3));
                map.put("id_execucao", cursor.getString(4));
                map.put("peri_intra", cursor.getString(5));
                map.put("obs", cursor.getString(6));
                map.put("agente", cursor.getString(7).replace(" ", "_"));
                map.put("status", cursor.getString(8));
                map.put("dt_insere", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM vc_ovitrampa where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM vc_ovitrampa";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }
    /**
     * Update Sync status against each User ID
     * @param id
     * @param status
     */
    public void atualizaStatus(String id, String status){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String updateQuery = "Update vc_ovitrampa set status = '"+ status +"' where _id="+"'"+ id +"'";
     //   Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);

        db.close();
    }

    public int Limpar(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int regs = 0;
        String sql = "DELETE FROM vc_ovitrampa where status = 1";
       // Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT changes() AS regs", null);
        if (cursor.moveToFirst()) {
            regs = cursor.getInt(0);
        }
        db.close();
        return regs;
    }

    public int LimparTudo(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int regs = 0;
        String sql = "DELETE FROM vc_ovitrampa";
     //   Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT changes() AS regs", null);
        if (cursor.moveToFirst()) {
            regs = cursor.getInt(0);
        }
        db.close();
        return regs;
    }

    public List<RelatorioList> getList(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        String selectQuery = "SELECT 'Ovitrampa','', count(v._id) FROM vc_ovitrampa v";

        Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);
        List<RelatorioList> lista = new ArrayList<RelatorioList>();

        if (cursor.moveToFirst()) {
            do {
                if (Integer.parseInt(cursor.getString(2))>0){
                    RelatorioList list = new RelatorioList(cursor.getString(0),cursor.getString(1),cursor.getString(2));
                    lista.add(list);
                }
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public ContentValues[] persiste() {
        int i = 0;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT  _id, dt_instala, dt_retira, id_ovitrampa, id_execucao, "
                + "peri_intra, obs, agente, status FROM vc_ovitrampa where status = 0";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        ContentValues[] total = new ContentValues[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                map.put("id_vc_ovitrampa", cursor.getString(0));
                map.put("dt_instala", cursor.getString(1));
                map.put("dt_retira", cursor.getString(2));
                map.put("id_ovitrampa", cursor.getString(3));
                map.put("id_execucao", cursor.getString(4));
                map.put("peri_intra", cursor.getString(5));
                map.put("obs", cursor.getString(6));
                map.put("agente", cursor.getString(7).replace(" ", "_"));
                map.put("status", cursor.getString(8));
                total[i++] = map;
            } while (cursor.moveToNext());
        }
        db.close();
        return total;
    }

    public boolean recupera(ContentValues[] dados){
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            for (int i = 0; i<dados.length; i++){
                ContentValues valores = dados[i];
                this._id = db.getWritableDatabase().insert("vc_ovitrampa", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }
}
