package entidades;

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

public class Censitario {
    private int id_censitario;
    private int id_area;
    private String codigo;
    private Context context;
    public List<String> id_Cens;
    MyToast toast;

    public Censitario() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Censitario(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public Censitario(int id_censitario, int id_area, String codigo, Context context) {
        super();
        this.id_censitario = id_censitario;
        this.id_area = id_area;
        this.codigo = codigo;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public int getId_censitario() {
        return id_censitario;
    }
    public void setId_censitario(int id_censitario) {
        this.id_censitario = id_censitario;
    }
    public int getId_area() {
        return id_area;
    }
    public void setId_area(int id_area) {
        this.id_area = id_area;
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
            db.getWritableDatabase().delete("censitario", null, null);
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
            db.getWritableDatabase().insert("censitario", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public List<String> combo(String id){
        List<String> cens = new ArrayList<String>();
        id_Cens = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT id_censitario, codigo FROM censitario where id_area=?";

        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        if(cursor.moveToFirst()){
            do {
                cens.add(cursor.getString(1));
                id_Cens.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cens;
    }
}
