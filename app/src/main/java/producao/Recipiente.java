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

public class Recipiente {
    long _id;
    int tabela;
    int id_fk;
    int id_grupo;
    int id_tipo;
    int existente;
    int agua;
    int larva;
    String amostra;
    int status;
    MyToast toast;
    Context context;

    public Recipiente(long _id) {
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
    public int getTabela() {
        return tabela;
    }
    public void setTabela(int tabela) {
        this.tabela = tabela;
    }
    public int getId_fk() {
        return id_fk;
    }
    public void setId_fk(long id_fk) {
        this.id_fk = (int) id_fk;
    }
    public int getId_grupo() {
        return id_grupo;
    }
    public void setId_grupo(int id_grupo) {
        this.id_grupo = id_grupo;
    }
    public int getId_tipo() {
        return id_tipo;
    }
    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }
    public int getExistente() {
        return existente;
    }
    public void setExistente(int existente) {
        this.existente = existente;
    }
    public int getAgua() {
        return agua;
    }
    public void setAgua(int agua) {
        this.agua = agua;
    }
    public int getLarva() {
        return larva;
    }
    public void setLarva(int larva) {
        this.larva = larva;
    }
    public String getAmostra() {
        return amostra;
    }
    public void setAmostra(String amostra) {
        this.amostra = amostra;
    }

    public void popula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT id_grupo, id_tipo, existente, agua, larva,"+
                "amostra, tabela, id_fk FROM recipiente v where _id=" + this._id;

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            this.id_grupo 	= cursor.getInt(0);
            this.id_tipo	= cursor.getInt(1);
            this.existente	= cursor.getInt(2);
            this.agua 		= cursor.getInt(3);
            this.larva 		= cursor.getInt(4);
            this.amostra 	= cursor.getString(5);
            this.tabela 	= cursor.getInt(6);
            this.id_fk 		= cursor.getInt(7);
        }
        db.close();
    }

    public boolean manipula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            ContentValues valores = new ContentValues();
            valores.put("id_grupo", this.id_grupo);
            valores.put("id_tipo", this.id_tipo);
            valores.put("existente", this.existente);
            valores.put("agua", this.agua);
            valores.put("larva", this.larva);
            valores.put("amostra", this.amostra);
            valores.put("tabela", this.tabela);
            valores.put("id_fk", this.id_fk);
            valores.put("status", 0);
            if (this._id >0){
                String[] args={Long.toString(this._id)};
                db.getWritableDatabase().update("recipiente", valores, "_id=?", args);
            } else {
                this._id = db.getWritableDatabase().insert("recipiente", null, valores);
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
            db.getWritableDatabase().delete("recipiente", "_id=?", args);
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show("Recipiente excluído!");
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }


    public ArrayList<HashMap<String, String>> getAllRecipientes() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  r._id as id, t.nome as texto FROM recipiente r join tipo_rec t on r.id_tipo=t.id_tipo_rec";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
      //  Log.w("registros",cursor.getCount() + " registros");

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
        String selectQuery = "SELECT  _id, id_grupo, id_tipo, existente, agua, "
                + "larva, amostra, tabela, id_fk, status FROM recipiente where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_mob", cursor.getString(0));
                map.put("id_grupo", cursor.getString(1));
                map.put("id_tipo", cursor.getString(2));
                map.put("existente", cursor.getString(3));
                map.put("agua", cursor.getString(4));
                map.put("larva", cursor.getString(5));
                String am = cursor.getString(6);
                if (am == "") am=" ";
                map.put("amostra", am);//cursor.getString(6));
                map.put("tabela", cursor.getString(7));
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

    public String composeJSONfromSQLite2(int tab, String id) {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        //String map = "[";
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  _id, id_grupo, id_tipo, existente, agua, "
                + "larva, amostra, tabela, id_fk, status FROM recipiente where tabela=" + tab + " and id_fk=" + id;
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_mob", cursor.getString(0));
                map.put("id_grupo", cursor.getString(1));
                map.put("id_tipo", cursor.getString(2));
                map.put("existente", cursor.getString(3));
                map.put("agua", cursor.getString(4));
                map.put("larva", cursor.getString(5));
                String am = cursor.getString(6);
                System.out.println(am);
                if (am == "") am=" ";
                System.out.println(am);
                map.put("amostra", am);
                map.put("tabela", cursor.getString(7));
                map.put("id_fk", cursor.getString(8));
                map.put("status", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        //map+="]";
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
        String selectQuery = "SELECT  * FROM recipiente where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM recipiente";
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
        String updateQuery = "Update recipiente set status = '"+ status +"' where _id="+"'"+ id +"'";
       // Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);
        db.close();
    }

    public List<RelatorioList> getList(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        String selectQuery = "SELECT ('Tipo: ' || t.nome), ('Exist: '|| r.existente) || '      ' || ('Amostra: '|| r.amostra), '' as qqq FROM recipiente r join grupo_rec g on r.id_grupo=g.id_grupo_rec " +
                "join tipo_rec t on t.id_tipo_rec=r.id_tipo where r.amostra<>''";

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


    public List<RecipienteList> getEdicao(int tab, Long id){

        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        String selectQuery = "SELECT _id, (' - Tp: ' || t.nome), ('Am: '|| r.amostra) FROM recipiente r join grupo_rec g on r.id_grupo=g.id_grupo_rec " +
                "join tipo_rec t on t.id_tipo_rec=r.id_tipo where tabela=" + tab + " and id_fk=" + id;

        Cursor cursor = db.getReadableDatabase().rawQuery(selectQuery, null);
        List<RecipienteList> lista = new ArrayList<RecipienteList>();

        if (cursor.moveToFirst()) {
            do {
                RecipienteList list = new RecipienteList(cursor.getString(2),cursor.getString(1),cursor.getInt(0));
                lista.add(list);
            } while (cursor.moveToNext());
        }
        return lista;
    }

    public ContentValues[] persiste() {
        int i = 0;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT  _id, id_grupo, id_tipo, existente, agua, "
                + "larva, amostra, tabela, id_fk, status FROM recipiente where status = 0";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        ContentValues[] total = new ContentValues[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                map.put("_id", cursor.getString(0));
                map.put("id_grupo", cursor.getString(1));
                map.put("id_tipo", cursor.getString(2));
                map.put("existente", cursor.getString(3));
                map.put("agua", cursor.getString(4));
                map.put("larva", cursor.getString(5));
                String am = cursor.getString(6);
                if (am == "") am=" ";
                map.put("amostra", am);//cursor.getString(6));
                map.put("tabela", cursor.getString(7));
                map.put("id_fk", cursor.getString(8));
                map.put("status", cursor.getString(9));
                total[i++]= map;
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
                this._id = db.getWritableDatabase().insert("recipiente", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }

}
