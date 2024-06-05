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

public class Condicao {
    long _id;
    long id_fk;
    int cond_casa;
    int cond_quintal;
    int cond_sombra;
    int pavimento;
    int galinha;
    int cao;
    int outros;
    MyToast toast;
    Context context;

    public Condicao(long id_fk) {
        super();
        this.id_fk = id_fk;
        this.context = PrincipalActivity.sisamobContext;
        if (id_fk>0){
            popula();
        }
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getId_fk() {
        return id_fk;
    }

    public void setId_fk(long id_fk) {
        this.id_fk = id_fk;
    }

    public int getCond_casa() {
        return cond_casa;
    }

    public void setCond_casa(int cond_casa) {
        this.cond_casa = cond_casa;
    }

    public int getCond_quintal() {
        return cond_quintal;
    }

    public void setCond_quintal(int cond_quintal) {
        this.cond_quintal = cond_quintal;
    }

    public int getCond_sombra() {
        return cond_sombra;
    }

    public void setCond_sombra(int cond_sombra) {
        this.cond_sombra = cond_sombra;
    }

    public int getPavimento() {
        return pavimento;
    }

    public void setPavimento(int pavimento) {
        this.pavimento = pavimento;
    }

    public int getGalinha() {
        return galinha;
    }

    public void setGalinha(int galinha) {
        this.galinha = galinha;
    }

    public int getCao() {
        return cao;
    }

    public void setCao(int cao) {
        this.cao = cao;
    }

    public int getOutros() {
        return outros;
    }

    public void setOutros(int outros) {
        this.outros = outros;
    }

    public void popula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT _id, cond_casa, cond_quintal, cond_sombra, pavimento, galinha, "+
                "cao, outros FROM condicao v where id_fk=" + this.id_fk;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this._id 			= cursor.getLong(0);
            this.cond_casa 		= cursor.getInt(1);
            this.cond_quintal	= cursor.getInt(2);
            this.cond_sombra 	= cursor.getInt(3);
            this.pavimento 		= cursor.getInt(4);
            this.galinha 		= cursor.getInt(5);
            this.cao 			= cursor.getInt(6);
            this.outros 		= cursor.getInt(7);
        }
        db.close();

    }


    public boolean manipula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            ContentValues valores = new ContentValues();
            valores.put("cond_casa", this.cond_casa);
            valores.put("cond_quintal", this.cond_quintal);
            valores.put("cond_sombra", this.cond_sombra);
            valores.put("pavimento", this.pavimento);
            valores.put("galinha", this.galinha);
            valores.put("cao", this.cao);
            valores.put("outros", this.outros);
            valores.put("id_fk", this.id_fk);
            valores.put("status", 0);
            if (this._id >0){
                String[] args={Long.toString(this._id)};
                db.getWritableDatabase().update("condicao", valores, "_id=?", args);
            } else {
                this._id = db.getWritableDatabase().insert("condicao", null, valores);
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

    public boolean delete(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            String[] args={Long.toString(this._id)};
            db.getWritableDatabase().delete("condicao", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public ArrayList<HashMap<String, String>> getAllCondicoes() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  c._id as id, c.cond_casa as texto FROM condicao c";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
       // Log.w("registros",cursor.getCount() + " registros");

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
        String selectQuery = "SELECT  _id, cond_casa, cond_quintal, cond_sombra, pavimento, galinha, "+
                "cao, outros, id_fk, status FROM condicao where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_mob", cursor.getString(0));
                map.put("cond_casa", cursor.getString(1));
                map.put("cond_quintal", cursor.getString(2));
                map.put("cond_sombra", cursor.getString(3));
                map.put("pavimento", cursor.getString(4));
                map.put("galinha", cursor.getString(5));
                map.put("cao", cursor.getString(6));
                map.put("outros", cursor.getString(7));
                map.put("id_fk", cursor.getString(8));
                map.put("status", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }

    public String composeJSONfromSQLite2(String id) {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        //String map = "[";
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  _id, cond_casa, cond_quintal, cond_sombra, pavimento, galinha, "+
                "cao, outros, id_fk, status FROM condicao where id_fk=" + id;
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_mob", cursor.getString(0));
                map.put("cond_casa", cursor.getString(1));
                map.put("cond_quintal", cursor.getString(2));
                map.put("cond_sombra", cursor.getString(3));
                map.put("pavimento", cursor.getString(4));
                map.put("galinha", cursor.getString(5));
                map.put("cao", cursor.getString(6));
                map.put("outros", cursor.getString(7));
                map.put("id_fk", cursor.getString(8));
                map.put("status", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        db.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
        //return map;
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM condicao where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM condicao";
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
        String updateQuery = "Update condicao set status = '"+ status +"' where _id="+"'"+ id +"'";
       // Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);
        db.close();
    }

    public List<RelatorioList> getList(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        String selectQuery = "SELECT 'Cond. Casa','', count(_id) FROM condicao c";

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
        String selectQuery = "SELECT  _id, cond_casa, cond_quintal, cond_sombra, pavimento, galinha, "+
                "cao, outros, id_fk, status FROM condicao where status = 0";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        ContentValues[] total = new ContentValues[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                map.put("_id", cursor.getString(0));
                map.put("cond_casa", cursor.getString(1));
                map.put("cond_quintal", cursor.getString(2));
                map.put("cond_sombra", cursor.getString(3));
                map.put("pavimento", cursor.getString(4));
                map.put("galinha", cursor.getString(5));
                map.put("cao", cursor.getString(6));
                map.put("outros", cursor.getString(7));
                map.put("id_fk", cursor.getString(8));
                map.put("status", cursor.getString(9));
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
                this._id = db.getWritableDatabase().insert("condicao", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }

}
