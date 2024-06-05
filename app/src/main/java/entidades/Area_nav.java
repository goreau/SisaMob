package entidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.Toast;

import com.sucen.sisamob.PrincipalActivity;

import java.util.ArrayList;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;

/**
 * Created by Henrique on 24/10/2016.
 */
public class Area_nav {
    private int id_area_nav;
    private int id_municipio;
    private String nome;
    private Context context;
    public List<String> id_Area_nav;
    MyToast toast;

    public Area_nav() {
        context = PrincipalActivity.getSisamobContext();
    }
    public Area_nav(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public Area_nav(int id_area_nav, int id_municipio, String nome, Context context) {
        super();
        this.id_area_nav = id_area_nav;
        this.id_municipio = id_municipio;
        this.nome = nome;
        this.context = context;
    }

    public int getId_Area_nav() {
        return id_area_nav;
    }
    public void setId_Area_nav(int id_area_nav) {
        this.id_area_nav = id_area_nav;
    }
    public int getId_municipio() {
        return id_municipio;
    }
    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }
    public String getnome() {
        return nome;
    }
    public void setnome(String nome) {
        this.nome = nome;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("Area_nav", null, null);
        } catch (SQLException e) {
            toast.show(e.getMessage());
        }
    }

    public boolean insere(String[] campos, String[] val){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            ContentValues valores = new ContentValues();
            for (int i = 0; i<val.length;i++){
                valores.put(campos[i],val[i]);
            }
            db.getWritableDatabase().insert("Area_nav", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    public List<String> combo(String id){
        List<String> areas = new ArrayList<String>();
        id_Area_nav = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_Area_nav, nome FROM Area_nav where id_municipio=? order by nome";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                areas.add(cursor.getString(1));
                id_Area_nav.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return areas;
    }
}
