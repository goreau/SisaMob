package entidades;

/**
 * Created by henrique on 20/09/2016.
 */
import java.util.ArrayList;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.Toast;

import com.sucen.sisamob.PrincipalActivity;

public class Area {
    private int id_area;
    private int id_municipio;
    private String codigo;
    private Context context;
    public List<String> id_Area;
    MyToast toast;

    public Area() {
        context = PrincipalActivity.getSisamobContext();
    }
    public Area(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }

    public Area(int id_area, int id_municipio, String codigo, Context context) {
        super();
        this.id_area = id_area;
        this.id_municipio = id_municipio;
        this.codigo = codigo;
        this.context = context;
    }
    public int getId_area() {
        return id_area;
    }
    public void setId_area(int id_area) {
        this.id_area = id_area;
    }
    public int getId_municipio() {
        return id_municipio;
    }
    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("area", null, null);
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
            db.getWritableDatabase().insert("area", null, valores);
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
        id_Area = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_area, codigo FROM area where id_municipio=?";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                areas.add(cursor.getString(1));
                id_Area.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return areas;
    }
}
