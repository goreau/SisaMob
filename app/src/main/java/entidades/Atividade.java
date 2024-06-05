package entidades;

import java.util.ArrayList;
import java.util.List;

import utilitarios.GerenciarBanco;
import utilitarios.MyToast;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.sucen.sisamob.PrincipalActivity;

public class Atividade {
    private int id_atividade;
    private String nome;
    private int grupo;
    private Context context;
    public List<String> id_Ativ;
    MyToast toast;

    public Atividade() {
        context = PrincipalActivity.getSisamobContext();
    }

    public Atividade(Context context) {
        super();
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public Atividade(int id_atividade, String nome, int grupo, Context context) {
        super();
        this.id_atividade = id_atividade;
        this.nome = nome;
        this.grupo = grupo;
        this.context = context;
        toast = new MyToast(context, Toast.LENGTH_SHORT);
    }
    public int getId_atividade() {
        return id_atividade;
    }
    public void setId_atividade(int id_atividade) {
        this.id_atividade = id_atividade;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public int getGrupo() {
        return grupo;
    }
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public void limpar(){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            db.getWritableDatabase().delete("atividade", null, null);
        } catch (SQLException e) {
            toast.show(e.getMessage());
        }
    }

    public boolean insere(String[] campos, String[] val){
        GerenciarBanco db = new GerenciarBanco(this.context);
        try{
            ContentValues valores = new ContentValues();
            for (int i = 0; i<val.length;i++){
                valores.put(campos[i], val[i]);
            }
            db.getWritableDatabase().insert("atividade", null, valores);
            return true;
        } catch (SQLException e) {
            toast.show(e.getMessage());
            return false;
        } finally {
            db.close();
        }

    }

    public List<String> combo(String id){
        List<String> ativ = new ArrayList<String>();
        String sql = "";
        id_Ativ = new ArrayList<String>();
        GerenciarBanco db = new GerenciarBanco(this.context);
        if (id=="0"){
            sql = "SELECT id_atividade, id_atividade || ' - ' || nome as  codigo FROM atividade where grupo > ? ORDER BY id_atividade";
        } else {
            sql = "SELECT id_atividade, id_atividade || ' - ' || nome as  codigo FROM atividade where grupo = ? ORDER BY id_atividade";
        }
        Cursor cursor = db.getReadableDatabase().rawQuery(sql, new String[]{id});
        int x = cursor.getCount();
        //Log.w("Registros Atividade", "" + x);
        if(cursor.moveToFirst()){
            do {
                ativ.add(cursor.getString(1));
                id_Ativ.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ativ;
    }
}
