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

public class Coordenadas {
    long _id;
    int id_imovel;
    String latitude;
    String longitude;
    int status;
    MyToast toast;
    Context context;

    public Coordenadas(long _id) {
        super();
        this.context = PrincipalActivity.sisamobContext;
        this._id = _id;
    }

    public int getId_imovel() {
        return id_imovel;
    }

    public void setId_imovel(int id_imovel) {
        this.id_imovel = id_imovel;
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

    public boolean manipula(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        try{
            ContentValues valores = new ContentValues();
            valores.put("id_imovel", this.id_imovel);
            valores.put("latitude", this.latitude);
            valores.put("longitude", this.longitude);
            valores.put("status", 0);
            if (this._id >0){
                String[] args={Long.toString(this._id)};
                db.getWritableDatabase().update("coordenadas", valores, "_id=?", args);
            } else {
                this._id = db.getWritableDatabase().insert("coordenadas", null, valores);
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
            db.getWritableDatabase().delete("coordenadas", "_id=?", args);
            return true;
        } catch (SQLException e) {
            toast = new MyToast(context, Toast.LENGTH_SHORT);
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public String composeJSONfromSQLite() {
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  _id, id_imovel, latitude, longitude, status " +
                "FROM coordenadas where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id_mob", cursor.getString(0));
                map.put("id_imovel", cursor.getString(1));
                map.put("latitude", cursor.getString(2));
                map.put("longitude", cursor.getString(3));
                map.put("status", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        Gson gson = new GsonBuilder().create();
        // Use GSON to serialize Array List to JSON
        System.out.println(wordList);
        return gson.toJson(wordList);
    }

    public int dbSyncCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM coordenadas where status = 0";
        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        count = cursor.getCount();
        db.close();
        return count;
    }

    public int dbCount(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int count = 0;
        String selectQuery = "SELECT  * FROM coordenadas";
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
        String updateQuery = "Update coordenadas set status = '"+ status +"' where _id='"+ id +"'";
     //   Log.d("query",updateQuery);
        db.getWritableDatabase().execSQL(updateQuery);
        db.close();
    }

    public List<RelatorioList> getList(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);

        String selectQuery = "SELECT i.endereco, ('Lat:' || c.latitude),('Long:' || c.longitude) " +
                "FROM coordenadas c join imovel i using(id_imovel)";

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

    public int Limpar(){
        Context context = PrincipalActivity.sisamobContext;
        GerenciarBanco db = new GerenciarBanco(context);
        int regs = 0;
        String sql = "DELETE FROM coordenadas where status = 1";
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
        String sql = "DELETE FROM coordenadas";
       // Log.d("query",sql);
        db.getWritableDatabase().execSQL(sql);
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT changes() AS regs", null);
        if (cursor.moveToFirst()) {
            regs = cursor.getInt(0);
        }
        db.close();
        return regs;
    }

    public ContentValues[] persiste() {
        int i = 0;
        GerenciarBanco db = new GerenciarBanco(context);
        String selectQuery = "SELECT  _id, id_imovel, latitude, longitude, status " +
                "FROM coordenadas where status = 0";

        Cursor cursor = db.getWritableDatabase().rawQuery(selectQuery, null);
        ContentValues[] total = new ContentValues[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                ContentValues map = new ContentValues();
                map.put("id_mob", cursor.getString(0));
                map.put("id_imovel", cursor.getString(1));
                map.put("latitude", cursor.getString(2));
                map.put("longitude", cursor.getString(3));
                map.put("status", cursor.getString(4));
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
                this._id = db.getWritableDatabase().insert("coordenadas", null, valores);
            }
            return true;
        } catch (SQLException e) {
            return false;
        } finally {
            db.close();
        }
    }

}
