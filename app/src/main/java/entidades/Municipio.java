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

public class Municipio {
    private int id_municipio;
    private String nome;
    private String codigo;
    private Context context;
    public List<String> id_Mun;
    MyToast toast;

    public Municipio() {
        context = PrincipalActivity.getSisamobContext();
    }
    public Municipio(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public Municipio(int id_municipio, String nome, String codigo, Context context) {
        super();
        this.id_municipio = id_municipio;
        this.nome = nome;
        this.codigo = codigo;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }


    public int getId_municipio() {
        return id_municipio;
    }
    public void setId_municipio(int id_municipio) {
        this.id_municipio = id_municipio;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
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
            db.getWritableDatabase().delete("municipio", null, null);
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
            db.getWritableDatabase().insert("municipio", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    public List<String> combo(){
        List<String> mun = new ArrayList<String>();
        id_Mun = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        String sql = "SELECT * FROM municipio";
        Cursor cursor = db.getReadableDatabase().rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do {
                mun.add(cursor.getString(1));
                id_Mun.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return mun;
    }
}

